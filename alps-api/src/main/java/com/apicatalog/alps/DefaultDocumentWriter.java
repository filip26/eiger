package com.apicatalog.alps;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Documentation.Content;
import com.apicatalog.alps.dom.element.Extension;
import com.apicatalog.alps.dom.element.Link;
import com.apicatalog.alps.error.DocumentWriterException;

public class DefaultDocumentWriter implements DocumentWriter {
    
    protected final DocumentPrinter printer;
    protected final boolean verbose;
    
    public DefaultDocumentWriter(final DocumentPrinter printer, final boolean verbose) {
        this.printer = printer;
        this.verbose = verbose;
    }

    @Override
    public void write(final Document document) throws IOException, DocumentWriterException {
        
        if (document == null) {
            throw new IllegalArgumentException();
        }
        
        printer.beginDocument(document.version());
        
        writeDocs(document.documentation());
        writeLinks(document.links());        
//
//        XmlDescriptor.write(document.descriptors(), writer, verbose);

        writeExts(document.extensions());

        printer.endDocument();        
    }
    
    protected void writeDocs(final Set<Documentation> docs)  throws IOException, DocumentWriterException { 
        
        if (docs == null || docs.isEmpty()) {
            return;
        }
        
        for (final Documentation doc : docs) {
            write(doc);
        }        
    }
    
    protected void write(final Documentation documentation)  throws IOException, DocumentWriterException { 
        
        if (documentation == null || (documentation.href().isEmpty() && documentation.content().isEmpty())) {
            return;
        }

        printer.beginDocumentation();

        final Optional<Content> content = documentation.content();
        
        if (documentation.href().isEmpty()
                && content.isPresent()
                && content
                       .map(Documentation.Content::type)
                       .filter(Predicate.isEqual("text/plain").or(Predicate.isEqual("text")))
                       .isPresent()
                ) {

            printer.printText(content.get().value());
            return ;            
        }
                     
        documentation.href().ifPresent(printer::printHref);

        if (verbose) {
            content
                .map(Documentation.Content::type)
                .ifPresentOrElse(
                        printer::printMedia, 
                        () -> printer.printMedia("text")
                        );
        } else {
            content
                .map(Documentation.Content::type)
                .filter(Predicate.isEqual("text/plain").negate().and(Predicate.isEqual("text").negate()))
                .ifPresent(printer::printMedia);            
        }
    
        content
            .map(Documentation.Content::value)
            .ifPresent(printer::printValue);
        
        printer.endDocumentation();
    }        
    
    protected void writeLinks(final Set<Link> links) throws IOException, DocumentWriterException {

        if (links == null || links.isEmpty()) {
            return;
        }
        
        for (final Link link : links) {

            printer.beginLink();
            
            if (link.href() != null) {
                printer.printHref(link.href());
            }
            
            if (link.rel() != null && !link.rel().isBlank()) {
                printer.printRel(link.rel());
            }
            
            printer.endLink();
        } 
    }


    protected void writeExts(final Set<Extension> exts) throws IOException, DocumentWriterException {

        if (exts == null || exts.isEmpty()) {
            return;
        }
        
        for (final Extension extension : exts) {
            
            printer.beginExtension();
            
            printer.printId(extension.id());
            
            extension.href().ifPresent(printer::printHref);
            extension.value().ifPresent(printer::printValue);
            
            // custom attributes
            extension
                    .attributes()
                    .forEach(printer::printAttribute);

            printer.endExtension();
        }
    }
}
