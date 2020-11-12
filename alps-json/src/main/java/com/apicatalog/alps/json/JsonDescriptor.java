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
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.apicatalog.alps.dom.element.Descriptor;
import com.apicatalog.alps.dom.element.DescriptorType;
import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Extension;
import com.apicatalog.alps.dom.element.Link;
import com.apicatalog.alps.error.DocumentError;
import com.apicatalog.alps.error.InvalidDocumentException;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

final class JsonDescriptor implements Descriptor {

    private URI id;
    
    private URI href;
    
    private URI definition;
    
    private String name;
    
    private DescriptorType type;
    
    private URI returnType;
    
    private Set<Documentation> doc;
    
    private Set<Descriptor> descriptors;
    
    private Set<Link> links;
    
    private Set<Extension> extensions;
    
    private Descriptor parent;
    
    private JsonDescriptor() {
        // default values
        this.type = DescriptorType.SEMANTIC;
    }
    
    @Override
    public Optional<URI> id() {
        return Optional.ofNullable(id);
    }

    @Override
    public Optional<URI> href() {
        return Optional.ofNullable(href);
    }

    @Override
    public Optional<String> name() {
        return Optional.ofNullable(name);
    }

    @Override
    public DescriptorType type() {
        return type;
    }

    @Override
    public Optional<URI> returnType() {
        return Optional.ofNullable(returnType);
    }

    @Override
    public Set<Documentation> documentation() {
        return doc;
    }

    @Override
    public Set<Extension> extensions() {
        return extensions;
    }

    @Override
    public Set<Descriptor> descriptors() {
        return descriptors;
    }

    @Override
    public Set<Link> links() {
        return links;
    }
    
    @Override
    public Optional<Descriptor> parent() {
        return Optional.ofNullable(parent);
    }
    
    @Override
    public Optional<URI> definition() {
        return Optional.ofNullable(definition);
    }

    public static Set<Descriptor> parse(Map<URI, Descriptor> index, JsonValue jsonValue) throws InvalidDocumentException {
        return parse(index, null, jsonValue);
    }
    
    private static Set<Descriptor> parse(Map<URI, Descriptor> index, JsonDescriptor parent, JsonValue jsonValue) throws InvalidDocumentException {
        
        if (JsonUtils.isObject(jsonValue)) {

            return Set.of(parseObject(index, parent, jsonValue.asJsonObject()));

        } else if (JsonUtils.isArray(jsonValue)) {
            
            final HashSet<Descriptor> descriptors = new HashSet<>();
            
            for (final JsonValue item : jsonValue.asJsonArray()) {
                
                if (JsonUtils.isObject(item)) {
                    descriptors.add(parseObject(index, parent, item.asJsonObject()));
                    
                } else {
                    throw new InvalidDocumentException(DocumentError.INVALID_DESCRIPTOR, "The 'descriptor' property must be an object or an array of objects but was " + item.getValueType());
                }
            }
            
            return descriptors;
            
        } else {
            throw new InvalidDocumentException(DocumentError.INVALID_DESCRIPTOR, "The 'descriptor' property must be an object or an array of objects but was " + jsonValue.getValueType());
        }
    }
    
    private static Descriptor parseObject(Map<URI, Descriptor> index, JsonDescriptor parent, JsonObject jsonObject) throws InvalidDocumentException {
        
        final JsonDescriptor descriptor = new JsonDescriptor();
        descriptor.parent = parent;

        if (!jsonObject.containsKey(JsonConstants.ID) && !jsonObject.containsKey(JsonConstants.HREF)) {
            throw new InvalidDocumentException(DocumentError.MISSING_ID, "Descriptor must define valid 'id' or 'href' property");            
        }
        
        // id
        if (jsonObject.containsKey(JsonConstants.ID)) {
            if (JsonUtils.isNotString(jsonObject.get(JsonConstants.ID))) {
                throw new InvalidDocumentException(DocumentError.INVALID_ID, "The 'id' property value must be valid URI represented as JSON string but was " + jsonObject.get(JsonConstants.ID));
            }
            
            try {
                descriptor.id = URI.create(jsonObject.getString(JsonConstants.ID));
                        
            } catch (IllegalArgumentException e) {
                throw new InvalidDocumentException(DocumentError.MALFORMED_URI, "The 'id' must be valid URI but was " + jsonObject.getString(JsonConstants.ID));
            }
            
            // check id conflict
            if (index.containsKey(descriptor.id)) {
                throw new InvalidDocumentException(DocumentError.DUPLICATED_ID, "Duplicate 'id' property value " + descriptor.id);
            }
            
            index.put(descriptor.id, descriptor);
        }

        // href
        if (jsonObject.containsKey(JsonConstants.HREF)) {
            descriptor.href = JsonUtils.getHref(jsonObject);
        }

        if (jsonObject.containsKey(JsonConstants.DEFINITION)) {
            descriptor.href = JsonUtils.getDefinition(jsonObject);
        }

        // name
        if (jsonObject.containsKey(JsonConstants.NAME)) {
            final JsonValue name = jsonObject.get(JsonConstants.NAME);
            
            if (JsonUtils.isNotString(name)) {
                throw new InvalidDocumentException(DocumentError.INVALID_NAME, "The 'name' property value must be JSON string but was " + name);
            }
            
            descriptor.name = JsonUtils.getString(name);
        }

        // type
        if (jsonObject.containsKey(JsonConstants.TYPE)) {
            
            final JsonValue type = jsonObject.get(JsonConstants.TYPE);
            
            if (JsonUtils.isNotString(type)) {
                throw new InvalidDocumentException(DocumentError.INVALID_TYPE, "The 'type' property value must be JSON string but was " + type);
            }
            
            try {
                descriptor.type = DescriptorType.valueOf(JsonUtils.getString(type).toUpperCase());
                
            } catch (IllegalArgumentException e) {
                throw new InvalidDocumentException(DocumentError.INVALID_TYPE, "The 'type' property value must be one of " + (Arrays.stream(DescriptorType.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.joining(", " ))) +  " but was " + type);
            }
        }

        // documentation
        if (jsonObject.containsKey(JsonConstants.DOCUMENTATION)) {
            descriptor.doc = JsonDocumentation.parse(jsonObject.get(JsonConstants.DOCUMENTATION));
            
        } else {
            descriptor.doc = Collections.emptySet();
        }
        
        // links
        if (jsonObject.containsKey(JsonConstants.LINK)) {
            descriptor.links = JsonLink.parse(jsonObject.get(JsonConstants.LINK));
            
        } else {
            descriptor.links = Collections.emptySet();
        }
        
        // return type
        if (jsonObject.containsKey(JsonConstants.RETURN_TYPE)) {
            
            final JsonValue returnType = jsonObject.get(JsonConstants.RETURN_TYPE);
            
            if (JsonUtils.isNotString(returnType)) {
                throw new InvalidDocumentException(DocumentError.INVALID_RT, "The 'rt' property value must be URI represented as JSON string but was " + returnType);
            }

            try {
                descriptor.returnType = URI.create(JsonUtils.getString(returnType));
                
            } catch (IllegalArgumentException e) {
                throw new InvalidDocumentException(DocumentError.MALFORMED_URI, "The 'rt' property value must be URI represented as JSON string but was " + returnType);
            }            
        }
        
        // nested descriptors
        if (jsonObject.containsKey(JsonConstants.DESCRIPTOR)) {
            descriptor.descriptors = JsonDescriptor.parse(index, descriptor, jsonObject.get(JsonConstants.DESCRIPTOR));
            
        } else {
            descriptor.descriptors = Collections.emptySet();
        }
        
        // extensions
        if (jsonObject.containsKey(JsonConstants.EXTENSION)) {
            descriptor.extensions = JsonExtension.parse(jsonObject.get(JsonConstants.EXTENSION));
            
        } else {
            descriptor.extensions = Collections.emptySet();
        }
        
        return descriptor;
    }
    
    public static final JsonValue toJson(final Set<Descriptor> descriptors, final boolean verbose) {
        
        if (descriptors.size() == 1) {
            return toJson(descriptors.iterator().next(), verbose);
        }
        
        final JsonArrayBuilder jsonDescriptors = Json.createArrayBuilder();
        
        descriptors.stream().map(d -> JsonDescriptor.toJson(d, verbose)).forEach(jsonDescriptors::add);
        
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
        descriptor.returnType().ifPresent(rt -> jsonDescriptor.add(JsonConstants.RETURN_TYPE, rt.toString()));

        // documentation
        JsonDocumentation.toJson(descriptor.documentation(), verbose).ifPresent(doc -> jsonDescriptor.add(JsonConstants.DOCUMENTATION, doc));
        
        // descriptors
        if (JsonDocument.isNotEmpty(descriptor.descriptors())) {
            jsonDescriptor.add(JsonConstants.DESCRIPTOR, toJson(descriptor.descriptors(), verbose));
        }

        // links
        if (JsonDocument.isNotEmpty(descriptor.links())) {
            jsonDescriptor.add(JsonConstants.LINK, JsonLink.toJson(descriptor.links()));
        }

        // extensions
        if (JsonDocument.isNotEmpty(descriptor.extensions())) {
            jsonDescriptor.add(JsonConstants.EXTENSION, JsonExtension.toJson(descriptor.extensions()));
        }

        return jsonDescriptor.build();
    }
    
}
