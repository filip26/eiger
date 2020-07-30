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
package com.apicatalog.alps.xml;

import javax.json.JsonObject;

public final class AlpsTestCase {

    private String id;
    private String name;
    private String input;
    private String expected;
    
    public static final AlpsTestCase of(JsonObject jsonObject) {
        final AlpsTestCase testCase = new AlpsTestCase();
        
        testCase.id = jsonObject.getString("@id");
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
    
}
