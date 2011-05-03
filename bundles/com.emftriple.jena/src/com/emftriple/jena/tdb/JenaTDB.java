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
package com.emftriple.jena.tdb;

import org.eclipse.emf.common.util.URI;

import com.emf4sw.rdf.NamedGraph;
import com.emf4sw.rdf.Triple;
import com.emf4sw.rdf.jena.TripleExtractor;
import com.emf4sw.rdf.resource.impl.NTriplesResourceImpl;
import com.emftriple.datasources.ISparqlUpdateDataSource;
import com.emftriple.datasources.ITransactionEnableDataSource;
import com.emftriple.jena.ModelNamedGraphDataSource;
import com.emftriple.jena.util.JenaUtil;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.tdb.base.file.Location;
import com.hp.hpl.jena.update.GraphStore;
import com.hp.hpl.jena.update.GraphStoreFactory;
import com.hp.hpl.jena.update.UpdateAction;

/**
 * TDB DataSource 
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class JenaTDB extends ModelNamedGraphDataSource implements ITransactionEnableDataSource, ISparqlUpdateDataSource {

	private final Dataset dataSet;
	
	protected JenaTDB() {
		dataSet = TDBFactory.createDataset();
	}
	
	protected JenaTDB(Location location) {
		dataSet = TDBFactory.createDataset(location);
	}
	
	protected JenaTDB(String fileLocation) {
		dataSet = TDBFactory.createDataset(fileLocation);
	}

	@Override
	public Model getModel() {
		return dataSet.getDefaultModel();
	}

	@Override
	public Model getModel(String graph) {
		return dataSet.getNamedModel(graph);
	}

	@Override
	public QueryExecution getQueryExecution(String query, Model model) {
		QueryExecution qe = null;
		try {
			qe = QueryExecutionFactory.create(query, model);
		} catch (QueryParseException e) {
			System.out.println(query);
			e.printStackTrace();
		}
		return qe;
	}
	
	@Override
	public void begin() {
		dataSet.getDefaultModel().begin();
	}

	@Override
	public void commit() {
		dataSet.getDefaultModel().commit();
	}

	@Override
	public void rollback() {
		dataSet.getDefaultModel().abort();
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
	public Iterable<String> getNamedGraphs() {
		return JenaUtil.getNamedGraphs(dataSet);
	}

	@Override
	public boolean containsGraph(String graph) {
		return dataSet.containsNamedModel(graph.toString());
	}

	@Override
	public void add(Iterable<Triple> triples) {
		final Model model = getModel();
		
		model.enterCriticalSection(Lock.WRITE);
		try {
			TripleExtractor.extract(triples, model);
		} finally { 
			model.leaveCriticalSection();
			model.commit();
		}	
	}
	
	@Override
	public void add(Iterable<Triple> triples, String namedGraphURI) {
		final Model model = getModel(namedGraphURI);
		
		model.enterCriticalSection(Lock.WRITE);
		try {
			TripleExtractor.extract(triples, model);
		} finally { 
			model.leaveCriticalSection();
			model.commit();
		}	
	}
	
	@Override
	public void remove(Iterable<Triple> triples) {
		final Model model = getModel();
		final Model removeModel = ModelFactory.createDefaultModel();
		TripleExtractor.extract(triples, removeModel);
		
		model.enterCriticalSection(Lock.WRITE);
		try {
			model.remove( removeModel );
		} finally { 
			model.leaveCriticalSection();
			model.commit();
		}
	}
	
	@Override
	public void remove(Iterable<Triple> triples, String namedGraphURI) {
		final Model model = getModel(namedGraphURI);
		final Model removeModel = ModelFactory.createDefaultModel();
		TripleExtractor.extract(triples, removeModel);
		
		model.enterCriticalSection(Lock.WRITE);
		try {
			model.remove( removeModel );
		} finally { 
			model.leaveCriticalSection();
			model.commit();
		}
	}

	@Override
	public void deleteGraph(String graph) {
		if (containsGraph(graph)) {
			dataSet.getNamedModel(graph.toString()).removeAll();
			dataSet.getNamedModel(graph.toString()).removeAll().commit();
		}
	}
	
	@Override
	public void update(String query) {
		final Model model = getModel();
		
		model.enterCriticalSection(Lock.WRITE);
		try {
			GraphStore graphStore = GraphStoreFactory.create(model);
			UpdateAction.parseExecute( query, graphStore);
		} finally {
			model.commit();
			model.leaveCriticalSection();
		}
	}

	@Override
	public boolean supportsTransaction() {
		return true;
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
	public void delete() {
		dataSet.getDefaultModel().removeAll();
		dataSet.getDefaultModel().commit();
	}

}
