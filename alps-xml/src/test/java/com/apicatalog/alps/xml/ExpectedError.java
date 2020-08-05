package com.apicatalog.alps.xml;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;

import javax.json.JsonObject;

import com.apicatalog.alps.AlpsErrorCode;

class ExpectedError {

    private AlpsErrorCode code;
    private String path;
    
    private int line;
    private int column;
    
    public static final ExpectedError of(JsonObject jsonObject) {
        
        final ExpectedError error = new ExpectedError();
        
        if (jsonObject.containsKey("code")) {
  
            try {
                error.code = AlpsErrorCode.valueOf(jsonObject.getString("code"));
      
            } catch (IllegalArgumentException e) {
                fail("Invalid expectedError value '" + jsonObject.getString("code") + ", must be one of " + Arrays.toString(AlpsErrorCode.values()));
            }
        }
      
        error.path = jsonObject.getString("path", null);
        error.line = jsonObject.getInt("line", -1);
        error.column = jsonObject.getInt("column", -1);
        
        return error;
    }
    
    public AlpsErrorCode getCode() {
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
