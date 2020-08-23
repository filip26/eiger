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

import javax.json.Json;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

import com.apicatalog.alps.DocumentWriter;
import com.apicatalog.alps.dom.Document;

public final class JsonDocumentWriter implements DocumentWriter {

    private final JsonWriterFactory writerFactory;
    private final boolean verbose;
    
    public JsonDocumentWriter(JsonWriterFactory writerFactory, boolean verbose) {
        this.writerFactory = writerFactory;
        this.verbose = verbose;
    }
    
    public static final DocumentWriter create(final boolean prettyPrint, final boolean verbose) {

        final Map<String, Object> properties = new HashMap<>(1);
        
        if (prettyPrint) {
            properties.put(JsonGenerator.PRETTY_PRINTING, true);
        }

        return new JsonDocumentWriter(Json.createWriterFactory(properties), verbose);
    }
    
    @Override
    public void write(final Document document, final OutputStream stream) {
        write(document, writerFactory.createWriter(stream));
    }

    @Override
    public void write(final Document document, final Writer writer) {
        write(document, writerFactory.createWriter(writer));
    }

    private final void write(final Document document, final JsonWriter jsonWriter) {
        jsonWriter.write(JsonDocument.toJson(document, verbose));
        jsonWriter.close();
    }
}
