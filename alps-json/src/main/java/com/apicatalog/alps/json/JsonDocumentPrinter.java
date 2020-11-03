package com.apicatalog.alps.json;

import java.io.Writer;
import java.net.URI;

import com.apicatalog.alps.DocumentPrinter;
import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.dom.element.DescriptorType;

import jakarta.json.Json;
import jakarta.json.stream.JsonGenerator;

public final class JsonDocumentPrinter implements DocumentPrinter {

    //private final Writer writer;
    private final JsonGenerator generator;
    private final boolean pretty;
    
    public JsonDocumentPrinter(final Writer writer, final boolean pretty) {
        //this.writer = writer;
        this.generator = Json.createGenerator(writer);  //TODO pretty
        this.pretty = pretty;
    }
    
    @Override
    public void beginDocument(DocumentVersion version) {
        generator.writeStartObject();
        generator.writeKey(JsonConstants.ROOT);
        generator.writeStartObject();
        generator.writeKey(JsonConstants.VERSION);
        generator.write("1.0");
    }


    @Override
    public void endDocument() {
        generator.writeEnd();
        generator.writeEnd();
        generator.flush();
    }

    @Override
    public void beginDescriptors(boolean oneItem) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void endDescriptors() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beginLinks(boolean oneItem) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void endLinks() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beginExtensions(boolean oneItem) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void endExtensions() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beginDocumentation(boolean oneItem) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void endDocumentation() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void next() {
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
    public void printType(DescriptorType type) {
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
