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
import java.util.HashSet;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;

import com.apicatalog.alps.AlpsParserException;
import com.apicatalog.alps.dom.element.AlpsDocumentation;

final class JsonDocumentation implements AlpsDocumentation {

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
        final JsonDocumentation doc = new JsonDocumentation();
        doc.content = value.getString();
        doc.mediaType = "text/plain";
        return doc;
    }

    private static AlpsDocumentation parseObject(final JsonObject value) throws AlpsParserException {
        
        final JsonDocumentation doc = new JsonDocumentation();
        doc.mediaType = "text/plain";
        
        if (value.containsKey(AlpsJsonKeys.VALUE)) {

            final JsonValue content = value.get(AlpsJsonKeys.VALUE);
            
            if (JsonUtils.isNotString(content)) {
                throw new AlpsParserException("doc.value property must be string but was " + content.getValueType());
            }
            doc.content = JsonUtils.getString(content);
            
        } else if (value.containsKey(AlpsJsonKeys.HREF)) {
            
            final JsonValue href = value.get(AlpsJsonKeys.HREF);
            
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
        
        if (value.containsKey(AlpsJsonKeys.FORMAT)) {
            
            final JsonValue format = value.get(AlpsJsonKeys.FORMAT);
            
            if (JsonUtils.isNotString(format)) {
                throw new AlpsParserException("doc.format property must be string but was " + format.getValueType());
            }

            doc.mediaType = JsonUtils.getString(format);
        }
                
        return doc;
    }
    
    public static final JsonValue toJson(Set<AlpsDocumentation> documentation) {
        
        if (documentation.size() == 1) {
            return toJson(documentation.iterator().next());
        }
        
        final JsonArrayBuilder jsonDocs = Json.createArrayBuilder();
        
        documentation.stream().map(JsonDocumentation::toJson).forEach(jsonDocs::add);
        
        return jsonDocs.build();
    }

    public static final JsonValue toJson(AlpsDocumentation documentation) {
        
        if (documentation.getHref() == null 
                && (documentation.getMediaType() == null
                    || "text/plain".equals(documentation.getMediaType())
                        )) {
            
            return Json.createValue(documentation.getContent());
        }
        
        final JsonObjectBuilder doc = Json.createObjectBuilder();
        
        if (documentation.getHref() != null) {
            doc.add(AlpsJsonKeys.HREF, documentation.getHref().toString());
        }
        
        if (documentation.getMediaType() != null && !"text/plain".equalsIgnoreCase(documentation.getMediaType())) {
            doc.add(AlpsJsonKeys.FORMAT, documentation.getMediaType());
        }

        if (documentation.getContent() != null) {
            doc.add(AlpsJsonKeys.VALUE, documentation.getContent());
        }
        
        return doc.build();
    }
}
