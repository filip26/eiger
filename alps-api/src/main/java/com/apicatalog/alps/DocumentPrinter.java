package com.apicatalog.alps;

import java.net.URI;

import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.dom.element.DescriptorType;

public interface DocumentPrinter {

    void beginDocument(DocumentVersion version);
    void endDocument();
    
    void beginDescriptors(boolean oneItem);
    void endDescriptors();
    
    void beginLinks(boolean oneItem);
    void endLinks();
    
    void beginExtensions(boolean oneItem);
    void endExtensions();
    
    void beginDocumentation(boolean oneItem);
    void endDocumentation();

    void next();
    
    void printId(URI id);
    void printHref(URI href);
    void printName(String name);
    void printReturnType(URI returnType);
    void printRel(String name);
    void printMedia(String mediaType);
    void printType(DescriptorType type);
    void printText(String content);
    void printValue(String value);
    
    void printAttribute(String name, String value);
}
