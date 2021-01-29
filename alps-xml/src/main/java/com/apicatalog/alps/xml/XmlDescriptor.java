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
import java.util.Arrays;
import java.util.Deque;
import java.util.Set;

import org.xml.sax.Attributes;

import com.apicatalog.alps.Alps;
import com.apicatalog.alps.DescriptorBuilder;
import com.apicatalog.alps.dom.element.Descriptor;
import com.apicatalog.alps.dom.element.DescriptorType;
import com.apicatalog.alps.error.DocumentError;
import com.apicatalog.alps.error.DocumentParserException;
import com.apicatalog.alps.error.DocumentWriterException;
import com.apicatalog.alps.error.InvalidDocumentException;

public class XmlDescriptor implements XmlElement {

    private final int elementIndex;
    
    final DescriptorBuilder builder;
        
    private int documentation;
    
    private int descriptors;
    
    private int links;
    
    private int extensions;
    
    private XmlDescriptor(int index) {
        this.elementIndex = index;
        this.builder = Alps.createDescriptor();

        this.documentation = 0;
        this.descriptors = 0;
        this.links = 0;
        this.extensions = 0;
    }
    
    public static final XmlDescriptor create(final Deque<XmlElement> stack, int index, final Attributes attrs) throws DocumentParserException {

        final XmlDescriptor descriptor = new XmlDescriptor(index);
        
        final String id = attrs.getValue(XmlConstants.ID);
        
        if (id != null && !id.isBlank()) {
            try {
                descriptor.builder.id(URI.create(id));
                
            } catch (IllegalArgumentException e) {
                throw new InvalidDocumentException(DocumentError.MALFORMED_URI, XPathUtil.getPath(stack, XmlConstants.DESCRIPTOR, index), "Descriptor id must be valid URI but was " + id);
            }
        }  

        final String href = attrs.getValue(XmlConstants.HREF);
        
        if (href != null && !href.isBlank()) {
            try {
                descriptor.builder.href(URI.create(href));
                
            } catch (IllegalArgumentException e) {
                throw new InvalidDocumentException(DocumentError.MALFORMED_URI, XPathUtil.getPath(stack, XmlConstants.DESCRIPTOR, index), "Descriptor href attribute must be valid URI but was " + href);
            }
        }

        final String definition = attrs.getValue(XmlConstants.DEFINITION);
        
        if (definition != null && !definition.isBlank()) {
            try {
                descriptor.builder.definition(URI.create(definition));
                
            } catch (IllegalArgumentException e) {
                throw new InvalidDocumentException(DocumentError.MALFORMED_URI, XPathUtil.getPath(stack, XmlConstants.DESCRIPTOR, index), "Descriptor def attribute must be valid URI but was " + href);
            }
        }

        descriptor.builder.type(parseType(stack, index, attrs));
        
        final String rt = attrs.getValue(XmlConstants.RETURN_TYPE);
        
        if (rt != null && !rt.isBlank()) {
            descriptor.builder.returnType(URI.create(rt));
        }
        
        final String name = attrs.getValue(XmlConstants.NAME);
        
        if (name != null && !name.isBlank()) {
            descriptor.builder.name(name);
        }

        final String title = attrs.getValue(XmlConstants.TITLE);
        
        if (title != null && !title.isBlank()) {
            descriptor.builder.title(title);
        }

        return descriptor;
    }
    
    private static DescriptorType parseType(final Deque<XmlElement> stack, final int index, final Attributes attrs) throws InvalidDocumentException {

        final String value = attrs.getValue(XmlConstants.TYPE);
        
        if (value == null || value.isBlank()) {
            return DescriptorType.SEMANTIC;
        }
     
        try { 
            return DescriptorType.valueOf(value.toUpperCase());
            
        } catch (IllegalArgumentException e) {
            throw new InvalidDocumentException(DocumentError.INVALID_TYPE, XPathUtil.getPath(stack, XmlConstants.TYPE), "Expected one of " + Arrays.toString(DescriptorType.values()) + " but was " + value);
        }
    }

    @Override
    public String getElementName() {
        return XmlConstants.DESCRIPTOR;
    }

    public static void write(final Set<Descriptor> descriptors, final DocumentStreamWriter writer, final boolean verbose) throws DocumentWriterException {
        if (descriptors == null || descriptors.isEmpty()) {
            return;
        }
        
        for (final Descriptor descriptor : descriptors) {

            final boolean selfClose = descriptor.descriptors().isEmpty()
                    && descriptor.documentation().isEmpty()
                    && descriptor.links().isEmpty()
                    && descriptor.extensions().isEmpty();
            
            writer.startDescriptor(descriptor, selfClose, verbose);
            
            if (!selfClose) {
                XmlDocumentation.write(descriptor.documentation(), writer, verbose);
                
                XmlLink.write(descriptor.links(), writer);
                
                XmlDescriptor.write(descriptor.descriptors(), writer, verbose);
                
                XmlExtension.write(descriptor.extensions(), writer);
                
                writer.endDescriptor();
            }
        }
    }

    @Override
    public int getElementIndex() {
        return elementIndex;
    }

    @Override
    public void beginDescriptor(Deque<XmlElement> stack, Attributes attrs) throws DocumentParserException {
        XmlDescriptor dsc = XmlDescriptor.create(stack, descriptors++, attrs);
        stack.push(dsc);
    }

    @Override
    public void beginLink(Deque<XmlElement> stack, Attributes attrs) throws DocumentParserException {
        final XmlLink link = XmlLink.create(stack, links++, attrs);
        stack.push(link);
    }

    @Override
    public void beginDocumentation(Deque<XmlElement> stack, Attributes attrs) throws DocumentParserException {
        final XmlDocumentation doc = XmlDocumentation.create(stack, documentation++, attrs);
        stack.push(doc);
    }

    @Override
    public void beginExtension(Deque<XmlElement> stack, Attributes attrs) throws DocumentParserException {
        final XmlExtension ext = XmlExtension.create(stack, extensions++, attrs);
        stack.push(ext);
    }  
    
    @Override
    public void complete(XmlDescriptor descriptor) {
        builder.add(descriptor.builder.build());
    }
    
    @Override
    public void complete(XmlDocumentation doc) {
        builder.add(doc.builder.build());
    }
    
    @Override
    public void complete(XmlLink link) {
        builder.add(link.link);
    }
    
    @Override
    public void complete(XmlExtension ext) {
        builder.add(ext.builder.build());
    }
}
