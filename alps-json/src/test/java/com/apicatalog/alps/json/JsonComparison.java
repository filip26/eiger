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

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.JsonValue.ValueType;

public class JsonComparison {


    private JsonComparison() {
    }

    public static final boolean equals(final JsonValue value1, final JsonValue value2) {

        return equals(value1, value2, null);
    }

    private static final boolean equals(final JsonValue value1, final JsonValue value2, final String parentProperty) {

        if (isNull(value1) && isNull(value2)) {
            return true;
        }

        if (isScalar(value1) && isScalar(value2)) {
            return Objects.equals(value1, value2);
        }

        if (JsonUtils.isArray(value1) && JsonUtils.isArray(value2)) {
            return arrayEquals(value1.asJsonArray(), value2.asJsonArray(), parentProperty);
        }

        if (JsonUtils.isObject(value1) && JsonUtils.isObject(value2)) {
            return objectEquals(value1.asJsonObject(), value2.asJsonObject());
        }

        return false;
    }

    private static final boolean objectEquals(final JsonObject object1, final JsonObject object2) {
        if (object1.size() != object2.size()) {
            return false;
        }

        for (final Entry<String, JsonValue> entry1 : object1.entrySet()) {

            if (!object2.containsKey(entry1.getKey())) {
                return false;
            }

            if (!equals(entry1.getValue(), object2.get(entry1.getKey()), entry1.getKey())) {
                return false;
            }
        }
        return true;
    }

    private static final boolean arrayEquals(final JsonArray array1, final JsonArray array2, final String parentProperty) {

        if (array1.size() != array2.size()) {
            return false;
        }

        if (array1.isEmpty()) {
            return true;
        }

        return arraysEqualsUnordered(array1, array2);
    }

    // JSON arrays are generally compared without regard to order
    private static final boolean arraysEqualsUnordered(final JsonArray array1, final JsonArray array2) {

        if (array1.size() != array2.size()) {
            return false;
        }

        if (array1.isEmpty()) {
            return true;
        }

        final List<JsonValue> remaining = new ArrayList<>(array2);

        for (final JsonValue item1 : array1) {

            boolean found = false;

            for (final JsonValue item2 : remaining) {

                found = equals(item1, item2);

                if (found) {
                    remaining.remove(item2);
                    break;
                }
            }

            if (!found) {
                return false;
            }
        }

        return remaining.isEmpty();
    }

    private static final boolean isNull(JsonValue value) {
        return value == null || ValueType.NULL.equals(value.getValueType());
    }

    private static final boolean isScalar(JsonValue value) {
        return value != null
                && !ValueType.ARRAY.equals(value.getValueType())
                && !ValueType.OBJECT.equals(value.getValueType());
    }
}
