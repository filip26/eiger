package com.apicatalog.alps.jsonp;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.json.JsonObject;
import javax.json.JsonValue;

import com.apicatalog.alps.AlpsParserException;
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
    
    private AlpsJsonDescriptor() {
        // default values
        this.type = AlpsDescriptorType.SEMANTIC;
    }
    
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

    public static Map<URI, AlpsDescriptor> parse(JsonValue jsonValue) throws AlpsParserException {
        return parse(new HashMap<>(), jsonValue);
    }
    
    private static Map<URI, AlpsDescriptor> parse(Map<URI, AlpsDescriptor> index, JsonValue jsonValue) throws AlpsParserException {
        
        if (JsonUtils.isObject(jsonValue)) {

            return parseObject(index, jsonValue.asJsonObject());

        } else if (JsonUtils.isArray(jsonValue)) {
            
            for (final JsonValue item : jsonValue.asJsonArray()) {
                parse(index, item);
            }
            
        } else {
            throw new AlpsParserException("The 'descriptor' property must be an object or an array of objects but was " + jsonValue.getValueType());
        }

        return index;
    }
    
    private static Map<URI, AlpsDescriptor> parseObject(Map<URI, AlpsDescriptor> index, JsonObject jsonObject) throws AlpsParserException {
        
        final AlpsJsonDescriptor descriptor = new AlpsJsonDescriptor();
        
        // id
        if (JsonUtils.isNotString(jsonObject.get(AlpsJsonConstant.ID_KEY))) {
            throw new AlpsParserException("The 'id' property value must be valid URI represented as JSON string but was " + jsonObject.get(AlpsJsonConstant.ID_KEY));
        }
        
        try {
            descriptor.id = URI.create(jsonObject.getString(AlpsJsonConstant.ID_KEY));
                    
        } catch (IllegalArgumentException e) {
            throw new AlpsParserException("The 'id' must be valid URI but was " + jsonObject.getString(AlpsJsonConstant.ID_KEY));
        }
        
        // check id conflict
        if (index.containsKey(descriptor.id)) {
            throw new AlpsParserException("Duplicate 'id' property value " + descriptor.id);
        }

        // name
        if (jsonObject.containsKey(AlpsJsonConstant.NAME_KEY)) {
            final JsonValue name = jsonObject.get(AlpsJsonConstant.NAME_KEY);
            
            if (JsonUtils.isNotString(name)) {
                throw new AlpsParserException("The 'name' property value must be JSON string but was " + name);
            }
            
            descriptor.name = JsonUtils.getString(name);
        }

        // type
        if (jsonObject.containsKey(AlpsJsonConstant.TYPE_KEY)) {
            
            final JsonValue type = jsonObject.get(AlpsJsonConstant.TYPE_KEY);
            
            if (JsonUtils.isNotString(type)) {
                throw new AlpsParserException("The 'type' property value must be JSON string but was " + type);
            }
            
            try {
                descriptor.type = AlpsDescriptorType.valueOf(JsonUtils.getString(type).toUpperCase());
                
            } catch (IllegalArgumentException e) {
                throw new AlpsParserException("The 'type' property value must be one of " + (Arrays.stream(AlpsDescriptorType.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.joining(", " ))) +  " but was " + type);
            }
        }

        // documentation
        if (jsonObject.containsKey(AlpsJsonConstant.DOCUMENTATION_KEY)) {
            descriptor.doc = AlpsJsonDocumentation.parse(jsonObject.get(AlpsJsonConstant.DOCUMENTATION_KEY));
        }
        
        // links
        if (jsonObject.containsKey(AlpsJsonConstant.LINK_KEY)) {
            descriptor.links = AlpsJsonLink.parse(jsonObject.get(AlpsJsonConstant.LINK_KEY));
        }
        
        // href
        if (jsonObject.containsKey(AlpsJsonConstant.HREF_KEY)) {
            final JsonValue href = jsonObject.get(AlpsJsonConstant.HREF_KEY);
            
            if (JsonUtils.isNotString(href)) {
                throw new AlpsParserException("The 'href' property value must be URI represented as JSON string but was " + href);
            }

            try {
                descriptor.href = URI.create(JsonUtils.getString(href));
                
            } catch (IllegalArgumentException e) {
                throw new AlpsParserException("The 'href' property value must be URI represented as JSON string but was " + href);
            }
        }

        if (jsonObject.containsKey(AlpsJsonConstant.DESCRIPTOR_KEY)) {
//            descriptor.
        }
        
        
        //TODO ext
        
        return index;
    }
}
