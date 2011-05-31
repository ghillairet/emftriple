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

import static com.emftriple.query.Sparql.literal;
import static com.emftriple.query.Sparql.triple;
import static com.emftriple.query.Sparql.var;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailException;

import com.emftriple.query.Sparql;
import com.emftriple.query.SparqlNative;
import com.emftriple.query.result.ListResult;
import com.emftriple.sail.SailResourceFactory;
import com.emftriple.util.ETripleOptions;
import com.emftriple.vocabularies.XSD;
import com.junit.model.ModelFactory;
import com.junit.model.ModelPackage;
import com.junit.model.PrimaryObject;
import com.tinkerpop.blueprints.pgm.TransactionalGraph.Mode;
import com.tinkerpop.blueprints.pgm.impls.neo4j.Neo4jGraph;
import com.tinkerpop.blueprints.pgm.oupls.sail.GraphSail;

public class QueryResultTest {
	static ResourceSet resourceSet;
	static Neo4jGraph neo;
	static Sail sail;
	
	@BeforeClass
	public static void tearUp() throws IOException {
		neo = new Neo4jGraph("basic-test");
		neo.setTransactionMode(Mode.MANUAL);
		sail = new GraphSail(neo);
	
		Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put("emftriple", new SailResourceFactory());
		resourceSet = new ResourceSetImpl();
		resourceSet.getLoadOptions().put(ETripleOptions.OPTION_DATASOURCE_OBJECT, sail);
		
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://tdb"));
		resource.delete(null);
		resource.load(null);
		
		assertTrue(resource.getContents().isEmpty());
		
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
	public void testQuery() throws IOException {
		Sparql sparql = new Sparql()
			.select("s")
			.where(triple(var("s"), var("p"), var("o")));
		
		URI uri = sparql.toURI(URI.createURI("emftriple://tdb"));
		
		assertTrue(uri.hasQuery());
		
		Resource resource = resourceSet.createResource(uri);
		resource.load(null);
		
		assertFalse(resource.getContents().isEmpty());
		assertTrue(resource.getContents().get(0) instanceof ListResult);
		
		ListResult result = (ListResult) resource.getContents().get(0);
		
		assertTrue(result.getResult().size() == 2);
		
		for (EObject obj: resource.getContents()) {
			System.out.println(obj);
		}
		
		for (EObject obj: result.getResult()) {
			System.out.println(obj);
		}
	}
	
	@Test
	public void testQueryString() throws IOException {
		SparqlNative sparql = new SparqlNative(
				"prefix xsd: <"+XSD.NS+"> "+
				"select ?s where { " +
				"?s <http://www.eclipselabs.org/emf/junit#name> ?p }");
		
		URI uri = sparql.toURI(URI.createURI("emftriple://tdb"));
		Resource resource = resourceSet.createResource(uri);
		resource.load(null);
		
		assertFalse(resource.getContents().isEmpty());
		
		EObject o = resource.getContents().get(0);
		
		System.out.println(o);
	}
	
	@Test
	public void testQueryLiteral() throws IOException {
		EAttribute name = ModelPackage.eINSTANCE.getPrimaryObject_Name();
		
		Sparql sparql = new Sparql()
			.prefix("xsd", XSD.NS)
			.where(triple(var("s"), name, literal("Foo", "xsd:string")));
		
		URI uri = sparql.toURI(URI.createURI("emftriple://tdb"));
		
		assertTrue(uri.hasQuery());
		
		Resource resource = resourceSet.createResource(uri);
		resource.load(null);
		
		assertFalse(resource.getContents().isEmpty());
		assertTrue(resource.getContents().get(0) instanceof ListResult);
		
		ListResult result = (ListResult) resource.getContents().get(0);
		
		assertTrue(result.getResult().size() == 1);
		
		for (EObject obj: resource.getContents()) {
			System.out.println(obj);
		}
		
		for (EObject obj: result.getResult()) {
			System.out.println(obj);
		}
	}
}
