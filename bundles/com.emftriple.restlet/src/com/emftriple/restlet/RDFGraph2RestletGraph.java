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
package com.emftriple.restlet;

import java.util.Collection;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.restlet.data.Reference;
import org.restlet.ext.rdf.Graph;
import org.restlet.ext.rdf.Literal;

import com.emftriple.resource.ETripleResource;
import com.emftriple.transform.Metamodel;
import com.emftriple.transform.RDFTransform;

/**
 * 
 * @author ghillairet
 * @since 0.1.0
 */
public class RDFGraph2RestletGraph extends RDFTransform<Reference, Literal, Object, Graph>{

	private RDFGraph2RestletGraph() {}

	@Override
	public Collection<Object> createTriples(EObject object, String key, Graph graph) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reference createURI(String uri, Graph graph) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object createTripleURI(Reference subject, Reference predicate, Reference object, Graph graph) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object createTripleLiteral(Reference subject, Reference predicate,
			Literal object, Graph graph) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reference createURI(EObject object, @SuppressWarnings("rawtypes") ETripleResource resource, Graph graph) {
//		graph.
		return null;
	}

	@Override
	public Reference createURI(EStructuralFeature feature, @SuppressWarnings("rawtypes") ETripleResource resource, Graph graph) {
		Reference ref = new Reference(Metamodel.INSTANCE.getRdfType(feature));
		
		return ref;
	}

	@Override
	public Literal createLiteral(EObject object, EAttribute attribute, Object value, Graph graph) {
		org.restlet.ext.rdf.Literal lit = new org.restlet.ext.rdf.Literal(value.toString());
//		if (object.getDatatype() != null)
			lit.setDatatypeRef(new Reference());
//		if (object.getLang() != null)
//			lit.setLanguage(Language.valueOf(object.getLang()));
		return lit;
	}
	
//	public static Graph toGraph(Collection<Triple> triples) {
//		final Graph rGraph = new Graph();
//		
//		for (Triple t: triples) {
//			Reference sbj = new Reference(((URIElement) t.getSubject()).getURI());
//			Reference prop = new Reference(t.getPredicate().getURI());
//			if (t.getObject() instanceof Literal)
//				rGraph.add(sbj, prop, literal((Literal) t.getObject()));
//			else rGraph.add(sbj, prop, new Reference(((URIElement) t.getObject()).getURI()));
//		}
//		
//		return rGraph;
//	}
//
//	private static org.restlet.ext.rdf.Literal literal(Literal object) {
//		org.restlet.ext.rdf.Literal lit = new org.restlet.ext.rdf.Literal(object.getLexicalForm());
//		if (object.getDatatype() != null)
//			lit.setDatatypeRef(new Reference(object.getDatatype().getURI()));
//		if (object.getLang() != null)
//			lit.setLanguage(Language.valueOf(object.getLang()));
//		
//		return lit;
//	}
}
