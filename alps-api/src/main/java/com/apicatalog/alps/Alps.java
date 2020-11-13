package com.apicatalog.alps;

import java.net.URI;

import com.apicatalog.alps.api.DescriptorBuilder;
import com.apicatalog.alps.api.DocumentBuilder;
import com.apicatalog.alps.api.DocumentationBuilder;
import com.apicatalog.alps.api.LinkImpl;
import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.dom.element.Descriptor;
import com.apicatalog.alps.dom.element.DescriptorType;
import com.apicatalog.alps.dom.element.Link;

public final class Alps {

    public static final DocumentBuilder createDocument(DocumentVersion version) {
        return new DocumentBuilder(version);
    }
    
    public static final DescriptorBuilder createDescriptor(DescriptorType type) {
        //TODO
        return new DescriptorBuilder();
    }

    public static final DescriptorBuilder createDescriptor(Descriptor descriptor) {
        //TODO
        return new DescriptorBuilder();        
    }

    public static final DocumentationBuilder createDocumentation(String mediaType) {
        return new DocumentationBuilder(mediaType);
    }
    
    public static final Link createLink(URI href, String rel) {
        return new LinkImpl(href, rel);
    }

    private Alps() {}
}
