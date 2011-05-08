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

import java.util.List;

import com.emftriple.jena.ModelDataSource;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.tdb.base.file.Location;

/**
 * TDB DataSource 
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class TDBDataSource 
	extends ModelDataSource {

	private final Dataset dataSet;
	
	protected TDBDataSource() {
		dataSet = TDBFactory.createDataset();
	}
	
	protected TDBDataSource(Location location) {
		dataSet = TDBFactory.createDataset(location);
	}
	
	protected TDBDataSource(String fileLocation) {
		dataSet = TDBFactory.createDataset(fileLocation);
	}

	@Override
	public Model getModel() {
		return dataSet.getDefaultModel();
	}

	@Override
	public Model getModel(String graph) {
		if (graph == null) {
			return dataSet.getDefaultModel();
		} else {
			return dataSet.getNamedModel(graph);
		}
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
	public void add(Iterable<Statement> triples, String namedGraphURI) {
		final Model model = getModel(namedGraphURI);
		
		model.enterCriticalSection(Lock.WRITE);
		try {
			model.add((List<Statement>)triples);
		} finally { 
			model.leaveCriticalSection();
			model.commit();
		}	
	}
	
	@Override
	public void remove(Iterable<Statement> triples, String namedGraphURI) {
		final Model model = getModel(namedGraphURI);
		
		model.enterCriticalSection(Lock.WRITE);
		try {
			model.remove((List<Statement>) triples);
		} finally { 
			model.leaveCriticalSection();
			model.commit();
		}
	}

	@Override
	public void delete(String graphURI) {
		if (graphURI == null) {
			dataSet.getDefaultModel().removeAll();
			dataSet.getDefaultModel().commit();	
		} else if (dataSet.containsNamedModel(graphURI)) {
			dataSet.getNamedModel(graphURI).removeAll();
			dataSet.getNamedModel(graphURI).removeAll().commit();
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
	public boolean supportsNamedGraph() {
		return true;
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public boolean supportsUpdateQuery() {		
		return true;
	}

}
