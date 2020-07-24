package com.apicatalog.alps.dom.element;

import java.net.URI;

public interface AlpsLink {

	/**
	 * {@link URI} pointing to an external document whose relationship to the current document 
	 * or <code>descriptor</code> is described by the associated {@link #getRel()} property
	 *  
	 * @return {@link URI} of an external document
	 */

	URI getHref();
	
	String getRel();
}
