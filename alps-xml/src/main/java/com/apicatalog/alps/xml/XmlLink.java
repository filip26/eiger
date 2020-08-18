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

import com.apicatalog.alps.dom.element.Link;

final class XmlLink implements Link, XmlElement {

    private final int elementIndex;
    
    private URI href;
    private String rel;
    
    private XmlLink(int index) {
        this.elementIndex = index;
    }
    
    @Override
    public URI href() {
        return href;
    }

    @Override
    public String rel() {
        return rel;
    }

    @Override
    public String getElementName() {
        return XmlConstants.LINK;
    }

    @Override
    public void addText(char[] ch, int start, int length) {
        // TODO Auto-generated method stub
        
    }

    public static void write(Set<Link> links, DocumentStreamWriter writer) throws DocumentStreamException {

        if (links == null || links.isEmpty()) {
            return;
        }
        
        for (final Link link : links) {
            write(link, writer);
        }        
    }
    
    public static void write(final Link link, DocumentStreamWriter writer) throws DocumentStreamException {
        
        writer.writeLink(link.href(), link.rel());
       
    }

    public static XmlLink create(Deque<XmlElement> stack, int index, Attributes attributes) {

        final XmlLink link = new XmlLink(index);
        
        String href = attributes.getValue(XmlConstants.HREF);
        
        if (href == null || href.isBlank()) {
            //TODO
        }
        
        link.href = URI.create(href);
        
        String rel = attributes.getValue(XmlConstants.RELATION);
        
        if (rel == null || rel.isBlank()) {
            //TODO
        }
        
        link.rel = rel;
        
        return link;
    }
    
    @Override
    public int getElementIndex() {
        return elementIndex;
    }
}
