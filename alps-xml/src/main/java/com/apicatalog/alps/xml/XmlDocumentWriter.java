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
    
    public XmlDocumentWriter() {
        this.factory = XMLOutputFactory.newDefaultFactory();
        factory.setProperty("escapeCharacters", false);
    }
    
    @Override
    public boolean canWrite(String mediaType) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void write(String mediaType, Document document, OutputStream stream) throws IOException, DocumentException {
        // TODO Auto-generated method stub
        try {
            XmlDocument.write(document, factory.createXMLStreamWriter(stream));
            
        } catch (XMLStreamException e) {
            throw new DocumentException(e);
        }        
    }

    @Override
    public void write(String mediaType, Document document, Writer writer) throws IOException, DocumentException {
        // TODO Auto-generated method stub
        
        try {
            XmlDocument.write(document, factory.createXMLStreamWriter(writer));
            
        } catch (XMLStreamException e) {
            throw new DocumentException(e);
        }
    }
}
