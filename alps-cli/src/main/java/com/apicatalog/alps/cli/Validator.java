package com.apicatalog.alps.cli;

import java.io.PrintStream;

public final class Validator {

    private Validator() {
        
    }
    
    public static final Validator create(final String mediaType, final String input) {
        
        return new Validator();
    }

    public void validate(final PrintStream output) {
        output.println("TODO: validate");
    }
    
}
