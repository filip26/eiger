package com.apicatalog.alps;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Set;

import com.apicatalog.alps.dom.AlpsDocument;

public interface AlpsParser {

    /**
     * A set of supported media types by the parser.
     * 
     * @return a set of supported media types, never <code>null</code>
     */
    Set<String> mediaTypes();
    
    AlpsDocument parse(String mediaType, InputStream stream) throws IOException, AlpsParserException;
    
    AlpsDocument parse(String mediaType, Reader reader) throws IOException, AlpsParserException;
    
}
