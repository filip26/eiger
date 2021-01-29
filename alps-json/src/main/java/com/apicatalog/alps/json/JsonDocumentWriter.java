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

import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.io.DocumentWriter;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonWriter;
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
        writer.write(toJson(document, verbose));            
    }
    
    @Override
    public void close() throws Exception {
        writer.close();
    }
    
    protected static final JsonObject toJson(final Document document, final boolean verbose) {
        
        final JsonObjectBuilder alps = Json.createObjectBuilder();

        // version
        alps.add(JsonConstants.VERSION, JsonConstants.VERSION_1_0);
        
        // documentation
        JsonDocumentationWriter.toJson(document.documentation(), verbose).ifPresent(doc -> alps.add(JsonConstants.DOCUMENTATION, doc));
        
        // links
        if (isNotEmpty(document.links())) {
            alps.add(JsonConstants.LINK, JsonLinkWriter.toJson(document.links()));
        }
        
        // descriptors
        if (isNotEmpty(document.descriptors())) {
            alps.add(JsonConstants.DESCRIPTOR, JsonDescriptorWriter.toJson(document.descriptors(), verbose));
        }
        
        // extensions
        if (isNotEmpty(document.extensions())) {
            alps.add(JsonConstants.EXTENSION, JsonExtensionWriter.toJson(document.extensions()));            
        }

        return Json.createObjectBuilder().add(JsonConstants.ROOT, alps).build();
    }
    
    protected static final boolean isNotEmpty(final Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }
}
