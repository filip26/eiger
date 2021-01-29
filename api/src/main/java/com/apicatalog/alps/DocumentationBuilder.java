package com.apicatalog.alps;

import java.net.URI;

import com.apicatalog.alps.dom.element.Documentation;

public interface DocumentationBuilder {

    Documentation build();

    DocumentationBuilder append(String line);

    DocumentationBuilder type(String contentType);

    DocumentationBuilder href(URI href);
}
