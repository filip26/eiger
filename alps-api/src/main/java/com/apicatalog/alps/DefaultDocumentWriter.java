package com.apicatalog.alps;

import java.io.IOException;
import java.util.Set;
import java.util.function.Predicate;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Documentation.Content;
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
        
        write(document.documentation());
        
//        XmlLink.write(document.links(), writer);
//
//        XmlDescriptor.write(document.descriptors(), writer, verbose);
//        
//        XmlExtension.write(document.extensions(), writer);

        printer.endDocument();        
    }
    
    protected void write(final Set<Documentation> docs)  throws IOException, DocumentWriterException { 
        
        if (docs == null || docs.isEmpty()) {
            return;
        }
        
        for (final Documentation doc : docs) {
            
            if (doc.content().isEmpty() && doc.href().isEmpty()) {
                continue;
            }

            printer.beginDocumentation();

//            writer.startDoc(doc, doc.content().isEmpty(), verbose);            
            doc.content()
                    .map(Content::value)
                    .filter(Predicate.not(String::isBlank))
                    .ifPresent(value -> printer.printDocContent(doc.content().get().type(), value));
            
            printer.endDocumentation();
        }        
    }
}
