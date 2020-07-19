package com.apicatalog.alps.model;

import java.net.URI;
import java.util.List;

import com.apicatalog.alps.model.element.Descriptor;
import com.apicatalog.alps.model.element.Documentation;
import com.apicatalog.alps.model.element.Link;

public interface Document {

	Descriptor findById(final String id);
	
	String getVersion();
	
	List<Descriptor> getDescriptors();
		
	List<Link> getLinks();
	
	Documentation getDocumentation();
	
	URI getHref();
	
}
