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
     
        stats.descriptors = document.descriptors().size();
        stats.docs = document.documentation().size();
        stats.links = document.links().size();
        stats.extensions = document.extensions().size();

        add(stats, document.descriptors());
        
        return stats;
    }

    static final void add(DocumentStatistics stats, Collection<Descriptor> descriptors) {
        
        stats.descriptors += descriptors.size();

        for (final Descriptor descriptor : descriptors) {
            
            stats.docs += descriptor.documentation().size();
            stats.links += descriptor.links().size();
            stats.extensions += descriptor.extensions().size();
            
            if (!descriptor.descriptors().isEmpty()) {
                add(stats, descriptor.descriptors());
            }
        }
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
