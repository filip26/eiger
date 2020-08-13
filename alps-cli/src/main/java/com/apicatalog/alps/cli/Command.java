package com.apicatalog.alps.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.error.DocumentException;
import com.apicatalog.alps.error.InvalidDocumentException;
import com.apicatalog.alps.error.MalformedDocumentException;

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
        
        String sourcePath = null;
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

            } else if (sourcePath == null) {                
                sourcePath = argument;
                
            } else {
                printUsage(output);
                return;
            }
        }            
        validate(sourceType, sourcePath, output);
    }
    
    private static final void validate(final String sourceType, final String sourcePath, PrintStream output) {
        final InputStream source;
        
        if (sourcePath != null) {
            
            source = fileToInputStream(sourcePath);
            
            if (source == null) {
                return;
            }
            
        } else {
            source = System.in;
        }
        
        try {
            printDocInfo(output, Validator.create(sourceType).parse(source));
            
        } catch (DocumentException e) {
            
            printError(System.err, sourcePath, e);
        }
    }

    private static final void transform(final PrintStream output, String...args) {

        if (args.length > 5) {
            printUsage(output);
            return;
        }

        InputStream source = null;
        String sourceType = null;
        InputStream target = null;
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
                
                source = fileToInputStream(argument);
                
            } else if (target == null) {
                
                target = fileToInputStream(argument);
                
            } else {
                printUsage(output);
                return;
            }
        }
    }
    
    private static final boolean isNotValidMediaType(final String type) {
        return !"xml".equalsIgnoreCase(type) && !"json".equalsIgnoreCase(type);         
    }

    private static final InputStream fileToInputStream(final String path) {
        
        final File file = new File(path);
        
        if (!file.exists()) {
            System.err.println("File '" + path + "' does not exist.");            
            return null;
        }

        if (!file.canRead()) {
            System.err.println("Input file '" + path + "' is not readable.");
            return null;
        }
        
        try {
            return new FileInputStream(file);
            
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        
        return null;
    }
    
    private static final void printError(PrintStream output, String path, DocumentException e) {
        
        output.println("Source is not valid ALPS document");
        
        if (e instanceof MalformedDocumentException) {
            
            final MalformedDocumentException me = (MalformedDocumentException)e;
            
            output.println("  error: " + me.getMessage());
            output.println("  cause: Unexpected character at line=" + me.getLineNumber() + ", column=" + me.getColumnNumber() + ".");
            
            
        } else if (e instanceof InvalidDocumentException) {
            
            final InvalidDocumentException ie = (InvalidDocumentException)e;
            output.println("  error: " + ie.getMessage());
            
            if (ie.getPath() != null) {
                output.println("  path:" + ie.getPath());
            }
            
        } else {
            output.println("  error:" + e.getMessage());
        }
    }
    
    private static final void printDocInfo(PrintStream output, Document document) {
        output.println("Source is valid ALPS document.");
        output.println("  version: " + versionToString(document.getVersion()));
        output.println("  descriptors:");
        output.println("    top_level: " + document.getDescriptors().size());
        output.println("    total: " + document.getAllDescriptors().size());
    }
    
    private static final String versionToString(DocumentVersion version) {
        if (version != null) {
            
            if (DocumentVersion.VERSION_1_0.equals(version)) {
                return "1.0";
            }
            
            return version.name();
        }
        return "n/a";
    }
}
