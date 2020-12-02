package com.apicatalog.alps.api;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import com.apicatalog.alps.dom.element.Descriptor;
import com.apicatalog.alps.dom.element.DescriptorType;
import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Extension;
import com.apicatalog.alps.dom.element.Link;

class DescriptorImpl implements Descriptor {

    final DescriptorType type;
    URI id;
    URI href;
    URI definition;
    URI returnType;
    String name;
    String title;
    
    Set<Descriptor> descriptors;
    
    public DescriptorImpl(final DescriptorType type) {
        this.type = type;
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
    public Optional<URI> definition() {
        return Optional.ofNullable(definition);
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
        return Optional.ofNullable(returnType);
    }

    @Override
    public Set<Documentation> documentation() {
        return Collections.emptySet();
    }

    @Override
    public Set<Extension> extensions() {
        return Collections.emptySet();
    }

    @Override
    public Set<Descriptor> descriptors() {
        return descriptors;
    }

    @Override
    public Optional<Descriptor> parent() {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public Set<Link> links() {
        return Collections.emptySet();
    }
    
    @Override
    public Optional<String> title() {
        return Optional.ofNullable(title);
    }
}
