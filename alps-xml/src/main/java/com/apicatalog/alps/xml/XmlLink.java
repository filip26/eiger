package com.apicatalog.alps.xml;

import java.net.URI;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;

import com.apicatalog.alps.dom.element.AlpsLink;

final class XmlLink implements AlpsLink, XmlElement {

    private URI href;
    private String rel;
    
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
        return AlpsXmlKeys.LINK;
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
    public void addDescriptor(XmlDescriptor descriptor) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addLink(XmlLink link) {
        // TODO Auto-generated method stub
    }

    public static void write(Set<AlpsLink> links, XMLStreamWriter writer) throws XMLStreamException {

        if (links == null || links.isEmpty()) {
            return;
        }
        
        for (final AlpsLink link : links) {
            write(link, writer);
        }        
    }
    
    public static void write(final AlpsLink link, XMLStreamWriter writer) throws XMLStreamException {
        
        writer.writeStartElement(AlpsXmlKeys.LINK);
        
        if (link.getHref() != null) {
            writer.writeAttribute(AlpsXmlKeys.HREF, link.getHref().toString());
        }
        
        if (link.getRel() != null && !link.getRel().isBlank()) {
            writer.writeAttribute(AlpsXmlKeys.RELATION, link.getRel());
        }
        
        writer.writeEndElement();

    }

    public static XmlLink create(Attributes attributes) {

        final XmlLink link = new XmlLink();
        
        String href = attributes.getValue(AlpsXmlKeys.HREF);
        
        if (href == null || href.isBlank()) {
            //TODO
        }
        
        link.href = URI.create(href);
        
        String rel = attributes.getValue(AlpsXmlKeys.RELATION);
        
        if (rel == null || rel.isBlank()) {
            //TODO
        }
        
        link.rel = rel;
        
        return link;
    }
}
