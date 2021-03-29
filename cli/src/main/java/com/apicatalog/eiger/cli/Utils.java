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
package com.apicatalog.eiger.cli;

import java.io.File;

import com.apicatalog.alps.io.DocumentParser;
import com.apicatalog.alps.json.JsonDocumentParser;
import com.apicatalog.alps.oas.OpenApiReader;
import com.apicatalog.alps.xml.XmlDocumentParser;

final class Utils {

    private Utils() {
    }

    static final String detectMediaType(File file) {
        if (file.getName() != null) {
            if (file.getName().toLowerCase().endsWith(".xml") || file.getName().toLowerCase().endsWith("+xml")) {
                return Constants.MEDIA_TYPE_ALPS_XML;
            }

            if (file.getName().toLowerCase().endsWith(".json") || file.getName().toLowerCase().endsWith("+json")) {
                return Constants.MEDIA_TYPE_ALPS_JSON;
            }

            if (file.getName().toLowerCase().endsWith(".yaml") || file.getName().toLowerCase().endsWith(".yml") || file.getName().toLowerCase().endsWith("+yaml")) {
                return Constants.MEDIA_TYPE_ALPS_YAML;
            }
        }
        return null;
    }

    static final DocumentParser getParser(final String mediaType) {

        if (Constants.MEDIA_TYPE_ALPS_JSON.equals(mediaType)) {
            return new JsonDocumentParser();
        }

        if (Constants.MEDIA_TYPE_ALPS_XML.equals(mediaType)) {
            return new XmlDocumentParser();
        }

        if (Constants.MEDIA_TYPE_OPEN_API.equals(mediaType)) {
            return new OpenApiReader();
        }

        throw new IllegalArgumentException("Unsupported source media type [" + mediaType + "].");
    }
}
