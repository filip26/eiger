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
import java.io.OutputStream;
import java.io.Writer;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import com.apicatalog.alps.DocumentWriter;
import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.error.DocumentException;

public class XmlDocumentWriter implements DocumentWriter {

    private final XMLOutputFactory factory;
    private int indentLength;
    
    public XmlDocumentWriter(int indentLength) {
        this.factory = XMLOutputFactory.newDefaultFactory();
        factory.setProperty("escapeCharacters", false);
        this.indentLength = indentLength;
    }
    
    public static final DocumentWriter create(boolean prettyPrint) {
        return new XmlDocumentWriter(prettyPrint ? 4 : -1);
    }
    
    @Override
    public void write(String mediaType, Document document, OutputStream stream) throws IOException, DocumentException {
        // TODO Auto-generated method stub
        try {
            XmlDocument.write(document, new XmlDocumentStreamWriter(factory.createXMLStreamWriter(stream), indentLength));
            
        } catch (XMLStreamException e) {
            throw new DocumentException(e);
        }        
    }

    @Override
    public void write(String mediaType, Document document, Writer writer) throws IOException, DocumentException {
        // TODO Auto-generated method stub
        
        try {
            XmlDocument.write(document, new XmlDocumentStreamWriter(factory.createXMLStreamWriter(writer), indentLength));
            
        } catch (XMLStreamException e) {
            throw new DocumentException(e);
        }
    }
}
