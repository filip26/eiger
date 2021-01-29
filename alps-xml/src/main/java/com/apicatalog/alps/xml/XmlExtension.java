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

import com.apicatalog.alps.Alps;
import com.apicatalog.alps.ExtensionBuilder;
import com.apicatalog.alps.dom.element.Extension;
import com.apicatalog.alps.error.DocumentError;
import com.apicatalog.alps.error.DocumentWriterException;
import com.apicatalog.alps.error.InvalidDocumentException;

final class XmlExtension implements XmlElement {

    final ExtensionBuilder builder;
    
    private int elementIndex;
    
    private XmlExtension(int index) {
        this.elementIndex = index;
        this.builder = Alps.createExtension();
    }
    
    @Override
    public String getElementName() {
        return XmlConstants.EXTENSION;
    }

    @Override
    public int getElementIndex() {
        return elementIndex;
    }

    public static final XmlExtension create(Deque<XmlElement> stack, int elementIndex, Attributes attributes) throws InvalidDocumentException {
        
        final XmlExtension ext = new XmlExtension(elementIndex);

        final String id = attributes.getValue(XmlConstants.ID);
        
        if (id != null && !id.isBlank()) {
            
            try {
                ext.builder.id(URI.create(id));
            } catch (IllegalArgumentException e) {
                throw new InvalidDocumentException(DocumentError.MALFORMED_URI, XPathUtil.getPath(stack, XmlConstants.EXTENSION, elementIndex), "Extension id must be valid URI but was " + id);
            }
            
        } else {
            throw new InvalidDocumentException(DocumentError.MISSING_ID, XPathUtil.getPath(stack, XmlConstants.EXTENSION, elementIndex));
        }

        final String href = attributes.getValue(XmlConstants.HREF);
        
        if (href != null && !href.isBlank()) {
            try {
                ext.builder.href(URI.create(href));
            } catch (IllegalArgumentException e) {
                throw new InvalidDocumentException(DocumentError.MALFORMED_URI, XPathUtil.getPath(stack, XmlConstants.EXTENSION, elementIndex), "Extension href must be valid URI but was " + href);
            }
        }
        
        final String value = attributes.getValue(XmlConstants.VALUE);
        
        if (value != null && !value.isBlank()) {
            ext.builder.value(value);
        }
        
        // custom attributes
        for (int i = 0; i < attributes.getLength(); i++) {
            
            String attrName = attributes.getLocalName(i);
            
            if (XmlConstants.HREF.equalsIgnoreCase(attrName) 
                    || XmlConstants.VALUE.equalsIgnoreCase(attrName) 
                    || XmlConstants.ID.equalsIgnoreCase(attrName)) {
                continue;
            }

            ext.builder.attribute(attrName, attributes.getValue(i));
        }
        

        return ext;
    }
    
    public static void write(Set<Extension> extensions, DocumentStreamWriter writer) throws DocumentWriterException {

        if (extensions == null || extensions.isEmpty()) {
            return;
        }
        
        for (final Extension extension : extensions) {
            writer.writeExtension(extension);
        }        
    }
}
