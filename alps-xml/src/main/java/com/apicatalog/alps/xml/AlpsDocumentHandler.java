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

import com.apicatalog.alps.DocumentException;
import com.apicatalog.alps.dom.AlpsDocument;

final class AlpsDocumentHandler extends DefaultHandler {

    private enum State { INIT, DOCUMENT, DOCUMENTATION, DONE }
    
    private State state;
    
    private Deque<XmlElement> stack;

    public AlpsDocumentHandler() {
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
            stack.peek().startElement(elementName, attributes);
            return;
        }
        
        try { 
        
            if (AlpsXmlKeys.DOCUMENT.equals(elementName)) {
                
                if (State.INIT.equals(state)) {
                    stack.push(XmlDocument.create(attributes));
                    state = State.DOCUMENT;
                }
                return;
            } 

            if (!State.DOCUMENT.equals(state)) {
                return;
            }
            
            if (AlpsXmlKeys.DOCUMENTATION.equals(elementName)) {
                
                state = State.DOCUMENTATION;
                XmlDocumentation doc = XmlDocumentation.create(stack, -1, attributes);//FIXME
                stack.peek().addDocumentation(doc);
                stack.push(doc);
                
            } else if (AlpsXmlKeys.DESCRIPTOR.equals(elementName)) {
                stack.push(stack.peek().addDescriptor(stack, attributes));
    
            } else if (AlpsXmlKeys.LINK.equals(elementName)) {
                XmlLink link = XmlLink.create(stack, -1, attributes);   //FIXME
                stack.peek().addLink(link);
                stack.push(link);
    
            } else {
                //TODO
    
            }
        } catch (DocumentException e) {
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
        
            if (AlpsXmlKeys.DOCUMENT.equals(elementName)) {
                state = State.DONE;

            } else {
                stack.pop();            
            }
                
            
        } else if (State.DOCUMENTATION.equals(state)) {
            
            if (AlpsXmlKeys.DOCUMENTATION.equals(elementName)) {
                state = State.DOCUMENT;
                stack.pop();
                
            } else {
                stack.peek().endElement(elementName);
            }
        }
    }
    
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        
        super.characters(ch, start, length);
        
        if (State.DOCUMENTATION.equals(state)) {
            stack.peek().addText(ch, start, length);
        }
    }
    
    public AlpsDocument getDocument() throws DocumentException {
        
        if (State.INIT.equals(state)) {
            throw new DocumentException("The document does not contain ALPS declaration.");
        }
        
        if (!State.DONE.equals(state))  {
            throw new DocumentException("The ALPS document declaration is unenclosed, expected " + stack.peek());
        }
        
        return (AlpsDocument)stack.pop();
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
