package com.apicatalog.alps.api;

import java.net.URI;

import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Documentation.Content;

public class DocumentationBuilder {

    private String contentType;
    private StringBuilder text;
    private URI href;

    public DocumentationBuilder(String contentType) {
        this.contentType = contentType;
        this.text = new StringBuilder();
    }
    
    public Documentation build() {
        
        Content content = null;
        
        if (text.length() > 0) {
            content = new DocumentationImpl.ContentImpl(contentType, text.toString());
        }

        return new DocumentationImpl(href, content);
    }

    public DocumentationBuilder append(String line) {
        text.append(line);
        return this;
    }
    
}
