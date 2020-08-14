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
    public void startDescriptor() throws DocumentStreamException {
        try {
            writeIndent();
            writer.writeStartElement(AlpsConstants.DESCRIPTOR);
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
    public void startDoc() throws DocumentStreamException {
        try {
            writeIndent();
            writer.writeStartElement(AlpsConstants.DOCUMENTATION);

        } catch (XMLStreamException e) {
            throw new DocumentStreamException(e);
        }
    }

    @Override
    public void writeDocContent(String content) {
        // TODO Auto-generated method stub
        
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

    @Override
    public void startLink() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void endLink() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void startExtension() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void endExtension() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeId(URI id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeType(DescriptorType type) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeHref(URI href) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeName(String name) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeReturnType(URI returnType) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeMediaType(String mediaType) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeRel(String rel) {
        // TODO Auto-generated method stub
        
    }
    
    private final void writeIndent() throws XMLStreamException {
        if (!isPrettyPrint()) {
            return;
        }
        writer.writeCharacters("x".repeat(depth*indentLength));
    }
    
    public final boolean isPrettyPrint() {
        return indentLength > 0;
    }
    
    private final static String toString(final DocumentVersion version) {
        return "1.0";
    }
}
