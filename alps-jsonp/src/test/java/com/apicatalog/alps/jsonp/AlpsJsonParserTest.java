package com.apicatalog.alps.jsonp;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.apicatalog.alps.AlpsParserException;
import com.apicatalog.alps.dom.AlpsDocument;

class AlpsJsonParserTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("testCaseMethodSource")
    void testCase(AlpsParserTestCase testCase) throws IOException {
        
        assertNotNull(testCase);
        assertNotNull(testCase.getInput());
        
        try (final InputStream is = AlpsJsonParserTest.class.getResourceAsStream(testCase.getInput())) {
            
            assertNotNull(is);
            
            AlpsDocument document = (new AlpsJsonParser()).parse(URI.create("http://example.com"), "application/json", is);
            
            assertNotNull(document);
            
        } catch (AlpsParserException e) {
            fail(e);
        }
    }
    
    static Stream<AlpsParserTestCase> testCaseMethodSource() throws IOException {
        
        try (final InputStream is = AlpsJsonParserTest.class.getResourceAsStream("manifest.json")) {
            
            final JsonParser jsonParser = Json.createParser(is);
            
            jsonParser.next();
            
            JsonArray tests = jsonParser.getObject().getJsonArray("sequence");
            
            return tests.stream().map(JsonObject.class::cast).map(AlpsParserTestCase::of);
        }
    }
    
}
