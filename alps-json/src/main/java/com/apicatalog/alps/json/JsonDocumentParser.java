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

import java.io.InputStream;
import java.io.Reader;
import java.net.URI;

import com.apicatalog.alps.Alps;
import com.apicatalog.alps.DocumentBuilder;
import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.error.DocumentError;
import com.apicatalog.alps.error.DocumentParserException;
import com.apicatalog.alps.error.InvalidDocumentException;
import com.apicatalog.alps.error.MalformedDocumentException;
import com.apicatalog.alps.io.DocumentParser;

import jakarta.json.Json;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParser.Event;
import jakarta.json.stream.JsonParsingException;

public final class JsonDocumentParser implements DocumentParser {

    @Override
    public Document parse(final URI baseUri, final InputStream stream) throws DocumentParserException {

        if (stream == null) {
            throw new IllegalArgumentException();
        }

        try {

            return parse(baseUri, Json.createParser(stream));

        } catch (JsonException e) {
            throw new DocumentParserException(e);
        }
    }

    @Override
    public Document parse(final URI baseUri, final Reader reader) throws DocumentParserException {

        if (reader == null) {
            throw new IllegalArgumentException();
        }

        try {

            return parse(baseUri, Json.createParser(reader));

        } catch (JsonException e) {
            throw new DocumentParserException(e);
        }
    }

    private static final Document parse(URI baseUri, JsonParser parser)  throws DocumentParserException {

        try {

            if (!parser.hasNext()) {
                throw new DocumentParserException("Expected JSON object but was an empty input");
            }

            final Event event = parser.next();

            if (!Event.START_OBJECT.equals(event)) {
                throw new DocumentParserException("Expected JSON object but was " + event);
            }

            final JsonObject rootObject = parser.getObject();

            if (!rootObject.containsKey(JsonConstants.ROOT)) {
                throw new InvalidDocumentException(DocumentError.MISSING_ROOT, "Property '" + JsonConstants.ROOT + "' is not present");
            }

            final JsonValue alpsObject = rootObject.get(JsonConstants.ROOT);

            if (JsonUtils.isNotObject(alpsObject)) {
                throw new InvalidDocumentException(DocumentError.MISSING_ROOT, "Property '" + JsonConstants.ROOT + "' does not contain JSON object");
            }

            return parse(baseUri, alpsObject.asJsonObject());

        } catch (JsonParsingException e) {
            throw new MalformedDocumentException(e.getLocation().getLineNumber(), e.getLocation().getColumnNumber(), "Document is not valid JSON document.");
        }
    }

    public static final Document parse(final URI baseUri, final JsonObject alpsObject) throws DocumentParserException {

        final DocumentBuilder builder = Alps.createDocument(DocumentVersion.VERSION_1_0).base(baseUri);

        // documentation
        if (alpsObject.containsKey(JsonConstants.DOCUMENTATION)) {
            JsonDocumentationParser.parse(alpsObject.get(JsonConstants.DOCUMENTATION)).forEach(builder::add);
        }

        // links
        if (alpsObject.containsKey(JsonConstants.LINK)) {
            JsonLinkParser.parse(alpsObject.get(JsonConstants.LINK)).forEach(builder::add);
        }

        // descriptors
        if (alpsObject.containsKey(JsonConstants.DESCRIPTOR)) {
            JsonDescriptorParser.parse(alpsObject.get(JsonConstants.DESCRIPTOR)).forEach(builder::add);
        }

        // extensions
        if (alpsObject.containsKey(JsonConstants.EXTENSION)) {
            JsonExtensionParser.parse(alpsObject.get(JsonConstants.EXTENSION)).forEach(builder::add);
        }

        return builder.build();
    }
}
