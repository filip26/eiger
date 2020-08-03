package com.apicatalog.alps.xml;

import java.util.Deque;
import java.util.stream.Collectors;

class XmlUtils {
    
    public static final String getPath(Deque<XmlElement> stack) {
        return "/" + stack.stream().map(XmlElement::getElementName).collect(Collectors.joining("/"));
    }

    public static final String getPath(Deque<XmlElement> stack, final String element) {
        return "/" + stack.stream().map(XmlElement::getElementName).collect(Collectors.joining("/")) + "/" + element;
    }

    private XmlUtils() {
    }
}
