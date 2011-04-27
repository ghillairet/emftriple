/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple.sesame.util;

import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.QueryEvaluationException;

import com.emf4sw.rdf.Node;
import com.emf4sw.rdf.Property;
import com.emf4sw.rdf.RDFFactory;
import com.emf4sw.rdf.RDFGraph;
import com.emf4sw.rdf.Triple;
import com.emf4sw.rdf.resource.RDFResource;
import com.emf4sw.rdf.resource.RDFResourceImpl;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.5.5
 */
public class SesameGraphResult2RDFGraph {

	private final GraphQueryResult graphResult;
	
	public SesameGraphResult2RDFGraph(GraphQueryResult graphResult) {
		this.graphResult = graphResult;
	}

	public void extract(RDFGraph aGraph) {
		try {
			for (;graphResult.hasNext();) {
				Statement stmt = graphResult.next();
				aGraph.addTriple(
						asResource( stmt.getSubject(), aGraph ),
						asProperty( stmt.getPredicate(), aGraph ),
						asNode( stmt.getObject(), aGraph ) );
			}
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		}
	}
	
	public RDFGraph extract() {
		final RDFResource resource = new RDFResourceImpl() {			
			@Override
			public Object getDelegate() { return null; }
			
			@Override
			public void addDelegate(Triple obj) {}
		};
		final RDFGraph aGraph = RDFFactory.eINSTANCE.createDocumentGraph();
		resource.getContents().add(aGraph);
		
		try {
			for (;graphResult.hasNext();) {
				Statement stmt = graphResult.next();
				aGraph.addTriple(
						asResource( stmt.getSubject(), aGraph ),
						asProperty( stmt.getPredicate(), aGraph ),
						asNode( stmt.getObject(), aGraph ) );
			}
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		}
		return aGraph;
	}
	
	private Property asProperty(URI aURI, RDFGraph aGraph) {
		return aGraph.getProperty( aURI.toString() );
	}

	private Node asNode(Value object, RDFGraph aGraph) {
		Node aNode = null;
		if (object instanceof URI) 
		{
			aNode = aGraph.getResource( ((URI)object).toString() );
		} 
		else if (object instanceof Literal) 
		{
			aNode = RDFFactory.eINSTANCE.createLiteral();
			((com.emf4sw.rdf.Literal)aNode).setLexicalForm( ((Literal)object).getLabel() );
			if (((Literal)object).getLanguage() != null)
			{
				((com.emf4sw.rdf.Literal)aNode).setLang(((Literal)object).getLanguage());
			}
			if (((Literal)object).getDatatype() != null) 
			{
				((com.emf4sw.rdf.Literal)aNode).setDatatype(aGraph.getDatatype( ((Literal)object).getDatatype().stringValue() ));
			}
		} 
		else if (object instanceof BNode) 
		{
			aNode = RDFFactory.eINSTANCE.createBlankNode();
		}
		return aNode;
	}

	private com.emf4sw.rdf.Resource asResource(Resource aResource, RDFGraph aGraph) {
		return aGraph.getResource( aResource.stringValue() );
	}

}
