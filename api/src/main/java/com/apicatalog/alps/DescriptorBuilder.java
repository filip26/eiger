package com.apicatalog.alps;

import java.net.URI;

import com.apicatalog.alps.dom.element.Descriptor;
import com.apicatalog.alps.dom.element.DescriptorType;
import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Extension;
import com.apicatalog.alps.dom.element.Link;

public interface DescriptorBuilder {
    
    DescriptorBuilder add(Descriptor descriptor);
    
    DescriptorBuilder add(DescriptorBuilder descriptor);

    
    DescriptorBuilder add(Documentation documentation);
        
    DescriptorBuilder add(DocumentationBuilder documentation);

    
    DescriptorBuilder add(Extension extension);

    DescriptorBuilder add(Link link);

    
    DescriptorBuilder id(URI id);
    DescriptorBuilder type(DescriptorType type);
    DescriptorBuilder title(String title);
    DescriptorBuilder name(String name);
    DescriptorBuilder href(URI href);
    DescriptorBuilder definition(URI definition);
    DescriptorBuilder returnType(URI returnType);

    Descriptor build();
    
}
