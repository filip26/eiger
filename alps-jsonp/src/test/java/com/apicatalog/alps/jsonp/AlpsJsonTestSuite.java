package com.apicatalog.alps.jsonp;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.apicatalog.alps.AlpsParserException;
import com.apicatalog.alps.AlpsWriterException;
import com.apicatalog.alps.dom.AlpsDocument;

class AlpsJsonTestSuite {

    @ParameterizedTest(name = "{0}")
    @MethodSource("testCaseMethodSource")
    void testCase(AlpsParserTestCase testCase) throws IOException {
        
        assertNotNull(testCase);
        assertNotNull(testCase.getInput());
        
        AlpsDocument document = null;
        
        try (final InputStream is = AlpsJsonTestSuite.class.getResourceAsStream(testCase.getInput())) {
            
            assertNotNull(is);
            
            document = (new AlpsJsonParser()).parse(URI.create("http://example.com"), "application/json", is);
            
        } catch (AlpsParserException e) {
            fail(e.getMessage(), e);
        }
        
        assertNotNull(document);
        
        compare(testCase, document);
    }
    
    static final Stream<AlpsParserTestCase> testCaseMethodSource() throws IOException {
        
        try (final InputStream is = AlpsJsonTestSuite.class.getResourceAsStream("manifest.json")) {
            
            final JsonParser jsonParser = Json.createParser(is);
            
            jsonParser.next();
            
            JsonArray tests = jsonParser.getObject().getJsonArray("sequence");
            
            return tests.stream().map(JsonObject.class::cast).map(AlpsParserTestCase::of);
        }
    }
    
    static final void compare(final AlpsParserTestCase testCase, final AlpsDocument document) {

        try (final InputStream is = AlpsJsonTestSuite.class.getResourceAsStream(testCase.getInput())) {
            
            final JsonParser expectedParser = Json.createParser(is);
         
            assertTrue(expectedParser.hasNext());
            
            expectedParser.next();
            
            final JsonObject expectedObject = expectedParser.getObject();

            final JsonObject outputObject = JsonDocument.toJson(document);

            final boolean match = expectedObject.equals(outputObject); 
            
            if (!match) {
                System.out.println("Test " + testCase.getId() + ": " + testCase.getName());
                System.out.println("Expected:");

                Map<String, Object> properties = new HashMap<>(1);
                properties.put(JsonGenerator.PRETTY_PRINTING, true);

                JsonWriterFactory writerFactory = Json.createWriterFactory(properties);

                StringWriter writer = new StringWriter();
                
                JsonWriter jsonWriter1 = writerFactory.createWriter(writer);
                jsonWriter1.write(expectedObject);
                jsonWriter1.close();

                writer.append("\n\n");
                writer.append("Actual:\n");

                JsonWriter jsonWriter2 = writerFactory.createWriter(writer);
                jsonWriter2.write(outputObject);
                jsonWriter2.close();

                System.out.print(writer.toString());
                System.out.println();
                System.out.println();
                
                fail("Expected " + expectedObject + ", but was" + outputObject);
            }
            
        } catch (AlpsWriterException e) {
            fail(e.getMessage(), e);
            
        } catch (IOException e) {
            fail(e.getMessage(), e);
        }
    }    
}
