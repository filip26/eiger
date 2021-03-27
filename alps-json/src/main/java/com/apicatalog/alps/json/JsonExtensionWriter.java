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

import com.apicatalog.alps.dom.element.Extension;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

final class JsonExtensionWriter {

    private JsonExtensionWriter() {}

    public static final JsonValue toJson(Set<Extension> extensions) {

        if (extensions.size() == 1) {
            return toJson(extensions.iterator().next());
        }

        final JsonArrayBuilder jsonExt = Json.createArrayBuilder();

        extensions.stream().map(JsonExtensionWriter::toJson).forEach(jsonExt::add);

        return jsonExt.build();
    }

    public static final JsonValue toJson(Extension extension) {

        final JsonObjectBuilder jsonExt = Json.createObjectBuilder();

        jsonExt.add(JsonConstants.ID, extension.id().toString());

        extension.href().ifPresent(href -> jsonExt.add(JsonConstants.HREF, href.toString()));
        extension.value().ifPresent(value -> jsonExt.add(JsonConstants.VALUE, value));

        // tag
        if (!extension.tag().isEmpty()) {
            jsonExt.add(JsonConstants.TAG, extension.tag().stream().map(Object::toString).collect(Collectors.joining(" ")));
        }

        // custom attributes
        extension
                .attributes()
                .forEach((name, value) -> jsonExt.add(name, JsonUtils.toValue(value)));

        return jsonExt.build();
    }
}
