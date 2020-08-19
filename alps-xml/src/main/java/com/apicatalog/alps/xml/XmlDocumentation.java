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
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.xml.sax.Attributes;

import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.error.DocumentWriterException;

final class XmlDocumentation implements Documentation, XmlElement {

    private final int elementIndex;
    
    private URI href;
    private XmlContent content;
    
    private XmlDocumentation(int index) {
        this.elementIndex = index;
    }
    
    public static final XmlDocumentation create(final Deque<XmlElement> stack, final int index, final Attributes attributes) {
        
        final XmlDocumentation doc = new XmlDocumentation(index);
        
        final XmlContent content = doc.new XmlContent();
        
        content.type = attributes.getValue(XmlConstants.MEDIA_TYPE);
        
        if (content.type == null || content.type.isBlank()) {
            content.type = "text/plain";
        }
        
        content.value = new StringBuilder();
        
        doc.content = content;
        return doc; 
    }

    @Override
    public Optional<URI> href() {
        return Optional.ofNullable(href);
    }

    @Override
    public Optional<Content> content() {
        return Optional.ofNullable(content);
    }

    @Override
    public String getElementName() {
        return XmlConstants.DOCUMENTATION;
    }
    
    @Override
    public void addText(char[] ch, int start, int length) {        
        content.value.append(ch, start, length);
    }

    public static void write(Set<Documentation> docs, DocumentStreamWriter writer) throws DocumentWriterException {
        
        if (docs == null || docs.isEmpty()) {
            return;
        }
        
        for (final Documentation doc : docs) {
            
            if (doc.content().isEmpty() && doc.href().isEmpty()) {
                continue;
            }
            
            writer.startDoc(doc.content().map(Content::type).orElse(null), doc.href().orElse(null));
            
            final Optional<String> value = doc.content().map(Content::value).filter(Predicate.not(String::isBlank));
            
            if (value.isPresent()) {
                writer.writeDocContent(value.get());
            }
            
            writer.endDoc();
        }
    }
    
    @Override
    public int getElementIndex() {
        return elementIndex;
    }
    
    class XmlContent implements Content {
        
        private StringBuilder value;
        private String type;

        @Override
        public String type() {
            return type;
        }
        
        @Override
        public String value() {
            return value.toString();
        } 
    }
}
