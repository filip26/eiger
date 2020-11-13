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
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.error.DocumentParserException;
import com.apicatalog.alps.error.DocumentWriterException;
import com.apicatalog.alps.io.DocumentParser;
import com.apicatalog.alps.io.DocumentWriter;

final class Transformer {

    private Transformer() {}
    
    public static final void transform(String...args) throws IOException {

        if (args.length > 5) {
            PrintUtils.printUsage();
            return;
        }

        String sourcePath = null;
        String sourceType = null;
        
        String targetType = null;
        
        boolean prettyPrint = false;
        boolean verbose = false;
        
        for (int i=0; i < args.length; i++) {

            final String argument = args[i];
            
            if (sourceType == null && argument.startsWith(Constants.ARG_SOURCE_SHORT)) {
                
                sourceType = argument.substring(Constants.ARG_SOURCE_SHORT.length());
                
            } else if (sourceType == null && argument.startsWith(Constants.ARG_SOURCE)) {

                sourceType = argument.substring(Constants.ARG_SOURCE.length());

            } else if (targetType == null && argument.startsWith(Constants.ARG_TARGET_SHORT)) {
                
                targetType = argument.substring(Constants.ARG_TARGET_SHORT.length());
                
            } else if (targetType == null && argument.startsWith(Constants.ARG_TARGET)) {

                targetType = argument.substring(Constants.ARG_TARGET.length());
                
            } else if (argument.startsWith(Constants.ARG_PRETTY_SHORT) || argument.startsWith(Constants.ARG_PRETTY)) {

                prettyPrint = true;

            } else if (argument.startsWith(Constants.ARG_VERBOSE_SHORT) || argument.startsWith(Constants.ARG_VERBOSE)) {

                verbose = true;

            } else if (sourcePath == null) {                
                sourcePath = argument;

            } else {
                PrintUtils.printUsage();
                return;
            }
        }
                
        transform(sourceType, sourcePath, targetType, prettyPrint, verbose);
    }
    
    private static final void transform(final String sourceType, final String sourcePath, final String targetType, final boolean prettyPrint, final boolean verbose) throws IOException {
        
        final String sourceMediaType = Utils.getMediaType(sourceType, sourcePath, true);
        
        final InputStream source;
        
        if (sourcePath != null) {
            
            source = Utils.fileToInputStream(sourcePath);
            
            if (source == null) {
                return;
            }
            
        } else {
            source = System.in;
        }
        
        final String targetMediaType = Utils.getMediaType(targetType, null, false);
        
        try {
            transform(sourceMediaType, source, targetMediaType, System.out, prettyPrint, verbose);
            
        } catch (DocumentParserException e) {
            PrintUtils.printError(e, sourceMediaType, sourcePath);
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
            
        }
    }
    
    protected static final void transform(final String sourceMediaType, final InputStream source, final String targetMediaType, final OutputStream target, boolean prettyPrint, boolean verbose) throws Exception {
        
        final DocumentParser parser = Utils.getParser(sourceMediaType);
        
        final Document document = parser.parse(null, source);
        
        final DocumentWriter writer = Utils.getWriter(new OutputStreamWriter(target), targetMediaType, prettyPrint, verbose);

        try {
            writer.write(document);
            
        } finally {
            writer.close();
        }
    }
}
