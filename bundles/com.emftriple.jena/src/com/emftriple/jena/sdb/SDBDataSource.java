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
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sdb.SDBFactory;
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
	
	private final Store store;

	protected SDBDataSource(Store store) {
		this.store = store;
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

	@Override
	public Model getModel(String graph) {
		Dataset ds = SDBFactory.connectDataset(store);
		if (graph == null) {	
			return ds.getDefaultModel();
		}
		return ds.getNamedModel(graph);
	}

	@Override
	public boolean supportsTransaction() {
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

	@Override
	public void delete(String graphURI) {
		final Dataset ds = SDBFactory.connectDataset(store);
		if (graphURI == null) {
			ds.getDefaultModel().removeAll();
			ds.getDefaultModel().commit();
			ds.close();
		} else {
			ds.getNamedModel(graphURI).removeAll();
			ds.getNamedModel(graphURI).commit();
			ds.close();
		}
	}

	
}
