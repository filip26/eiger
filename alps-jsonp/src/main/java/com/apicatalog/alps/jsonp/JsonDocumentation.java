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

import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.error.DocumentError;
import com.apicatalog.alps.error.InvalidDocumentException;

final class JsonDocumentation implements Documentation {

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

    public static Set<Documentation> parse(final JsonValue jsonValue) throws InvalidDocumentException {

        final Set<Documentation> docs = new HashSet<>();
                
        for (final JsonValue item : JsonUtils.toArray(jsonValue)) {
            
            if (JsonUtils.isString(item)) {
                docs.add(parseString((JsonString)item));
                
            } else if (JsonUtils.isObject(item)) {
                docs.add(parseObject(item.asJsonObject()));
                
            } else {
                throw new InvalidDocumentException(DocumentError.INVALID_DOC, "Expected JSON string or object but was " + item.getValueType());
            }
        }
        
        return docs;
    }
    
    private static Documentation parseString(final JsonString value) {
        final JsonDocumentation doc = new JsonDocumentation();
        doc.content = value.getString();
        doc.mediaType = "text/plain";
        return doc;
    }

    private static Documentation parseObject(final JsonObject value) throws InvalidDocumentException {
        
        final JsonDocumentation doc = new JsonDocumentation();
        doc.mediaType = "text/plain";
        
        if (value.containsKey(JsonConstants.VALUE)) {

            final JsonValue content = value.get(JsonConstants.VALUE);
            
            if (JsonUtils.isNotString(content)) {
                throw new InvalidDocumentException(DocumentError.INVALID_DOC_VALUE, "doc.value property must be string but was " + content.getValueType());
            }
            doc.content = JsonUtils.getString(content);
            
        } else if (value.containsKey(JsonConstants.HREF)) {
            
            final JsonValue href = value.get(JsonConstants.HREF);
            
            if (JsonUtils.isNotString(href)) {
                throw new InvalidDocumentException(DocumentError.INVALID_HREF, "'href' property must have string value but was " + href.getValueType());
            }
            
            try {
            
                doc.href = URI.create(JsonUtils.getString(href));
                
            } catch (IllegalArgumentException e) {
                throw new InvalidDocumentException(DocumentError.MALFORMED_URI, "'href' property value is not URI but was " + JsonUtils.getString(href));
            }
            
        } else {
            throw new InvalidDocumentException(DocumentError.MISSING_HREF, "doc object must contain href of value property");
        }
        
        if (value.containsKey(JsonConstants.FORMAT)) {
            
            final JsonValue format = value.get(JsonConstants.FORMAT);
            
            if (JsonUtils.isNotString(format)) {
                throw new InvalidDocumentException(DocumentError.INVALID_DOC_MEDIATYPE, "doc.format property must be string but was " + format.getValueType());
            }

            doc.mediaType = JsonUtils.getString(format);
        }
                
        return doc;
    }
    
    public static final JsonValue toJson(Set<Documentation> documentation) {
        
        if (documentation.size() == 1) {
            return toJson(documentation.iterator().next());
        }
        
        final JsonArrayBuilder jsonDocs = Json.createArrayBuilder();
        
        documentation.stream().map(JsonDocumentation::toJson).forEach(jsonDocs::add);
        
        return jsonDocs.build();
    }

    public static final JsonValue toJson(Documentation documentation) {
        
        if (documentation.getHref() == null 
                && (documentation.getMediaType() == null
                    || "text/plain".equals(documentation.getMediaType())
                        )) {
            
            return Json.createValue(documentation.getContent());
        }
        
        final JsonObjectBuilder doc = Json.createObjectBuilder();
        
        if (documentation.getHref() != null) {
            doc.add(JsonConstants.HREF, documentation.getHref().toString());
        }
        
        if (documentation.getMediaType() != null && !"text/plain".equalsIgnoreCase(documentation.getMediaType())) {
            doc.add(JsonConstants.FORMAT, documentation.getMediaType());
        }

        if (documentation.getContent() != null) {
            doc.add(JsonConstants.VALUE, documentation.getContent());
        }
        
        return doc.build();
    }
}
