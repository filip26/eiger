package com.apicatalog.alps.api;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.dom.DocumentVersion;

public final class DocumentBuilder {

    final DocumentImpl document;
    
    public DocumentBuilder(DocumentVersion version) {
        this.document = new DocumentImpl();
        this.document.version = version;
    }
    
    public Document build() {
        return document;
    }
    
}
