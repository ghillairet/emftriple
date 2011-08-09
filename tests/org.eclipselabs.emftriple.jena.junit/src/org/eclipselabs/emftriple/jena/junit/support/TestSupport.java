/**
 * 
 */
package org.eclipselabs.emftriple.jena.junit.support;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * @author ghillairet
 *
 */
public interface TestSupport {

	void init();
	
	Resource createResource(String query);
	
	void saveObject(EObject object);
	
	void saveObject(EObject object, URI uri);
	
	boolean existsInDataStore(String resourceURI);
	
	URI createObjectURI(String objectURI, String graphURI);
	
	boolean dataStoreIsEmpty();
	
	void populate() throws IOException;
	
	void populateWithBNode() throws IOException;
	
	ResourceSet getResourceSet();
	
	URI createBaseURI();
}
