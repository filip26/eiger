package com.apicatalog.alps;

import java.net.URI;
import java.util.Optional;

import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Documentation.Content;

final class DocumentationBuilderImpl implements DocumentationBuilder {

    private String contentType;
    private StringBuilder text;
    private URI href;

    public DocumentationBuilderImpl(String contentType) {
        this.contentType = contentType;
        this.text = new StringBuilder();
    }
    
    @Override
    public Documentation build() {
        
        Content content = null;
        
        if (text.length() > 0) {
            content = new ContentImpl(contentType, text.toString());
        }

        return new DocumentationImpl(href, content);
    }

    @Override
    public DocumentationBuilderImpl append(String line) {
        text.append(line);
        return this;
    }

    @Override
    public DocumentationBuilder type(String contentType) {
        this.contentType = contentType;
        return this;
    }

    @Override
    public DocumentationBuilder href(URI href) {
        this.href = href;
        return this;
    }
    
    static final class DocumentationImpl implements Documentation {

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
        
    }
    
    static final class ContentImpl implements Content {

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
