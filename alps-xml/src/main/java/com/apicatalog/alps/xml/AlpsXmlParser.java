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

import org.xml.sax.SAXException;

import com.apicatalog.alps.AlpsErrorCode;
import com.apicatalog.alps.AlpsParser;
import com.apicatalog.alps.AlpsParserException;
import com.apicatalog.alps.dom.AlpsDocument;

public class AlpsXmlParser implements AlpsParser {

    private final SAXParserFactory factory;
    
    public AlpsXmlParser() {
        this(SAXParserFactory.newDefaultInstance());
    }

    public AlpsXmlParser(SAXParserFactory factory) {
        this.factory = factory;
    }

    @Override
    public boolean canParse(String mediaType) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public AlpsDocument parse(URI baseUri, String mediaType, InputStream stream) throws IOException, AlpsParserException {

        // TODO check media and arguments

        try {
            final SAXParser parser = factory.newSAXParser();

            final AlpsDocumentHandler handler = new AlpsDocumentHandler();
            
            parser.parse(stream, handler);
          
            // TODO
            return handler.getDocument();

        } catch (ParserConfigurationException e) {            
            throw new AlpsParserException(AlpsErrorCode.PARSER_ERROR, e);
            
        } catch (SAXException e) {
            if (e.getCause() instanceof AlpsParserException) {
                throw (AlpsParserException)e.getCause();
            }
            throw new AlpsParserException(AlpsErrorCode.PARSER_ERROR, e);
        }        
    }

    @Override
    public AlpsDocument parse(URI baseUri, String mediaType, Reader reader) throws IOException, AlpsParserException {
        // TODO Auto-generated method stub
        return null;
    }    
}
