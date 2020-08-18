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

import org.xml.sax.Attributes;

import com.apicatalog.alps.dom.element.Extension;

final class XmlExtension implements Extension, XmlElement {

    private URI id; 
    
    private URI href;
    
    private String value;
    
    private int elementIndex;
    
    private XmlExtension(int index) {
        this.elementIndex = index;
    }
    
    @Override
    public String getElementName() {
        return XmlConstants.EXTENSION;
    }

    @Override
    public int getElementIndex() {
        return elementIndex;
    }

    @Override
    public Optional<URI> href() {
        return Optional.ofNullable(href);
    }

    @Override
    public URI id() {
        return id;
    }

    @Override
    public Optional<String> value() {
        return Optional.ofNullable(value);
    }

    public static final XmlExtension create(Deque<XmlElement> stack, int elementIndex, Attributes attributes) {
        
        final XmlExtension ext = new XmlExtension(elementIndex);

        final String id = attributes.getValue(XmlConstants.ID);
        
        if (id != null && !id.isBlank()) {
            
            try {
                ext.id = URI.create(id);
            } catch (IllegalArgumentException e) {
                //TODO
            }
            
        } else {
            //TODO error
        }

        final String href = attributes.getValue(XmlConstants.HREF);
        
        if (href != null && !href.isBlank()) {
            try {
                ext.href = URI.create(href);
            } catch (IllegalArgumentException e) {
                //TODO
            }
        }
        
        final String value = attributes.getValue(XmlConstants.VALUE);
        
        if (value != null && !value.isBlank()) {
            ext.value = value;
        }

        return ext;
    }
    
    public static void write(Set<Extension> extensions, DocumentStreamWriter writer) throws DocumentStreamException {

        if (extensions == null || extensions.isEmpty()) {
            return;
        }
        
        for (final Extension extension : extensions) {
            write(extension, writer);
        }        
    }
    
    public static void write(final Extension extension, DocumentStreamWriter writer) throws DocumentStreamException {
        writer.writeExtension(extension.id(), extension.href().orElse(null), extension.value().orElse(null));       
    }

}
