package com.apicatalog.alps.xml;

public interface XmlElement {

    void addDocumentation(XmlDocumentation doc);

    void addText(char[] ch, int start, int length);

    String getElementName();

}
