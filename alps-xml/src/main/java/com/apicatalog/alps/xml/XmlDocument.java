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
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.dom.element.Descriptor;
import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Extension;
import com.apicatalog.alps.dom.element.Link;
import com.apicatalog.alps.error.DocumentError;
import com.apicatalog.alps.error.DocumentException;
import com.apicatalog.alps.error.InvalidDocumentException;

final class XmlDocument implements Document, XmlElement {

    private DocumentVersion version;

    private URI baseUri;

    private Set<Documentation> documentation;
    
    private Set<Link> links;
    
    private Set<Extension> extensions;
    
    private Set<Descriptor> descriptors;

    
    public static final XmlDocument create(Attributes attrs) throws SAXException {
        
        final XmlDocument doc = new XmlDocument();
        doc.version = readVersion(attrs);
        doc.documentation = new LinkedHashSet<>();
        doc.descriptors = new LinkedHashSet<>();
        doc.links = new LinkedHashSet<>();
        doc.extensions = new LinkedHashSet<>();
        return doc;
    }

    private static final DocumentVersion readVersion(Attributes attrs) throws SAXException {

        // version
        String version = attrs.getValue(XmlConstants.VERSION);

        if (version == null || version.isBlank() || "1.0".equals(version)) {

            return DocumentVersion.VERSION_1_0;            
        }
        
        throw new SAXException();
    }
    
    @Override
    public Optional<Descriptor> findById(final URI id) {
        return findById(descriptors, id);
    }

    private static final Optional<Descriptor> findById(final Set<Descriptor> descriptors, final URI id) {
        
        for (final Descriptor descriptor : descriptors) {
            
            if (descriptor.id().filter(id::equals).isPresent()) {
                return Optional.of(descriptor);
            }
            
            final Optional<Descriptor> result = findById(descriptor.descriptors(), id);
            
            if (result.isPresent()) {
                return result;
            }
            
        }
        return Optional.empty();
    }

    
    @Override
    public Set<Descriptor> findByName(final String name) {
        
        if (descriptors.isEmpty()) {
            return Collections.emptySet();
        }
        
        final Set<Descriptor> result = new LinkedHashSet<>();
        
        findByName(result, descriptors, name);
        
        return result;
    }

    private static final void findByName(Set<Descriptor> result, Set<Descriptor> descriptors, final String name) {
        
        for (final Descriptor descriptor : descriptors) {
            
            if (descriptor.name().filter(name::equalsIgnoreCase).isPresent()) {
                result.add(descriptor);
            }
            
            findByName(result, descriptor.descriptors(), name);
        }        
    }

    @Override
    public DocumentVersion version() {
        return version;
    }

    @Override
    public Set<Descriptor> descriptors() {
        return descriptors;
    }

    @Override
    public Collection<Descriptor> allDescriptors() {

        if (descriptors.isEmpty()) {
            return Collections.emptySet();
        }

        final Collection<Descriptor> result = new LinkedList<>();
        
        getAllDescriptors(result, descriptors);
        
        return result;
    }
    
    private static final void getAllDescriptors(final Collection<Descriptor> result, final Set<Descriptor> descriptors) {
        for (final Descriptor descriptor : descriptors) {
            result.add(descriptor);
            getAllDescriptors(result, descriptor.descriptors());
        }
    }

    @Override
    public Set<Link> links() {
        return links;
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
    public URI baseUri() {
        return baseUri;
    }

    @Override
    public String getElementName() {
        return XmlConstants.DOCUMENT;
    }

    @Override
    public void addDescriptor(Deque<XmlElement> stack, Attributes attrs) throws DocumentException {
        
        final XmlDescriptor dsc = XmlDescriptor.create(stack, descriptors.size(), attrs);
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
        final XmlExtension ext = XmlExtension.create(stack, extensions.size(), attrs);
        extensions.add(ext);
        stack.push(ext);
    }

    public static void write(Document document, DocumentStreamWriter writer) throws DocumentStreamException, DocumentException {

        if (document.version() == null) {
            throw new InvalidDocumentException(DocumentError.MISSING_VERSION, "The document version is not defined.");
        }
        
        writer.startDocument(document.version());
                
        XmlDocumentation.write(document.documentation(), writer);
        
        XmlLink.write(document.links(), writer);

        XmlDescriptor.write(document.descriptors(), writer);
        
        XmlExtension.write(document.extensions(), writer);

        writer.endDocument();        
    }
    
    @Override
    public int getElementIndex() {
        return -1;
    }
}
