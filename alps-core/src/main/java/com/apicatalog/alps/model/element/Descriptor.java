package com.apicatalog.alps.model.element;

import java.util.List;

import com.apicatalog.alps.model.ref.Reference;

public interface Descriptor {
    
	String getId();
	
	Reference getHref();
	
	String getName();
	
	DescriptorType getType();
	
	Reference getReturnType();
	
	Documentation getDocumentation();
	
	List<Extension> getExtensions();
	
	List<Descriptor> getDescriptors();
	
	List<Link> getLinks();
}
