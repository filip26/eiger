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

import java.io.IOException;
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
import com.apicatalog.alps.error.DocumentException;

public final class JsonDocumentWriter implements DocumentWriter {

    private final JsonWriterFactory writerFactory;
    
    public JsonDocumentWriter(JsonWriterFactory writerFactory) {
        this.writerFactory = writerFactory;
    }
    
    public static final DocumentWriter create(boolean prettyPrint) {

        final Map<String, Object> properties = new HashMap<>(1);
        
        if (prettyPrint) {
            properties.put(JsonGenerator.PRETTY_PRINTING, true);
        }

        return new JsonDocumentWriter(Json.createWriterFactory(properties));
    }
    
    @Override
    public void write(String mediaType, Document document, OutputStream stream) throws IOException, DocumentException {
        
        //TODO check media type and arguments
    
        write(document, writerFactory.createWriter(stream));
    }

    @Override
    public void write(final String mediaType, final Document document, final Writer writer) throws IOException, DocumentException {

        //TODO check media type and arguments

        write(document, writerFactory.createWriter(writer));
    }

    private void write(Document document, JsonWriter jsonWriter) throws IOException, DocumentException {
        jsonWriter.write(JsonDocument.toJson(document));
    }

}
