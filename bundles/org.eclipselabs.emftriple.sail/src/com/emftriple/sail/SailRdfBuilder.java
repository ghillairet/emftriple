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
package com.emftriple.sail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.openrdf.model.Graph;
import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

import com.emftriple.resource.ETripleResource;
import com.emftriple.transform.DatatypeConverter;
import com.emftriple.transform.Metamodel;
import com.emftriple.transform.RDFTransform;

public class SailRdfBuilder 
	extends RDFTransform<URI, Literal, Statement, Graph>{

	@Override
	public URI createURI(String uri, Graph graph) {
		ValueFactory factory = new ValueFactoryImpl();
		
		return factory.createURI(uri);
	}

	@Override
	public Statement createTripleURI(URI subject, URI predicate, URI object, Graph graph) {
		ValueFactory factory = new ValueFactoryImpl();
		
		Statement stmt = factory.createStatement(
				subject, predicate, object);
		
		return stmt;
	}

	@Override
	public Statement createTripleLiteral(URI subject, URI predicate, Literal object, Graph graph) {
		ValueFactory factory = new ValueFactoryImpl();
		
		Statement stmt = factory.createStatement(
				subject, predicate, object);
		
		return stmt;
	}

	@Override
	public URI createURI(EObject object, @SuppressWarnings("rawtypes") ETripleResource resource, Graph graph) {
		ValueFactory factory = new ValueFactoryImpl();
		
		org.eclipse.emf.common.util.URI uri = resource.getID(object);
		if (uri == null) {
			throw new IllegalArgumentException("Cannot create ID for object "+object);
		}
		return factory.createURI(uri.toString());
	}

	@Override
	public URI createURI(EStructuralFeature feature, @SuppressWarnings("rawtypes") ETripleResource resource, Graph graph) {
		ValueFactory factory = new ValueFactoryImpl();
		
		String uri = Metamodel.INSTANCE.getRdfType(feature);
		if (uri == null) {
			throw new IllegalArgumentException("Cannot create URI for feature "+feature);
		}
		return factory.createURI(uri);
	}

	@Override
	public Literal createLiteral(EObject object, EAttribute attribute, Object value, Graph graph) {
		ValueFactory factory = new ValueFactoryImpl();
		
		final String literalValue = DatatypeConverter.toString(attribute.getEType().getName(), value);
		final String dataTypeURI = DatatypeConverter.get((EDataType) attribute.getEType());
		
		return factory.createLiteral(literalValue, dataTypeURI);
	}

	@Override
	public Collection<Statement> createTriples(EObject object, String key, Graph graph) {
//		long startTime = System.currentTimeMillis();
		
		final List<Statement> triples = new ArrayList<Statement>();
		final URI subject = createURI(key, graph);
		
		triples.addAll(createRdfTypes(object, subject, graph));
		
		for (EAttribute attr: object.eClass().getEAllAttributes()) {
			triples.addAll(createTriples(object, attr, subject, graph));
		}
		
		for (EReference ref: object.eClass().getEAllReferences()) {
			triples.addAll(createTriples(object, ref, subject, graph));
		}
		
//		long endTime = System.currentTimeMillis();
//		System.out.println("Time to create " + triples.size() + " triples: " + ((endTime - startTime) / 1000.0) + " sec");
	
		return triples;
	}

}
