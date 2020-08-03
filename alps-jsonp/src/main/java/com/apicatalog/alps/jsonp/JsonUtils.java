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

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import com.apicatalog.alps.AlpsErrorCode;
import com.apicatalog.alps.AlpsParserException;

final class JsonUtils {

    public static final boolean isObject(final JsonValue value) {
        return value != null && ValueType.OBJECT.equals(value.getValueType());
    }

    public static final boolean isNotObject(final JsonValue value) {
        return value == null || !ValueType.OBJECT.equals(value.getValueType());
    }

    public static final boolean isString(final JsonValue value) {
        return value != null && ValueType.STRING.equals(value.getValueType());
    }

    public static final boolean isNotString(final JsonValue value) {
        return value == null || !ValueType.STRING.equals(value.getValueType());
    }

    public static final String getString(final JsonValue value) {
        return ((JsonString)value).getString();
    }
    
    public static final boolean isArray(final JsonValue value) {
        return value != null && ValueType.ARRAY.equals(value.getValueType());
    }
    
    public static final JsonArray toArray(final JsonValue value) {
        return isArray(value)
                    ? value.asJsonArray()
                    : Json.createArrayBuilder().add(value).build();
    }
    
    public static final URI getHref(final JsonObject object) throws AlpsParserException {
        
        final JsonValue href = object.get(AlpsJsonKeys.HREF);
        
        if (JsonUtils.isNotString(href)) {
            throw new AlpsParserException(AlpsErrorCode.NOT_URI, "The 'href' property value must be URI represented as JSON string but was " + href);
        }

        try {
            return URI.create(JsonUtils.getString(href));
            
        } catch (IllegalArgumentException e) {
            throw new AlpsParserException(AlpsErrorCode.NOT_URI, "The 'href' property value must be URI represented as JSON string but was " + href);
        }
    }

    private JsonUtils() {
    }
}
