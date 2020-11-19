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
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.xml.sax.Attributes;

import com.apicatalog.alps.dom.element.Descriptor;
import com.apicatalog.alps.dom.element.DescriptorType;
import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Extension;
import com.apicatalog.alps.dom.element.Link;
import com.apicatalog.alps.error.DocumentError;
import com.apicatalog.alps.error.DocumentParserException;
import com.apicatalog.alps.error.DocumentWriterException;
import com.apicatalog.alps.error.InvalidDocumentException;

public class XmlDescriptor implements Descriptor, XmlElement {

    private final int elementIndex;
    
    private URI id;
    
    private URI href;
    
    private URI definition;
    
    private String name;
    
    private DescriptorType type;
    
    private URI returnValue;
    
    private String title;
    
    private Set<Documentation> documentation;
    
    private Set<Descriptor> descriptors;
    
    private Set<Link> links;
    
    private Set<Extension> extensions;
    
    private Descriptor parent;
    
    private XmlDescriptor(int index) {
        this.elementIndex = index;
    }
    
    public static final XmlDescriptor create(final Deque<XmlElement> stack, int index, final Attributes attrs) throws DocumentParserException {

        final XmlDescriptor descriptor = new XmlDescriptor(index);
        
        final String id = attrs.getValue(XmlConstants.ID);
        
        if (id != null && !id.isBlank()) {
            try {
                descriptor.id = URI.create(id);
                
            } catch (IllegalArgumentException e) {
                throw new InvalidDocumentException(DocumentError.MALFORMED_URI, XPathUtil.getPath(stack, XmlConstants.DESCRIPTOR, index), "Descriptor id must be valid URI but was " + id);
            }
        }  

        final String href = attrs.getValue(XmlConstants.HREF);
        
        if (href != null && !href.isBlank()) {
            try {
                descriptor.href = URI.create(href);
                
            } catch (IllegalArgumentException e) {
                throw new InvalidDocumentException(DocumentError.MALFORMED_URI, XPathUtil.getPath(stack, XmlConstants.DESCRIPTOR, index), "Descriptor href attribute must be valid URI but was " + href);
            }
        }

        if (descriptor.id == null && descriptor.href == null) {
            throw new InvalidDocumentException(DocumentError.MISSING_ID, XPathUtil.getPath(stack, XmlConstants.DESCRIPTOR, index));
        }

        final String definition = attrs.getValue(XmlConstants.DEFINITION);
        
        if (definition != null && !definition.isBlank()) {
            try {
                descriptor.definition = URI.create(definition);
                
            } catch (IllegalArgumentException e) {
                throw new InvalidDocumentException(DocumentError.MALFORMED_URI, XPathUtil.getPath(stack, XmlConstants.DESCRIPTOR, index), "Descriptor def attribute must be valid URI but was " + href);
            }
        }

        descriptor.type = parseType(stack, index, attrs);
        
        final String rt = attrs.getValue(XmlConstants.RETURN_TYPE);
        
        if (rt != null && !rt.isBlank()) {
            descriptor.returnValue = URI.create(rt);
        }
        
        final String name = attrs.getValue(XmlConstants.NAME);
        
        if (name != null && !name.isBlank()) {
            descriptor.name = name;
        }

        final String title = attrs.getValue(XmlConstants.TITLE);
        
        if (title != null && !title.isBlank()) {
            descriptor.title = title;
        }

        descriptor.documentation = new LinkedHashSet<>();
        
        descriptor.descriptors = new LinkedHashSet<>();
        
        descriptor.links = new LinkedHashSet<>();
        
        descriptor.extensions = new LinkedHashSet<>();
        
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

    @Override
    public Optional<URI> id() {
        return Optional.ofNullable(id);
    }

    @Override
    public Optional<URI> href() {
        return Optional.ofNullable(href);
    }

    @Override
    public Optional<String> name() {
        return Optional.ofNullable(name);
    }

    @Override
    public DescriptorType type() {
        return type;
    }

    @Override
    public Optional<URI> returnType() {
        return Optional.ofNullable(returnValue);
    }
    
    @Override
    public Optional<String> title() {
        return Optional.ofNullable(title);
    }

    @Override
    public Set<Documentation> documentation() {
        return documentation;
    }

    @Override
    public Set<Extension> extensions() {
        return extensions;
    }

    @Override
    public Set<Descriptor> descriptors() {
        return descriptors;
    }

    @Override
    public Optional<Descriptor> parent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public Set<Link> links() {
        return links;
    }
    
    @Override
    public Optional<URI> definition() {
        return Optional.ofNullable(definition);
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
    public void addDescriptor(Deque<XmlElement> stack, Attributes attrs) throws DocumentParserException {
        XmlDescriptor dsc = XmlDescriptor.create(stack, descriptors.size(), attrs);
        dsc.parent = this;
        descriptors.add(dsc);
        stack.push(dsc);
    }

    @Override
    public void addLink(Deque<XmlElement> stack, Attributes attrs) throws DocumentParserException {
        final XmlLink link = XmlLink.create(stack, links.size(), attrs);
        links.add(link);
        stack.push(link);
    }

    @Override
    public void addDocumentation(Deque<XmlElement> stack, Attributes attrs) throws DocumentParserException {
        final XmlDocumentation doc = XmlDocumentation.create(stack, documentation.size(), attrs);
        documentation.add(doc);
        stack.push(doc);
    }

    @Override
    public void addExtension(Deque<XmlElement> stack, Attributes attrs) throws DocumentParserException {
        final XmlExtension ext = XmlExtension.create(stack, documentation.size(), attrs);
        extensions.add(ext);
        stack.push(ext);
    }    
}
