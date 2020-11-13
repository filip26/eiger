package com.apicatalog.alps.api;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.dom.DocumentVersion;
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
        
        return document;
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
