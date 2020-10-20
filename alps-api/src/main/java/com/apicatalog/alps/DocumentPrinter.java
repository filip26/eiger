package com.apicatalog.alps;

import java.net.URI;

import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.dom.element.DescriptorType;

public interface DocumentPrinter {

    void beginDocument(DocumentVersion version);
    void endDocument();
    
    void beginDescriptor(DescriptorType type);
    void endDescriptor();
    
    void beginLink();
    void endLink();
    
    void beginDocumentation();
    void beginDocumentation(String value);
    void endDocumentation();
    
    void beginExtension();
    void endExtension();
    
    void printId(URI id);
    void printHref(URI href);
    void printName(String name);
    void printReturnType(URI returnType);
    void printRel(String name);
    void printMedia(String mediaType);
    
    void printText(String content);
    
    void printValue(String value);
    void printAttribute(String name, String value);
}
