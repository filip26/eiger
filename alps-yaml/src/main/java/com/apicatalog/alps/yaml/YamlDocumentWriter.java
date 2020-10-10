package com.apicatalog.alps.yaml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import com.apicatalog.alps.DocumentWriter;
import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.error.DocumentWriterException;
import com.apicatalog.yaml.Yaml;
import com.apicatalog.yaml.YamlException;
import com.apicatalog.yaml.writer.YamlWriter;

public final class YamlDocumentWriter implements DocumentWriter {

    private final boolean verbose;
    
    public YamlDocumentWriter(boolean verbose) {
        this.verbose = verbose;
    }

    public static final DocumentWriter create(final boolean verbose) {
        return new YamlDocumentWriter(verbose);
    }
    
    @Override
    public void write(Document document, OutputStream stream) throws IOException, DocumentWriterException {

        try (final YamlWriter yamlWriter = Yaml.createWriter(stream).build()) {
            
            yamlWriter.write(YamlDocument.toYaml(document, verbose));
            
        } catch (YamlException e) {
            throw new DocumentWriterException(e);
        }        
    }

    @Override
    public void write(Document document, Writer writer) throws IOException, DocumentWriterException {

        try (final YamlWriter yamlWriter = Yaml.createWriter(writer).build()) {
            
            yamlWriter.write(YamlDocument.toYaml(document, verbose));
            
        } catch (YamlException e) {
            throw new DocumentWriterException(e);
        }
    }

}
