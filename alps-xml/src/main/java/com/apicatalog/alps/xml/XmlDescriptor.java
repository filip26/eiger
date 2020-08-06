package com.apicatalog.alps.xml;

import java.net.URI;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;

import com.apicatalog.alps.dom.element.Descriptor;
import com.apicatalog.alps.dom.element.DescriptorType;
import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Extension;
import com.apicatalog.alps.dom.element.Link;
import com.apicatalog.alps.error.DocumentError;
import com.apicatalog.alps.error.DocumentException;
import com.apicatalog.alps.error.InvalidDocumentException;

public class XmlDescriptor implements Descriptor, XmlElement {

    private final int elementIndex;
    
    private URI id;
    
    private DescriptorType type;
    
    private URI returnValue;
    
    private Set<Documentation> documentation;
    
    private Set<Descriptor> descriptors;
    
    private Set<Link> links;
    
    private XmlDescriptor(int index) {
        this.elementIndex = index;
    }
    
    public static final XmlDescriptor create(Deque<XmlElement> stack, int index, Attributes attrs) throws DocumentException {
    
        String id = attrs.getValue(AlpsConstants.ID);
        
        if (id == null || id.isBlank()) {            
            throw new InvalidDocumentException(DocumentError.MISSING_ID, XPathUtil.getPath(stack, AlpsConstants.DESCRIPTOR, index));
        }
        
        XmlDescriptor descriptor = new XmlDescriptor(index);
        
        try {
            descriptor.id = URI.create(id);
            
        } catch (IllegalArgumentException e) {
            throw new InvalidDocumentException(DocumentError.MALFORMED_URI, XPathUtil.getPath(stack, AlpsConstants.DESCRIPTOR, index), "Descriptor id must be valid URI but was " + id);
        }
        
        descriptor.type = parseType(attrs.getValue(AlpsConstants.TYPE));
        
        String rt = attrs.getValue(AlpsConstants.RETURN_VALUE);
        
        if (rt != null && !rt.isBlank()) {
            descriptor.returnValue = URI.create(rt);
        }

        descriptor.documentation = new LinkedHashSet<>();
        
        descriptor.descriptors = new LinkedHashSet<>();
        
        descriptor.links = new LinkedHashSet<>();
        
        // TODO Auto-generated constructor stub
        return descriptor;
    }
    
    private static DescriptorType parseType(String value) {

        if (value == null || value.isBlank()) {
            return DescriptorType.SEMANTIC;
        }
        
        //TODO check 
        return DescriptorType.valueOf(value.toUpperCase());
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
        return AlpsConstants.DESCRIPTOR;
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
    public DescriptorType getType() {
        return type;
    }

    @Override
    public Optional<URI> getReturnType() {
        return Optional.ofNullable(returnValue);
    }

    @Override
    public Set<Documentation> getDocumentation() {
        return documentation;
    }

    @Override
    public Set<Extension> getExtensions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<Descriptor> getDescriptors() {
        return descriptors;
    }

    @Override
    public Optional<Descriptor> getParent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<Link> getLinks() {
        return links;
    }

    @Override
    public XmlDescriptor addDescriptor(Deque<XmlElement> stack, Attributes attrs) throws DocumentException {
        XmlDescriptor dsc = XmlDescriptor.create(stack, descriptors.size(), attrs);
        descriptors.add(dsc);
        return dsc;
    }

    @Override
    public void addLink(XmlLink link) {
        links.add(link);
        
    }
    public static void write(Set<Descriptor> descriptors, XMLStreamWriter writer) throws XMLStreamException {
        if (descriptors == null || descriptors.isEmpty()) {
            return;
        }
        
        for (final Descriptor descriptor : descriptors) {
            
            writer.writeStartElement(AlpsConstants.DESCRIPTOR);
            
            // id
            if (descriptor.getId() == null) {
                //TODO
            }
            writer.writeAttribute(AlpsConstants.ID, descriptor.getId().toString());
         
            // type
            final DescriptorType type = descriptor.getType();
            
            if (type != null && !DescriptorType.SEMANTIC.equals(type)) {
                writer.writeAttribute(AlpsConstants.TYPE, type.toString().toLowerCase());
            }
            
            // return type
            final Optional<URI> returnType = descriptor.getReturnType(); 
            
            if (returnType.isPresent()) {
                writer.writeAttribute(AlpsConstants.RETURN_VALUE, returnType.get().toString());
            }

            XmlDocumentation.write(descriptor.getDocumentation(), writer);
            
            XmlLink.write(descriptor.getLinks(), writer);
            
            XmlDescriptor.write(descriptor.getDescriptors(), writer);
            
            writer.writeEndElement();
        }
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
