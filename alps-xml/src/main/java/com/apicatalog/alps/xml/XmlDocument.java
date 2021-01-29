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

import java.util.Deque;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.apicatalog.alps.Alps;
import com.apicatalog.alps.DocumentBuilder;
import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.error.DocumentParserException;
import com.apicatalog.alps.error.DocumentWriterException;
import com.apicatalog.alps.error.InvalidDocumentException;

final class XmlDocument implements XmlElement {

    final DocumentBuilder builder;
    
    private int descriptors;
    private int links;
    private int docs;
    private int exts;
    
    public XmlDocument(DocumentVersion version) {
        this.builder = Alps.createDocument(version);
        
        this.descriptors = 0;
        this.links = 0;
        this.docs = 0;
        this.exts = 0;
    }
    
    public static final XmlDocument create(Attributes attrs) throws SAXException {
        return new XmlDocument(readVersion(attrs));
    }

    private static final DocumentVersion readVersion(Attributes attrs) throws SAXException {

        // version
        String version = attrs.getValue(XmlConstants.VERSION);

        if (version == null || version.isBlank() || "1.0".equals(version)) {

            return DocumentVersion.VERSION_1_0;            
        }
        
        throw new SAXException();
    }
    
    @Override
    public String getElementName() {
        return XmlConstants.DOCUMENT;
    }

    @Override
    public void beginDescriptor(final Deque<XmlElement> stack, Attributes attrs) throws DocumentParserException {
        final XmlDescriptor dsc = XmlDescriptor.create(stack, descriptors++, attrs);
        stack.push(dsc);
    }

    @Override
    public void beginLink(Deque<XmlElement> stack, Attributes attrs) throws DocumentParserException {
        final XmlLink link = XmlLink.create(stack, links++, attrs);
        stack.push(link);
    }

    @Override
    public void beginDocumentation(Deque<XmlElement> stack, Attributes attrs) throws DocumentParserException {
        final XmlDocumentation doc = XmlDocumentation.create(stack, docs++, attrs);
        stack.push(doc);
    }
    
    @Override
    public void complete(XmlDescriptor descriptor) {
        builder.add(descriptor.builder.build());
    }

    @Override
    public void complete(XmlDocumentation doc) {
        builder.add(doc.builder.build());
    }
    
    @Override
    public void complete(XmlLink link) {
        builder.add(link.link);
    }
    
    @Override
    public void complete(XmlExtension ext) {
        builder.add(ext.builder.build());
    }

    @Override
    public void beginExtension(Deque<XmlElement> stack, Attributes attrs) throws DocumentParserException {
        final XmlExtension ext = XmlExtension.create(stack, exts++, attrs);
        stack.push(ext);
    }

    public static void write(Document document, DocumentStreamWriter writer, boolean verbose) throws DocumentWriterException {

        writer.startDocument(document.version());
                
        XmlDocumentation.write(document.documentation(), writer, verbose);
        
        XmlLink.write(document.links(), writer);

        XmlDescriptor.write(document.descriptors(), writer, verbose);
        
        XmlExtension.write(document.extensions(), writer);

        writer.endDocument();        
    }
    
    @Override
    public int getElementIndex() {
        return -1;
    }

    public Document build() throws InvalidDocumentException {
        return builder.build();
    }
}
