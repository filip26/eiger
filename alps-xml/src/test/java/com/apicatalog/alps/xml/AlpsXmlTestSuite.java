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
import java.io.StringWriter;
import java.net.URI;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.apicatalog.alps.AlpsParserException;
import com.apicatalog.alps.AlpsWriterException;
import com.apicatalog.alps.dom.AlpsDocument;

class AlpsXmlTestSuite {

    @ParameterizedTest(name = "{0}")
    @MethodSource("testCaseMethodSource")
    void testCase(AlpsTestCase testCase) throws IOException {
        
        assertNotNull(testCase);
        assertNotNull(testCase.getInput());
        
        AlpsDocument document = null;
        
        try (final InputStream is = AlpsXmlTestSuite.class.getResourceAsStream(testCase.getInput())) {
            
            assertNotNull(is);
            
            document = (new AlpsXmlParser()).parse(URI.create("http://example.com"), "application/xml", is);
            
        } catch (AlpsParserException e) {
            e.printStackTrace();
            fail(e.getMessage(), e);
        }
        
        assertNotNull(document);
        
        compare(testCase, document);
    }
    
    static final Stream<AlpsTestCase> testCaseMethodSource() throws IOException {
        
        try (final InputStream is = AlpsXmlTestSuite.class.getResourceAsStream("manifest.json")) {
            
            assertNotNull(is);
            
            final JsonParser jsonParser = Json.createParser(is);
            
            jsonParser.next();
            
            JsonArray tests = jsonParser.getObject().getJsonArray("sequence");
            
            return tests.stream().map(JsonObject.class::cast).map(AlpsTestCase::of);
        }
    }
    
    static final void compare(final AlpsTestCase testCase, final AlpsDocument document) {

        if (testCase.getExpected() == null) {
            return;
        }

        final DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        documentFactory.setNamespaceAware(false);
        documentFactory.setCoalescing(true);
        documentFactory.setIgnoringElementContentWhitespace(true);
        documentFactory.setIgnoringComments(true);
                
        try (final InputStream is = AlpsXmlTestSuite.class.getResourceAsStream(testCase.getExpected())) {
            
            assertNotNull(is);

            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            
            final Document expected = documentFactory.newDocumentBuilder().parse(is);
            expected.normalizeDocument();
            
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            
            (new AlpsXmlWriter()).write("application/xml", document, outputStream);
            
            final Document output = documentFactory.newDocumentBuilder().parse(new ByteArrayInputStream(outputStream.toByteArray()));
            output.normalizeDocument();

            final boolean match = expected.isEqualNode(output);
            
            if (!match) {
            
                System.out.println("Test " + testCase.getId() + ": " + testCase.getName());
                System.out.println("Expected:");

                StringWriter writer = new StringWriter();
  
                tr.transform(new DOMSource(expected), new StreamResult(writer));
                
                writer.append("\n\n");
                writer.append("Actual:\n");

                tr.transform(new DOMSource(output), new StreamResult(writer));

                System.out.print(writer.toString());
                System.out.println();
                System.out.println();
                
                fail("Expected output does not match.");
            }
            
        } catch (IOException | SAXException | ParserConfigurationException | AlpsWriterException | TransformerException e) {
            e.printStackTrace();
            fail(e.getMessage(), e);            
        }
    }    
}
