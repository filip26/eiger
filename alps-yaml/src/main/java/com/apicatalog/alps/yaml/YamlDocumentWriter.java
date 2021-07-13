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
import java.util.Collection;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.error.DocumentWriterException;
import com.apicatalog.alps.io.DocumentWriter;
import com.apicatalog.yaml.Yaml;
import com.apicatalog.yaml.YamlException;
import com.apicatalog.yaml.node.YamlMapping;
import com.apicatalog.yaml.node.builder.YamlMappingBuilder;
import com.apicatalog.yaml.writer.YamlWriter;

public final class YamlDocumentWriter implements DocumentWriter {

    private final YamlWriter writer;
    private final boolean verbose;

    public YamlDocumentWriter(YamlWriter writer, boolean verbose) {
        this.writer = writer;
        this.verbose = verbose;
    }

    public static final DocumentWriter create(final Writer writer, final boolean verbose) {
        return new YamlDocumentWriter(Yaml.createWriterBuilder(writer).build(), verbose);
    }

    @Override
    public void write(Document document) throws IOException, DocumentWriterException {

        if (document == null) {
            throw new IllegalArgumentException("The 'document' must not be null.");
        }

        try {
            writer.write(toYaml(document, verbose));

        } catch (YamlException e) {
            throw new DocumentWriterException(e);
        }
    }

    @Override
    public void close() throws Exception {
        writer.close();
    }

    private static final YamlMapping toYaml(final Document document, final boolean verbose) {

        final YamlMappingBuilder alps = Yaml.createMappingBuilder();

        // version
        alps.add(YamlConstants.VERSION, Yaml.createScalar(YamlConstants.VERSION_1_0));

        // title
        document.title().ifPresent(title -> alps.add(YamlConstants.TITLE, title));
        
        // documentation
        YamlDocumentationWriter.toYaml(document.documentation(), verbose).ifPresent(doc -> alps.add(YamlConstants.DOCUMENTATION, doc));

        // links
        if (isNotEmpty(document.links())) {
            alps.add(YamlConstants.LINK, YamlLinkWriter.toYaml(document.links()));
        }

        // descriptors
        if (isNotEmpty(document.descriptors())) {
            alps.add(YamlConstants.DESCRIPTOR, YamlDescriptorWriter.toYaml(document.descriptors(), verbose));
        }

        // extensions
        if (isNotEmpty(document.extensions())) {
            alps.add(YamlConstants.EXTENSION, YamlExtensionWriter.toYaml(document.extensions()));
        }

        return Yaml.createMappingBuilder().add(YamlConstants.ROOT, alps).build();
    }

    protected static final boolean isNotEmpty(final Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }
}
