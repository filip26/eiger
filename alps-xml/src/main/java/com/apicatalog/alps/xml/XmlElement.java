package com.apicatalog.alps.xml;

import java.util.Deque;

import org.xml.sax.Attributes;

import com.apicatalog.alps.AlpsParserException;

interface XmlElement {

    String getElementName();
    
    int getElementIndex();

    void addText(char[] ch, int start, int length);

    void addDocumentation(XmlDocumentation doc);

    void addLink(XmlLink link);

    XmlDescriptor addDescriptor(Deque<XmlElement> stack, Attributes attrs) throws AlpsParserException;

    void startElement(String elementName, Attributes attributes);

    void endElement(String elementName);
}
