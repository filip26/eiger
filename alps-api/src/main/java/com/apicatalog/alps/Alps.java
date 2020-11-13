package com.apicatalog.alps;

import com.apicatalog.alps.api.DescriptorBuilder;
import com.apicatalog.alps.api.DocumentBuilder;
import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.dom.element.Descriptor;
import com.apicatalog.alps.dom.element.DescriptorType;

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

    private Alps() {}
}
