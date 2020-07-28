package com.apicatalog.alps;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;

import com.apicatalog.alps.dom.AlpsDocument;

public interface AlpsWriter {

    boolean canWrite(String mediaType);

    AlpsDocument parse(URI baseUri, String mediaType, InputStream stream) throws IOException, AlpsWriterException;
    
    AlpsDocument parse(URI baseUri, String mediaType, Reader reader) throws IOException, AlpsWriterException;
    
}
