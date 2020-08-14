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

import java.net.URI;
import java.nio.charset.Charset;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.dom.element.DescriptorType;

final class XmlDocumentStreamWriter implements DocumentStreamWriter {

    private final XMLStreamWriter writer;
    private final int indentLength;
    
    private int depth;
    
    public XmlDocumentStreamWriter(XMLStreamWriter writer, int indentLength) {
        this.writer = writer;
        this.indentLength = indentLength;
        this.depth = 1;
    }

    @Override
    public void startDocument(DocumentVersion version) throws DocumentStreamException {
        try {
            
          writer.writeStartDocument(Charset.defaultCharset().name(), "1.0");
          if (isPrettyPrint()) {
              writer.writeCharacters("\n");
          }
          writer.writeStartElement(AlpsConstants.DOCUMENT);
          writer.writeAttribute(AlpsConstants.VERSION, toString(version));
          if (isPrettyPrint()) {
              writer.writeCharacters("\n");
          }
          
        } catch (XMLStreamException e) {
            throw new DocumentStreamException(e);
        }
    }

    @Override
    public void endDocument() throws DocumentStreamException {
        try {
            writer.writeEndElement();
            writer.writeEndDocument();
        } catch (XMLStreamException e) {
            throw new DocumentStreamException(e);
        }        
    }

    @Override
    public void startDescriptor(URI id, URI href, DescriptorType type, URI returnType, String name) throws DocumentStreamException {
        try {
            writeIndent();
            writer.writeStartElement(AlpsConstants.DESCRIPTOR);

            if (id != null) {
                writer.writeAttribute(AlpsConstants.ID, id.toString());
            }

            if (href != null) {
                writer.writeAttribute(AlpsConstants.HREF, href.toString());
            }

            if (type != null && !DescriptorType.SEMANTIC.equals(type)) {
                writer.writeAttribute(AlpsConstants.TYPE, type.name().toLowerCase());
            }
            
            if (returnType != null) {
                writer.writeAttribute(AlpsConstants.RETURN_TYPE, returnType.toString());
            }

            if (name != null) {
                writer.writeAttribute(AlpsConstants.NAME, name);
            }

            if (isPrettyPrint()) {
                writer.writeCharacters("\n");
            }
            
            depth++;
            
        } catch (XMLStreamException e) {
            throw new DocumentStreamException(e);
        }
    }

    @Override
    public void endDescriptor() throws DocumentStreamException {
        try {
            depth--;
            writeIndent();
            writer.writeEndElement();
            if (isPrettyPrint()) {
                writer.writeCharacters("\n");
            }
        } catch (XMLStreamException e) {
            throw new DocumentStreamException(e);
        }            
    }

    @Override
    public void startDoc(String mediaType, URI href) throws DocumentStreamException {
        try {
            writeIndent();
            writer.writeStartElement(AlpsConstants.DOCUMENTATION);
            
            if (mediaType != null && !"text".equals(mediaType) && !"text/plain".equals(mediaType)) {
                writer.writeAttribute(AlpsConstants.MEDIA_TYPE, mediaType);
            }
            if (href != null) {
                writer.writeAttribute(AlpsConstants.HREF, href.toString());
            }

        } catch (XMLStreamException e) {
            throw new DocumentStreamException(e);
        }
    }

    @Override
    public void writeDocContent(String content) throws DocumentStreamException {

        try {
            if (content.matches(".*[<>&].*")) {
                writer.writeCData(content);
            } else {
                writer.writeCharacters(content);
            }
            
        } catch (XMLStreamException e) {
            throw new DocumentStreamException(e);
        }        
    }

    @Override
    public void endDoc() throws DocumentStreamException {
        try {
            writer.writeEndElement();
            if (isPrettyPrint()) {
                writer.writeCharacters("\n");
            }
        } catch (XMLStreamException e) {
            throw new DocumentStreamException(e);
        }            
    }

    
    private final void writeIndent() throws XMLStreamException {
        if (!isPrettyPrint()) {
            return;
        }
        writer.writeCharacters(" ".repeat(depth*indentLength));
    }
    
    public final boolean isPrettyPrint() {
        return indentLength > 0;
    }
    
    private final static String toString(final DocumentVersion version) {
        return "1.0";
    }

    @Override
    public void writeLink(URI href, String rel) throws DocumentStreamException {
        
        try {
            writer.writeStartElement(AlpsConstants.LINK);
            
            if (href != null) {
                writer.writeAttribute(AlpsConstants.HREF, href.toString());
            }
            
            if (rel != null && !rel.isBlank()) {
                writer.writeAttribute(AlpsConstants.RELATION, rel);
            }
            
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            throw new DocumentStreamException(e);
        }
    }

    @Override
    public void writeExtension(URI id, URI href, String value) throws DocumentStreamException {
        // TODO Auto-generated method stub
        
    }
}
