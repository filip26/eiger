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

import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.apicatalog.alps.DocumentWriter;
import com.apicatalog.alps.dom.Document;

import jakarta.json.Json;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import jakarta.json.stream.JsonGenerator;

public final class JsonDocumentWriter implements DocumentWriter {

    private final JsonWriter writer;
    private final boolean verbose;
    
    public JsonDocumentWriter(JsonWriter writer, boolean verbose) {
        this.writer = writer;
        this.verbose = verbose;
    }
    
    public static final DocumentWriter create(final Writer writer, final boolean prettyPrint, final boolean verbose) {

        final Map<String, Object> properties = new HashMap<>(1);
        
        if (prettyPrint) {
            properties.put(JsonGenerator.PRETTY_PRINTING, true);
        }

        return new JsonDocumentWriter(Json.createWriterFactory(properties).createWriter(writer), verbose);
    }

    @Override
    public void write(final Document document) {
        try {
            writer.write(JsonDocument.toJson(document, verbose));
            
        } finally {
            writer.close();             
        }
    }
}
