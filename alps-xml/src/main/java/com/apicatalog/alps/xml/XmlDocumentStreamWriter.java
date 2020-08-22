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
import java.util.Optional;
import java.util.function.Predicate;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.dom.element.Descriptor;
import com.apicatalog.alps.dom.element.DescriptorType;
import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Documentation.Content;
import com.apicatalog.alps.dom.element.Extension;
import com.apicatalog.alps.dom.element.Link;
import com.apicatalog.alps.error.DocumentWriterException;

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
    public void startDocument(DocumentVersion version) throws DocumentWriterException {
        try {
            
          writer.writeStartDocument(Charset.defaultCharset().name(), "1.0");
          if (isPrettyPrint()) {
              writer.writeCharacters("\n");
          }
          writer.writeStartElement(XmlConstants.DOCUMENT);
          writer.writeAttribute(XmlConstants.VERSION, toString(version));
          if (isPrettyPrint()) {
              writer.writeCharacters("\n");
          }
          
        } catch (XMLStreamException e) {
            throw new DocumentWriterException(e);
        }
    }

    @Override
    public void endDocument() throws DocumentWriterException {
        try {
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
            
        } catch (XMLStreamException e) {
            throw new DocumentWriterException(e);
        }        
    }

    @Override
    public void startDescriptor(final Descriptor descriptor, final boolean selfClose, final boolean verbose) throws DocumentWriterException {
        try {
            writeIndent();
            
            if (selfClose) {
                
                writer.writeEmptyElement(XmlConstants.DESCRIPTOR);
                
            } else {
                writer.writeStartElement(XmlConstants.DESCRIPTOR);
            }

            final Optional<URI> id = descriptor.id();
            
            if (id.isPresent()) {
                writer.writeAttribute(XmlConstants.ID, id.get().toString());
            }

            final Optional<URI> href = descriptor.href();
            
            if (href.isPresent()) {
                writer.writeAttribute(XmlConstants.HREF, href.get().toString());
            }

            final Optional<String> name = descriptor.name();
            
            if (name.isPresent()) {
                writer.writeAttribute(XmlConstants.NAME, name.get());
            }
            
            final DescriptorType type = descriptor.type();
            
            if (type != null && !DescriptorType.SEMANTIC.equals(type)) {
                writer.writeAttribute(XmlConstants.TYPE, type.name().toLowerCase());
                
            } else if (verbose) {
                writer.writeAttribute(XmlConstants.TYPE, DescriptorType.SEMANTIC.name().toLowerCase());
            }
            
            final Optional<URI> returnType = descriptor.returnType();
            
            if (returnType.isPresent()) {
                writer.writeAttribute(XmlConstants.RETURN_TYPE, returnType.get().toString());
            }

            if (isPrettyPrint()) {
                writer.writeCharacters("\n");
            }

            if (!selfClose) {
                depth++;
            }
            
        } catch (XMLStreamException e) {
            throw new DocumentWriterException(e);
        }
    }

    @Override
    public void endDescriptor() throws DocumentWriterException {
        try {
            depth--;
            
            writeIndent();
            writer.writeEndElement();
            
            if (isPrettyPrint()) {
                writer.writeCharacters("\n");
            }
        } catch (XMLStreamException e) {
            throw new DocumentWriterException(e);
        }            
    }

    @Override
    public void startDoc(final Documentation doc, final boolean selfClose, final boolean verbose) throws DocumentWriterException {
        try {
            writeIndent();
            
            if (selfClose) {
                writer.writeEmptyElement(XmlConstants.DOCUMENTATION);
                
            } else {
                writer.writeStartElement(XmlConstants.DOCUMENTATION);
            }
            
            final Optional<String> mediaType = doc.content().map(Content::type).filter(Predicate.not(Predicate.isEqual("text").or(Predicate.isEqual("text/plain"))));
            
            if (mediaType.isPresent()) {
                writer.writeAttribute(XmlConstants.MEDIA_TYPE, mediaType.get());
                
            } else if (verbose) {
                writer.writeAttribute(XmlConstants.MEDIA_TYPE, "text");
            }
            
            final Optional<URI> href = doc.href();
            
            if (href.isPresent()) {
                writer.writeAttribute(XmlConstants.HREF, href.get().toString());
            }

        } catch (XMLStreamException e) {
            throw new DocumentWriterException(e);
        }
    }

    @Override
    public void writeDocContent(String content) throws DocumentWriterException {

        try {
            if (content.matches(".*[<>&].*")) {
                writer.writeCData(content);
            } else {
                writer.writeCharacters(content);
            }
            
        } catch (XMLStreamException e) {
            throw new DocumentWriterException(e);
        }        
    }

    @Override
    public void endDoc() throws DocumentWriterException {
        try {
            writer.writeEndElement();
            if (isPrettyPrint()) {
                writer.writeCharacters("\n");
            }
        } catch (XMLStreamException e) {
            throw new DocumentWriterException(e);
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
    
    private static final String toString(final DocumentVersion version) {
        return "1.0";
    }

    @Override
    public void writeLink(final Link link) throws DocumentWriterException {
        
        try {
            writeIndent();
            
            writer.writeEmptyElement(XmlConstants.LINK);
            
            final URI href = link.href();
            
            if (href != null) {
                writer.writeAttribute(XmlConstants.HREF, href.toString());
            }
            
            final String rel = link.rel();
            
            if (rel != null && !rel.isBlank()) {
                writer.writeAttribute(XmlConstants.RELATION, rel);
            }

            if (isPrettyPrint()) {
                writer.writeCharacters("\n");
            }

        } catch (XMLStreamException e) {
            throw new DocumentWriterException(e);
        }
    }

    @Override
    public void writeExtension(final Extension extension) throws DocumentWriterException {
        
        try {
            writeIndent();
            
            writer.writeEmptyElement(XmlConstants.EXTENSION);

            final URI id = extension.id();
            
            if (id != null) {
                writer.writeAttribute(XmlConstants.ID, id.toString());
            }

            final Optional<URI> href = extension.href();
            
            if (href.isPresent()) {
                writer.writeAttribute(XmlConstants.HREF, href.get().toString());
            }
            
            final Optional<String> value = extension.value().filter(Predicate.not(String::isBlank));
            
            if (value.isPresent()) {
                writer.writeAttribute(XmlConstants.VALUE, value.get());
            }

            if (isPrettyPrint()) {
                writer.writeCharacters("\n");
            }

        } catch (XMLStreamException e) {
            throw new DocumentWriterException(e);
        }        
    }
}
