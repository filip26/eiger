package com.apicatalog.alps.api;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.dom.element.Descriptor;
import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Extension;
import com.apicatalog.alps.dom.element.Link;

class DocumentImpl implements Document {

    final DocumentVersion version;
    
    Set<Documentation> docs;
    Set<Link> links;
    
    public DocumentImpl(DocumentVersion version) {
        this.version = version;
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
    public DocumentVersion version() {
        return version;
    }

    @Override
    public Set<Descriptor> descriptors() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Descriptor> allDescriptors() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<Link> links() {
        return links;
    }

    @Override
    public Set<Documentation> documentation() {
        return docs;
    }

    @Override
    public Set<Extension> extensions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public URI baseUri() {
        // TODO Auto-generated method stub
        return null;
    }
}
