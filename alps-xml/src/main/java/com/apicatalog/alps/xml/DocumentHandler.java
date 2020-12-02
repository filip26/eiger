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

    public DocumentHandler() {
        this.stack = new ArrayDeque<>(10);
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
                
                stack.peek().addDocumentation(stack, attributes);
                state = State.DOCUMENTATION;
                
            } else if (XmlConstants.DESCRIPTOR.equals(elementName)) {
                
                stack.peek().addDescriptor(stack, attributes);
    
            } else if (XmlConstants.LINK.equals(elementName)) {
                
                stack.peek().addLink(stack, attributes);
    
            } else if (XmlConstants.EXTENSION.equals(elementName)) {
                
                stack.peek().addExtension(stack, attributes);                    
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
                stack.peek().complete();

            } else if (stack.peek().getElementName().equals(elementName)) {
                stack.pop().complete();
            }
            
        } else if (State.DOCUMENTATION.equals(state) && XmlConstants.DOCUMENTATION.equals(elementName)) {
            
            state = State.DOCUMENT;
            stack.pop().complete();             
        }
    }
    
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        
        super.characters(ch, start, length);
        
        if (State.DOCUMENTATION.equals(state)) {
            stack.peek().addText(ch, start, length);
        }
    }

    public Document getDocument() throws DocumentParserException {
        
        if (State.INIT.equals(state)) {
            throw new DocumentParserException("The document does not contain ALPS declaration.");
        }
        
        if (!State.DONE.equals(state))  {
            throw new DocumentParserException("The ALPS document declaration is unenclosed, expected " + stack.peek());
        }
        
        return (Document)stack.peek();
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
