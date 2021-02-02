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
package com.apicatalog.eiger.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.Callable;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.error.DocumentParserException;
import com.apicatalog.alps.error.DocumentWriterException;
import com.apicatalog.alps.io.DocumentParser;
import com.apicatalog.alps.io.DocumentWriter;
import com.apicatalog.alps.json.JsonDocumentWriter;
import com.apicatalog.alps.xml.XmlDocumentWriter;
import com.apicatalog.alps.yaml.YamlDocumentWriter;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

@Command(
        name = "transform",
        mixinStandardHelpOptions = false,
        sortOptions = false,
        description =  "Transform documents into ALPS",
        descriptionHeading = "%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "%nOptions:%n"
        )
final class Transformer implements Callable<Integer> {

    enum Source { XML, JSON, OAS }
    enum Target { XML, JSON, YAML }
        
    @Option(names = { "-s", "--source" },  description = "source media type, e.g. --source=oas for OpenAPI", paramLabel = "(xml|json|oas)")
    Source source = null;

    @Option(names = { "-t", "--target" },  description = "target media type, e.g. --target=yaml for alps+yaml", paramLabel = "(xml|json|yaml)", required = true)
    Target target = null;

    @Option(names = { "-h", "--help" },  hidden = true, usageHelp = true)
    boolean help = false;

    @Parameters(index = "0", arity = "0..1", description = "input file") 
    File file;

    @Option(names = { "-p", "--pretty" }, description = "print pretty JSON|XML")
    boolean pretty = false;

    @Option(names = { "-v", "--verbose" }, description = "include default values")
    boolean verbose = false;

    @Spec CommandSpec spec;
    
    Transformer() {}
    
    public final int transform() throws Exception {

        String sourceMediaType = null;
        
        if (Source.JSON.equals(source)) {
            sourceMediaType = Constants.MEDIA_TYPE_ALPS_JSON;
            
        } else if (Source.XML.equals(source)) {
            sourceMediaType = Constants.MEDIA_TYPE_ALPS_XML;
            
        } else if (Source.OAS.equals(source)) {
            sourceMediaType = Constants.MEDIA_TYPE_OPEN_API;
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
            spec.commandLine().getErr().println("Missing '--source=(xml|json|oas)' option.");
            return spec.exitCodeOnInvalidInput();
        }
        
        return transform(sourceMediaType);
    }
    
    private final int transform(final String sourceMediaType) throws Exception {
        
        InputStream inputStream = null;
        
        if (file != null) {
            inputStream = new FileInputStream(file);
        }
        
        if (inputStream == null) {
            inputStream = System.in;
        }
        
        try {
            return transform(sourceMediaType, inputStream, spec.commandLine().getOut());
            
        } catch (DocumentParserException e) {
            Validator.printError(spec.commandLine().getErr(), e, sourceMediaType, file);
            
        } catch (DocumentWriterException e) {
            spec.commandLine().getErr().println(e.getMessage());
        }
        return spec.exitCodeOnExecutionException();
    }
    
    protected final int transform(final String sourceMediaType, final InputStream source, final PrintWriter target) throws Exception {
        
        final DocumentParser parser;
        
        try {
            parser = Utils.getParser(sourceMediaType);
            
        } catch (IllegalArgumentException e) {
            spec.commandLine().getErr().println(e.getMessage());
            return spec.exitCodeOnInvalidInput();            
        }
        
        final Document document = parser.parse(null, source);
        
        if (document == null) {
            return spec.exitCodeOnInvalidInput();
        }
        
        try (final DocumentWriter writer = getWriter(target)) {
            writer.write(document);            
        }

        return spec.exitCodeOnSuccess();
    }

    @Override
    public Integer call() throws Exception {
        return transform();
    }
    
    private final DocumentWriter getWriter(final Writer writer) throws DocumentWriterException {

        if (Target.JSON.equals(target)) {
            return JsonDocumentWriter.create(writer, pretty, verbose);
            
        } else if (Target.XML.equals(target)) {
            return XmlDocumentWriter.create(writer, pretty, verbose);
            
        } else if (Target.YAML.equals(target)) {
            return YamlDocumentWriter.create(writer, verbose);
        }

        throw new IllegalStateException();
    }    
}
