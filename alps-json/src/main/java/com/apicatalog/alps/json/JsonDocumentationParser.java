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
import java.util.HashSet;
import java.util.Set;

import com.apicatalog.alps.Alps;
import com.apicatalog.alps.DocumentationBuilder;
import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.error.DocumentError;
import com.apicatalog.alps.error.InvalidDocumentException;

import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;

final class JsonDocumentationParser {
    
    private JsonDocumentationParser() {}

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
        return Alps.createDocumentation()
                    .type(JsonConstants.MEDIA_TYPE_TEXT_PLAIN)
                    .append(value.getString())
                    .build();
    }

    private static Documentation parseObject(final JsonObject value) throws InvalidDocumentException {
        
        final DocumentationBuilder doc = Alps.createDocumentation().type(JsonConstants.MEDIA_TYPE_TEXT_PLAIN);
                
        if (value.containsKey(JsonConstants.VALUE)) {

            final JsonValue contentValue = value.get(JsonConstants.VALUE);
            
            if (JsonUtils.isNotString(contentValue)) {
                throw new InvalidDocumentException(DocumentError.INVALID_DOC_VALUE, "doc.value property must be string but was " + contentValue.getValueType());
            }
            
            doc.append(JsonUtils.getString(contentValue));
                        
        } else if (value.containsKey(JsonConstants.HREF)) {
            
            final JsonValue href = value.get(JsonConstants.HREF);
            
            if (JsonUtils.isNotString(href)) {
                throw new InvalidDocumentException(DocumentError.INVALID_HREF, "'href' property must have string value but was " + href.getValueType());
            }
            
            try {
            
                doc.href(URI.create(JsonUtils.getString(href)));
                
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

            doc.type(JsonUtils.getString(format));
            
        } else if (value.containsKey(JsonConstants.CONTENT_TYPE)) {

            final JsonValue contentType = value.get(JsonConstants.CONTENT_TYPE);
            
            if (JsonUtils.isNotString(contentType)) {
                throw new InvalidDocumentException(DocumentError.INVALID_DOC_MEDIATYPE, "doc.contentType property must be string but was " + contentType.getValueType());
            }

            doc.type(JsonUtils.getString(contentType));
        }
        
        
        return doc.build();
    } 
}
