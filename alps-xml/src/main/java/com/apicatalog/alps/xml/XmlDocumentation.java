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
import java.util.Deque;
import java.util.Set;

import org.xml.sax.Attributes;

import com.apicatalog.alps.dom.element.Documentation;

final class XmlDocumentation implements Documentation, XmlElement {

    private final int elementIndex;
    private StringBuilder content;
    private String mediaType;
    
    private XmlDocumentation(int index) {
        this.elementIndex = index;
    }
    
    public static final XmlDocumentation create(Deque<XmlElement> stack, int index, Attributes attributes) {
        //TODO
        XmlDocumentation doc = new XmlDocumentation(index);
        
        doc.mediaType = attributes.getValue(AlpsConstants.MEDIA_TYPE);
        
        if (doc.mediaType == null) {
            doc.mediaType = "text";
        }
        
        doc.content = new StringBuilder();
        
        return doc; 
    }

    @Override
    public URI getHref() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getMediaType() {
        return mediaType;
    }

    @Override
    public String getContent() {
        return content.toString();
    }

    @Override
    public void addDocumentation(XmlDocumentation doc) {
        // TODO Auto-generated method stub
    }

    @Override
    public String getElementName() {
        return AlpsConstants.DOCUMENTATION;
    }
    
    @Override
    public void addText(char[] ch, int start, int length) {
        //TODO strip leading and trailing spaces
        content.append(ch, start, length);
    }

    public static void write(Set<Documentation> docs, DocumentStreamWriter writer) throws DocumentStreamException {
        
        if (docs == null || docs.isEmpty()) {
            return;
        }
        
        for (final Documentation doc : docs) {
            writer.startDoc(doc.getMediaType(), doc.getHref());
            
            writer.writeDocContent(doc.getContent());
            
            writer.endDoc();
        }
    }

    @Override
    public XmlDescriptor addDescriptor(Deque<XmlElement> stack, Attributes attrs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addLink(XmlLink link) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public int getElementIndex() {
        return elementIndex;
    }

    @Override
    public void startElement(String elementName, Attributes attributes) {
        content.append('<');
        content.append(elementName);
        
        for (int i=0; i < attributes.getLength(); i++) {
            content.append(' ');
            content.append(attributes.getLocalName(i));
            content.append("=\"");
            content.append(attributes.getValue(i));
            content.append('"');
        }

        content.append('>');
    }

    @Override
    public void endElement(String elementName) {
        content.append("</");
        content.append(elementName);        
        content.append('>');
    }
    
}
