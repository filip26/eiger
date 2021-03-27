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

import java.util.Set;
import java.util.stream.Collectors;

import com.apicatalog.alps.dom.element.Link;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

final class JsonLinkWriter {

    private JsonLinkWriter() {}

    public static final JsonValue toJson(Set<Link> links) {

        if (links.size() == 1) {
            return toJson(links.iterator().next());
        }

        final JsonArrayBuilder jsonLinks = Json.createArrayBuilder();

        links.stream().map(JsonLinkWriter::toJson).forEach(jsonLinks::add);

        return jsonLinks.build();
    }

    public static final JsonValue toJson(Link link) {

        final JsonObjectBuilder jsonLink = Json.createObjectBuilder();

        if (link.href() != null) {
            jsonLink.add(JsonConstants.HREF, link.href().toString());
        }

        if (link.rel() != null && !link.rel().isBlank()) {
            jsonLink.add(JsonConstants.RELATION, link.rel());
        }

        // tag
        if (!link.tag().isEmpty()) {
            jsonLink.add(JsonConstants.TAG, link.tag().stream().map(Object::toString).collect(Collectors.joining(" ")));
        }

        return jsonLink.build();
    }
}
