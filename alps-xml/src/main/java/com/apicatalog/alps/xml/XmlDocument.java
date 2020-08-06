package com.apicatalog.alps.xml;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedHashSet;
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
    
    private Set<Documentation> documentation;
    
    private Set<Descriptor> descriptors;
    
    private Set<Link> links;
    
    public static final XmlDocument create(Attributes attrs) throws SAXException {
        
        final XmlDocument doc = new XmlDocument();
        doc.version = readVersion(attrs);
        doc.documentation = new LinkedHashSet<>();
        doc.descriptors = new LinkedHashSet<>();
        doc.links = new LinkedHashSet<>();
        return doc;
    }

    private static final DocumentVersion readVersion(Attributes attrs) throws SAXException {

        // version
        String version = attrs.getValue(AlpsXmlKeys.VERSION);

        if (version == null || version.isBlank() || "1.0".equals(version)) {

            return DocumentVersion.VERSION_1_0;            
        }
        
        throw new SAXException();
    }
    
    @Override
    public Optional<Descriptor> findById(URI id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<Descriptor> findByName(String name) {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public URI getBaseUri() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addDocumentation(XmlDocumentation doc) {
        documentation.add(doc);
    }

    @Override
    public String getElementName() {
        return AlpsXmlKeys.DOCUMENT;
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
    
    public static void write(Document document, XMLStreamWriter writer) throws XMLStreamException, DocumentException {

        writer.writeStartDocument(Charset.defaultCharset().name(), "1.0");
        
        writer.writeStartElement(AlpsXmlKeys.DOCUMENT);
        
        if (document.getVersion() == null) {
            throw new InvalidDocumentException(DocumentError.MISSING_VERSION, "The document version is not defined.");
        }
        writer.writeAttribute(AlpsXmlKeys.VERSION, "1.0");
        
        if (document.getDocumentation() != null && !document.getDocumentation().isEmpty()) {
            XmlDocumentation.write(document.getDocumentation(), writer);
        }
        
        if (document.getLinks() != null && !document.getLinks().isEmpty()) {
            XmlLink.write(document.getLinks(), writer);
        }

        if (document.getDescriptors() != null && !document.getDescriptors().isEmpty()) {
            XmlDescriptor.write(document.getDescriptors(), writer);
        }

        writer.writeEndElement();
        writer.writeEndDocument();        
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
