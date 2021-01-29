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
package com.apicatalog.alps.json;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Documentation.Content;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

final class JsonDocumentationWriter {
    
    private JsonDocumentationWriter() {}
    
    public static final Optional<JsonValue> toJson(final Set<Documentation> documentation, final boolean verbose) {
        
        if (documentation == null || documentation.isEmpty()) {
            return Optional.empty();
        }
        
        if (documentation.size() == 1) {
            return toJson(documentation.iterator().next(), verbose);
        }
        
        final JsonArrayBuilder jsonDocs = Json.createArrayBuilder();
        
        documentation.stream().map(d -> JsonDocumentationWriter.toJson(d, verbose)).flatMap(Optional::stream).forEach(jsonDocs::add);
        
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
                       .filter(Predicate.isEqual(JsonConstants.MEDIA_TYPE_TEXT_PLAIN).or(Predicate.isEqual("text")))
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
                        t -> doc.add(JsonConstants.CONTENT_TYPE, t), 
                        () -> doc.add(JsonConstants.CONTENT_TYPE, JsonConstants.MEDIA_TYPE_TEXT_PLAIN)
                        );
        } else {
            content
                .map(Documentation.Content::type)
                .filter(Predicate.isEqual(JsonConstants.MEDIA_TYPE_TEXT_PLAIN).negate().and(Predicate.isEqual("text").negate()))
                .ifPresent(type -> doc.add(JsonConstants.CONTENT_TYPE, type));            
        }
    
        content
            .map(Documentation.Content::value)
            .ifPresent(value -> doc.add(JsonConstants.VALUE, value));
        
        final JsonObject jsonDoc = doc.build();
        
        return jsonDoc.isEmpty() ? Optional.empty() : Optional.of(jsonDoc);
    }    
}
