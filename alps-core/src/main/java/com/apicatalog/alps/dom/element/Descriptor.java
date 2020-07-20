package com.apicatalog.alps.dom.element;

import java.util.List;

import com.apicatalog.alps.dom.ref.Reference;

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
