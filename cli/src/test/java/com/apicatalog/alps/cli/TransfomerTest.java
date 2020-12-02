package com.apicatalog.alps.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

class TransfomerTest {

    @Test
    void testXml2Json() throws Exception {

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        try (final InputStream is = TransfomerTest.class.getResourceAsStream("contacts.min.xml")) {
        
            Transformer.transform(Constants.MEDIA_TYPE_ALPS_XML, is, Constants.MEDIA_TYPE_ALPS_JSON, out, false, false);
        }
        
        try (final InputStream is = TransfomerTest.class.getResourceAsStream("contacts.min.json")) {
        
            assertEquals(IOUtils.toString(is, Charset.defaultCharset()), out.toString());
            
        }        
    }
    
    
}
