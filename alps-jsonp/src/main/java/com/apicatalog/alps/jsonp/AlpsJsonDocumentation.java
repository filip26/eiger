package com.apicatalog.alps.jsonp;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

import com.apicatalog.alps.AlpsParserException;
import com.apicatalog.alps.dom.element.AlpsDocumentation;

class AlpsJsonDocumentation implements AlpsDocumentation {

    private URI href;
    private String mediaType;
    private String content;
    
    @Override
    public URI getHref() {
        return href;
    }

    @Override
    public String getMediaType() {
        return mediaType;
    }

    @Override
    public String getContent() {
        return content;
    }

    public static Set<AlpsDocumentation> parse(final JsonValue jsonValue) throws AlpsParserException {

        final Set<AlpsDocumentation> docs = new HashSet<>();
                
        for (final JsonValue item : JsonUtils.toArray(jsonValue)) {
            
            if (JsonUtils.isString(item)) {
                docs.add(parseString((JsonString)item));
                
            } else if (JsonUtils.isObject(item)) {
                docs.add(parseObject(item.asJsonObject()));
                
            } else {
                throw new AlpsParserException("Expected JSON string or object but was " + item.getValueType());
            }
        }
        
        return docs;
    }
    
    private static AlpsDocumentation parseString(final JsonString value) {
        final AlpsJsonDocumentation doc = new AlpsJsonDocumentation();
        doc.content = value.getString();
        doc.mediaType = "text/plain";
        return doc;
    }

    private static AlpsDocumentation parseObject(final JsonObject value) throws AlpsParserException {
        
        final AlpsJsonDocumentation doc = new AlpsJsonDocumentation();
        doc.mediaType = "text/plain";
        
        if (value.containsKey(AlpsJsonConstant.VALUE_KEY)) {

            final JsonValue content = value.get(AlpsJsonConstant.VALUE_KEY);
            
            if (JsonUtils.isNotString(content)) {
                throw new AlpsParserException("doc.value property must be string but was " + content.getValueType());
            }
            doc.content = JsonUtils.getString(content);
            
        } else if (value.containsKey(AlpsJsonConstant.HREF_KEY)) {
            
            final JsonValue href = value.get(AlpsJsonConstant.HREF_KEY);
            
            if (JsonUtils.isNotString(href)) {
                throw new AlpsParserException("'href' property must have string value but was " + href.getValueType());
            }
            
            try {
            
                doc.href = URI.create(JsonUtils.getString(href));
                
            } catch (IllegalArgumentException e) {
                throw new AlpsParserException("'href' property value is not URI but was " + JsonUtils.getString(href));
            }
            
        } else {
            throw new AlpsParserException("doc object must contain href of value property");
        }
        
        if (value.containsKey(AlpsJsonConstant.FORMAT_KEY)) {
            
            final JsonValue format = value.get(AlpsJsonConstant.FORMAT_KEY);
            
            if (JsonUtils.isNotString(format)) {
                throw new AlpsParserException("doc.format property must be string but was " + format.getValueType());
            }

            doc.mediaType = JsonUtils.getString(format);
        }
                
        return doc;
    }

}
