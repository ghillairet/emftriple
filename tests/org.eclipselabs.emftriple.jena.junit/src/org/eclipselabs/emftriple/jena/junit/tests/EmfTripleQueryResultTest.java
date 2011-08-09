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
package org.eclipselabs.emftriple.jena.junit.tests;

import static org.eclipselabs.emftriple.query.Sparql.literal;
import static org.eclipselabs.emftriple.query.Sparql.triple;
import static org.eclipselabs.emftriple.query.Sparql.var;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipselabs.emftriple.jena.junit.model.ModelPackage;
import org.eclipselabs.emftriple.jena.junit.support.TestSupport;
import org.eclipselabs.emftriple.query.Sparql;
import org.eclipselabs.emftriple.query.SparqlNative;
import org.eclipselabs.emftriple.query.result.ListResult;
import org.eclipselabs.emftriple.vocabularies.XSD;
import org.junit.Test;

public abstract class EmfTripleQueryResultTest {
	
	TestSupport support;
	
	public EmfTripleQueryResultTest(TestSupport support) {
		this.support = support;
		this.support.init();
		try {
			this.support.populate();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testQuery() throws IOException {
		Sparql sparql = new Sparql()
			.select("s")
			.where(triple(var("s"), var("p"), var("o")));
		
		URI uri = sparql.toURI(support.createBaseURI());
		
		assertTrue(uri.hasQuery());
		
		Resource resource = support.getResourceSet().createResource(uri);
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
				"?s <http://www.eclipselabs.org/emf/junit#name> \"Foo\"^^xsd:string }");
		
		URI uri = sparql.toURI(support.createBaseURI());
		Resource resource = support.getResourceSet().createResource(uri);
		resource.load(null);
		
		assertFalse(resource.getContents().isEmpty());
	}
	
	@Test
	public void testQueryLiteral() throws IOException {
		EAttribute name = ModelPackage.eINSTANCE.getPrimaryObject_Name();
		
		Sparql sparql = new Sparql()
			.prefix("xsd", XSD.NS)
			.where(triple(var("s"), name, literal("Foo", "xsd:string")));
		
		URI uri = sparql.toURI(support.createBaseURI());
		
		assertTrue(uri.hasQuery());
		
		Resource resource = support.getResourceSet().createResource(uri);
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
