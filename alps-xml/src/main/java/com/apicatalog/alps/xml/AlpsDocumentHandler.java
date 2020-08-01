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

import com.apicatalog.alps.AlpsParserException;
import com.apicatalog.alps.dom.AlpsDocument;

class AlpsDocumentHandler extends DefaultHandler {

    private Deque<XmlElement> stack;

    public AlpsDocumentHandler() {
        this.stack = new ArrayDeque<>(10);
    }
    
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        stack.clear();
    }
    
    @Override
    public void endDocument() throws SAXException {
        if (stack.size() != 1) {
            throw new SAXException();
        }
        
        // TODO validate document
        super.endDocument();
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        
        String elementName = localName.toLowerCase();
        
        if (elementName == null || elementName.isEmpty()) {
            elementName = qName.toLowerCase();
        }
        
        if (elementName == null || elementName.isEmpty()) {
            //TODO
            return;
        }

        if (AlpsXmlKeys.DOCUMENT.equals(elementName)) {

            if (!stack.isEmpty()) {
                throw new SAXException();
            }
            
            stack.push(XmlDocument.create(attributes));
            return;
        } 

        if (stack.isEmpty()) {
            throw new SAXException();
        }

        if (AlpsXmlKeys.DOCUMENTATION.equals(elementName)) {
            XmlDocumentation doc = XmlDocumentation.create(attributes);
            stack.peek().addDocumentation(doc);
            stack.push(doc);
            
        } else if (AlpsXmlKeys.DESCRIPTOR.equals(elementName)) {
            XmlDescriptor dsc = XmlDescriptor.create(attributes);
            stack.peek().addDescriptor(dsc);
            stack.push(dsc);
            
        } else {
            //TODO

        }
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        
        String elementName = localName.toLowerCase();
        
        if (elementName == null || elementName.isEmpty()) {
            elementName = qName.toLowerCase();
        }
        
        if (elementName == null || elementName.isEmpty()) {
            //TODO
            return;
        }
        
        if (!AlpsXmlKeys.DOCUMENT.equals(elementName) && elementName.equals(stack.peek().getElementName())) {
            //TODO validate
            stack.pop();
        }
        
        
        // TODO Auto-generated method stub
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
        
        if (stack.isEmpty()) {
            //TODO
            throw new AlpsParserException();
            
        } else if (stack.size() > 1)  {
            //TODO
            throw new AlpsParserException();
        }
        
        return (AlpsDocument)stack.pop();
    }

}
