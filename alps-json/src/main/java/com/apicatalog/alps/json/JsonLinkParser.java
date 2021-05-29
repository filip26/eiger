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
import java.util.Set;

import com.apicatalog.alps.Alps;
import com.apicatalog.alps.dom.element.Link;
import com.apicatalog.alps.error.DocumentError;
import com.apicatalog.alps.error.InvalidDocumentException;

import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;

final class JsonLinkParser {

    private JsonLinkParser() {}

    public static final Set<Link> parse(final JsonValue value) throws InvalidDocumentException {

        final Set<Link> links = new HashSet<>();

        for (final JsonValue item : JsonUtils.toArray(value)) {

            if (JsonUtils.isNotObject(item)) {
                throw new InvalidDocumentException(DocumentError.INVALID_LINK, "Link property must be JSON object but was " + item.getValueType());
            }

            links.add(parseObject(item.asJsonObject()));
        }

        return links;
    }

    private static final Link parseObject(final JsonObject linkObject) throws InvalidDocumentException {


        if (!linkObject.containsKey(JsonConstants.HREF)) {
            throw new InvalidDocumentException(DocumentError.MISSING_HREF, "Link object must contain 'href' property");
        }

        if (!linkObject.containsKey(JsonConstants.RELATION)) {
            throw new InvalidDocumentException(DocumentError.MISSING_REL, "Link object must contain 'rel' property");
        }

        final JsonValue jsonHref = linkObject.get(JsonConstants.HREF);
        final URI href;

        if (JsonUtils.isNotString(jsonHref)) {
            throw new InvalidDocumentException(DocumentError.MALFORMED_URI, "Link.href property must be URI but was " + jsonHref.getValueType());
        }

        try {

            href = URI.create(JsonUtils.getString(jsonHref));

        } catch (IllegalArgumentException e) {
            throw new InvalidDocumentException(DocumentError.MALFORMED_URI, "Link.href property must be URI but was " + jsonHref);
        }

        final JsonValue jsonRel = linkObject.get(JsonConstants.RELATION);

        if (JsonUtils.isNotString(jsonHref)) {
            throw new InvalidDocumentException(DocumentError.INVALID_REL, "Link.rel property must be string but was " + jsonRel.getValueType());
        }

        String title = null;
        final JsonValue jsonTitle = linkObject.get(JsonConstants.TITLE);

        if (jsonTitle != null) {
        
            if (JsonUtils.isNotString(jsonTitle)) {
                throw new InvalidDocumentException(DocumentError.INVALID_REL, "Link.title property must be string but was " + jsonTitle.getValueType());
            }

            title = ((JsonString)jsonTitle).getString();
        }

        return Alps.createLink()
                    .href(href)
                    .rel(JsonUtils.getString(jsonRel))
                    .title(title)
                    .tag(JsonDescriptorParser.parseTag(linkObject))
                    .build();
    }
}
