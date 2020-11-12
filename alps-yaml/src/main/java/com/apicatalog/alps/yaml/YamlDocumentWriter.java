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
package com.apicatalog.alps.yaml;

import java.io.IOException;
import java.io.Writer;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.error.DocumentWriterException;
import com.apicatalog.alps.io.DocumentWriter;
import com.apicatalog.yaml.Yaml;
import com.apicatalog.yaml.YamlException;
import com.apicatalog.yaml.writer.YamlWriter;

public final class YamlDocumentWriter implements DocumentWriter {

    private final YamlWriter writer;
    private final boolean verbose;
    
    public YamlDocumentWriter(YamlWriter writer, boolean verbose) {
        this.writer = writer;
        this.verbose = verbose;
    }

    public static final DocumentWriter create(final Writer writer, final boolean verbose) {
        return new YamlDocumentWriter(Yaml.createWriter(writer).build(), verbose);
    }
    
    @Override
    public void write(Document document) throws IOException, DocumentWriterException {

        try {

            writer.write(YamlDocument.toYaml(document, verbose));
            
        } catch (YamlException e) {
            throw new DocumentWriterException(e);
        }
    }
}
