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
package com.apicatalog.alps.dom.element;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Extension  {

    URI id();
    
    List<String> tag();
    
	/**
	 * {@link URI} pointing to an external document which provides the definition of the extension
	 *  
	 * @return {@link URI} of an external document
	 */
	default Optional<URI> href() {
	    return Optional.empty();
	}
	
	default Optional<String> value() {
	    return Optional.empty();
	}
	
	/**
	 * A map of custom attributes that are not specified by the ALPS specification.
     *
	 * @return {@link Map} of custom attributes
	 */
	default Map<String, String> attributes() {
	    return Collections.emptyMap();
	}
}
