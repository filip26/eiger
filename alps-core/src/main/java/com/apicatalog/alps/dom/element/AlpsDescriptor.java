package com.apicatalog.alps.dom.element;

import java.net.URI;
import java.util.Set;

public interface AlpsDescriptor {
    
	String getId();
	
	URI getHref();
	
	String getName();
	
	AlpsDescriptorType getType();
	
	URI getReturnType();
	
	Set<AlpsDocumentation> getDocumentation();
	
	Set<AlpsExtension> getExtensions();
	
	Set<AlpsDescriptor> getDescriptors();
	
	Set<AlpsLink> getLinks();
}
