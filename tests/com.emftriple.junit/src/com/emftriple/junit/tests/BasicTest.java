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
package com.emftriple.junit.tests;

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

import com.emftriple.jena.tdb.TDBResourceFactory;
import com.emftriple.query.SparqlString;
import com.emftriple.resource.ETripleResource;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.junit.model.Book;
import com.junit.model.ModelFactory;
import com.junit.model.ModelPackage;
import com.junit.model.Person;

public class BasicTest {

	Map<String, Object> options = new HashMap<String, Object>();
	
	@Before
	public void tearUp() {
		Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put("emftriple", new TDBResourceFactory());
		EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, ModelPackage.eINSTANCE);
		
		options.put(ETripleResource.OPTION_DATASOURCE_LOCATION, "data");
	}
	
	@Test
	public void testDelete() throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getLoadOptions().putAll(options);
		
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://tdb"));
		
		resource.delete(null);
		
		Dataset ds = TDBFactory.createDataset("data");
		assertTrue(ds.getDefaultModel().isEmpty());
		ds.close();
	}
	
	@Test
	public void testDeleteGraph() throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		options.put(ETripleResource.OPTION_DATASOURCE_LOCATION, "data");
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://tdb?graph=http://graph"));
		
		resource.delete(options);
		
		Dataset ds = TDBFactory.createDataset("data");
		assertTrue(ds.getDefaultModel().isEmpty());
		assertTrue(ds.getNamedModel("http://graph").isEmpty());
		ds.close();
	}
	
	@Test
	public void testCreateAndStore() throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://tdb"));
		resource.load(options);
		
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
		resource.save(options);
		
		Dataset ds = TDBFactory.createDataset("data");
		assertFalse(ds.getDefaultModel().isEmpty());
		
//		ds.getDefaultModel().write(System.out);
		
		assertTrue(ds.getDefaultModel().listSubjects().toList().size() == 3);
		ds.close();
	}
		
//	@Test
	public void testCreateAndStoreInGraph() throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://tdb?graph=http://graph"));
		resource.load(options);
		
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
		resource.save(options);
		
		Dataset ds = TDBFactory.createDataset("data");
		Model m = ds.getNamedModel("http://graph");
		assertFalse(m.isEmpty());
		
		assertTrue(m.listSubjects().toList().size() == 3);
		ds.close();
	}
	
	@Test
	public void testLoadResource() throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://data"));
		
		assertNotNull(resource);
		resource.load(options);
		
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
	
//	@Test
	public void testLoadResourceFromGraph() throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://data?graph=http://graph"));
		
		assertNotNull(resource);
		resource.load(options);
		
		assertFalse(resource.getContents().isEmpty());
		assertTrue(resource.getContents().size() == 3);
		
		Object obj = EcoreUtil.getObjectByType(resource.getContents(), ModelPackage.eINSTANCE.getPerson());
		
		assertTrue(obj instanceof Person);
		assertFalse( ((Person)obj).getBooks().isEmpty() );
		assertEquals( ((Person)obj).getBooks().size(), 2 );
		
//		System.out.println(((Person) obj).getBooks());
		
		Book b = ((Person)obj).getBooks().get(0);
		
		assertFalse(b.eIsProxy());
		assertNotNull(b.getTitle());
		assertEquals(b.getTitle(), "Valley Of Thing");
	}
	
//	@Test
	public void testBasicQuery() throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		
		Resource query = resourceSet.createResource(
				new SparqlString(
						"prefix m: <http://www.eclipselabs.org/emf/junit#> " +
						"select ?s where { ?s a m:Person }").toURI(
				URI.createURI("emftriple://data")));
		query.load(options);
		
		assertFalse(query.getContents().isEmpty());
		assertTrue(query.getContents().size() == 2);
		
		EObject obj = query.getContents().get(0);
		assertTrue(obj instanceof Person);
		
		assertEquals(((Person) obj).getName(), "John Doe");
	}
	
//	@Test
	public void testBasicQueryOnGraph() throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		
		Resource query = resourceSet.createResource(
				new SparqlString(
						"prefix m: <http://www.eclipselabs.org/emf/junit#> " +
						"select ?s where { ?s a m:Person }").toURI(
				URI.createURI("emftriple://data?graph=http://graph")));
		query.load(options);
		
		assertFalse(query.getContents().isEmpty());
		assertTrue(query.getContents().size() == 2);
		
		EObject obj = query.getContents().get(0);
		assertTrue(obj instanceof Person);
		
		assertEquals(((Person) obj).getName(), "John Doe");
	}
	
//	@Test
	public void testGetObjectById() throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://data"));
		
		assertNotNull(resource);
		resource.load(null);
		
		assertFalse(resource.getContents().isEmpty());
		assertTrue(resource.getContents().size() == 2);
		
		EObject o1 = resource.getContents().get(0);
		assertNotNull(o1);
		
		URI uri = ((ETripleResource)resource).getID(o1);
		
		EObject o2 = resource.getEObject("uri="+uri.toString());
		assertNotNull(o2);
		assertEquals(o1, o2);
	}
	
}
