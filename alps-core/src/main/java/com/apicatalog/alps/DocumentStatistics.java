package com.apicatalog.alps;

import java.util.Collection;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.dom.element.Descriptor;

public final class DocumentStatistics {

    private long descriptors;
    private long docs;
    private long links;
    private long extensions = 0;

    private DocumentStatistics() {
        this.descriptors = 0;
        this.docs = 0;
        this.links = 0;
        this.extensions = 0;
    }

    public static final DocumentStatistics of(Document document) {
        
        final DocumentStatistics stats = new DocumentStatistics();
     
        final Collection<Descriptor> descriptors = document.getAllDescriptors(); 
        
        stats.descriptors = descriptors.size();
        stats.docs = document.getDocumentation().size();
        stats.links = document.getLinks().size();
        stats.extensions = document.getExtensions().size();
        
        for (final Descriptor descriptor : descriptors) {
            
            stats.docs += descriptor.getDocumentation().size();
            stats.links += descriptor.getLinks().size();
            stats.extensions += descriptor.getExtensions().size();
        }
        
        return stats;
    }
    
    public long getDescriptors() {
        return descriptors;
    }
    
    public long getDocs() {
        return docs;
    }
    
    public long getLinks() {
        return links;
    }
    
    public long getExtensions() {
        return extensions;
    }
}
