package com.apicatalog.alps.jsonp;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

import javax.json.JsonValue;

import com.apicatalog.alps.dom.element.AlpsExtension;

class JsonExtesion implements AlpsExtension {

    private URI id;
    private URI href;
    private String value;
    
    @Override
    public Optional<URI> getHref() {
        return Optional.ofNullable(href);
    }

    @Override
    public URI getId() {
        return id;
    }

    @Override
    public Optional<String> getValue() {
        return Optional.ofNullable(value);
    }

    protected static final Set<AlpsExtension> parse(final JsonValue value) {
        //TODO
        return null;
    }
}
