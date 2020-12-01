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
import java.io.IOException;
import java.io.InputStream;

import com.apicatalog.alps.error.DocumentParserException;
import com.apicatalog.alps.io.DocumentParser;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
        name = "validate",
        mixinStandardHelpOptions = false,
        description =  "Validate ALPS document",
        sortOptions = false,
        descriptionHeading = "%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "%nOptions:%n"
        )
final class Validator implements Runnable {
    
    enum Source { XML, JSON };
    
    @Option(names = { "-s", "--source" },  description = "source media type, e.g. --source=json for alps+json", paramLabel = "(json|xml)")
    Source source = null;

    @Option(names = { "-h", "--help" },  hidden = true, usageHelp = true)
    boolean help = false;

    @Parameters(index = "0", arity = "0..1") 
    File input;

    private Validator() {
    }

    public static void validate(final String[] args) throws IOException {
        
        if (args.length > 2) {
            PrintUtils.printUsage();
            return;
        }
        
        String sourcePath = null;
        String sourceType = null;
        
        for (int i=0; i < args.length; i++) {

            final String argument = args[i];
            
            if (sourceType == null && argument.startsWith(Constants.ARG_SOURCE_SHORT)) {
                
                sourceType = argument.substring(Constants.ARG_SOURCE_SHORT.length());
                
            } else if (sourceType == null && argument.startsWith(Constants.ARG_SOURCE)) {

                sourceType = argument.substring(Constants.ARG_SOURCE.length());
                
            } else if (sourcePath == null) {                
                sourcePath = argument;
                
            } else {
                PrintUtils.printUsage();
                return;
            }
        }
        
        validate(sourceType, sourcePath);
    }
    
    private static final void validate(final String sourceType, final String sourcePath) throws IOException {
        
        final String sourceMediaType = Utils.getMediaType(sourceType, sourcePath, true);
        
        final DocumentParser parser = Utils.getParser(sourceMediaType);

        final InputStream source;
        
        if (sourcePath != null) {
            
            source = Utils.fileToInputStream(sourcePath);
            
            if (source == null) {
                return;
            }
            
        } else {
            source = System.in;
        }

        try {
            
            PrintUtils.printDocInfo(parser.parse(null, source), sourceMediaType, sourcePath);
            
        } catch (DocumentParserException e) {
            PrintUtils.printError(e, sourceMediaType, sourcePath);
        }
    }

    @Override
    public void run() {
        System.out.println("validate: " + input.exists());
        
    }
    
}
