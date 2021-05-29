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

import java.net.URI;
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

final class XmlDocument extends XmlElement {

    final DocumentBuilder builder;

    public XmlDocument(DocumentVersion version) {
        super(XmlConstants.DOCUMENT, -1);

        this.builder = Alps.createDocument(version);
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
    public void complete(XmlTitle title) {
        builder.title(title.getText());
    }

    public static void write(Document document, DocumentStreamWriter writer, boolean verbose) throws DocumentWriterException {

        writer.startDocument(document);
        
        XmlDocumentation.write(document.documentation(), writer, verbose);

        XmlLink.write(document.links(), writer);

        XmlDescriptor.write(document.descriptors(), writer, verbose);

        XmlExtension.write(document.extensions(), writer);

        writer.endDocument();
    }

    public Document build(URI baseUri) throws InvalidDocumentException {
        return builder.base(baseUri).build();
    }
    
    public void binTitle(Deque<XmlElement> stack, Attributes attributes)  throws DocumentParserException {
        
    }
}
