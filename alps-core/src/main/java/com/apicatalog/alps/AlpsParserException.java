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

public final class AlpsParserException extends Exception {
    
    private static final long serialVersionUID = 7826650786160247358L;
    
    private final AlpsErrorCode code;
    
    private final String path;

    private final int columnNumber;
    private final int lineNumber;
    
    public AlpsParserException(final AlpsErrorCode code) {
        this(code, null, code.name());
    }

    public AlpsParserException(AlpsErrorCode code, String path) {
        this(code, path, path + ": " + code.name());
    }

    public AlpsParserException(int lineNumber, int columnNumber, String message) {
        super(message);
        this.code = AlpsErrorCode.PARSER_ERROR;
        this.columnNumber = columnNumber;
        this.lineNumber = lineNumber;
        this.path = null;
    }
    
    public AlpsParserException(AlpsErrorCode code, String path, final String message) {
        super(message);
        this.code = code;
        this.path = path;
        this.columnNumber = -1;
        this.lineNumber = -1;
    }
    
    public AlpsParserException(AlpsErrorCode code, final Throwable throwable) {
        super(code.name(), throwable);
        this.code = code;
        this.path = null;
        this.columnNumber = -1;
        this.lineNumber = -1;        
    }

    public AlpsErrorCode getCode() {
        return code;
    }
    
    public String getPath() {
        return path;
    }
    
    public int getColumnNumber() {
        return columnNumber;
    }
    
    public int getLineNumber() {
        return lineNumber;
    }
}
