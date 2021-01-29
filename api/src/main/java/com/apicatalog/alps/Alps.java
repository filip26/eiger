package com.apicatalog.alps;

import java.net.URI;

import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.dom.element.Link;

public final class Alps {

    public static final DocumentBuilder createDocument(DocumentVersion version) {
        return new DocumentBuilderImpl(version);
    }
    
    public static final DescriptorBuilder createDescriptor() {
        return new DescriptorBuilderImpl();
    }

    public static final DocumentationBuilder createDocumentation() {
        return new DocumentationBuilderImpl();
    }
    
    public static final Link createLink(URI href, String rel) {
        return new LinkImpl(href, rel);
    }

    public static final ExtensionBuilder createExtension() {
        return new ExtensionBuilderImpl();
    }
    
    private Alps() {}
}
