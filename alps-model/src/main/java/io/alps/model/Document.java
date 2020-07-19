package io.alps.model;

import java.net.URI;
import java.util.List;

import io.alps.model.element.Descriptor;
import io.alps.model.element.Documentation;
import io.alps.model.element.Link;

public interface Document {

	Descriptor findById(final String id);
	
	String getVersion();
	
	List<Descriptor> getDescriptors();
		
	List<Link> getLinks();
	
	Documentation getDocumentation();
	
	URI getHref();
	
}
