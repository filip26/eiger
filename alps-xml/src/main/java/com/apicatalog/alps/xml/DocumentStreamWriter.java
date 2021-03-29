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

import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.dom.element.Descriptor;
import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Extension;
import com.apicatalog.alps.dom.element.Link;
import com.apicatalog.alps.error.DocumentWriterException;

public interface DocumentStreamWriter {

    void startDocument(DocumentVersion version) throws DocumentWriterException;

    void endDocument() throws DocumentWriterException;

    void startDescriptor(Descriptor descriptor, boolean selfClose, boolean verbose) throws DocumentWriterException;

    void endDescriptor() throws DocumentWriterException;

    void startDoc(Documentation doc, boolean selfClose, boolean verbose) throws DocumentWriterException;

    void writeDocContent(String content) throws DocumentWriterException;

    void endDoc() throws DocumentWriterException;

    void writeLink(Link link) throws DocumentWriterException;

    void writeExtension(Extension extension) throws DocumentWriterException;

}