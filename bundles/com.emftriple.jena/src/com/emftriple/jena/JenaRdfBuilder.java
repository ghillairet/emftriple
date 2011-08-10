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
package com.emftriple.jena;

import static com.emftriple.util.ETripleEcoreUtil.isBlankNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.emftriple.resource.ETripleResource;
import com.emftriple.transform.DatatypeConverter;
import com.emftriple.transform.Metamodel;
import com.emftriple.transform.RDFTransform;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

public class JenaRdfBuilder 
	extends RDFTransform<RDFNode, Resource, Literal, Statement, Model> {
	
	public JenaRdfBuilder() {
		
	}
	
	@Override
	public Statement createTripleURI(RDFNode subject, Resource predicate, Resource object, Model graph) {
		final Property p = graph.createProperty(predicate.getURI());
		return graph.createStatement(subject.asResource(), p, object);
	}

	@Override
	protected Statement createTripleBlankNode(RDFNode subject, Resource predicate, RDFNode object, Model graph) {
		final Property p = graph.createProperty(predicate.getURI());
		return graph.createStatement(subject.asResource(), p, object); 
	}
	
	@Override
	protected RDFNode createBlankNode(EObject object, @SuppressWarnings("rawtypes") ETripleResource resource, Model graph) {
		
		return graph.createResource(AnonId.create());
	}
	
	@Override
	public Statement createTripleLiteral(RDFNode subject, Resource predicate, Literal object, Model graph) {
		return graph.createStatement(subject.asResource(), (Property) predicate, object);
	}

	@Override
	public Literal createLiteral(EObject object, EAttribute attribute, Object value, Model graph) {
		final String literalValue = DatatypeConverter.toString(attribute.getEType().getName(), value);
		final String dataTypeURI = DatatypeConverter.get((EDataType) attribute.getEType());
		
		return  graph.createTypedLiteral(literalValue, dataTypeURI);
	}

	@Override
	public Resource createURI(EObject object, @SuppressWarnings("rawtypes") ETripleResource resource, Model graph) {
		URI uri = resource.getID(object);
		if (uri == null) {
			throw new IllegalArgumentException("Cannot create ID for object "+object);
		}
		return graph.createResource(uri.toString());
	}

	@Override
	public Resource createURI(EStructuralFeature feature, @SuppressWarnings("rawtypes") ETripleResource resource, Model graph) {
		final String uri = Metamodel.INSTANCE.getRdfType(feature);
		if (uri == null) {
			throw new IllegalArgumentException("Cannot create URI for feature "+feature);
		}
		return graph.createProperty(uri);
	}

	@Override
	public Resource createURI(String uri, Model graph) {
		return graph.createResource(uri);
	}

	@Override
	public Collection<Statement> createTriples(EObject object, String key, Model graph) {
		final List<Statement> triples = new ArrayList<Statement>();
		@SuppressWarnings("rawtypes")
		final RDFNode subject = isBlankNode(object) ? 
				createBlankNode(object, (ETripleResource) object.eResource(), graph) : createURI(key, graph);
		
		triples.addAll(createRdfTypes(object, subject, graph));
		
		for (EAttribute attr: object.eClass().getEAllAttributes()) {
			triples.addAll(createTriples(object, attr, subject, graph));
		}
		
		for (EReference ref: object.eClass().getEAllReferences()) {
			triples.addAll(createTriples(object, ref, subject, graph));
		}
		
		return triples;
	}
	
}
