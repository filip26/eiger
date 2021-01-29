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

import com.apicatalog.alps.dom.element.Descriptor;
import com.apicatalog.alps.dom.element.DescriptorType;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

final class JsonDescriptorWriter {
    
    private JsonDescriptorWriter() {}

    public static final JsonValue toJson(final Set<Descriptor> descriptors, final boolean verbose) {
        
        if (descriptors.size() == 1) {
            return toJson(descriptors.iterator().next(), verbose);
        }
        
        final JsonArrayBuilder jsonDescriptors = Json.createArrayBuilder();
        
        descriptors.stream().map(d -> JsonDescriptorWriter.toJson(d, verbose)).forEach(jsonDescriptors::add);
        
        return jsonDescriptors.build();
    }

    public static final JsonValue toJson(final Descriptor descriptor, final boolean verbose) {
        
        final JsonObjectBuilder jsonDescriptor = Json.createObjectBuilder();
        
        descriptor.id().ifPresent(id -> jsonDescriptor.add(JsonConstants.ID, id.toString()));
        
        if (descriptor.type() != null && !DescriptorType.SEMANTIC.equals(descriptor.type())) {
            jsonDescriptor.add(JsonConstants.TYPE, descriptor.type().name().toLowerCase());
            
        } else if (verbose) {
            jsonDescriptor.add(JsonConstants.TYPE, DescriptorType.SEMANTIC.name().toLowerCase());
        }
        
        descriptor.href().ifPresent(href -> jsonDescriptor.add(JsonConstants.HREF, href.toString()));
        descriptor.definition().ifPresent(def -> jsonDescriptor.add(JsonConstants.DEFINITION, def.toString()));
        descriptor.name().ifPresent(name -> jsonDescriptor.add(JsonConstants.NAME, name));
        descriptor.title().ifPresent(title -> jsonDescriptor.add(JsonConstants.TITLE, title));
        descriptor.returnType().ifPresent(rt -> jsonDescriptor.add(JsonConstants.RETURN_TYPE, rt.toString()));

        // documentation
        JsonDocumentationWriter.toJson(descriptor.documentation(), verbose).ifPresent(doc -> jsonDescriptor.add(JsonConstants.DOCUMENTATION, doc));
        
        // descriptors
        if (JsonDocumentWriter.isNotEmpty(descriptor.descriptors())) {
            jsonDescriptor.add(JsonConstants.DESCRIPTOR, toJson(descriptor.descriptors(), verbose));
        }

        // links
        if (JsonDocumentWriter.isNotEmpty(descriptor.links())) {
            jsonDescriptor.add(JsonConstants.LINK, JsonLinkWriter.toJson(descriptor.links()));
        }

        // extensions
        if (JsonDocumentWriter.isNotEmpty(descriptor.extensions())) {
            jsonDescriptor.add(JsonConstants.EXTENSION, JsonExtensionWriter.toJson(descriptor.extensions()));
        }

        return jsonDescriptor.build();
    }  
}
