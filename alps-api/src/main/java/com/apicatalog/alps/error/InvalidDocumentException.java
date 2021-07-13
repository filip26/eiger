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
package com.apicatalog.alps.error;

public final class InvalidDocumentException extends DocumentParserException {

    private static final long serialVersionUID = -7523514970503251586L;

    private final DocumentError code;

    private final String path;

    public InvalidDocumentException(DocumentError code, final String message) {
        this(code, null, message);
    }

    public InvalidDocumentException(DocumentError code, String path, final String message) {
        super(message);
        this.code = code;
        this.path = path;
    }

    public InvalidDocumentException(DocumentError code, String path, final Throwable cause) {
        super(cause);
        this.code = code;
        this.path = path;
    }

    public DocumentError getCode() {
        return code;
    }

    public String getPath() {
        return path;
    }
}
