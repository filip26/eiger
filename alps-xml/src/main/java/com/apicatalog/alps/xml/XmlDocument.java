package com.apicatalog.alps.xml;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import com.apicatalog.alps.dom.AlpsDocument;
import com.apicatalog.alps.dom.AlpsVersion;
import com.apicatalog.alps.dom.element.AlpsDescriptor;
import com.apicatalog.alps.dom.element.AlpsDocumentation;
import com.apicatalog.alps.dom.element.AlpsExtension;
import com.apicatalog.alps.dom.element.AlpsLink;

final class XmlDocument implements AlpsDocument, XmlElement {

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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<AlpsDescriptor> getDescriptors() {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getElementName() {
        return AlpsXmlKeys.DOCUMENT;
    }

    @Override
    public void addText(char[] ch, int start, int length) {
        // TODO Auto-generated method stub
        
    }

}