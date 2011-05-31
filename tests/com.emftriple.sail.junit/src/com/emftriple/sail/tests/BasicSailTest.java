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
package com.emftriple.sail.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailException;

import com.emftriple.sail.SailResourceFactory;
import com.emftriple.util.ETripleOptions;
import com.junit.model.Book;
import com.junit.model.ModelFactory;
import com.junit.model.ModelPackage;
import com.junit.model.Person;
import com.tinkerpop.blueprints.pgm.TransactionalGraph.Mode;
import com.tinkerpop.blueprints.pgm.impls.neo4j.Neo4jGraph;
import com.tinkerpop.blueprints.pgm.oupls.sail.GraphSail;

public class BasicSailTest {
	
	static ResourceSet resourceSet;
	static Neo4jGraph neo;
	static Sail sail;
	
	@BeforeClass
	public static void tearUp() {
		neo = new Neo4jGraph("basic-test");
		neo.setTransactionMode(Mode.MANUAL);
		sail = new GraphSail(neo);
	
		Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put("emftriple", new SailResourceFactory());
		resourceSet = new ResourceSetImpl();
		resourceSet.getLoadOptions().put(ETripleOptions.OPTION_DATASOURCE_OBJECT, sail);
	}
	
	@AfterClass
	public static void tearDown() {
		neo.shutdown();
		
		try {
			sail.shutDown();
			neo.shutdown();
		} catch (SailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testClearDataSource() throws IOException {
		
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://sail"));		
		resource.delete(null);
		
		Resource deleted = resourceSet.createResource(URI.createURI("emftriple://sail"));		
		deleted.load(null);
		
		assertTrue(deleted.getContents().isEmpty());
	}
	
	@Test
	public void testSaveSail() throws IOException {
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://sail"));
		
		Person p1 = ModelFactory.eINSTANCE.createPerson();
		p1.setName("John Doe");
		
		Book b1 = ModelFactory.eINSTANCE.createBook();
		b1.setTitle("Some Book");
		
		Book b2 = ModelFactory.eINSTANCE.createBook();
		b2.setTitle("Another Book");
		
		p1.getBooks().add(b1);
		p1.getBooks().add(b2);
		
		resource.getContents().add(p1);
		resource.getContents().add(b1);
		resource.getContents().add(b2);
		resource.save(null);
	}

	@Test
	public void testLoadSail() throws IOException {
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://sail"));
		resource.load(null);
	
		assertFalse(resource.getContents().isEmpty());
		System.out.println(resource.getContents().size());
		System.out.println(resource.getContents());
		assertTrue(resource.getContents().size() == 3);
		
		Person p = (Person) EcoreUtil.getObjectByType(resource.getContents(), ModelPackage.eINSTANCE.getPerson());
		System.out.println(p);
		
		System.out.println(p.getBooks());
	}

	@Test
	public void testClearDataSourceGraph() throws IOException {
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://sail"));		
		resource.delete(null);
		
		Resource deleted = resourceSet.createResource(URI.createURI("emftriple://sail"));		
		deleted.load(null);
		
		assertTrue(deleted.getContents().isEmpty());
	}
	
	@Test
	public void testSaveSailOnGraph() throws IOException {
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://sail?graph=http://mygraph"));
		
		Person p1 = ModelFactory.eINSTANCE.createPerson();
		p1.setName("John Doe");
		
		Book b1 = ModelFactory.eINSTANCE.createBook();
		b1.setTitle("Some Book");
		
		Book b2 = ModelFactory.eINSTANCE.createBook();
		b2.setTitle("Another Book");
		
		p1.getBooks().add(b1);
		p1.getBooks().add(b2);
		
		resource.getContents().add(p1);
		resource.getContents().add(b1);
		resource.getContents().add(b2);
		resource.save(null);
	}
	
	@Test
	public void testLoadSailOnGraph() throws IOException {
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://sail?graph=http://mygraph"));
		resource.load(null);

		assertFalse(resource.getContents().isEmpty());
		assertTrue(resource.getContents().size() == 3);
		
		Person p = (Person) EcoreUtil.getObjectByType(resource.getContents(), ModelPackage.eINSTANCE.getPerson());
		System.out.println(p);
		
		System.out.println(p.getBooks());
	}
}
