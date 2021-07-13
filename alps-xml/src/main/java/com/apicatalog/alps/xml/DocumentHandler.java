/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.apicatalog.alps.xml;

import java.net.URI;
import java.util.ArrayDeque;
import java.util.Deque;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.error.DocumentParserException;

final class DocumentHandler extends DefaultHandler {

    private enum State { INIT, DOCUMENT, DOCUMENTATION, DONE }

    private State state;

    private Deque<XmlElement> stack;

    private final URI baseUri;

    public DocumentHandler(URI baseUri) {
        this.stack = new ArrayDeque<>(10);
        this.baseUri = baseUri;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        stack.clear();
        state = State.INIT;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        super.startElement(uri, localName, qName, attributes);

        final String elementName = getElementName(localName, qName);

        if (elementName == null) {
            return;
        }

        if (State.DOCUMENTATION.equals(state)) {
            return;
        }

        try {

            if (XmlConstants.DOCUMENT.equals(elementName)) {

                if (State.INIT.equals(state)) {
                    stack.push(XmlDocument.create(attributes));
                    state = State.DOCUMENT;
                }
                return;
            }

            if (!State.DOCUMENT.equals(state)) {
                return;
            }

            if (XmlConstants.DOCUMENTATION.equals(elementName)) {
                stack.peek().beginDocumentation(stack, attributes);
                state = State.DOCUMENTATION;

            } else if (XmlConstants.DESCRIPTOR.equals(elementName)) {
                stack.peek().beginDescriptor(stack, attributes);

            } else if (XmlConstants.LINK.equals(elementName)) {
                stack.peek().beginLink(stack, attributes);

            } else if (XmlConstants.EXTENSION.equals(elementName)) {
                stack.peek().beginExtension(stack, attributes);
                
            } else if (XmlConstants.TITLE.equals(elementName)) {
                stack.peek().beginTitle(stack, attributes);
            }

        } catch (DocumentParserException e) {
            throw new SAXException(e);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        super.endElement(uri, localName, qName);

        final String elementName = getElementName(localName, qName);

        if (elementName == null) {
            return;
        }

        if (State.DOCUMENT.equals(state)) {

            if (XmlConstants.DOCUMENT.equals(elementName)) {
                state = State.DONE;

            } else if (stack.peek().getElementName().equals(elementName)) {

                XmlElement child =  stack.pop();

                if (XmlConstants.DESCRIPTOR.equals(elementName)) {
                    stack.peek().complete((XmlDescriptor)child);

                } else if (XmlConstants.LINK.equals(elementName)) {
                    stack.peek().complete((XmlLink)child);

                } else if (XmlConstants.EXTENSION.equals(elementName)) {
                    stack.peek().complete((XmlExtension)child);
                    
                } else if (XmlConstants.TITLE.equals(elementName)) {
                    stack.peek().complete((XmlTitle)child);
                }
            }

        } else if (State.DOCUMENTATION.equals(state) && XmlConstants.DOCUMENTATION.equals(elementName)) {

            final XmlDocumentation doc = (XmlDocumentation)stack.pop();

            stack.peek().complete(doc);

            state = State.DOCUMENT;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        stack.peek().addText(ch, start, length);
    }

    public Document getDocument() throws DocumentParserException {

        if (State.INIT.equals(state)) {
            throw new DocumentParserException("The document does not contain ALPS declaration.");
        }

        if (!State.DONE.equals(state))  {
            throw new DocumentParserException("The ALPS document declaration is unenclosed, expected " + stack.peek());
        }

        return ((XmlDocument)stack.peek()).build(baseUri);
    }

    private static final String getElementName(String localName, String qName) {

        String elementName = localName.toLowerCase();

        if (elementName == null || elementName.isBlank()) {
            elementName = qName.toLowerCase();
        }

        if (elementName == null || elementName.isBlank()) {
            return null;
        }

        return elementName;
    }
}
