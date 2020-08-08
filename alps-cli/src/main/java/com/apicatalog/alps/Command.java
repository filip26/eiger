package com.apicatalog.alps;

import java.io.PrintStream;

public class Command {

    public static void main(String...args) {
        
        final PrintStream output = System.out;
        
        if (args == null || args.length == 0) {
            printUsage(output);
            return;
        }
        
        switch (args[0]) {
        case "validate":
            validate(output, args);
            break;
            
        case "transform":
            transform(output, args);
            break;
            
        default:
            printUsage(output);
            return;
        }
    }

    private static void printUsage(final PrintStream output) {
        output.println("Usage:");
        output.println("   alps.sh validate [{-s|--source}={json|xml}] [input]");
        output.println("   alps.sh transform [{-s|--source}={json|xml}] [input] {-t|--target}={json|xml} [output]");
        output.println("   alps.sh [{-h|--help}]");
    }
    
    private static final void validate(final PrintStream output, String...args) {
        
    }

    private static final void transform(final PrintStream output, String...args) {
        
    }

}
