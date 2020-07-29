package com.apicatalog.alps;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import com.apicatalog.alps.dom.AlpsDocument;

public interface AlpsWriter {

    boolean canWrite(String mediaType);

    void write(String mediaType, AlpsDocument document, OutputStream stream) throws IOException, AlpsWriterException;
    
    void write(String mediaType, AlpsDocument document, Writer writer) throws IOException, AlpsWriterException;
    
}
