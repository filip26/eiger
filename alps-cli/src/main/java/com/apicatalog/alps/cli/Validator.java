package com.apicatalog.alps.cli;

import java.io.InputStream;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.error.DocumentException;
import com.apicatalog.alps.jsonp.JsonDocumentParser;

public final class Validator {
    
    private Validator() {
        
    }
    
    public static final Validator create(final String mediaType) {
        
        return new Validator();
    }

    public Document parse(final InputStream input) throws DocumentException {
        
//        try {
        
        return (new JsonDocumentParser()).parse(null, "application/json", input);
        
//        } catch (DocumentException e) {
////            System.err.println( + e.getMessage());
////            e.printStackTrace();
//        }
        
    }
    
}
