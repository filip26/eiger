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

import com.apicatalog.alps.DocumentParser;
import com.apicatalog.alps.DocumentWriter;
import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.error.DocumentException;

final class Transformer {

    public static final void transform(String...args) throws IOException {

        if (args.length > 5) {
            PrintUtils.printUsage();
            return;
        }

        String sourcePath = null;
        String sourceType = null;
        
        String targetPath = null;
        String targetType = null;
        
        boolean prettyPrint = false;
        
        for (int i=0; i < args.length; i++) {

            final String argument = args[i];
            
            if (sourceType == null && argument.startsWith(Constants.ARG_S)) {
                
                sourceType = argument.substring(Constants.ARG_S.length());
                
            } else if (sourceType == null && argument.startsWith(Constants.ARG_SOURCE)) {

                sourceType = argument.substring(Constants.ARG_SOURCE.length());

            } else if (targetType == null && argument.startsWith(Constants.ARG_T)) {
                
                targetType = argument.substring(Constants.ARG_T.length());
                
            } else if (targetType == null && argument.startsWith(Constants.ARG_TARGET)) {

                targetType = argument.substring(Constants.ARG_TARGET.length());
                
            } else if (argument.startsWith(Constants.ARG_P) || argument.startsWith(Constants.ARG_PRETTY)) {

                prettyPrint = true;
                
            } else if (sourcePath == null) {                
                sourcePath = argument;

            } else if (targetPath == null) {                
                targetPath = argument;

            } else {
                PrintUtils.printUsage();
                return;
            }

        }
        transform(sourceType, sourcePath, targetType, targetPath, prettyPrint);
    }
    
    private static final void transform(final String sourceType, final String sourcePath, final String targetType, final String targetPath, final boolean prettyPrint) throws IOException {
        
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
        
        final String targetMediaType = Utils.getMediaType(targetType, targetPath, false);
        
        final DocumentWriter writer = Utils.getWriter(targetMediaType, prettyPrint);

        final OutputStream target;
        
        if (targetPath != null) {
            
            target = Utils.fileToOutputStream(targetPath);
            
            if (source == null) {
                return;
            }
            
        } else {
            target = System.out;
        }
        
        try {
            
            Document document = parser.parse(null, source);
            
            writer.write(document, target);
            
        } catch (DocumentException e) {
            
            PrintUtils.printError(sourcePath, e, sourceMediaType, sourcePath);
        }

    } 
}
