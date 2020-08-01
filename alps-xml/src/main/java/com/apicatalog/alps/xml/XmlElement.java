package com.apicatalog.alps.xml;

interface XmlElement {

    String getElementName();

    void addText(char[] ch, int start, int length);

    void addDocumentation(XmlDocumentation doc);

    void addDescriptor(XmlDescriptor descriptor);

}
