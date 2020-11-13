package com.apicatalog.alps.api;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashSet;

import com.apicatalog.alps.dom.element.Descriptor;
import com.apicatalog.alps.dom.element.DescriptorType;
import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Extension;
import com.apicatalog.alps.dom.element.Link;

public final class DescriptorBuilder {
    
    private final DescriptorImpl descriptor;
    
    public DescriptorBuilder(DescriptorType type) {
        this.descriptor = new DescriptorImpl(type);
    }
    
    public final DescriptorBuilder add(Descriptor descriptor) {
        if (this.descriptor.descriptors == null) {
            this.descriptor.descriptors = new LinkedHashSet<>();
        }
        
        this.descriptor.descriptors.add(descriptor);

        return this;
    }

    public final DescriptorBuilder add(DescriptorBuilder descriptor) {
        return add(descriptor.build());
    }

    public final DescriptorBuilder add(Link link) {
        //TODO
        return this;
    }
    
    public final DescriptorBuilder add(Extension extension) {
        //TODO
        return this;
    }

    public final DescriptorBuilder add(Documentation documentation) {
        //TODO
        return this;
    }

    public final DescriptorBuilder id(URI id) {
        descriptor.id = id;
        return this;
    }
    
    public final DescriptorBuilder title(String title) {
        descriptor.title = title;
        return this;
    }
    
    public final DescriptorBuilder name(String name) {
        descriptor.name = name;
        return this;
    }

    public final DescriptorBuilder href(URI href) {
        descriptor.href = href;
        return this;
    }
    
    public final DescriptorBuilder definition(URI def) {
        //TODO
        return this;
    }
    
    public final DescriptorBuilder returnType(String URI) {
        //TODO
        return this;        
    }

    public final Descriptor build() {
        if (descriptor.descriptors == null) {
            descriptor.descriptors = Collections.emptySet();
        }
        
        return descriptor;
    }
    
}
