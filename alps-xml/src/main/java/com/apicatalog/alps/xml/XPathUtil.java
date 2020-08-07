package com.apicatalog.alps.xml;

import java.util.Deque;
import java.util.stream.Collectors;

class XPathUtil {
    
    public static final String getPath(Deque<XmlElement> stack) {
        return stack.stream().map(XPathUtil::getElementPath).collect(Collectors.joining());
    }

    public static final String getPath(Deque<XmlElement> stack, final String element, final int index) {
        return  stack.stream().map(XPathUtil::getElementPath).collect(Collectors.joining()) 
                + getElementPath(element, index)
                ;
    }

    private static final String getElementPath(XmlElement element) {
        return getElementPath(element.getElementName(), element.getElementIndex());
    }

    private static final String getElementPath(final String elementName, final int elementIndex) {
        return "/" + elementName 
                + (elementIndex != -1 ? "[position()=" + (elementIndex + 1) + "]" : ""); 
    }

    private XPathUtil() {
    }
}
