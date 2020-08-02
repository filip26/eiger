package com.apicatalog.alps.xml;

import java.net.URI;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;

import com.apicatalog.alps.dom.element.AlpsDocumentation;

final class XmlDocumentation implements AlpsDocumentation, XmlElement {

    private StringBuilder content;
    
    public static final XmlDocumentation create(Attributes attributes) {
        //TODO
        XmlDocumentation doc = new XmlDocumentation();
        doc.content = new StringBuilder();
        
        return doc; 
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
        return content.toString();
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
        //TODO strip leading and trailing spaces
        content.append(ch, start, length);
    }

    public static void write(Set<AlpsDocumentation> docs, XMLStreamWriter writer) throws XMLStreamException {
        if (docs == null || docs.isEmpty()) {
            return;
        }
        
        for (final AlpsDocumentation doc : docs) {
            writer.writeStartElement(AlpsXmlKeys.DOCUMENTATION);
            writer.writeCharacters(doc.getContent());
            writer.writeEndElement();
        }
    }

    @Override
    public void addDescriptor(XmlDescriptor descriptor) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addLink(XmlLink link) {
        // TODO Auto-generated method stub
        
    }
    
}
