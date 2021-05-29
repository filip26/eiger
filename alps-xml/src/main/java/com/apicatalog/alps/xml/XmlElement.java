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

import com.apicatalog.alps.error.DocumentParserException;

abstract class XmlElement {

    private final int elementIndex;
    private final String elementName;

    private int descriptors;
    private int links;
    private int docs;
    private int exts;

    protected XmlElement(String elementName, int elementIndex) {
        this.elementName = elementName;
        this.elementIndex = elementIndex;

        this.descriptors = 0;
        this.links = 0;
        this.docs = 0;
        this.exts = 0;
    }

    public String getElementName() {
        return elementName;
    }

    public int getElementIndex() {
        return elementIndex;
    }

    public void beginDescriptor(final Deque<XmlElement> stack, Attributes attrs) throws DocumentParserException {
        final XmlDescriptor dsc = XmlDescriptor.create(stack, descriptors++, attrs);
        stack.push(dsc);
    }

    public void beginLink(Deque<XmlElement> stack, Attributes attrs) {
        final XmlLink link = XmlLink.create(links++, attrs);
        stack.push(link);
    }

    public void beginDocumentation(Deque<XmlElement> stack, Attributes attrs) {
        final XmlDocumentation doc = XmlDocumentation.create(docs++, attrs);
        stack.push(doc);
    }

    public void beginExtension(Deque<XmlElement> stack, Attributes attrs) throws DocumentParserException {
        final XmlExtension ext = XmlExtension.create(stack, exts++, attrs);
        stack.push(ext);
    }

    public void addText(char[] ch, int start, int length) {}

//    public <T extends XmlElement> void complete(T elemenet) {}

    public void complete(XmlDescriptor doc) {}
    
    public void complete(XmlDocumentation doc) {}

    public void complete(XmlLink link) {}

    public void complete(XmlExtension ext) {}

    public void beginTitle(Deque<XmlElement> stack, Attributes attributes)  throws DocumentParserException {
        final XmlTitle title = XmlTitle.create(stack);
        stack.push(title);
    }
    
    public void complete(XmlTitle title) {}

}