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
import java.io.Writer;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.error.DocumentWriterException;
import com.apicatalog.alps.io.DocumentWriter;

public class XmlDocumentWriter implements DocumentWriter {

    private final XMLStreamWriter writer;
    private final int indentLength;
    private final boolean verbose;
    
    public XmlDocumentWriter(final XMLStreamWriter writer, final int indentLength, final boolean verbose) {
        this.writer = writer;
        this.indentLength = indentLength;
        this.verbose = verbose;
    }
    
    public static final DocumentWriter create(final Writer writer, final boolean prettyPrint, final boolean verbose) throws DocumentWriterException {
        
        final XMLOutputFactory factory = XMLOutputFactory.newDefaultFactory();
        factory.setProperty("escapeCharacters", false);

        try {
            return new XmlDocumentWriter(factory.createXMLStreamWriter(writer), prettyPrint ? 4 : -1, verbose);
            
        } catch (XMLStreamException e) {
            throw new DocumentWriterException(e);
        }
    }
    
    @Override
    public void write(Document document) throws IOException, DocumentWriterException {
        XmlDocument.write(document, new XmlDocumentStreamWriter(writer, indentLength), verbose);        
    }    
}
