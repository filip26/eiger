package com.apicatalog.alps.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.apicatalog.alps.AlpsWriter;
import com.apicatalog.alps.AlpsWriterException;
import com.apicatalog.alps.dom.AlpsDocument;

public class AlpsXmlWriter implements AlpsWriter {

    private final XMLOutputFactory factory;
    
    public AlpsXmlWriter() {
        this.factory = XMLOutputFactory.newDefaultFactory();
        
        
    }
    
    @Override
    public boolean canWrite(String mediaType) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void write(String mediaType, AlpsDocument document, OutputStream stream) throws IOException, AlpsWriterException {
        // TODO Auto-generated method stub
        try {
            write(document, factory.createXMLStreamWriter(stream));
        } catch (XMLStreamException e) {
            throw new AlpsWriterException(e);
        }        
    }

    @Override
    public void write(String mediaType, AlpsDocument document, Writer writer) throws IOException, AlpsWriterException {
        // TODO Auto-generated method stub
        
        try {
            write(document, factory.createXMLStreamWriter(writer));
        } catch (XMLStreamException e) {
            throw new AlpsWriterException(e);
        }
    }
    
    private void write(AlpsDocument document, XMLStreamWriter writer) throws XMLStreamException, AlpsWriterException {

        writer.writeStartDocument(Charset.defaultCharset().name(), "1.0");
        
        writer.writeStartElement(AlpsXmlKeys.DOCUMENT);
        
        if (document.getVersion() == null) {
            throw new AlpsWriterException();
        }
        
        writer.writeAttribute(AlpsXmlKeys.VERSION, "1.0");
        
        writer.writeEndElement();
        writer.writeEndDocument();
        
    }
}
