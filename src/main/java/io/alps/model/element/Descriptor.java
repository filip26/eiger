package io.alps.model.element;

import java.util.List;

import io.alps.model.ref.Reference;

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
