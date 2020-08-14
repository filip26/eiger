package com.apicatalog.alps.cli;

import java.io.PrintStream;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.error.DocumentException;
import com.apicatalog.alps.error.InvalidDocumentException;
import com.apicatalog.alps.error.MalformedDocumentException;

final class PrintUtils {

    private PrintUtils() {
    }
    
    static void printUsage(final PrintStream output) {
        output.println("Usage:");
        output.print("   alps.sh ");
        output.print(Constants.VALIDATE);
        output.println(" [{-s|--source}={json|xml}] [input]");
//        output.print("   alps.sh ");
//        output.print(Constants.TRANSFORM);
//        output.println(" [{-s|--source}={json|xml}] [input] {-t|--target}={json|xml} [output]");
        output.println("   alps.sh [{-h|--help}]");
    }
    
    static final void printError(PrintStream output, String path, DocumentException e) {

        output.println("# Invalid ALPS document");
        output.println("- error:");
        output.println("    message: " + e.getMessage());

        if (e instanceof MalformedDocumentException) {
            
            final MalformedDocumentException me = (MalformedDocumentException)e;
            
            output.println("    location:");
            output.println("      line: " + me.getLineNumber());
            output.println("      column: " + me.getColumnNumber());

        } else if (e instanceof InvalidDocumentException) {
            
            final InvalidDocumentException ie = (InvalidDocumentException)e;
            
            if (ie.getPath() != null) {
                output.println("  path:" + ie.getPath());
            }            
        }
    }
    
    static final void printDocInfo(final PrintStream output, final Document document, final String mediaType, final String filePath) {
        output.println("# Valid ALPS document");
        output.println("- document: ");

        if (mediaType != null) {
            output.println("    media_type: " + mediaType);
        }

        if (filePath != null) {
            output.println("    file: " + filePath);
        }
        
        output.println("    version: " + versionToString(document.getVersion()));
        output.println("    descriptors:");
        output.println("      top_level: " + document.getDescriptors().size());
        output.println("      total: " + document.getAllDescriptors().size());
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
