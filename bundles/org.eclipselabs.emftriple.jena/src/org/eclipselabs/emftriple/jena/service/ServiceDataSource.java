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
package org.eclipselabs.emftriple.jena.service;

import org.eclipselabs.emftriple.datasources.IDataSource;
import org.eclipselabs.emftriple.jena.ModelDataSource;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;

/**
 * {@link ServiceDataSource} is a SPARQL endpoint {@link IDataSource} representation.
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class ServiceDataSource 
	extends ModelDataSource {

	private final String service;
	
	ServiceDataSource(String service) {
		this.service = service;
	}

	@Override
	public QueryExecution getQueryExecution(String query, Model model) {
		return QueryExecutionFactory.sparqlService(service, QueryFactory.create( query ));
	}
	
	@Override
	public Model getModel(String graph) {
		return ModelFactory.createDefaultModel();
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
	public boolean supportsNamedGraph() {
		return true;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public boolean supportsUpdateQuery() {
		return false;
	}

	@Override
	public void add(Iterable<Statement> triples, String graphURI) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(String graphURI) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove(Iterable<Statement> triples, String graphURI) {
		throw new UnsupportedOperationException();
	}

}
