package com.apicatalog.alps;

import java.net.URI;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.dom.element.Descriptor;
import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Extension;
import com.apicatalog.alps.dom.element.Link;
import com.apicatalog.alps.error.InvalidDocumentException;

public interface DocumentBuilder {

    DocumentBuilder add(Descriptor descriptor);

    DocumentBuilder add(DescriptorBuilder descriptor);

    DocumentBuilder add(Documentation documentation);

    DocumentBuilder add(DocumentationBuilder documentation);

    DocumentBuilder add(Extension extension);

    DocumentBuilder add(ExtensionBuilder extension);

    DocumentBuilder add(Link link);

    DocumentBuilder add(LinkBuilder link);

    DocumentBuilder base(URI baseUri);
    
    DocumentBuilder title(String title);

    Document build() throws InvalidDocumentException;
}
