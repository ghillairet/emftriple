/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple.jena.service;

import java.util.List;

import org.eclipse.emf.common.util.URI;

import com.emf4sw.rdf.NamedGraph;
import com.emf4sw.rdf.Node;
import com.emf4sw.rdf.RDFGraph;
import com.emf4sw.rdf.URIElement;
import com.emf4sw.rdf.resource.impl.NTriplesResourceImpl;
import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.INamedGraphDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.datasources.IResultSet.Solution;
import com.emftriple.jena.ModelDataSource;
import com.google.inject.internal.Lists;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * {@link JenaService} is a SPARQL endpoint {@link IDataSource} representation.
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class JenaService extends ModelDataSource implements INamedGraphDataSource {

	private final String service;
	
	JenaService(String name, String service) {
		super( name );
		this.service = service;
	}

	@Override
	public QueryExecution getQueryExecution(String query, Model model) {
		return QueryExecutionFactory.sparqlService(service, QueryFactory.create( query ));
	}
	
	@Override
	public Model getModel() {
		return ModelFactory.createDefaultModel();
	}
	
	@Override
	public Model getModel(String graph) {
		return getModel();
	}

	@Override
	public boolean supportsTransaction() {
		return Boolean.FALSE;
	}

	@Override
	public void connect() {
		setConnected(true);
	}
	
	@Override
	public void disconnect() {
		setConnected(false);
	}

	@Override
	public boolean containsGraph(String graph) {
		return askQuery("ask where { graph < " + graph.toString() + "> { ?s ?p ?o } }");
	}

	@Override
	public Iterable<String> getNamedGraphs() {
		final List<String> list = Lists.newArrayList();
		final IResultSet result = selectQuery("select ?g where { graph ?g { ?s ?p ?o } }");
		for (;result.hasNext();) {
			Solution sol = result.next();
			Node n = sol.get("g");
			if (n instanceof URIElement)
				list.add(((URIElement) n).getURI());
		}
		return list;
	}

	@Override
	public NamedGraph getNamedGraph(String graphURI) {
		NamedGraph aGraph = new NTriplesResourceImpl().createNamedGraph(URI.createURI(graphURI));
		
		constructQuery(
				"construct { ?s ?p ?o } " +
				"where { " +
				"	graph <" + graphURI.toString() + "> { ?s ?p ?o } " +
				"}", 
				aGraph);
		
		return aGraph;
	}

	@Override
	public IResultSet selectQuery(String query, String graph) {
		return selectQuery(query);
	}

	@Override
	public RDFGraph constructQuery(String query, String graph) {
		return constructQuery(query);
	}

	@Override
	public RDFGraph describeQuery(String query, String graph) {
		return describeQuery(query);
	}

	@Override
	public boolean askQuery(String query, String graph) {
		return askQuery(query);
	}
}
