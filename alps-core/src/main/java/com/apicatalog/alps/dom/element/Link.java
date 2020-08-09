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

public interface Link {

	/**
	 * {@link URI} pointing to an external document whose relationship to the current document 
	 * or <code>descriptor</code> is described by the associated {@link #getRel()} property
	 *  
	 * @return {@link URI} of an external document
	 */

	URI getHref();
	
	String getRel();
}