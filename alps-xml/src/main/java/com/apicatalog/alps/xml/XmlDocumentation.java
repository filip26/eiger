package com.apicatalog.alps.xml;

import java.net.URI;

import org.xml.sax.Attributes;

import com.apicatalog.alps.dom.element.AlpsDocumentation;

final class XmlDocumentation implements AlpsDocumentation, XmlElement {

    
    public static final XmlDocumentation create(Attributes attributes) {
        //TODO
        return new XmlDocumentation(); 
    }

    @Override
    public URI getHref() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getMediaType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getContent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addDocumentation(XmlDocumentation doc) {
        // TODO Auto-generated method stub
    }

    @Override
    public String getElementName() {
        return AlpsXmlKeys.DOCUMENTATION;
    }

    @Override
    public void addText(char[] ch, int start, int length) {
        // TODO Auto-generated method stub
        
    }
    
}
