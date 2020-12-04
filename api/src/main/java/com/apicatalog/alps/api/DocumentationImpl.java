package com.apicatalog.alps.api;

import java.net.URI;
import java.util.Optional;

import com.apicatalog.alps.dom.element.Documentation;

class DocumentationImpl implements Documentation {

    private final URI href;
    private final Content content;
    
    public DocumentationImpl(URI href) {
        this(href, null);
    }

    public DocumentationImpl(Content content) {
        this(null, content);
    }

    public DocumentationImpl(URI href, Content content) {
        this.href = href;
        this.content = content;
    }

    @Override
    public Optional<URI> href() {
        return Optional.ofNullable(href);
    }

    @Override
    public Optional<Content> content() {
        return Optional.ofNullable(content);
    }

    static class ContentImpl implements Content {

        final String type;
        final String value;
        
        public ContentImpl(String type, String value) {
            this.type = type;
            this.value = value;
        }
        
        @Override
        public String type() {
            return type;
        }

        @Override
        public String value() {
            return value;
        }
    }
    
}
