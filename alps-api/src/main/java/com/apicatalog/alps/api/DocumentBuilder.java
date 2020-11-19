package com.apicatalog.alps.api;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.dom.element.Descriptor;
import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Link;

public final class DocumentBuilder {

    final DocumentImpl document;
    
    Set<Documentation> docs;
    
    public DocumentBuilder(DocumentVersion version) {
        this.document = new DocumentImpl(version);  
    }
    
    public Document build() {
        if (document.docs == null) {
            document.docs = Collections.emptySet();
        }
        if (document.links == null) {
            document.links = Collections.emptySet();
        }
        if (document.descriptors == null) {
            document.descriptors = Collections.emptySet();
        }
        
        return document;
    }

    public DocumentBuilder add(Descriptor descriptor) {
        
        if (document.descriptors == null) {
            document.descriptors = new LinkedHashSet<>();
        }
        
        document.descriptors.add(descriptor);
        
        return this;
    }
    
    public DocumentBuilder add(DescriptorBuilder descriptor) {
        return add(descriptor.build());
    }
    
    public DocumentBuilder add(Documentation documentation) {
        
        if (document.docs == null) {
            document.docs = new LinkedHashSet<>();
        }
        
        document.docs.add(documentation);
        
        return this;
    }
    
    public DocumentBuilder add(DocumentationBuilder documentation) {
        return add(documentation.build());
    }

    public DocumentBuilder add(Link link) {
        if (document.links == null) {
            document.links = new LinkedHashSet<>();
        }
        
        document.links().add(link);
        
        return this;
    }

}
