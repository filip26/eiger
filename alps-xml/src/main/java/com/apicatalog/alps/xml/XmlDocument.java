package com.apicatalog.alps.xml;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

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
        String version = attrs.getValue(AlpsConstants.VERSION);

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
            
            if (id.equals(descriptor.getId())) {
                return Optional.of(descriptor);
            }
            
            final Optional<Descriptor> result = findById(descriptor.getDescriptors(), id);
            
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
            
            if (descriptor.getName().filter(name::equalsIgnoreCase).isPresent()) {
                result.add(descriptor);
            }
            
            findByName(result, descriptor.getDescriptors(), name);
        }        
    }

    @Override
    public DocumentVersion getVersion() {
        return version;
    }

    @Override
    public Set<Descriptor> getDescriptors() {
        return descriptors;
    }

    @Override
    public Collection<Descriptor> getAllDescriptors() {

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
            getAllDescriptors(result, descriptor.getDescriptors());
        }
    }

    @Override
    public Set<Link> getLinks() {
        return links;
    }

    @Override
    public Set<Documentation> getDocumentation() {
        return documentation;
    }

    @Override
    public Set<Extension> getExtensions() {
        return extensions;
    }

    @Override
    public URI getBaseUri() {
        return baseUri;
    }

    @Override
    public void addDocumentation(XmlDocumentation doc) {
        documentation.add(doc);
    }

    @Override
    public String getElementName() {
        return AlpsConstants.DOCUMENT;
    }

    @Override
    public void addText(char[] ch, int start, int length) {
        // TODO Auto-generated method stub   
    }

    @Override
    public XmlDescriptor addDescriptor(Deque<XmlElement> stack, Attributes attrs) throws DocumentException {
        XmlDescriptor dsc = XmlDescriptor.create(stack, descriptors.size(), attrs);
        descriptors.add(dsc);
        return dsc;
    }

    @Override
    public void addLink(XmlLink link) {
        links.add(link);
    }
    
    public static void write(Document document, DocumentStreamWriter writer) throws DocumentStreamException, DocumentException {

        if (document.getVersion() == null) {
            throw new InvalidDocumentException(DocumentError.MISSING_VERSION, "The document version is not defined.");
        }
        
        writer.startDocument(document.getVersion());
                
        if (document.getDocumentation() != null && !document.getDocumentation().isEmpty()) {
            XmlDocumentation.write(document.getDocumentation(), writer);
        }
        
        if (document.getLinks() != null && !document.getLinks().isEmpty()) {
            XmlLink.write(document.getLinks(), writer);
        }

        if (document.getDescriptors() != null && !document.getDescriptors().isEmpty()) {
            XmlDescriptor.write(document.getDescriptors(), writer);
        }

        writer.endDocument();        
    }
    
    @Override
    public int getElementIndex() {
        return -1;
    }

    @Override
    public void startElement(String elementName, Attributes attributes) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void endElement(String elementName) {
        // TODO Auto-generated method stub
        
    }
}
