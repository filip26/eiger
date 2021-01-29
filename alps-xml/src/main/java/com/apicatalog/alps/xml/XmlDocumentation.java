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
package com.apicatalog.alps.xml;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.xml.sax.Attributes;

import com.apicatalog.alps.Alps;
import com.apicatalog.alps.DocumentationBuilder;
import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Documentation.Content;
import com.apicatalog.alps.error.DocumentWriterException;

final class XmlDocumentation extends XmlElement {

    final DocumentationBuilder builder;
    
    final StringBuilder content;
    
    private XmlDocumentation(String contentType, int index) {
        super(XmlConstants.DOCUMENTATION, index);

        this.builder = Alps.createDocumentation().type(contentType);
        this.content = new StringBuilder();
    }
    
    public static final XmlDocumentation create(final int index, final Attributes attributes) {
        
        String contentType = attributes.getValue(XmlConstants.FORMAT);
        
        if (contentType == null || contentType.isBlank()) {
            contentType = attributes.getValue(XmlConstants.CONTENT_TYPE);
        }
        
        if (contentType == null || contentType.isBlank()) {
            contentType = "text/plain";
        }
        
        return new XmlDocumentation(contentType, index);
    }

    @Override
    public void addText(char[] ch, int start, int length) {        
        builder.append(new String(ch, start, length));
    }

    public static void write(final Set<Documentation> docs, final DocumentStreamWriter writer, boolean verbose) throws DocumentWriterException {
        
        if (docs == null || docs.isEmpty()) {
            return;
        }
        
        for (final Documentation doc : docs) {
            
            if (doc.content().isEmpty() && doc.href().isEmpty()) {
                continue;
            }
            
            writer.startDoc(doc, doc.content().isEmpty(), verbose);
            
            final Optional<String> value = doc.content().map(Content::value).filter(Predicate.not(String::isBlank));
            
            if (value.isPresent()) {
                writer.writeDocContent(value.get());
            }
            
            writer.endDoc();
        }
    }
}
