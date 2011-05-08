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
package com.emftriple.jena.sdb;

import com.emftriple.jena.ModelDataSource;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.sdb.Store;

/**
 * {@link SDBDataSource}
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class SDBDataSource 
	extends ModelDataSource {

	public static final String OPTION_SDB_USER = "emftriple.db.user";
	
	public static final String OPTION_SDB_PASSWORD = "emftriple.db.password";
	
	public static final String OPTION_SDB_TYPE = "emftriple.db.sdb.type";
	
	@SuppressWarnings("unused")
	private final Store store;

	protected SDBDataSource(Store store) {
		this.store = store;
	}

	@Override
	public boolean supportsNamedGraph() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMutable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supportsUpdateQuery() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void add(Iterable<Statement> triples, String graphURI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String graphURI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Iterable<Statement> triples, String graphURI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Model getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model getModel(String graph) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryExecution getQueryExecution(String query, Model model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supportsTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	
}
