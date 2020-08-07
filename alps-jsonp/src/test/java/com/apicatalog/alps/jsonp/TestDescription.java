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

import java.util.Set;
import java.util.stream.Collectors;

import javax.json.JsonObject;
import javax.json.JsonString;

final class TestDescription {

    private String id;
    private String name;
    private String input;
    private String expected;
    
    private Set<String> type;
    
    public static final TestDescription of(JsonObject jsonObject) {
        final TestDescription testCase = new TestDescription();
        
        testCase.id = jsonObject.getString("@id");
        testCase.type = jsonObject.getJsonArray("@type").stream().map(JsonString.class::cast).map(JsonString::getString).collect(Collectors.toSet());

        testCase.name = jsonObject.getString("name");
        testCase.input = jsonObject.getString("input");
        testCase.expected = jsonObject.getString("expected", null);
            
        return testCase;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getInput() {
        return input;
    }
    
    public String getExpected() {
        return expected;
    }
    
    @Override
    public String toString() {
        return id + ": " + name;
    }

    public boolean isType(final String type) {
        return type != null && type.contains(type);
    }
    
    public boolean isNegativeTest() {
        return isType("#NegativeEvaluationTest");
    }
    
}
