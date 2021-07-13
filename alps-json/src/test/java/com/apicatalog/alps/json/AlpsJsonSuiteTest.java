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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.error.DocumentParserException;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonParser;

class AlpsJsonSuiteTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("testCaseMethodSource")
    void testCase(TestDescription testCase) throws IOException {

        assertNotNull(testCase);
        assertNotNull(testCase.getInput());

        //TODO fix tags
        assumeFalse("#t006".equals(testCase.getId()));
        
        Document document = null;

        try (final InputStream is = AlpsJsonSuiteTest.class.getResourceAsStream(testCase.getInput())) {

            assertNotNull(is);

            document = (new JsonDocumentParser()).parse(URI.create("http://example.com"), is);

            assertTrue(testCase.isPositiveTest());

        } catch (DocumentParserException e) {

            if (testCase.isNegativeTest()) {
                return;
            }

            fail(e.getMessage(), e);
            return;
        }

        assertNotNull(document);

        if (testCase.getExpected() == null) {
            return;
        }

        compare(testCase, document);
    }

    static final Stream<TestDescription> testCaseMethodSource() throws IOException {

        try (final InputStream is = AlpsJsonSuiteTest.class.getResourceAsStream("manifest.json")) {

            assertNotNull(is);

            final JsonParser jsonParser = Json.createParser(is);

            jsonParser.next();

            JsonArray tests = jsonParser.getObject().getJsonArray("sequence");

            return tests.stream().map(JsonObject.class::cast).map(TestDescription::of);
        }
    }

    static final void compare(final TestDescription testCase, final Document document) {

        try (final InputStream is = AlpsJsonSuiteTest.class.getResourceAsStream(testCase.getExpected())) {

            final JsonParser expectedParser = Json.createParser(is);

            assertTrue(expectedParser.hasNext());

            expectedParser.next();

            final JsonObject expectedObject = expectedParser.getObject();

            final JsonObject outputObject = JsonDocumentWriter.toJson(document, false);

            final boolean match = JsonComparison.equals(expectedObject, outputObject);

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


        } catch (IOException e) {
            fail(e.getMessage(), e);
        }
    }
}
