package io.alps.model.element;

import java.net.URI;

public interface Documentation {
	
	URI getHref();
	String getFormat();
	
	String getValue();
}
