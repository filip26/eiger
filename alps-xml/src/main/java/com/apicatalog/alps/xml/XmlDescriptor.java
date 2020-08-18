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
import com.apicatalog.alps.error.DocumentException;
import com.apicatalog.alps.error.InvalidDocumentException;

public class XmlDescriptor implements Descriptor, XmlElement {

    private final int elementIndex;
    
    private URI id;
    
    private URI href;
    
    private String name;
    
    private DescriptorType type;
    
    private URI returnValue;
    
    private Set<Documentation> documentation;
    
    private Set<Descriptor> descriptors;
    
    private Set<Link> links;
    
    private Set<Extension> extensions;
    
    private Descriptor parent;
    
    private XmlDescriptor(int index) {
        this.elementIndex = index;
    }
    
    public static final XmlDescriptor create(final Deque<XmlElement> stack, int index, final Attributes attrs) throws DocumentException {

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
                throw new InvalidDocumentException(DocumentError.MALFORMED_URI, XPathUtil.getPath(stack, XmlConstants.DESCRIPTOR, index), "Descriptor href must be valid URI but was " + href);
            }
        }

        if (descriptor.id == null && descriptor.href == null) {
            throw new InvalidDocumentException(DocumentError.MISSING_ID, XPathUtil.getPath(stack, XmlConstants.DESCRIPTOR, index));
        }
        
        descriptor.type = parseType(attrs.getValue(XmlConstants.TYPE));
        
        String rt = attrs.getValue(XmlConstants.RETURN_TYPE);
        
        if (rt != null && !rt.isBlank()) {
            descriptor.returnValue = URI.create(rt);
        }

        descriptor.documentation = new LinkedHashSet<>();
        
        descriptor.descriptors = new LinkedHashSet<>();
        
        descriptor.links = new LinkedHashSet<>();
        
        descriptor.extensions = new LinkedHashSet<>();
        
        // TODO Auto-generated constructor stub
        return descriptor;
    }
    
    private static DescriptorType parseType(String value) {

        if (value == null || value.isBlank()) {
            return DescriptorType.SEMANTIC;
        }
        
        //TODO check 
        return DescriptorType.valueOf(value.toUpperCase());
    }

    @Override
    public void addText(char[] ch, int start, int length) {
        // TODO Auto-generated method stub
        
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
    
    public static void write(final Set<Descriptor> descriptors, final DocumentStreamWriter writer) throws DocumentStreamException {
        if (descriptors == null || descriptors.isEmpty()) {
            return;
        }
        
        for (final Descriptor descriptor : descriptors) {
            
            writer.startDescriptor(
                        descriptor.id().orElse(null),
                        descriptor.href().orElse(null),
                        descriptor.type(),
                        descriptor.returnType().orElse(null),
                        descriptor.name().orElse(null)
                    );
            
            XmlDocumentation.write(descriptor.documentation(), writer);
            
            XmlLink.write(descriptor.links(), writer);
            
            XmlDescriptor.write(descriptor.descriptors(), writer);
            
            writer.endDescriptor();
        }
    }

    @Override
    public int getElementIndex() {
        return elementIndex;
    }

    @Override
    public void addDescriptor(Deque<XmlElement> stack, Attributes attrs) throws DocumentException {
        XmlDescriptor dsc = XmlDescriptor.create(stack, descriptors.size(), attrs);
        dsc.parent = this;
        descriptors.add(dsc);
        stack.push(dsc);
    }

    @Override
    public void addLink(Deque<XmlElement> stack, Attributes attrs) throws DocumentException {
        final XmlLink link = XmlLink.create(stack, links.size(), attrs);
        links.add(link);
        stack.push(link);
    }

    @Override
    public void addDocumentation(Deque<XmlElement> stack, Attributes attrs) throws DocumentException {
        final XmlDocumentation doc = XmlDocumentation.create(stack, documentation.size(), attrs);
        documentation.add(doc);
        stack.push(doc);
    }

    @Override
    public void addExtension(Deque<XmlElement> stack, Attributes attrs) throws DocumentException {
        final XmlExtension ext = XmlExtension.create(stack, documentation.size(), attrs);
        extensions.add(ext);
        stack.push(ext);
    }    
}
