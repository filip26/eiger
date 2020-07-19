package com.apicatalog.alps.model.element;

import java.net.URI;

public interface Extension  {

	/**
	 * {@link URI} pointing to an external document which provides the definition of the extension
	 *  
	 * @return {@link URI} of an external document
	 */
	URI getHref();
	
	String getId();
	
	String getValue();
}
