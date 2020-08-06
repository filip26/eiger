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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;

import com.apicatalog.alps.dom.AlpsDocument;

public interface AlpsParser {

    /**
     * Indicates where the media type can be parsed by the parser instance.
     * 
     * @return <code>true</code> if the parser supports the given media type, otherwise false.
     */
    boolean canParse(String mediaType);
    
    AlpsDocument parse(URI baseUri, String mediaType, InputStream stream) throws IOException, DocumentException;
    
    AlpsDocument parse(URI baseUri, String mediaType, Reader reader) throws IOException, DocumentException;

}
