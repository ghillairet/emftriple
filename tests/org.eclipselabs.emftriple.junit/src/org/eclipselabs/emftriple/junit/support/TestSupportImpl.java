/*******************************************************************************
 * Copyright (c) 2011 Guillaume Hillairet.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Guillaume Hillairet - initial API and implementation
 *******************************************************************************/
package org.eclipselabs.emftriple.junit.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipselabs.emftriple.internal.util.ETripleEcoreUtil;
import org.eclipselabs.emftriple.junit.model.BookBNode;
import org.eclipselabs.emftriple.junit.model.ModelFactory;
import org.eclipselabs.emftriple.junit.model.PersonBNode;
import org.eclipselabs.emftriple.junit.model.PrimaryObject;

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
	
	protected abstract EObject getObject(URI key);
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupport#checkObject(org.eclipselabs.emftriple.jena.junit.model.TargetObject)
	 */
	@Override
	public void checkObject(EObject expected) {
		if (expected == null) {
			return;
		}
		
		URI key = ETripleEcoreUtil.getID(expected);
		
		assertNotNull(key);
		
		EObject actual = getObject(key);
		
		assertNotNull(actual);
		assertEquals(actual.eClass(), expected.eClass());
		
		checkObject(expected, actual);
	}
	
	protected void checkObject(EObject expected, EObject actual) {
		if (expected == null) {
			assertNull(actual);
			return;
		}
		
		for (EAttribute attr: expected.eClass().getEAllAttributes()) {
			assertEquals(expected.eGet(attr), actual.eGet(attr));
		}
		
		for (EReference ref: expected.eClass().getEAllReferences()) {
			if (ref.isMany()) {
				@SuppressWarnings("unchecked")
				EList<EObject> expectedObjects = (EList<EObject>) expected.eGet(ref);
				@SuppressWarnings("unchecked")
				EList<EObject> actualObjects = (EList<EObject>) actual.eGet(ref);
				
				assertEquals(expectedObjects.size(), actualObjects.size());
				
				for (int i=0;i<expectedObjects.size();i++) {
					checkObject(expectedObjects.get(i), actualObjects.get(i));
				}
			} else {
				EObject exp = (EObject) expected.eGet(ref);
				EObject act = (EObject) actual.eGet(ref);
				
				checkObject(exp, act);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupport#dataStoreSize()
	 */
	@Override
	public abstract long dataStoreSize();
}
