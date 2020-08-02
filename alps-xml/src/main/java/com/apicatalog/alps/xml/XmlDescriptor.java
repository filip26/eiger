package com.apicatalog.alps.xml;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;

import com.apicatalog.alps.dom.element.AlpsDescriptor;
import com.apicatalog.alps.dom.element.AlpsDescriptorType;
import com.apicatalog.alps.dom.element.AlpsDocumentation;
import com.apicatalog.alps.dom.element.AlpsExtension;
import com.apicatalog.alps.dom.element.AlpsLink;

public class XmlDescriptor implements AlpsDescriptor, XmlElement {

    private URI id;
    
    private AlpsDescriptorType type;
    
    private URI returnValue;
    
    private Set<AlpsDocumentation> documentation;
    
    private Set<AlpsDescriptor> descriptors;
    
    private Set<AlpsLink> links;
    
    public static final XmlDescriptor create(Attributes attrs) {
    
        String id = attrs.getValue(AlpsXmlKeys.ID);
        
        if (id == null || id.isBlank()) {
            //TODO
        }
        
        XmlDescriptor descriptor = new XmlDescriptor();
        
        descriptor.id = URI.create(id);
        
        descriptor.type = parseType(attrs.getValue(AlpsXmlKeys.TYPE));
        
        String rt = attrs.getValue(AlpsXmlKeys.RETURN_VALUE);
        
        if (rt != null && !rt.isBlank()) {
            descriptor.returnValue = URI.create(rt);
        }

        descriptor.documentation = new LinkedHashSet<>();
        
        descriptor.descriptors = new LinkedHashSet<>();
        
        descriptor.links = new LinkedHashSet<>();
        
        // TODO Auto-generated constructor stub
        return descriptor;
    }
    
    private static AlpsDescriptorType parseType(String value) {

        if (value == null || value.isBlank()) {
            return AlpsDescriptorType.SEMANTIC;
        }
        
        //TODO check 
        return AlpsDescriptorType.valueOf(value.toUpperCase());
    }

    @Override
    public void addDocumentation(XmlDocumentation doc) {
        documentation.add(doc);
    }

    @Override
    public void addText(char[] ch, int start, int length) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getElementName() {
        return AlpsXmlKeys.DESCRIPTOR;
    }

    @Override
    public URI getId() {
        return id;
    }

    @Override
    public Optional<URI> getHref() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<String> getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AlpsDescriptorType getType() {
        return type;
    }

    @Override
    public Optional<URI> getReturnType() {
        return Optional.ofNullable(returnValue);
    }

    @Override
    public Set<AlpsDocumentation> getDocumentation() {
        return documentation;
    }

    @Override
    public Set<AlpsExtension> getExtensions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<AlpsDescriptor> getDescriptors() {
        return descriptors;
    }

    @Override
    public Optional<AlpsDescriptor> getParent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<AlpsLink> getLinks() {
        return links;
    }

    @Override
    public void addDescriptor(XmlDescriptor descriptor) {
        descriptors.add(descriptor);
    }

    @Override
    public void addLink(XmlLink link) {
        links.add(link);
        
    }
    public static void write(Set<AlpsDescriptor> descriptors, XMLStreamWriter writer) throws XMLStreamException {
        if (descriptors == null || descriptors.isEmpty()) {
            return;
        }
        
        for (final AlpsDescriptor descriptor : descriptors) {
            
            writer.writeStartElement(AlpsXmlKeys.DESCRIPTOR);
            
            // id
            if (descriptor.getId() == null) {
                //TODO
            }
            writer.writeAttribute(AlpsXmlKeys.ID, descriptor.getId().toString());
         
            // type
            final AlpsDescriptorType type = descriptor.getType();
            
            if (type != null && !AlpsDescriptorType.SEMANTIC.equals(type)) {
                writer.writeAttribute(AlpsXmlKeys.TYPE, type.toString().toLowerCase());
            }
            
            // return type
            if (descriptor.getReturnType().isPresent()) {
                writer.writeAttribute(AlpsXmlKeys.RETURN_VALUE, descriptor.getReturnType().get().toString());
            }

            XmlDocumentation.write(descriptor.getDocumentation(), writer);
            
            XmlLink.write(descriptor.getLinks(), writer);
            
            XmlDescriptor.write(descriptor.getDescriptors(), writer);
            
            writer.writeEndElement();
        }
    }
}
