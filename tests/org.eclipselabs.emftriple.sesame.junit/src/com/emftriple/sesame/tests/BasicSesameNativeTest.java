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
package com.emftriple.sesame.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Before;
import org.junit.Test;

import com.emftriple.sesame.nat.SesameNativeResourceFactory;
import com.emftriple.util.ETripleOptions;
import com.junit.model.Book;
import com.junit.model.ModelFactory;
import com.junit.model.ModelPackage;
import com.junit.model.Person;

public class BasicSesameNativeTest {
	ResourceSet resourceSet;
	
	@Before
	public void tearUp() {
		Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put("emftriple", new SesameNativeResourceFactory());
		EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, ModelPackage.eINSTANCE);
		
		resourceSet = new ResourceSetImpl();
		Map<String, Object> options = new HashMap<String, Object>();
		options.put(ETripleOptions.OPTION_DATASOURCE_LOCATION, "/tmp/sesame/native/test");
		resourceSet.getLoadOptions().putAll(options);
	}
	
	@Test
	public void testDelete() throws IOException {
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://sesame"));
		
		resource.delete(null);
		
		resource.load(null);
		
		assertTrue(resource.getContents().isEmpty());
	}
	
	@Test
	public void testCreateAndStore() throws IOException {
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://sesame"));
		
		assertTrue(resource.getContents().isEmpty());
		
		Person person = ModelFactory.eINSTANCE.createPerson();
		person.setName("John Doe");
		
		Book b1 = ModelFactory.eINSTANCE.createBook();
		b1.setTitle("Valley Of Thing");
		
		Book b2 = ModelFactory.eINSTANCE.createBook();
		b2.setTitle("Book of Stuff");
		
		person.getBooks().add(b1);
		person.getBooks().add(b2);
		
		resource.getContents().add(person);
		resource.getContents().add(b1);
		resource.getContents().add(b2);
		resource.save(null);
	}
	
	@Test
	public void testLoadResource() throws IOException {
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://sesame"));
		
		assertNotNull(resource);
		
		resource.load(null);
		
		assertFalse(resource.getContents().isEmpty());
		System.out.println(resource.getContents().size());
		for (EObject o: resource.getContents())
			System.out.println(o);
		assertTrue(resource.getContents().size() == 3);
		
		Object obj = EcoreUtil.getObjectByType(resource.getContents(), ModelPackage.eINSTANCE.getPerson());
		
		assertTrue(obj instanceof Person);
		assertFalse( ((Person)obj).getBooks().isEmpty() );
		assertEquals( ((Person)obj).getBooks().size(), 2 );
		
//		System.out.println(((Person) obj).getBooks());
		
		for (Book b: ((Person) obj).getBooks()) {
			System.out.println(b.getTitle());
		}
		
		for (EObject o: resource.getContents())
			System.out.println(o);
//		Book b = ((Person)obj).getBooks().get(0);
//		
//		assertFalse(b.eIsProxy());
//		assertNotNull(b.getTitle());
//		assertEquals(b.getTitle(), "Valley Of Thing");
	}
	
	
}
