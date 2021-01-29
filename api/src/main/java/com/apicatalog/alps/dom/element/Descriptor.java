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
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface Descriptor {
    
	Optional<URI> id();
	
	Optional<URI> href();
	
	Optional<URI> definition();
	
	Optional<String> name();
	
	DescriptorType type();
	
	Optional<URI> returnType();
	
	Optional<String> title();
	
	List<String> tag();
	
	Set<Documentation> documentation();
	
	Set<Extension> extensions();
	
	Set<Descriptor> descriptors();
	
	Set<Link> links();
}
