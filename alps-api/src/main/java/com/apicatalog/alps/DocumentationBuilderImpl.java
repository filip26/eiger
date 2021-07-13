package com.apicatalog.alps;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Documentation.Content;

final class DocumentationBuilderImpl implements DocumentationBuilder {

    private String contentType;
    private StringBuilder text;
    private URI href;
    private List<String> tag;

    public DocumentationBuilderImpl() {
        this.text = new StringBuilder();
    }

    @Override
    public Documentation build() {

        Content content = null;

        if (text.length() > 0) {
            content = new ContentImpl(contentType, text.toString());
        }

        return new DocumentationImpl(href, content, tag);
    }

    @Override
    public DocumentationBuilderImpl append(String line) {
        text.append(line);
        return this;
    }

    @Override
    public DocumentationBuilder type(String contentType) {

        if ("html".equalsIgnoreCase(contentType)) {
            this.contentType = "text/html";

        } else  if ("markdown".equalsIgnoreCase(contentType)) {
            this.contentType = "text/markdown";

        } else if ("text".equalsIgnoreCase(contentType) || "asciidoc".equals(contentType)) {
            this.contentType = "text/plain";

        } else {
            this.contentType = contentType;
        }

        return this;
    }

    @Override
    public DocumentationBuilder href(URI href) {
        this.href = href;
        return this;
    }

    @Override
    public final DocumentationBuilder tag(List<String> tag) {
        this.tag = tag;
        return this;
    }

    static final class DocumentationImpl implements Documentation {

        private final URI href;
        private final Content content;
        private List<String> tag;

        public DocumentationImpl(URI href) {
            this(href, null, null);
        }

        public DocumentationImpl(Content content) {
            this(null, content, null);
        }

        public DocumentationImpl(URI href, Content content, List<String> tag) {
            this.href = href;
            this.content = content;
            this.tag = tag;
        }

        @Override
        public Optional<URI> href() {
            return Optional.ofNullable(href);
        }

        @Override
        public Optional<Content> content() {
            return Optional.ofNullable(content);
        }

        @Override
        public List<String> tag() {
            return tag != null ? tag : Collections.emptyList();
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
