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

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.dom.element.Descriptor;
import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Extension;
import com.apicatalog.alps.dom.element.Link;
import com.apicatalog.alps.error.InvalidDocumentException;

final class JsonDocument implements Document {

    private DocumentVersion version;
    
    private URI baseUri;
    
    private Set<Documentation> documentation;
    
    private Set<Link> links;
    
    private Set<Extension> extensions;
    
    private Map<URI, Descriptor> descriptors;
    
    public JsonDocument() {
        this.version = DocumentVersion.VERSION_1_0;
    }
    
    @Override
    public Optional<Descriptor> findById(URI id) {
        return Optional.ofNullable(descriptors.get(id));
    }

    @Override
    public Set<Descriptor> findByName(final String name) {
        return descriptors
                    .values().stream()
                    .filter(d -> d.getName().isPresent() && name.equals(d.getName().get()))
                    .collect(Collectors.toSet());
    }

    @Override
    public DocumentVersion getVersion() {
        return version;
    }

    @Override
    public Set<Descriptor> getDescriptors() {
        return descriptors
                    .values().stream()
                    .filter(d -> d.getParent().isEmpty())
                    .collect(Collectors.toSet())
                    ;
    }

    @Override
    public Collection<Descriptor> getAllDescriptors() {
        return descriptors.values();
    }

    @Override
    public Set<Link> getLinks() {
        return links;
    }

    @Override
    public Set<Documentation> getDocumentation() {
        return documentation;
    }
    
    @Override
    public Set<Extension> getExtensions() {
        return extensions;
    }

    @Override
    public URI getBaseUri() {
        return baseUri;
    }
    
    public static final Document parse(final URI baseUri, final JsonObject alpsObject) throws InvalidDocumentException {
        
        JsonDocument document = new JsonDocument();
        document.baseUri = baseUri;

        // version
        if (alpsObject.containsKey(AlpsConstants.VERSION)) {
            
            final JsonValue alpsVersion = alpsObject.get(AlpsConstants.VERSION);
            
            if (JsonUtils.isString(alpsVersion) 
                    && AlpsConstants.VERSION_1_0.equals(JsonUtils.getString(alpsVersion))) {
                
                document.version = DocumentVersion.VERSION_1_0;
            }
        }
        
        // documentation
        if (alpsObject.containsKey(AlpsConstants.DOCUMENTATION)) {
            document.documentation = JsonDocumentation.parse(alpsObject.get(AlpsConstants.DOCUMENTATION));
            
        } else {
            document.documentation = Collections.emptySet();
        }
        
        // links
        if (alpsObject.containsKey(AlpsConstants.LINK)) {
            document.links = JsonLink.parse(alpsObject.get(AlpsConstants.LINK));
            
        } else {
            document.links = Collections.emptySet();
        }
        
        // descriptors
        if (alpsObject.containsKey(AlpsConstants.DESCRIPTOR)) {
            
            document.descriptors = new HashMap<>();
            JsonDescriptor.parse(document.descriptors, alpsObject.get(AlpsConstants.DESCRIPTOR));
            
        } else {
            document.descriptors = Collections.emptyMap();
        }
        
        // extensions
        if (alpsObject.containsKey(AlpsConstants.EXTENSION)) {
            document.extensions = JsonExtension.parse(alpsObject.get(AlpsConstants.EXTENSION));
            
        } else {
            document.extensions = Collections.emptySet();
        }        

        return document;
    }
    
    public static final JsonObject toJson(Document document) {
        
        final JsonObjectBuilder alps = Json.createObjectBuilder();
        
        // version
        alps.add(AlpsConstants.VERSION, toJson(document.getVersion()));
        
        // documentation
        if (isNotEmpty(document.getDocumentation())) {
            alps.add(AlpsConstants.DOCUMENTATION, JsonDocumentation.toJson(document.getDocumentation()));
        }
        
        // links
        if (isNotEmpty(document.getLinks())) {
            alps.add(AlpsConstants.LINK, JsonLink.toJson(document.getLinks()));            
        }
        
        // descriptors
        if (isNotEmpty(document.getDescriptors())) {
            alps.add(AlpsConstants.DESCRIPTOR, JsonDescriptor.toJson(document.getDescriptors()));
        }
        
        // extensions
        if (isNotEmpty(document.getExtensions())) {
            alps.add(AlpsConstants.EXTENSION, JsonExtension.toJson(document.getExtensions()));            
        }

        return Json.createObjectBuilder().add(AlpsConstants.ROOT, alps).build();
    }
    
    private static final JsonString toJson(DocumentVersion version) {
        return Json.createValue("1.0");        
    }
    
    protected static final boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }
}
