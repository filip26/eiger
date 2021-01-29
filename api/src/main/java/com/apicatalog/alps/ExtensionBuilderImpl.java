package com.apicatalog.alps;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import com.apicatalog.alps.dom.element.Extension;

final class ExtensionBuilderImpl implements ExtensionBuilder {

    private ExtensionImpl extension;
    
    public ExtensionBuilderImpl() {
        this.extension = new ExtensionImpl();
        this.extension.attributes = new LinkedHashMap<>();
    }
    
    @Override
    public Extension build() {
        return extension;
    }

    @Override
    public ExtensionBuilder attribute(String key, String value) {
        extension.attributes.put(key, value);
        return this;
    }

    @Override
    public ExtensionBuilder href(URI href) {
        extension.href = href;
        return this;
    }

    @Override
    public ExtensionBuilder value(String value) {
        extension.value = value;
        return this;
    }

    @Override
    public ExtensionBuilder id(URI id) {
        extension.id = id;
        return this;
    }    

    static final class ExtensionImpl implements Extension {

        private URI id;
        private URI href;
        private String value;
        
        private Map<String, String> attributes;
        
        @Override
        public Optional<URI> href() {
            return Optional.ofNullable(href);
        }
    
        @Override
        public URI id() {
            return id;
        }
    
        @Override
        public Optional<String> value() {
            return Optional.ofNullable(value);
        }
        
        @Override
        public Map<String, String> attributes() {
            return attributes;
        }
    }
}
