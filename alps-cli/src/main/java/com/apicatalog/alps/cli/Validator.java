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

import java.io.IOException;
import java.io.InputStream;

import com.apicatalog.alps.DocumentParser;
import com.apicatalog.alps.error.DocumentParserException;

final class Validator {
    
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
    
}
