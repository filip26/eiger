package com.apicatalog.alps.cli;

import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.error.DocumentException;
import com.apicatalog.alps.jsonp.JsonDocumentParser;

public final class Validator {
    
    private Validator() {
        
    }
    
    public static final Validator create(final String mediaType) {
        
        return new Validator();
    }

    public void validate(final InputStream input, final PrintStream output) {
        
        try {
        
        Document doc = (new JsonDocumentParser()).parse(null, "application/json", input);
        output.println("TODO: validate " + doc);
        
        } catch (DocumentException e) {
            System.err.println(e);
            e.printStackTrace();
        }
        
    }
    
}
