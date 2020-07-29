package com.apicatalog.alps.dom;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import com.apicatalog.alps.dom.element.AlpsDescriptor;
import com.apicatalog.alps.dom.element.AlpsDocumentation;
import com.apicatalog.alps.dom.element.AlpsExtension;
import com.apicatalog.alps.dom.element.AlpsLink;

/**
 * An ALPS document contains a machine-readable collection of
 * identifying strings and their human-readable explanations.
 * 
 * @see <a href="https://tools.ietf.org/html/draft-amundsen-richardson-foster-alps-02#section-2">ALPS Document</a>
 */
public interface AlpsDocument {

	Optional<AlpsDescriptor> findById(final URI id);
	
	Set<AlpsDescriptor> findByName(final String name);
	
	/**
	 * Indicates the version of the ALPS specification used in the document.
     * Currently the only valid value is '1.0'.  If no value appears, then
     * 'version="1.0"' is implied.
     *
     * @see <a href="https://tools.ietf.org/html/draft-amundsen-richardson-foster-alps-02#section-2.2.14">ALPS Version</a>
     * 
     * @return ALPS document version, nevern <code>null</code>
	 */
	AlpsVersion getVersion();

	/**
	 * Returns top level document descriptors.
	 * 
	 * @return top level document descriptors
	 */
	Set<AlpsDescriptor> getDescriptors();
	
	/**
	 * Returns flattened document descriptors.
	 * 
	 * @return all descriptors present in the document
	 */
	Collection<AlpsDescriptor> getAllDescriptors();
	
	Set<AlpsLink> getLinks();
	
	Set<AlpsDocumentation> getDocumentation();
	
	Set<AlpsExtension> getExtensions();
	
	/**
	 * Base {@link URI} of the ALPS document 
	 * 
	 * @return base {@link URI}
	 */
	URI getBaseUri();	
}