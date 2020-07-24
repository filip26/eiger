package com.apicatalog.alps.jsonp;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

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
    
    private JsonUtils() {
    }
}
