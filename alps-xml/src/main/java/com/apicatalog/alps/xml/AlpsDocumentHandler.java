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

import com.apicatalog.alps.AlpsErrorCode;
import com.apicatalog.alps.AlpsParserException;
import com.apicatalog.alps.dom.AlpsDocument;

final class AlpsDocumentHandler extends DefaultHandler {

    private enum State { INIT, START, END };
    
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

        try { 
        
            if (AlpsXmlKeys.DOCUMENT.equals(elementName)) {
                
                if (State.INIT.equals(state)) {
                    stack.push(XmlDocument.create(attributes));
                    state = State.START;
                }
                return;
            } 

            if (!State.START.equals(state)) {
                return;
            }
            
            if (AlpsXmlKeys.DOCUMENTATION.equals(elementName)) {
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
        } catch (AlpsParserException e) {
            throw new SAXException(e);
        }
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        
        final String elementName = getElementName(localName, qName);
        
        if (elementName == null) {
            return;
        }

        if (State.START.equals(state)) {
        
            try {
                
                if (!elementName.equals(stack.peek().getElementName())) {
                    throw new AlpsParserException(AlpsErrorCode.MALFORMED_DOCUMENT);
                }
        
                if (AlpsXmlKeys.DOCUMENT.equals(elementName)) {
                    state = State.END;
                    
                } else {
                    stack.pop();            
                }
                
            } catch (AlpsParserException e) {
                throw new SAXException(e);
            }
        }
        
        super.endElement(uri, localName, qName);
    }
    
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        
        if (stack.isEmpty()) {
            throw new SAXException();
        }
        
        stack.peek().addText(ch, start, length);
    }
    
    public AlpsDocument getDocument() throws AlpsParserException {
        
        if (State.INIT.equals(state)) {
            throw new AlpsParserException(AlpsErrorCode.INVALID_DOCUMENT);
        }
        
        if (State.START.equals(state))  {
            throw new AlpsParserException(AlpsErrorCode.MALFORMED_DOCUMENT);
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
