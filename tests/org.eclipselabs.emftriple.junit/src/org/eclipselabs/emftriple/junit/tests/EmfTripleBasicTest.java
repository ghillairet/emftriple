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
package org.eclipselabs.emftriple.junit.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipselabs.emftriple.ETripleURIHandlerImpl;
import org.eclipselabs.emftriple.datasources.IDataSource;
import org.eclipselabs.emftriple.internal.util.ETripleEcoreUtil;
import org.eclipselabs.emftriple.junit.model.ModelFactory;
import org.eclipselabs.emftriple.junit.model.PrimaryObject;
import org.eclipselabs.emftriple.junit.support.TestSupport;
import org.junit.Test;

/**
 * @author ghillairet
 *
 */
public abstract class EmfTripleBasicTest {
	
	TestSupport support;
	
	public EmfTripleBasicTest(TestSupport support) {
		this.support = support;
		this.support.init();
	}
	
	@Test
	public void testGetStoreName() {
		URI uri1 = URI.createURI("my_scheme://my_store_name");
		URI uri2 = URI.createURI("my_scheme://my_store_name/my_sub_store");
		URI uri3 = URI.createURI("my_scheme://C://my_path_name/to_my_file/file_name.rdf");
		
		ETripleURIHandlerImpl handler = new ETripleURIHandlerImpl() {
			@Override
			protected IDataSource<?, ?> getDataSource(URI uri) {
				return null;
			}
		};
		
		assertEquals("my_store_name", handler.getStoreName(uri1));
		assertEquals("my_store_name", handler.getStoreName(uri2));
		assertEquals("C:", handler.getStoreName(uri3));
	}
	
	@Test
	public void testSaveSingleObject() {
		PrimaryObject object = ModelFactory.eINSTANCE.createPrimaryObject();
		object.setId(1);
		
		support.saveObject(object);
		
		String expectedID = "http://junit.org/primary/1";
		
		assertEquals(URI.createURI(expectedID), ETripleEcoreUtil.getID(object));
		assertTrue(support.existsInDataStore(expectedID));
	}
	
	@Test
	public void testExistObject() {
		Resource resource = support.createResource(null);
		
		URI existingURI = support.createObjectURI("http://junit.org/primary/1", null);
		URI nonExistingURI = support.createObjectURI("http://junit.org/primary/2", null);
		
		assertTrue(resource.getResourceSet().getURIConverter().exists(existingURI, null));
		assertFalse(resource.getResourceSet().getURIConverter().exists(nonExistingURI, null));
	}
	
	@Test
	public void testLoadSingleObject() throws IOException {
		Resource resource = support.createResource(null);
		
		resource.load(null);
		
		assertFalse(resource.getContents().isEmpty());
		
		assertEquals(1, resource.getContents().size());
		
		EObject content = resource.getContents().get(0);
		
		assertTrue(content instanceof PrimaryObject);
		
		assertEquals(1, ((PrimaryObject)content).getId());
	}
	
	@Test
	public void testDeleteDataStoreContent() throws IOException {
		Resource resource = support.createResource(null);
		
		resource.load(null);
		
		assertFalse(resource.getContents().isEmpty());
		assertFalse(support.dataStoreIsEmpty());
		
		resource.delete(null);
		
		assertTrue(resource.getContents().isEmpty());
		assertTrue(support.dataStoreIsEmpty());
	}
}
