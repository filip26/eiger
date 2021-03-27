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

import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.apicatalog.alps.Alps;
import com.apicatalog.alps.ExtensionBuilder;
import com.apicatalog.alps.dom.element.Extension;
import com.apicatalog.alps.error.DocumentError;
import com.apicatalog.alps.error.InvalidDocumentException;

import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

final class JsonExtensionParser {

    private JsonExtensionParser() {}

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

        final ExtensionBuilder builder = Alps.createExtension();

        try {

            builder.id(URI.create(jsonObject.getString(JsonConstants.ID)));

        } catch (IllegalArgumentException e) {
            throw new InvalidDocumentException(DocumentError.MALFORMED_URI, "An extension id must be valid URI but was " + jsonObject.getString(JsonConstants.ID));
        }

        // href
        if (jsonObject.containsKey(JsonConstants.HREF)) {
            builder.href(JsonUtils.getHref(jsonObject));
        }

        // value
        if (jsonObject.containsKey(JsonConstants.VALUE)) {

            final JsonValue value = jsonObject.get(JsonConstants.VALUE);

            if (JsonUtils.isNotString(value)) {
                throw new InvalidDocumentException(DocumentError.INVALID_EXTENSION_VALUE, "An extension value must be represented as JSON string but was " + value);
            }

            builder.value(JsonUtils.getString(value));
        }

        // tag
        builder.tag(JsonDescriptorParser.parseTag(jsonObject));

        return parseAttributes(builder, jsonObject.entrySet()).build();
    }

    private static final ExtensionBuilder parseAttributes(ExtensionBuilder builder, Set<Map.Entry<String, JsonValue>> attrs) {

        // custom attributes
        for (Map.Entry<String, JsonValue> attr : attrs) {

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

                builder.attribute(attr.getKey(), value);
            }
        }

        return builder;
    }

}
