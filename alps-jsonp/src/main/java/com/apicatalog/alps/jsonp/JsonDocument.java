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

import com.apicatalog.alps.AlpsParserException;
import com.apicatalog.alps.AlpsWriterException;
import com.apicatalog.alps.dom.AlpsDocument;
import com.apicatalog.alps.dom.AlpsVersion;
import com.apicatalog.alps.dom.element.AlpsDescriptor;
import com.apicatalog.alps.dom.element.AlpsDocumentation;
import com.apicatalog.alps.dom.element.AlpsExtension;
import com.apicatalog.alps.dom.element.AlpsLink;

final class JsonDocument implements AlpsDocument {

    private AlpsVersion version;
    
    private URI baseUri;
    
    private Set<AlpsDocumentation> documentation;
    
    private Set<AlpsLink> links;
    
    private Set<AlpsExtension> extensions;
    
    private Map<URI, AlpsDescriptor> descriptors;
    
    public JsonDocument() {
        this.version = AlpsVersion.VERSION_1_0;
    }
    
    @Override
    public Optional<AlpsDescriptor> findById(URI id) {
        return Optional.ofNullable(descriptors.get(id));
    }

    @Override
    public Set<AlpsDescriptor> findByName(final String name) {
        return descriptors
                    .values().stream()
                    .filter(d -> d.getName().isPresent() && name.equals(d.getName().get()))
                    .collect(Collectors.toSet());
    }

    @Override
    public AlpsVersion getVersion() {
        return version;
    }

    @Override
    public Set<AlpsDescriptor> getDescriptors() {
        return descriptors
                    .values().stream()
                    .filter(d -> d.getParent().isEmpty())
                    .collect(Collectors.toSet())
                    ;
    }

    @Override
    public Collection<AlpsDescriptor> getAllDescriptors() {
        return descriptors.values();
    }

    @Override
    public Set<AlpsLink> getLinks() {
        return links;
    }

    @Override
    public Set<AlpsDocumentation> getDocumentation() {
        return documentation;
    }
    
    @Override
    public Set<AlpsExtension> getExtensions() {
        return extensions;
    }

    @Override
    public URI getBaseUri() {
        return baseUri;
    }
    
    public static final AlpsDocument parse(final URI baseUri, final JsonObject alpsObject) throws AlpsParserException {
        
        JsonDocument document = new JsonDocument();
        document.baseUri = baseUri;

        // version
        if (alpsObject.containsKey(AlpsJsonKeys.VERSION)) {
            
            final JsonValue alpsVersion = alpsObject.get(AlpsJsonKeys.VERSION);
            
            if (JsonUtils.isString(alpsVersion) 
                    && AlpsJsonKeys.VERSION_1_0.equals(JsonUtils.getString(alpsVersion))) {
                
                document.version = AlpsVersion.VERSION_1_0;
            }
        }
        
        // documentation
        if (alpsObject.containsKey(AlpsJsonKeys.DOCUMENTATION)) {
            document.documentation = JsonDocumentation.parse(alpsObject.get(AlpsJsonKeys.DOCUMENTATION));
            
        } else {
            document.documentation = Collections.emptySet();
        }
        
        // links
        if (alpsObject.containsKey(AlpsJsonKeys.LINK)) {
            document.links = JsonLink.parse(alpsObject.get(AlpsJsonKeys.LINK));
            
        } else {
            document.links = Collections.emptySet();
        }
        
        // descriptors
        if (alpsObject.containsKey(AlpsJsonKeys.DESCRIPTOR)) {
            
            document.descriptors = new HashMap<>();
            JsonDescriptor.parse(document.descriptors, alpsObject.get(AlpsJsonKeys.DESCRIPTOR));
            
        } else {
            document.descriptors = Collections.emptyMap();
        }
        
        // extensions
        if (alpsObject.containsKey(AlpsJsonKeys.EXTENSION)) {
            document.extensions = JsonExtension.parse(alpsObject.get(AlpsJsonKeys.EXTENSION));
            
        } else {
            document.extensions = Collections.emptySet();
        }        

        return document;
    }
    
    public static final JsonObject toJson(AlpsDocument document) throws AlpsWriterException {
        
        final JsonObjectBuilder alps = Json.createObjectBuilder();
        
        // version
        alps.add(AlpsJsonKeys.VERSION, toJson(document.getVersion()));
        
        // documentation
        if (isNotEmpty(document.getDocumentation())) {
            alps.add(AlpsJsonKeys.DOCUMENTATION, JsonDocumentation.toJson(document.getDocumentation()));
        }
        
        // links
        if (isNotEmpty(document.getLinks())) {
            alps.add(AlpsJsonKeys.LINK, JsonLink.toJson(document.getLinks()));            
        }
        
        // descriptors
        if (isNotEmpty(document.getDescriptors())) {
            
        }
        
        // extensions
        if (isNotEmpty(document.getExtensions())) {
            
        }

        return Json.createObjectBuilder().add(AlpsJsonKeys.ROOT, alps).build();
    }
    
    private static final JsonString toJson(AlpsVersion version) {
        return Json.createValue("1.0");        
    }
    
    private static final boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }
}
