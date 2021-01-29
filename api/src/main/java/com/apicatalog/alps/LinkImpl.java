package com.apicatalog.alps;

import java.net.URI;

import com.apicatalog.alps.dom.element.Link;

final class LinkImpl implements Link {

    final URI href;
    final String rel;
    
    public LinkImpl(URI href, String rel) {
        this.href = href;
        this.rel = rel;
    }
    
    @Override
    public URI href() {
        return href;
    }

    @Override
    public String rel() {
        return rel;
    }

}
