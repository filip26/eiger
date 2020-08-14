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
        
        output.println("valid: false");
        
        if (e instanceof MalformedDocumentException) {
            
            final MalformedDocumentException me = (MalformedDocumentException)e;
            
            output.println("error:");
            output.println("  message: " + me.getMessage());
            output.println("  location:");
            output.println("    line: " + me.getLineNumber());
            output.println("    column: " + me.getColumnNumber());

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
    
    static final void printDocInfo(PrintStream output, Document document) {
        output.println("valid: true");
        output.println("alps: ");
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
