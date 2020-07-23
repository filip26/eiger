package com.apicatalog.alps;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;

import com.apicatalog.alps.dom.AlpsDocument;

public interface AlpsParser {

    /**
     * Indicates where the media type can be parsed by the parser instance.
     * 
     * @return <code>true</code> if the parser supports the given media type, otherwise false.
     */
    boolean canParse(String mediaType);
    
    AlpsDocument parse(URI baseUri, String mediaType, InputStream stream) throws IOException, AlpsParserException;
    
    AlpsDocument parse(URI baseUri, String mediaType, Reader reader) throws IOException, AlpsParserException;

}
