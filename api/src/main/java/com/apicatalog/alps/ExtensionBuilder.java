package com.apicatalog.alps;

import java.net.URI;

import com.apicatalog.alps.dom.element.Extension;

public interface ExtensionBuilder {

    Extension build();

    ExtensionBuilder attribute(String key, String value);

    ExtensionBuilder href(URI href);

    ExtensionBuilder value(String string);

    ExtensionBuilder id(URI create);

}
