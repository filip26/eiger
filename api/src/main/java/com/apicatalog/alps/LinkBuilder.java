package com.apicatalog.alps;

import java.net.URI;
import java.util.List;

import com.apicatalog.alps.dom.element.Link;

public interface LinkBuilder {

    Link build();

    LinkBuilder href(URI href);

    LinkBuilder rel(String string);

    LinkBuilder tag(List<String> tag);
}
