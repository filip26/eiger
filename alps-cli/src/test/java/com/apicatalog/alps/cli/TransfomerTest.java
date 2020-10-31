package com.apicatalog.alps.cli;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import com.apicatalog.alps.error.DocumentParserException;
import com.apicatalog.alps.error.DocumentWriterException;

class TransfomerTest {

    @Test
    void testXml2Json() throws IOException, DocumentParserException, DocumentWriterException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        try (final InputStream is = TransfomerTest.class.getResourceAsStream("contacts.min.xml")) {
        
            Transformer.transform(Constants.MEDIA_TYPE_ALPS_XML, is, Constants.MEDIA_TYPE_ALPS_JSON, out, false, false);
        }
        
        try (final InputStream is = TransfomerTest.class.getResourceAsStream("contacts.min.json")) {
        
            assertArrayEquals(IOUtils.toByteArray(is), out.toByteArray());
            
        }        
    }
    
    
}
