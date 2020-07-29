package com.apicatalog.alps.jsonp;

import javax.json.JsonObject;

public final class AlpsParserTestCase {

    private String id;
    private String name;
    private String input;
    private String expected;
    
    public static final AlpsParserTestCase of(JsonObject jsonObject) {
        final AlpsParserTestCase testCase = new AlpsParserTestCase();
        
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
