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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.concurrent.Callable;

import com.apicatalog.alps.DocumentStatistics;
import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.error.DocumentParserException;
import com.apicatalog.alps.error.InvalidDocumentException;
import com.apicatalog.alps.error.MalformedDocumentException;
import com.apicatalog.alps.io.DocumentParser;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

@Command(
        name = "validate",
        mixinStandardHelpOptions = false,
        description =  "Validate ALPS document",
        sortOptions = false,
        descriptionHeading = "%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "%nOptions:%n"
        )
final class Validator implements Callable<Integer> {
    
    enum Source { XML, JSON }
    
    @Option(names = { "-s", "--source" },  description = "source media type, e.g. --source=json for alps+json", paramLabel = "(json|xml)")
    Source source = null;

    @Option(names = { "-h", "--help" },  hidden = true, usageHelp = true)
    boolean help = false;

    @Parameters(index = "0", arity = "0..1", description = "input file") 
    File file;
    
    @Spec CommandSpec spec;

    private Validator() {
    }

    public int validate() throws IOException {
                
        String sourceMediaType = null;
     
        if (Source.JSON.equals(source)) {
            sourceMediaType = Constants.MEDIA_TYPE_ALPS_JSON;
            
        } else if (Source.XML.equals(source)) {
            sourceMediaType = Constants.MEDIA_TYPE_ALPS_XML;
        }
        
        if (file != null) {
            
            if (!file.exists()) {
                spec.commandLine().getErr().println("Input file '" + file + "' does not exist.");            
                return spec.exitCodeOnInvalidInput();
            }

            if (!file.canRead()) {
                spec.commandLine().getErr().println("Input file '" + file + "' is not readable.");
                return spec.exitCodeOnInvalidInput();
            }

            if (sourceMediaType == null) {
                sourceMediaType = Utils.detectMediaType(file);
            }
        }
        
        if (sourceMediaType == null) {
            spec.commandLine().getErr().println("Missing '--source=(xml|json)' option.");
            return spec.exitCodeOnInvalidInput();
        }

        return validate(sourceMediaType);        
    }
    
    private final int validate(final String sourceMediaType) throws IOException {

        final DocumentParser parser;
        
        try {
            parser = Utils.getParser(sourceMediaType);
            
        } catch (IllegalArgumentException e) {
            spec.commandLine().getErr().println(e.getMessage());
            return spec.exitCodeOnInvalidInput();            
        }

        InputStream inputStream = null;
        
        if (file != null) {
            inputStream = new FileInputStream(file);
        }
        
        if (inputStream == null) {
            inputStream = System.in;
        }

        try {
            printDocInfo(spec.commandLine().getOut(), parser.parse(null, inputStream), sourceMediaType);
                        
        } catch (DocumentParserException e) {
            printError(spec.commandLine().getErr(), e, sourceMediaType, file);
        }
        
        return spec.exitCodeOnSuccess();
    }

    @Override
    public Integer call() throws Exception {
        return validate();
    }
    
    private final void printDocInfo(final PrintWriter out, final Document document, final String mediaType) {
        out.println("# Valid ALPS document");
        out.println("- document: ");

        if (mediaType != null) {
            out.println("    media_type: " + mediaType);
        }

        if (file != null) {
            out.println("    file: " + file);
        }
        
        out.println("    version: " + PrintUtils.versionToString(document.version()));
        out.println("    statistics:");
        
        final DocumentStatistics stats = DocumentStatistics.of(document);
        
        out.println("      descriptors: " + stats.getDescriptors());
        out.println("      docs: " + stats.getDocs());
        out.println("      links: " + stats.getLinks());
        out.println("      extensions: " + stats.getExtensions());
    }

    public static final void printError(final PrintWriter err, final DocumentParserException e, final String mediaType, final File file) {

        err.println("# Invalid ALPS document");
        err.println("- error:");
        err.println("    message: " + e.getMessage());

        if (e instanceof MalformedDocumentException) {
            
            final MalformedDocumentException me = (MalformedDocumentException)e;
            
            err.println("    location:");
            err.println("      line: " + me.getLineNumber());
            err.println("      column: " + me.getColumnNumber());

        } else if (e instanceof InvalidDocumentException) {
            
            final InvalidDocumentException ie = (InvalidDocumentException)e;
            
            if (ie.getPath() != null) {
                err.println("  path:" + ie.getPath());
            }            
        }
        
        if (mediaType != null) {
            err.println("    media_type: " + mediaType);
        }

        if (file != null) {
            err.println("    file: " + file);
        }
    }
}
