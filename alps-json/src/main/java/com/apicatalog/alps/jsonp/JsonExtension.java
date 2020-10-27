/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.apicatalog.alps.jsonp;

import java.net.URI;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.apicatalog.alps.dom.element.Extension;
import com.apicatalog.alps.error.DocumentError;
import com.apicatalog.alps.error.InvalidDocumentException;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

class JsonExtension implements Extension {

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

    protected static final Set<Extension> parse(final JsonValue jsonValue) throws InvalidDocumentException {
        
        final Set<Extension> extension = new HashSet<>();
        
        for (final JsonValue item : JsonUtils.toArray(jsonValue)) {
            
            if (JsonUtils.isObject(item)) {
                extension.add(parseObject(item.asJsonObject()));
                
            } else {
                throw new InvalidDocumentException(DocumentError.INVALID_EXTENSION, "Expected JSON string or object but was " + item.getValueType());
            }
        }        
        return extension;
    }
    
    private static final Extension parseObject(final JsonObject jsonObject) throws InvalidDocumentException {
        
        // id
        if (JsonUtils.isNotString(jsonObject.get(JsonConstants.ID))) {
            throw new InvalidDocumentException(DocumentError.MISSING_ID, "An extension must have valid 'id' property but was " + jsonObject);
        }
 
        final JsonExtension extension = new JsonExtension();
        
        try {
            
            extension.id = URI.create(jsonObject.getString(JsonConstants.ID));
            
        } catch (IllegalArgumentException e) {
            throw new InvalidDocumentException(DocumentError.MALFORMED_URI, "An extension id must be valid URI but was " + jsonObject.getString(JsonConstants.ID));
        }

        // href
        if (jsonObject.containsKey(JsonConstants.HREF)) {
            extension.href = JsonUtils.getHref(jsonObject);
        }
        
        // value
        if (jsonObject.containsKey(JsonConstants.VALUE)) {
            
            final JsonValue value = jsonObject.get(JsonConstants.VALUE);
            
            if (JsonUtils.isNotString(value)) {
                throw new InvalidDocumentException(DocumentError.INVALID_EXTENSION_VALUE, "An extension value must be represented as JSON string but was " + value);
            }
            
            extension.value = JsonUtils.getString(value);
        }
        
        // custom attributes
        final Map<String, String> attributes = new LinkedHashMap<>();
        
        for (Map.Entry<String, JsonValue> attr : jsonObject.entrySet()) {
            
            if (JsonConstants.HREF.equalsIgnoreCase(attr.getKey()) 
                    || JsonConstants.VALUE.equalsIgnoreCase(attr.getKey()) 
                    || JsonConstants.ID.equalsIgnoreCase(attr.getKey())) {
                continue;
            }

            if (JsonUtils.isScalar(attr.getValue())) {
                
                final String value;
                
                if (JsonUtils.isString(attr.getValue())) {

                    value = JsonUtils.getString(attr.getValue());
                    
                } else {
                    value = attr.toString();
                }
                
                attributes.put(attr.getKey(), value);
            }
        }
        
        extension.attributes = attributes;
        
        return extension;
    }
    
    public static final JsonValue toJson(Set<Extension> extensions) {
        
        if (extensions.size() == 1) {
            return toJson(extensions.iterator().next());
        }
        
        final JsonArrayBuilder jsonExt = Json.createArrayBuilder();
        
        extensions.stream().map(JsonExtension::toJson).forEach(jsonExt::add);
        
        return jsonExt.build();
    }

    public static final JsonValue toJson(Extension extension) {
        
        final JsonObjectBuilder jsonExt = Json.createObjectBuilder();

        jsonExt.add(JsonConstants.ID, extension.id().toString());

        extension.href().ifPresent(href -> jsonExt.add(JsonConstants.HREF, href.toString()));
        extension.value().ifPresent(value -> jsonExt.add(JsonConstants.VALUE, value));
        
        // custom attributes
        extension
                .attributes()
                .forEach((name, value) -> jsonExt.add(name, JsonUtils.toValue(value)));
        
        return jsonExt.build();
    }
}
