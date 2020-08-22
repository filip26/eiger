/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.apicatalog.alps.cli;

import java.io.PrintStream;

import com.apicatalog.alps.DocumentStatistics;
import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.error.DocumentParserException;
import com.apicatalog.alps.error.InvalidDocumentException;
import com.apicatalog.alps.error.MalformedDocumentException;

final class PrintUtils {

    private PrintUtils() {
    }
    
    static void printUsage() {
        System.out.println("Usage:");
        System.out.print("   alps.sh ");
        System.out.print(Constants.VALIDATE);
        System.out.println(" [{-s|--source}={json|xml}] [input]");
        System.out.print("   alps.sh ");
        System.out.print(Constants.TRANSFORM);
        System.out.println(" [{-s|--source}={json|xml}] [input] {-t|--target}={json|xml} [{-p|--pretty}] [{-v|--verbose}]");
        System.out.println("   alps.sh [{-h|--help}]");
    }
    
    static final void printError(final String path, final DocumentParserException e, final String mediaType, final String filePath) {

        final PrintStream output = System.err;
        
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
        
        if (mediaType != null) {
            output.println("    media_type: " + mediaType);
        }

        if (filePath != null) {
            output.println("    file: " + filePath);
        }
    }

    static final void printDocInfo(final Document document, final String mediaType, final String filePath) {
        System.out.println("# Valid ALPS document");
        System.out.println("- document: ");

        if (mediaType != null) {
            System.out.println("    media_type: " + mediaType);
        }

        if (filePath != null) {
            System.out.println("    file: " + filePath);
        }
        
        System.out.println("    version: " + versionToString(document.version()));
        System.out.println("    statistics:");
        
        final DocumentStatistics stats = DocumentStatistics.of(document);
        
        System.out.println("      descriptors: " + stats.getDescriptors());
        System.out.println("      docs: " + stats.getDocs());
        System.out.println("      links: " + stats.getLinks());
        System.out.println("      extensions: " + stats.getExtensions());
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
