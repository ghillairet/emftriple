/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple.datasources.impl;

import com.emf4sw.rdf.RDFFactory;
import com.emf4sw.rdf.RDFGraph;
import com.emf4sw.rdf.Triple;
import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.IMutableDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.datasources.ITransactionEnableDataSource;
import com.emftriple.datasources.MalformedQueryException;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.5.6
 */
public class TransactionDataSource extends AbstractDataSource implements IMutableDataSource, ITransactionEnableDataSource {

	private IDataSource dataSource;

	private boolean isRunning = false;

	private RDFGraph removedGraph;

	private RDFGraph addedGraph;

	public TransactionDataSource(IDataSource dataSource) {
		super(dataSource.getName());
		this.dataSource = dataSource;
	}
	
	@Override
	public void begin() {
		if (isRunning) {
			throw new IllegalStateException("DataSource is currently in an active transaction.");
		}
		isRunning = true;
		addedGraph = RDFFactory.eINSTANCE.createDocumentGraph();
		removedGraph = RDFFactory.eINSTANCE.createDocumentGraph();
	}

	@Override
	public void commit() {
		if (!isRunning) {
			throw new IllegalStateException("DataSource is not in an active transaction.");
		}
		isRunning = false;
		addedGraph = null;
		removedGraph = null;
	}

	@Override
	public void rollback() {
		if (!isRunning) {
			throw new IllegalStateException("DataSource is not in an active transaction.");
		}
		if (addedGraph != null && dataSource instanceof IMutableDataSource) {
			((IMutableDataSource) dataSource).remove( addedGraph );
		}
		if (removedGraph != null && dataSource instanceof IMutableDataSource) {
			((IMutableDataSource) dataSource).add( removedGraph );
		}
		commit();
	}

	@Override
	public boolean askQuery(String query) throws MalformedQueryException {
		return dataSource.askQuery(query);
	}

	@Override
	public void connect() {
		dataSource.connect();
	}

	@Override
	public void disconnect() {
		dataSource.disconnect();
	}
	
	@Override
	public RDFGraph constructQuery(String query) throws MalformedQueryException {
		return dataSource.constructQuery(query);
	}

	@Override
	public boolean isConnected() {
		return dataSource.isConnected();
	}

	@Override
	public IResultSet selectQuery(String query) throws MalformedQueryException {
		return dataSource.selectQuery(query);
	}

	@Override
	public boolean supportsTransaction() {
		return Boolean.TRUE;
	}

	@Override
	public void add(RDFGraph graph) {
		if (!isRunning) {
			throw new IllegalStateException("DataSource is not in an active transaction.");
		}
		if (dataSource instanceof IMutableDataSource) {
			if (addedGraph == RDFFactory.eINSTANCE.createDocumentGraph()) {
				addedGraph = graph;
			}
			addedGraph.add(graph);
			((IMutableDataSource) dataSource).add(graph);
		}
	}

	@Override
	public void remove(RDFGraph graph) {
		if (!isRunning) {
			throw new IllegalStateException("DataSource is not in an active transaction.");
		}
		if (dataSource instanceof IMutableDataSource) {
			if (removedGraph == RDFFactory.eINSTANCE.createDocumentGraph()) {
				removedGraph = graph;
			}
			removedGraph.add(graph);
			((IMutableDataSource) dataSource).remove(graph);
		}
	}

	@Override
	public RDFGraph describeQuery(String query) throws MalformedQueryException {
		return dataSource.describeQuery(query);
	}

	@Override
	public void constructQuery(String aQuery, RDFGraph aGraph) throws MalformedQueryException {
		dataSource.constructQuery(aQuery, aGraph);
	}

	@Override
	public void describeQuery(String aQuery, RDFGraph aGraph) throws MalformedQueryException {
		dataSource.describeQuery(aQuery, aGraph);
	}

	@Override
	public void delete() {
		if (dataSource instanceof IMutableDataSource)
			((IMutableDataSource) dataSource).delete();
	}

	@Override
	public void add(Iterable<Triple> triples) {
		if (dataSource instanceof IMutableDataSource)
			((IMutableDataSource) dataSource).add(triples);
	}

}
