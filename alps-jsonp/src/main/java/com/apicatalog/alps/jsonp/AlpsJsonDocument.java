package com.apicatalog.alps.jsonp;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.json.JsonObject;
import javax.json.JsonValue;

import com.apicatalog.alps.AlpsParserException;
import com.apicatalog.alps.dom.AlpsDocument;
import com.apicatalog.alps.dom.AlpsVersion;
import com.apicatalog.alps.dom.element.AlpsDescriptor;
import com.apicatalog.alps.dom.element.AlpsDocumentation;
import com.apicatalog.alps.dom.element.AlpsLink;

final class AlpsJsonDocument implements AlpsDocument {

    private AlpsVersion version;
    
    private URI baseUri;
    
    private Set<AlpsDocumentation> documentation;
    
    private Set<AlpsLink> links;
    
    private Map<URI, AlpsDescriptor> descriptors;
    
    public AlpsJsonDocument() {
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
    public URI getBaseUri() {
        return baseUri;
    }
    
    public static final AlpsDocument parse(final URI baseUri, final JsonObject alpsObject) throws AlpsParserException {
        
        AlpsJsonDocument document = new AlpsJsonDocument();
        document.baseUri = baseUri;

        // parse version
        if (alpsObject.containsKey(AlpsJsonConstant.VERSION_KEY)) {
            
            final JsonValue alpsVersion = alpsObject.get(AlpsJsonConstant.VERSION_KEY);
            
            if (JsonUtils.isString(alpsVersion) 
                    && AlpsJsonConstant.VERSION_1_0.equals(JsonUtils.getString(alpsVersion))) {
                
                document.version = AlpsVersion.VERSION_1_0;
            }
        }
        
        // parse documentation
        if (alpsObject.containsKey(AlpsJsonConstant.DOCUMENTATION_KEY)) {
            document.documentation = AlpsJsonDocumentation.parse(alpsObject.get(AlpsJsonConstant.DOCUMENTATION_KEY));
            
        } else {
            document.documentation = Collections.emptySet();
        }
        
        // parse links
        if (alpsObject.containsKey(AlpsJsonConstant.LINK_KEY)) {
            document.links = AlpsJsonLink.parse(alpsObject.get(AlpsJsonConstant.LINK_KEY));
            
        } else {
            document.links = Collections.emptySet();
        }
        
        // parse descriptors
        if (alpsObject.containsKey(AlpsJsonConstant.DESCRIPTOR_KEY)) {
            document.descriptors = AlpsJsonDescriptor.parse(alpsObject.get(AlpsJsonConstant.DESCRIPTOR_KEY));
            
        } else {
            document.descriptors = Collections.emptyMap();
        }

        return document;
    }
}
