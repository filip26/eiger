package com.apicatalog.alps.xml;

import java.net.URI;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.apicatalog.alps.dom.AlpsDocument;
import com.apicatalog.alps.dom.AlpsVersion;
import com.apicatalog.alps.dom.element.AlpsDescriptor;
import com.apicatalog.alps.dom.element.AlpsDocumentation;
import com.apicatalog.alps.dom.element.AlpsExtension;
import com.apicatalog.alps.dom.element.AlpsLink;

final class XmlDocument implements AlpsDocument, XmlElement {

    private AlpsVersion version;
    
    private Set<AlpsDocumentation> documentation;
    
    private Set<AlpsDescriptor> descriptors;
    
    public static final XmlDocument create(Attributes attrs) throws SAXException {
        
        final XmlDocument doc = new XmlDocument();
        doc.version = readVersion(attrs);
        doc.documentation = new LinkedHashSet<>();
        doc.descriptors = new LinkedHashSet<>();
        return doc;
    }

    private static final AlpsVersion readVersion(Attributes attrs) throws SAXException {

        // version
        String version = attrs.getValue(AlpsXmlKeys.VERSION);

        if ("1.0".equals(version)) {

            return AlpsVersion.VERSION_1_0;            
        }
        
        throw new SAXException();
    }
    
    @Override
    public Optional<AlpsDescriptor> findById(URI id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<AlpsDescriptor> findByName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AlpsVersion getVersion() {
        return version;
    }

    @Override
    public Set<AlpsDescriptor> getDescriptors() {
        return descriptors;
    }

    @Override
    public Collection<AlpsDescriptor> getAllDescriptors() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<AlpsLink> getLinks() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<AlpsDocumentation> getDocumentation() {
        return documentation;
    }

    @Override
    public Set<AlpsExtension> getExtensions() {
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
    public void addDescriptor(XmlDescriptor descriptor) {
        descriptors.add(descriptor);
    }

}
