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
package com.apicatalog.alps.cli;

final class Constants {

   
    static final String ARG_VALIDATE = "validate";
   
    static final String ARG_TRANSFORM = "transform";
   
    static final String ARG_SOURCE_SHORT = "-s=";
   
    static final String ARG_SOURCE = "--source=";
   
    static final String ARG_TARGET_SHORT = "-t=";
   
    static final String ARG_TARGET = "--target=";

    static final String ARG_PRETTY_SHORT = "-p";
    static final String ARG_PRETTY = "--pretty";

    static final String ARG_VERBOSE_SHORT = "-v";
    static final String ARG_VERBOSE = "--verbose";
    
    static final String ARG_PARAM_JSON = "json";
    static final String ARG_PARAM_XML = "xml";
    static final String ARG_PARAM_YAML = "yaml";
    static final String ARG_PARAM_OPEN_API = "oas";

    static final String MEDIA_TYPE_ALPS_JSON = "application/alps+json";
    static final String MEDIA_TYPE_ALPS_XML = "application/alps+xml";
    static final String MEDIA_TYPE_ALPS_YAML = "application/alps+yaml";
    
    static final String MEDIA_TYPE_OPEN_API = "application/vnd.oai.openapi";
    
    private Constants() {
    }
}
