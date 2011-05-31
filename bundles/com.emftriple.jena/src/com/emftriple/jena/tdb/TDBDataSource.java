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

import com.emftriple.jena.ModelDataSource;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.rdf.model.Model;
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
	public QueryExecution getQueryExecution(String query, Model model) {
		QueryExecution qe = null;
		try {
			qe = QueryExecutionFactory.create(query, dataSet);
		} catch (QueryParseException e) {
			System.out.println(query);
			e.printStackTrace();
		}
		return qe;
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
	public Model getModel(String graph) {
		if (graph == null) {
			return dataSet.getDefaultModel();
		} else {
			return dataSet.getNamedModel(graph);
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
