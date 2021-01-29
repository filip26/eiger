package com.apicatalog.alps;

import com.apicatalog.alps.dom.DocumentVersion;

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
    
    public static final LinkBuilder createLink() {
        return new LinkBuilderImpl();
    }

    public static final ExtensionBuilder createExtension() {
        return new ExtensionBuilderImpl();
    }
    
    private Alps() {}
}
