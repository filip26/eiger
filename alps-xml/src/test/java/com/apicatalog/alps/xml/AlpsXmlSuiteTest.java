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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.stream.Stream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.error.DocumentParserException;
import com.apicatalog.alps.error.DocumentWriterException;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonParser;

class AlpsXmlSuiteTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("testCaseMethodSource")
    void testCase(final TestDescription testCase) throws IOException {
        
        assertNotNull(testCase);
        assertNotNull(testCase.getInput());
        
        Document document = null;
        
        try (final InputStream is = AlpsXmlSuiteTest.class.getResourceAsStream(testCase.getInput())) {
            
            assertNotNull(is);
            
            document = (new XmlDocumentParser()).parse(URI.create("http://example.com"), is);
            
        } catch (DocumentParserException e) {

            if (testCase.isNegativeTest()) {
                return;
            }
            
            fail(e.getMessage(), e);                
        }
        
        assertNotNull(document);
        
        compare(testCase, document);
    }
    
    static final Stream<TestDescription> testCaseMethodSource() throws IOException {
        
        try (final InputStream is = AlpsXmlSuiteTest.class.getResourceAsStream("manifest.json")) {
            
            assertNotNull(is);
            
            final JsonParser jsonParser = Json.createParser(is);
            
            jsonParser.next();
            
            final JsonArray tests = jsonParser.getObject().getJsonArray("sequence");
            
            return tests.stream().map(JsonObject.class::cast).map(TestDescription::of);
        }
    }
    
    static final void compare(final TestDescription testCase, final Document document) {

        if (testCase.getExpected() == null) {
            return;
        }
                
        try (final InputStream is = AlpsXmlSuiteTest.class.getResourceAsStream(testCase.getExpected())) {
            
            assertNotNull(is);
            
            byte[] expectedBytes = is.readAllBytes();
            
            Transformer inputTransformer = createInputTransformer();
            
            final org.w3c.dom.Document expected = readDocument(inputTransformer, new ByteArrayInputStream(expectedBytes));
            expected.normalizeDocument();
            
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            
            XmlDocumentWriter.create(new OutputStreamWriter(outputStream), true, false).write(document);
            
            final byte[] outputBytes = outputStream.toByteArray();

            final org.w3c.dom.Document output = readDocument(inputTransformer, new ByteArrayInputStream(outputBytes));
            output.normalizeDocument();

            final boolean match = expected.isEqualNode(output);
            
            if (!match) {
                
                System.out.println("Test " + testCase.getId() + ": " + testCase.getName());
                System.out.println("Expected:");

                System.out.println(new String(expectedBytes));
                
                System.out.println("\n\n");
                System.out.println("Actual:\n");

                System.out.println(new String(outputBytes));
                System.out.println();
                
                fail("Expected output does not match.");
            }
            
        } catch (IOException | DocumentWriterException e) {
            fail(e.getMessage(), e);            
        }
    }    
    
    private static final Transformer createInputTransformer() {

        try (final InputStream is = AlpsXmlSuiteTest.class.getResourceAsStream("strip-whitespace.xsl")) {

            assertNotNull(is);

            return TransformerFactory.newInstance().newTransformer(new StreamSource(is));

        } catch (TransformerFactoryConfigurationError | TransformerException | IOException e) {
            fail(e.getMessage(), e);
        }
        
        return null;
    }
    
    private static final org.w3c.dom.Document readDocument(Transformer transformer, InputStream source) {

        try {
            final DOMResult result = new DOMResult();
            
            transformer.transform(new StreamSource(source), result);
            
            return (org.w3c.dom.Document)result.getNode();

        } catch (TransformerFactoryConfigurationError | TransformerException e) {
            fail(e.getMessage(), e);
        }
        
        return null;
    }
}
