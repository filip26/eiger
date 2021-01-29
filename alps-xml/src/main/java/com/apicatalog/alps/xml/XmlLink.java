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
import java.util.Set;

import org.xml.sax.Attributes;

import com.apicatalog.alps.Alps;
import com.apicatalog.alps.dom.element.Link;
import com.apicatalog.alps.error.DocumentWriterException;

final class XmlLink extends XmlElement {

    Link link;
    
    private XmlLink(int index) {
        super(XmlConstants.LINK, index);
    }
    
    public static void write(Set<Link> links, DocumentStreamWriter writer) throws DocumentWriterException {

        if (links == null || links.isEmpty()) {
            return;
        }
        
        for (final Link link : links) {
            writer.writeLink(link);
        }        
    }
    
    public static XmlLink create(int index, Attributes attributes) {

        final XmlLink link = new XmlLink(index);
        
        String href = attributes.getValue(XmlConstants.HREF);
        
        if (href != null && href.isBlank()) {
            href = null;
        }
        
        String rel = attributes.getValue(XmlConstants.RELATION);
        
        if (rel != null && rel.isBlank()) {
            rel = null;
        }
        
        link.link = Alps.createLink(URI.create(href), rel);
        
        return link;
    }    
}
