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

import java.util.Map;

import org.eclipse.emf.common.util.URI;

import com.emftriple.datasources.IDataSource;
import com.emftriple.jena.JenaResourceImpl;
import com.emftriple.util.ETripleOptions;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.sdb.Store;

public class SDBResourceImpl 
	extends JenaResourceImpl {

	public SDBResourceImpl(URI uri) {
		super(uri);
	}

	@Override
	public IDataSource<Model, Statement, RDFNode, Resource, Literal> getDataSource(Map<?, ?> options) {
		if (options.containsKey(ETripleOptions.OPTION_DATASOURCE_OBJECT)) {
			Store store = (Store) options.get(ETripleOptions.OPTION_DATASOURCE_OBJECT);
			
			return new SDBDataSource(store);
		}
		return null;
	}

	@Override
	public IDataSource<Model, Statement, RDFNode, Resource, Literal> getDataSource() {
		return getDataSource(resourceSet.getLoadOptions());
	}

}
