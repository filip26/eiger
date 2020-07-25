package com.apicatalog.alps.jsonp;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.json.JsonValue;

import com.apicatalog.alps.dom.element.AlpsDescriptor;
import com.apicatalog.alps.dom.element.AlpsDescriptorType;
import com.apicatalog.alps.dom.element.AlpsDocumentation;
import com.apicatalog.alps.dom.element.AlpsExtension;
import com.apicatalog.alps.dom.element.AlpsLink;

final class AlpsJsonDescriptor implements AlpsDescriptor {

    private URI id;
    
    private URI href;
    
    private String name;
    
    private AlpsDescriptorType type;
    
    private URI returnType;
    
    private Set<AlpsDocumentation> doc;
    
    private Set<AlpsDescriptor> descriptors;
    
    private Set<AlpsLink> links;
    
    private AlpsDescriptor parent;
    
    @Override
    public URI getId() {
        return id;
    }

    @Override
    public Optional<URI> getHref() {
        return Optional.ofNullable(href);
    }

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    @Override
    public AlpsDescriptorType getType() {
        return type;
    }

    @Override
    public URI getReturnType() {
        return returnType;
    }

    @Override
    public Set<AlpsDocumentation> getDocumentation() {
        return doc;
    }

    @Override
    public Set<AlpsExtension> getExtensions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<AlpsDescriptor> getDescriptors() {
        return descriptors;
    }

    @Override
    public Set<AlpsLink> getLinks() {
        return links;
    }
    
    @Override
    public Optional<AlpsDescriptor> getParent() {
        return Optional.ofNullable(parent);
    }

    public static Map<URI, AlpsDescriptor> parse(JsonValue jsonValue) {
        return parse(new HashMap<>(), jsonValue);
    }
    
    private static Map<URI, AlpsDescriptor> parse(Map<URI, AlpsDescriptor> index, JsonValue jsonValue) {
        
        if (JsonUtils.isObject(jsonValue)) {
            
            AlpsJsonDescriptor descriptor = new AlpsJsonDescriptor();
            
            try {
                descriptor.id = URI.create(JsonUtils.getString(jsonValue));
                        
            } catch (IllegalArgumentException e) {
                //TODO
            }
            
        } else if (JsonUtils.isString(jsonValue)) {

        } else {
            
        }
        //TODO
        return index;
    }
}
