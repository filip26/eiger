package com.apicatalog.alps.dom.element;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

public interface AlpsDescriptor {
    
	URI getId();
	
	Optional<URI> getHref();
	
	Optional<String> getName();
	
	AlpsDescriptorType getType();
	
	URI getReturnType();
	
	Set<AlpsDocumentation> getDocumentation();
	
	Set<AlpsExtension> getExtensions();
	
	Set<AlpsDescriptor> getDescriptors();
	
	Optional<AlpsDescriptor> getParent();
	
	Set<AlpsLink> getLinks();
}
