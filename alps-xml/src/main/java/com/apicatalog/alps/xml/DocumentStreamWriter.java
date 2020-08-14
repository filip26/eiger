package com.apicatalog.alps.xml;

import java.net.URI;

import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.dom.element.DescriptorType;

public interface DocumentStreamWriter {

    void startDocument(DocumentVersion version) throws DocumentStreamException;
    
    void endDocument() throws DocumentStreamException;
    
    void startDescriptor(URI id, URI href, DescriptorType type, URI returnType, String name) throws DocumentStreamException;
    
    void endDescriptor() throws DocumentStreamException;
    
    void startDoc(String mediaType, URI href) throws DocumentStreamException;
    
    void writeDocContent(String content) throws DocumentStreamException;
    
    void endDoc() throws DocumentStreamException;
    
    void writeLink(URI href, String rel) throws DocumentStreamException;
    
    void writeExtension(URI id, URI href, String value) throws DocumentStreamException;
            
}