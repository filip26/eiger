package com.apicatalog.alps.cli;

import java.io.InputStream;
import java.io.PrintStream;

final class Transformer {

    private static final void transform(final PrintStream output, String...args) {

        if (args.length > 5) {
            PrintUtils.printUsage(output);
            return;
        }

        InputStream source = null;
        String sourceType = null;
        InputStream target = null;
        String targetType = null;
        
        for (int i=1; i < args.length; i++) {

            final String argument = args[i];
            
            if (argument.startsWith(Constants.ARG_S)) {
                
                sourceType = argument.substring(Constants.ARG_S.length());
                
                if (isNotValidMediaType(sourceType)) {
                    PrintUtils.printUsage(output);
                    return;
                }

            } else if (argument.startsWith(Constants.ARG_SOURCE)) {

                sourceType = argument.substring(Constants.ARG_SOURCE.length());
                
                if (isNotValidMediaType(sourceType)) {
                    PrintUtils.printUsage(output);
                    return;
                }

            } else if (argument.startsWith(Constants.ARG_T)) {
                
                targetType = argument.substring(Constants.ARG_T.length());
                
                if (isNotValidMediaType(sourceType)) {
                    PrintUtils.printUsage(output);
                    return;
                }

            } else if (argument.startsWith(Constants.ARG_TARGET)) {

                targetType = argument.substring(Constants.ARG_TARGET.length());
                
                if (isNotValidMediaType(sourceType)) {
                    PrintUtils.printUsage(output);
                    return;
                }

            } else if (source == null) {
                
                source = Utils.fileToInputStream(argument);
                
            } else if (target == null) {
                
                target = Utils.fileToInputStream(argument);
                
            } else {
                PrintUtils.printUsage(output);
                return;
            }
        }
    }
    
    @Deprecated
    private static final boolean isNotValidMediaType(final String type) {
        return !"xml".equalsIgnoreCase(type) && !"json".equalsIgnoreCase(type);         
    }
}
