package com.apicatalog.alps.json;

import java.io.Writer;
import java.net.URI;

import com.apicatalog.alps.DocumentPrinter;
import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.dom.element.DescriptorType;

public final class JsonDocumentPrinter implements DocumentPrinter {

    private final Writer writer;
    private final boolean pretty;
    
    public JsonDocumentPrinter(final Writer writer, final boolean pretty) {
        this.writer = writer;
        this.pretty = pretty;
    }
    
    @Override
    public void beginDocument(DocumentVersion version) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void endDocument() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beginDescriptor(DescriptorType type) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void endDescriptor() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beginLink() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void endLink() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beginDocumentation() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beginDocumentation(String value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void endDocumentation() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beginExtension() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void endExtension() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void printId(URI id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void printHref(URI href) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void printName(String name) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void printReturnType(URI returnType) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void printRel(String name) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void printMedia(String mediaType) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void printText(String content) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void printValue(String value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void printAttribute(String name, String value) {
        // TODO Auto-generated method stub
        
    }

}
