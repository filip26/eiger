package com.apicatalog.alps.xml;

import java.util.Deque;

public class XmlTitle extends XmlElement {

    private final StringBuilder builder;
    
    protected XmlTitle(String elementName, int elementIndex) {
        super(elementName, elementIndex);
        this.builder = new StringBuilder();
    }
    
    @Override
    public void addText(char[] ch, int start, int length) {
        builder.append(ch, start, length);
    }

    public static XmlTitle create(Deque<XmlElement> stack) {
        final XmlTitle title = new XmlTitle(XmlConstants.TITLE, -1);
        return title;
    }

    public String getText() {
        return builder.toString();
    }
    
}
