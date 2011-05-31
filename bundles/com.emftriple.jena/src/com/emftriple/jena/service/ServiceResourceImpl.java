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
package com.emftriple.jena.service;

import java.util.Map;

import org.eclipse.emf.common.util.URI;

import com.emftriple.datasources.IDataSource;
import com.emftriple.jena.JenaResourceImpl;
import com.emftriple.util.ETripleOptions;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;

public class ServiceResourceImpl 
	extends JenaResourceImpl {

	public ServiceResourceImpl(URI uri) {
		super(uri);
	}

	@Override
	public IDataSource<Model, Statement, RDFNode, com.hp.hpl.jena.rdf.model.Resource, Literal> getDataSource(Map<?, ?> options) {
		if (!options.containsKey(ETripleOptions.OPTION_DATASOURCE_LOCATION)) {
			throw new IllegalArgumentException("");
		}
		
		String url = (String) options.get(ETripleOptions.OPTION_DATASOURCE_LOCATION);
		
		return new ServiceDataSource(url);
	}

	@Override
	public IDataSource<Model, Statement, RDFNode, com.hp.hpl.jena.rdf.model.Resource, Literal> getDataSource() {
		return getDataSource(resourceSet.getLoadOptions());
	}
	
	
}
