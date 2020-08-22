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
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;

import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.error.DocumentError;
import com.apicatalog.alps.error.InvalidDocumentException;

final class JsonDocumentation implements Documentation {

    private URI href;
    private Content content;
    
    @Override
    public Optional<URI> href() {
        return Optional.ofNullable(href);
    }

    @Override
    public Optional<Content> content() {
        return Optional.ofNullable(content);
    }

    public static Set<Documentation> parse(final JsonValue jsonValue) throws InvalidDocumentException {

        final Set<Documentation> docs = new HashSet<>();
                
        for (final JsonValue item : JsonUtils.toArray(jsonValue)) {
            
            if (JsonUtils.isString(item)) {
                docs.add(parseString((JsonString)item));
                
            } else if (JsonUtils.isObject(item)) {
                docs.add(parseObject(item.asJsonObject()));
                
            } else {
                throw new InvalidDocumentException(DocumentError.INVALID_DOC, "Expected JSON string or object but was " + item.getValueType());
            }
        }
        
        return docs;
    }
    
    private static Documentation parseString(final JsonString value) {
        
        final JsonDocumentation doc = new JsonDocumentation();

        final JsonContent content =  doc.new JsonContent();
        content.value = value.getString();
        content.type = "text/plain";
        
        doc.content = content;
        
        return doc;
    }

    private static Documentation parseObject(final JsonObject value) throws InvalidDocumentException {
        
        final JsonDocumentation doc = new JsonDocumentation();
        
        final JsonContent content = doc.new JsonContent();
        content.type = "text/plain";
        
        if (value.containsKey(JsonConstants.VALUE)) {

            final JsonValue contentValue = value.get(JsonConstants.VALUE);
            
            if (JsonUtils.isNotString(contentValue)) {
                throw new InvalidDocumentException(DocumentError.INVALID_DOC_VALUE, "doc.value property must be string but was " + contentValue.getValueType());
            }
            
            content.value = JsonUtils.getString(contentValue);
            
            doc.content = content;
            
        } else if (value.containsKey(JsonConstants.HREF)) {
            
            final JsonValue href = value.get(JsonConstants.HREF);
            
            if (JsonUtils.isNotString(href)) {
                throw new InvalidDocumentException(DocumentError.INVALID_HREF, "'href' property must have string value but was " + href.getValueType());
            }
            
            try {
            
                doc.href = URI.create(JsonUtils.getString(href));
                
            } catch (IllegalArgumentException e) {
                throw new InvalidDocumentException(DocumentError.MALFORMED_URI, "'href' property value is not URI but was " + JsonUtils.getString(href));
            }
            
        } else {
            throw new InvalidDocumentException(DocumentError.MISSING_HREF, "doc object must contain href of value property");
        }
        
        if (value.containsKey(JsonConstants.FORMAT)) {
            
            final JsonValue format = value.get(JsonConstants.FORMAT);
            
            if (JsonUtils.isNotString(format)) {
                throw new InvalidDocumentException(DocumentError.INVALID_DOC_MEDIATYPE, "doc.format property must be string but was " + format.getValueType());
            }

            content.type = JsonUtils.getString(format);
        }
        
        return doc;
    }
    
    public static final Optional<JsonValue> toJson(final Set<Documentation> documentation, final boolean verbose) {
        
        if (documentation == null || documentation.isEmpty()) {
            return Optional.empty();
        }
        
        if (documentation.size() == 1) {
            return toJson(documentation.iterator().next(), verbose);
        }
        
        final JsonArrayBuilder jsonDocs = Json.createArrayBuilder();
        
        documentation.stream().map(d -> JsonDocumentation.toJson(d, verbose)).flatMap(Optional::stream).forEach(jsonDocs::add);
        
        final JsonArray array = jsonDocs.build();
        
        return array.isEmpty() ? Optional.empty() : Optional.of(array);
    }

    public static final Optional<JsonValue> toJson(final Documentation documentation, final boolean verbose) {
        
        if (documentation == null || (documentation.href().isEmpty() && documentation.content().isEmpty())) {
            return Optional.empty();
        }
        
        final Optional<Content> content = documentation.content();
        
        if (documentation.href().isEmpty()
                && content.isPresent()
                && content
                       .map(Documentation.Content::type)
                       .filter(Predicate.isEqual("text/plain").or(Predicate.isEqual("text")))
                       .isPresent()
                ) {

            return Optional.of(Json.createValue(content.get().value()));            
        }
                     
        final JsonObjectBuilder doc = Json.createObjectBuilder();
        
        documentation.href().ifPresent(href -> doc.add(JsonConstants.HREF, href.toString()));

        if (verbose) {
            content
                .map(Documentation.Content::type)
                .ifPresentOrElse(
                        t -> doc.add(JsonConstants.FORMAT, t), 
                        () -> doc.add(JsonConstants.FORMAT, "text")
                        );
        } else {
            content
                .map(Documentation.Content::type)
                .filter(Predicate.isEqual("text/plain").negate().and(Predicate.isEqual("text").negate()))
                .ifPresent(type -> doc.add(JsonConstants.FORMAT, type));            
        }
    
        content
            .map(Documentation.Content::value)
            .ifPresent(value -> doc.add(JsonConstants.VALUE, value));
        
        final JsonObject jsonDoc = doc.build();
        
        return jsonDoc.isEmpty() ? Optional.empty() : Optional.of(jsonDoc);
    }
    
    class JsonContent implements Content {
        
        private String type;
        private String value;
        
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
