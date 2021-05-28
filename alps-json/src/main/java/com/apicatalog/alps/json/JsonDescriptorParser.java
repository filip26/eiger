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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.apicatalog.alps.Alps;
import com.apicatalog.alps.DescriptorBuilder;
import com.apicatalog.alps.dom.element.Descriptor;
import com.apicatalog.alps.dom.element.DescriptorType;
import com.apicatalog.alps.error.DocumentError;
import com.apicatalog.alps.error.InvalidDocumentException;

import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

final class JsonDescriptorParser {

    private JsonDescriptorParser() {}

    public static Set<Descriptor> parse(JsonValue jsonValue) throws InvalidDocumentException {

        if (JsonUtils.isObject(jsonValue)) {

            return Set.of(parseObject(jsonValue.asJsonObject()));

        } else if (JsonUtils.isArray(jsonValue)) {

            final HashSet<Descriptor> descriptors = new HashSet<>();

            for (final JsonValue item : jsonValue.asJsonArray()) {

                if (JsonUtils.isObject(item)) {
                    descriptors.add(parseObject(item.asJsonObject()));

                } else {
                    throw new InvalidDocumentException(DocumentError.INVALID_DESCRIPTOR, "The 'descriptor' property must be an object or an array of objects but was " + item.getValueType());
                }
            }

            return descriptors;

        } else {
            throw new InvalidDocumentException(DocumentError.INVALID_DESCRIPTOR, "The 'descriptor' property must be an object or an array of objects but was " + jsonValue.getValueType());
        }
    }

    private static Descriptor parseObject(JsonObject jsonObject) throws InvalidDocumentException {

        final DescriptorBuilder builder = Alps.createDescriptor();

        if (!jsonObject.containsKey(JsonConstants.ID) && !jsonObject.containsKey(JsonConstants.HREF)) {
            throw new InvalidDocumentException(DocumentError.MISSING_ID, "Descriptor must define valid 'id' or 'href' property");
        }

        parseId(builder, jsonObject);
        parseHref(builder, jsonObject);

        // name
        if (jsonObject.containsKey(JsonConstants.NAME)) {
            final JsonValue name = jsonObject.get(JsonConstants.NAME);

            if (JsonUtils.isNotString(name)) {
                throw new InvalidDocumentException(DocumentError.INVALID_NAME, "The 'name' property value must be JSON string but was " + name);
            }

            builder.name(JsonUtils.getString(name));
        }

        // title
        if (jsonObject.containsKey(JsonConstants.TITLE)) {
            final JsonValue title = jsonObject.get(JsonConstants.TITLE);

            if (JsonUtils.isNotString(title)) {
                throw new InvalidDocumentException(DocumentError.INVALID_TITLE, "The 'title' property value must be JSON string but was " + title);
            }

            builder.title(JsonUtils.getString(title));
        }

        parseType(builder, jsonObject);

        // documentation
        if (jsonObject.containsKey(JsonConstants.DOCUMENTATION)) {
            JsonDocumentationParser.parse(jsonObject.get(JsonConstants.DOCUMENTATION)).forEach(builder::add);
        }

        // links
        if (jsonObject.containsKey(JsonConstants.LINK)) {
            JsonLinkParser.parse(jsonObject.get(JsonConstants.LINK)).forEach(builder::add);
        }

        parseReturnType(builder, jsonObject);

        builder.tag(parseTag(jsonObject));

        // nested descriptors
        if (jsonObject.containsKey(JsonConstants.DESCRIPTOR)) {
            JsonDescriptorParser.parse(jsonObject.get(JsonConstants.DESCRIPTOR)).forEach(builder::add);
        }

        // extensions
        if (jsonObject.containsKey(JsonConstants.EXTENSION)) {
            JsonExtensionParser.parse(jsonObject.get(JsonConstants.EXTENSION)).forEach(builder::add);
        }

        return builder.build();
    }

    private static void parseId(DescriptorBuilder builder, JsonObject jsonObject) throws InvalidDocumentException {

        // id
        if (jsonObject.containsKey(JsonConstants.ID)) {
            if (JsonUtils.isNotString(jsonObject.get(JsonConstants.ID))) {
                throw new InvalidDocumentException(DocumentError.INVALID_ID, "The 'id' property value must be valid URI represented as JSON string but was " + jsonObject.get(JsonConstants.ID));
            }

            try {
                builder.id(URI.create(jsonObject.getString(JsonConstants.ID)));

            } catch (IllegalArgumentException e) {
                throw new InvalidDocumentException(DocumentError.MALFORMED_URI, "The 'id' must be valid URI but was " + jsonObject.getString(JsonConstants.ID));
            }
        }
    }

    private static void parseHref(DescriptorBuilder builder, JsonObject jsonObject) throws InvalidDocumentException {

        // href
        if (jsonObject.containsKey(JsonConstants.HREF)) {
            builder.href(JsonUtils.getHref(jsonObject));
        }

        if (jsonObject.containsKey(JsonConstants.DEFINITION)) {
            builder.definition(JsonUtils.getDefinition(jsonObject));
        }
    }

    private static void parseType(DescriptorBuilder builder, JsonObject jsonObject) throws InvalidDocumentException {

        // type
        if (jsonObject.containsKey(JsonConstants.TYPE)) {

            final JsonValue type = jsonObject.get(JsonConstants.TYPE);

            if (JsonUtils.isNotString(type)) {
                throw new InvalidDocumentException(DocumentError.INVALID_TYPE, "The 'type' property value must be JSON string but was " + type);
            }

            try {
                builder.type(DescriptorType.valueOf(JsonUtils.getString(type).toUpperCase()));

            } catch (IllegalArgumentException e) {
                throw new InvalidDocumentException(DocumentError.INVALID_TYPE, "The 'type' property value must be one of " + (Arrays.stream(DescriptorType.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.joining(", " ))) +  " but was " + type);
            }
        }
    }

    protected static List<String> parseTag(final JsonObject jsonObject) throws InvalidDocumentException {

        // tag
        if (jsonObject.containsKey(JsonConstants.TAG)) {

            final JsonValue tag = jsonObject.get(JsonConstants.TAG);

            if (JsonUtils.isNotString(tag)) {
                throw new InvalidDocumentException(DocumentError.INVALID_TYPE, "The 'tag' property value must be JSON string but was " + tag);
            }

            try {
                final String value = JsonUtils.getString(tag);

                if (value != null && !value.isBlank()) {
                    return Arrays.asList(value.split("\\s+")).stream().filter(Predicate.not(String::isBlank)).collect(Collectors.toList());
                }

            } catch (IllegalArgumentException e) {
                throw new InvalidDocumentException(DocumentError.INVALID_TYPE, "The 'tag' property value must be one of " + (Arrays.stream(DescriptorType.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.joining(", " ))) +  " but was " + tag);
            }
        }

        return Collections.emptyList();
    }


    private static void parseReturnType(DescriptorBuilder builder, JsonObject jsonObject) throws InvalidDocumentException {

        // return type
        if (jsonObject.containsKey(JsonConstants.RETURN_TYPE)) {

            final JsonValue returnType = jsonObject.get(JsonConstants.RETURN_TYPE);

            if (JsonUtils.isNotString(returnType)) {
                throw new InvalidDocumentException(DocumentError.INVALID_RT, "The 'rt' property value must be URI represented as JSON string but was " + returnType);
            }

            try {
                builder.returnType(URI.create(JsonUtils.getString(returnType)));

            } catch (IllegalArgumentException e) {
                throw new InvalidDocumentException(DocumentError.MALFORMED_URI, "The 'rt' property value must be URI represented as JSON string but was " + returnType);
            }
        }
    }
}
