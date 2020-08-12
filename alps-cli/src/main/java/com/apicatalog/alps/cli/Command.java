package com.apicatalog.alps.cli;

import java.io.PrintStream;

public class Command {

    private static final String VALIDATE = "validate";
    
    private static final String TRANSFORM = "transform";
    
    private static final String ARG_S = "-s=";
    private static final String ARG_SOURCE = "--source=";

    private static final String ARG_T = "-t=";
    private static final String ARG_TARGET = "--target=";
    
    public static void main(String...args) {
        
        final PrintStream output = System.out;
        
        if (args == null || args.length == 0) {
            printUsage(output);
            return;
        }
        
        switch (args[0]) {
        case VALIDATE:
            validate(output, args);
            break;
            
        case TRANSFORM:
            transform(output, args);
            break;
            
        default:
            printUsage(output);
            return;
        }
    }

    private static void printUsage(final PrintStream output) {
        output.println("Usage:");
        output.print("   alps.sh ");
        output.print(VALIDATE);
        output.println(" [{-s|--source}={json|xml}] [input]");
        output.print("   alps.sh ");
        output.print(TRANSFORM);
        output.println(" [{-s|--source}={json|xml}] [input] {-t|--target}={json|xml} [output]");
        output.println("   alps.sh [{-h|--help}]");
    }
    
    private static final void validate(final PrintStream output, String...args) {

        if (args.length > 3) {
            printUsage(output);
            return;
        }
        
        String source = null;
        String sourceType = null;
        
        for (int i=1; i < args.length; i++) {

            final String argument = args[i];
            
            if (argument.startsWith(ARG_S)) {
                
                sourceType = argument.substring(ARG_S.length());
                
                if (isNotValidMediaType(sourceType)) {
                    printUsage(output);
                    return;
                }

            } else if (argument.startsWith(ARG_SOURCE)) {

                sourceType = argument.substring(ARG_SOURCE.length());
                
                if (isNotValidMediaType(sourceType)) {
                    printUsage(output);
                    return;
                }

            } else if (source == null) {
                
                source = argument;
                
            } else {
                printUsage(output);
                return;
            }
        }    
        
        Validator.create(sourceType, source).validate(output);
    }

    private static final void transform(final PrintStream output, String...args) {

        if (args.length > 5) {
            printUsage(output);
            return;
        }

        String source = null;
        String sourceType = null;
        String target = null;
        String targetType = null;
        
        for (int i=1; i < args.length; i++) {

            final String argument = args[i];
            
            if (argument.startsWith(ARG_S)) {
                
                sourceType = argument.substring(ARG_S.length());
                
                if (isNotValidMediaType(sourceType)) {
                    printUsage(output);
                    return;
                }

            } else if (argument.startsWith(ARG_SOURCE)) {

                sourceType = argument.substring(ARG_SOURCE.length());
                
                if (isNotValidMediaType(sourceType)) {
                    printUsage(output);
                    return;
                }

            } else if (argument.startsWith(ARG_T)) {
                
                targetType = argument.substring(ARG_T.length());
                
                if (isNotValidMediaType(sourceType)) {
                    printUsage(output);
                    return;
                }

            } else if (argument.startsWith(ARG_TARGET)) {

                targetType = argument.substring(ARG_TARGET.length());
                
                if (isNotValidMediaType(sourceType)) {
                    printUsage(output);
                    return;
                }

            } else if (source == null) {
                
                source = argument;
                
            } else if (target == null) {
                
                target = argument;
                
            } else {
                printUsage(output);
                return;
            }
        }
    }
    
    private static final boolean isNotValidMediaType(final String type) {
        return !"xml".equalsIgnoreCase(type) && !"json".equalsIgnoreCase(type);         
    }

}
