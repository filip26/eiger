package com.apicatalog.alps.dom.element;

import java.util.List;

import com.apicatalog.alps.dom.ref.Reference;

public interface AlpsDescriptor {
    
	String getId();
	
	Reference getHref();
	
	String getName();
	
	AlpsDescriptorType getType();
	
	Reference getReturnType();
	
	AlpsDocumentation getDocumentation();
	
	List<AlpsExtension> getExtensions();
	
	List<AlpsDescriptor> getDescriptors();
	
	List<AlpsLink> getLinks();
}
