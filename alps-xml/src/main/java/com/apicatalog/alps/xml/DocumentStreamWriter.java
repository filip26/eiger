package com.apicatalog.alps.xml;

import java.net.URI;

import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.dom.element.DescriptorType;

public interface DocumentStreamWriter {

    void startDocument(DocumentVersion version) throws DocumentStreamException;
    
    void endDocument() throws DocumentStreamException;
    
    void startDescriptor() throws DocumentStreamException;
    
    void endDescriptor() throws DocumentStreamException;
    
    void startDoc() throws DocumentStreamException;
    
    void writeDocContent(String content) throws DocumentStreamException;
    
    void endDoc() throws DocumentStreamException;
    
    void startLink() throws DocumentStreamException;
    
    void endLink() throws DocumentStreamException;
    
    void startExtension() throws DocumentStreamException;
    
    void endExtension() throws DocumentStreamException;
    
    void writeId(URI id) throws DocumentStreamException;
    
    void writeType(DescriptorType type) throws DocumentStreamException;
    
    void writeHref(URI href) throws DocumentStreamException;
    
    void writeName(String name) throws DocumentStreamException;
    
    void writeReturnType(URI returnType) throws DocumentStreamException;
    
    void writeMediaType(String mediaType) throws DocumentStreamException;
    
    void writeRel(String rel) throws DocumentStreamException;

}