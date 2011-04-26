/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple.jena;

import org.eclipse.emf.common.util.URI;

import com.emf4sw.rdf.NamedGraph;
import com.emf4sw.rdf.RDFGraph;
import com.emf4sw.rdf.jena.RDFGraphExtractor;
import com.emf4sw.rdf.resource.impl.NTriplesResourceImpl;
import com.emftriple.datasources.ISparqlUpdateDataSource;
import com.emftriple.datasources.ITransactionEnableDataSource;
import com.google.inject.internal.Lists;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.store.DatasetStore;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.update.GraphStore;
import com.hp.hpl.jena.update.GraphStoreFactory;
import com.hp.hpl.jena.update.UpdateAction;

/**
 * {@link JenaSDB}
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class JenaSDB extends ModelNamedGraphDataSource implements ISparqlUpdateDataSource, ITransactionEnableDataSource {

	public static final String JENA_SDB_OPTION_USER = "emftriple.db.user";
	
	public static final String JENA_SDB_OPTION_PASS = "emftriple.db.password";
	
	public static final String JENA_SDB_OPTION_TYPE = "emftriple.db.sdb.type";
	
	private final Store store;

	protected JenaSDB(String name, Store store) {
		super(name);
		this.store = store;
	}

	@Override
	public void add(RDFGraph graph) {
		final Dataset ds = DatasetStore.create(store);
		ds.getDefaultModel().add(new RDFGraphExtractor().extract(graph));
	}

	@Override
	public void add(NamedGraph graph) {
		final Dataset ds = DatasetStore.create(store);
		if (ds.containsNamedModel(graph.getURI())) {
			ds.getNamedModel(graph.getURI()).add(new RDFGraphExtractor().extract(graph));
		}
	}
	
	@Override
	public void remove(RDFGraph graph) {
		final Dataset ds = DatasetStore.create(store);
		ds.getDefaultModel().remove(new RDFGraphExtractor().extract(graph));
	}

	@Override
	public void remove(NamedGraph graph) {
		final Dataset ds = DatasetStore.create(store);
		if (ds.containsNamedModel(graph.getURI())) {
			ds.getNamedModel(graph.getURI()).remove(new RDFGraphExtractor().extract(graph));
		}
	}

	@Override
	public boolean supportsTransaction() {
		return true;
	}

	@Override
	public void begin() {
		Dataset ds = DatasetStore.create(store);
		ds.getDefaultModel().begin();
	}

	@Override
	public void commit() {
		Dataset ds = DatasetStore.create(store);
		ds.getDefaultModel().commit();
	}

	@Override
	public void rollback() {
		Dataset ds = DatasetStore.create(store);
		ds.getDefaultModel().abort();
	}

	@Override
	public boolean containsGraph(URI graph) {
		Dataset ds = DatasetStore.create(store);
		
		return ds.containsNamedModel(graph.toString());
	}
	
	@Override
	public void deleteGraph(URI graph) {
		if (containsGraph(graph)) {
			Dataset ds = DatasetStore.create(store);
			ds.getNamedModel(graph.toString()).removeAll();
		}
	}
		
	@Override
	public void connect() {
		setConnected(true);
	}
	
	@Override
	public void disconnect() {
		setConnected(false);
		store.getConnection().close();
	}

	@Override
	public Model getModel() {
		return DatasetStore.create(store).getDefaultModel();
	}

	@Override
	public Model getModel(URI graph) {
		Dataset ds = DatasetStore.create(store);
		if (ds.containsNamedModel(graph.toString()))
			return ds.getNamedModel(graph.toString());
		 
		return null;
	}

	@Override
	public QueryExecution getQueryExecution(String query, Model model) {
		return QueryExecutionFactory.create(query, getModel());
	}
	
	@Override
	public void update(String query) {
		final Model model = getModel();
		
		model.enterCriticalSection(Lock.WRITE);
		try {
			GraphStore graphStore = GraphStoreFactory.create(model);
			UpdateAction.parseExecute( query, graphStore);
		} finally {
			model.leaveCriticalSection();
		}
	}

	@Override
	public NamedGraph getNamedGraph(URI graphURI) {
		NamedGraph aGraph = new NTriplesResourceImpl().createNamedGraph(graphURI);
		
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
		return Lists.newArrayList(DatasetStore.create(store).listNames());
	}
}
