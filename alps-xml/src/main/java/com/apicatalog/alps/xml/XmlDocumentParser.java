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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.apicatalog.alps.DocumentParser;
import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.error.DocumentParserException;
import com.apicatalog.alps.error.MalformedDocumentException;

public class XmlDocumentParser implements DocumentParser {

    private final SAXParserFactory factory;
    
    public XmlDocumentParser() {
        this(SAXParserFactory.newDefaultInstance());
    }

    public XmlDocumentParser(final SAXParserFactory factory) {
        this.factory = factory;
    }

    @Override
    public Document parse(final URI baseUri, final InputStream stream) throws IOException, DocumentParserException {
        return parse(baseUri, new InputSource(stream));
    }

    @Override
    public Document parse(final URI baseUri, final Reader reader) throws DocumentParserException, IOException {
        return parse(baseUri, new InputSource(reader));
    }    
    
    private Document parse(final URI baseUri, final InputSource soure) throws DocumentParserException, IOException {
        try {
            final SAXParser parser = factory.newSAXParser();

            final AlpsDocumentHandler handler = new AlpsDocumentHandler();
            
            parser.parse(soure, handler);
          
            return handler.getDocument();
            
        } catch (SAXParseException e) {
            throw new MalformedDocumentException(e.getLineNumber(), e.getColumnNumber(), e.getMessage());

        } catch (ParserConfigurationException e) {     
            
            throw new DocumentParserException(e);
            
        } catch (SAXException e) {
            
            if (e.getCause() instanceof DocumentParserException) {
                throw (DocumentParserException)e.getCause();
            }
                        
            throw new DocumentParserException(e);            
        }        
    }    

}
