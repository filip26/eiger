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

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;

import com.apicatalog.alps.error.DocumentError;

import jakarta.json.JsonObject;

class ExpectedError {

    private DocumentError code;
    private String path;
    
    private int line;
    private int column;
    
    public static final ExpectedError of(JsonObject jsonObject) {
        
        final ExpectedError error = new ExpectedError();
        
        if (jsonObject.containsKey("code")) {
  
            try {
                error.code = DocumentError.valueOf(jsonObject.getString("code"));
      
            } catch (IllegalArgumentException e) {
                fail("Invalid expectedError value '" + jsonObject.getString("code") + ", must be one of " + Arrays.toString(DocumentError.values()));
            }
        }
      
        error.path = jsonObject.getString("path", null);
        error.line = jsonObject.getInt("line", -1);
        error.column = jsonObject.getInt("column", -1);
        
        return error;
    }
    
    public DocumentError getCode() {
        return code;
    }
    
    public int getLine() {
        return line;
    }
    
    public int getColumn() {
        return column;
    }
    
    public String getPath() {
        return path;
    }
}
