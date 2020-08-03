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

public class AlpsParserException extends Exception {
    
    private static final long serialVersionUID = 7826650786160247358L;
    
    private final AlpsErrorCode code;
    private final String path;
    
//TODO    private final String mediaType;
    
    public AlpsParserException(final AlpsErrorCode code) {
        this(code, null, code.name());
    }

    public AlpsParserException(AlpsErrorCode code, String path) {
        this(code, path, path + ": " + code.name());
    }

    public AlpsParserException(AlpsErrorCode code, String path, final String message) {
        super(message);
        this.code = code;
        this.path = path;
    }
    
    public AlpsParserException(AlpsErrorCode code, final Throwable throwable) {
        super(code.name(), throwable);
        this.code = code;
        this.path = null;
    }

    public AlpsErrorCode getCode() {
        return code;
    }
    
    public String getPath() {
        return path;
    }
}
