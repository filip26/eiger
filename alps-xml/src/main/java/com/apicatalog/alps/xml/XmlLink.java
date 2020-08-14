package com.apicatalog.alps.xml;

import java.net.URI;
import java.util.Deque;
import java.util.Set;

import org.xml.sax.Attributes;

import com.apicatalog.alps.dom.element.Link;

final class XmlLink implements Link, XmlElement {

    private final int elementIndex;
    
    private URI href;
    private String rel;
    
    private XmlLink(int index) {
        this.elementIndex = index;
    }
    
    @Override
    public URI getHref() {
        return href;
    }

    @Override
    public String getRel() {
        return rel;
    }

    @Override
    public String getElementName() {
        return AlpsConstants.LINK;
    }

    @Override
    public void addText(char[] ch, int start, int length) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addDocumentation(XmlDocumentation doc) {
        // TODO Auto-generated method stub        
    }

    @Override
    public XmlDescriptor addDescriptor(Deque<XmlElement> stack, Attributes attrs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addLink(XmlLink link) {
        // TODO Auto-generated method stub
    }

    public static void write(Set<Link> links, DocumentStreamWriter writer) throws DocumentStreamException {

        if (links == null || links.isEmpty()) {
            return;
        }
        
        for (final Link link : links) {
            write(link, writer);
        }        
    }
    
    public static void write(final Link link, DocumentStreamWriter writer) throws DocumentStreamException {
        
        writer.writeLink(link.getHref(), link.getRel());
       
    }

    public static XmlLink create(Deque<XmlElement> stack, int index, Attributes attributes) {

        final XmlLink link = new XmlLink(index);
        
        String href = attributes.getValue(AlpsConstants.HREF);
        
        if (href == null || href.isBlank()) {
            //TODO
        }
        
        link.href = URI.create(href);
        
        String rel = attributes.getValue(AlpsConstants.RELATION);
        
        if (rel == null || rel.isBlank()) {
            //TODO
        }
        
        link.rel = rel;
        
        return link;
    }
    
    @Override
    public int getElementIndex() {
        return elementIndex;
    }

    @Override
    public void startElement(String elementName, Attributes attributes) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void endElement(String elementName) {
        // TODO Auto-generated method stub
        
    }
}
