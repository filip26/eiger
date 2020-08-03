package com.apicatalog.alps.xml;

import java.util.Deque;
import java.util.stream.Collectors;

class XPathUtil {
    
    public static final String getPath(Deque<XmlElement> stack) {
        return "/" + stack.stream().map(XmlElement::getElementName).collect(Collectors.joining("/"));
    }

    public static final String getPath(Deque<XmlElement> stack, final String element, final int index) {
        return "/" 
                + stack.stream().map(XmlElement::getElementName).collect(Collectors.joining("/")) 
                + "/" + element
                + (index != -1 ? "[position()=" + (index + 1) + "]" : "");
    }

    private XPathUtil() {
    }
}
