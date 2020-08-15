package com.apicatalog.alps.xml;

import java.net.URI;
import java.util.Deque;
import java.util.Optional;

import org.xml.sax.Attributes;

import com.apicatalog.alps.dom.element.Extension;
import com.apicatalog.alps.error.DocumentException;

final class XmlExtension implements Extension, XmlElement {

    private URI id; 
    
    private URI href;
    
    private String value;
    
    private int elementIndex;
    
    private XmlExtension(int index) {
        this.elementIndex = index;
    }
    
    @Override
    public String getElementName() {
        return AlpsConstants.EXTENSION;
    }

    @Override
    public int getElementIndex() {
        return elementIndex;
    }

    @Override
    public void addText(char[] ch, int start, int length) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addDocumentation(Deque<XmlElement> stack, Attributes attrs) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addLink(Deque<XmlElement> stack, Attributes attrs) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addDescriptor(Deque<XmlElement> stack, Attributes attrs) throws DocumentException {
        // TODO Auto-generated method stub
    }

    @Override
    public void addExtension(Deque<XmlElement> stack, Attributes attrs) throws DocumentException {
        // TODO Auto-generated method stub
    }

    @Override
    public void startElement(String elementName, Attributes attributes) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void endElement(String elementName) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Optional<URI> getHref() {
        return Optional.ofNullable(href);
    }

    @Override
    public URI getId() {
        return id;
    }

    @Override
    public Optional<String> getValue() {
        return Optional.ofNullable(value);
    }

    public static final XmlExtension create(Deque<XmlElement> stack, int elementIndex, Attributes attrs) {
        
        final XmlExtension ext = new XmlExtension(elementIndex);
        
        // TODO Auto-generated method stub
        return ext;
    }  
}
