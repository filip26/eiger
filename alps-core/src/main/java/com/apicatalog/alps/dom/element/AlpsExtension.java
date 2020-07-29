package com.apicatalog.alps.dom.element;

import java.net.URI;
import java.util.Optional;

public interface AlpsExtension  {

	/**
	 * {@link URI} pointing to an external document which provides the definition of the extension
	 *  
	 * @return {@link URI} of an external document
	 */
	Optional<URI> getHref();
	
	URI getId();
	
	Optional<String> getValue();
}
