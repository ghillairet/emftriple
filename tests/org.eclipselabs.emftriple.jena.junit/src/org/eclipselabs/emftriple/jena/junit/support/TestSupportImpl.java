/**
 * 
 */
package org.eclipselabs.emftriple.jena.junit.support;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipselabs.emftriple.jena.junit.model.BookBNode;
import org.eclipselabs.emftriple.jena.junit.model.ModelFactory;
import org.eclipselabs.emftriple.jena.junit.model.PersonBNode;
import org.eclipselabs.emftriple.jena.junit.model.PrimaryObject;

/**
 * @author ghillairet
 *
 */
public abstract class TestSupportImpl implements TestSupport {

	protected ResourceSet resourceSet;
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupport#init()
	 */
	@Override
	public abstract void init();

	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupport#createResource(java.lang.String)
	 */
	@Override
	public abstract Resource createResource(String query);

	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupport#saveObject(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public void saveObject(EObject object) {
		Resource resource = createResource(null);
		resource.getContents().add(object);
		try {
			resource.save(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupport#saveObject(org.eclipse.emf.ecore.EObject, org.eclipse.emf.common.util.URI)
	 */
	@Override
	public void saveObject(EObject object, URI uri) {
		Resource resource = resourceSet.createResource(uri);
		resource.getContents().add(object);
		try {
			resource.save(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupport#existsInDataStore(java.lang.String)
	 */
	@Override
	public abstract boolean existsInDataStore(String resourceURI);
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupport#createObjectURI(java.lang.String, java.lang.String)
	 */
	@Override
	public abstract URI createObjectURI(String objectURI, String graphURI);

	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupport#getResourceSet()
	 */
	@Override
	public ResourceSet getResourceSet() {
		return resourceSet;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupport#populate()
	 */
	@Override
	public void populate() throws IOException {		
		Resource resource = createResource(null);
		resource.delete(null);
		
		resource = createResource(null);
		
		PrimaryObject p1 = ModelFactory.eINSTANCE.createPrimaryObject();
		p1.setId(1);
		p1.setName("Foo");
		
		PrimaryObject p2 = ModelFactory.eINSTANCE.createPrimaryObject();
		p2.setId(2);
		p2.setName("Bar");
		
		resource.getContents().add(p1);
		resource.getContents().add(p2);
		resource.save(null);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupport#populateWithBNode()
	 */
	@Override
	public void populateWithBNode() throws IOException {
		Resource resource = createResource(null);
		resource.delete(null);
		
		resource = createResource(null);
		
		PersonBNode p = ModelFactory.eINSTANCE.createPersonBNode();
		p.setName("John Doe");
		
		BookBNode b1 = ModelFactory.eINSTANCE.createBookBNode();
		b1.setTitle("Book of Stuff");
		
		BookBNode b2 = ModelFactory.eINSTANCE.createBookBNode();
		b1.setTitle("Valley Of Thing");
		
		p.getBooks().add(b1);
		p.getBooks().add(b2);
		
		resource.getContents().add(p);
		resource.save(null);
	}
}
