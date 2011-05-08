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
package com.emftriple.jena.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import com.emftriple.jena.ModelDataSource;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.shared.Lock;

/**
 * {@link JenaDataSourceFactory}
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class FileDataSource 
	extends ModelDataSource {

	private final String fileLocation;

	private final Model model;

	private final String fileFormat;

	protected FileDataSource(Model model, String fileLocation, String format) {
		this.fileLocation = fileLocation;
		this.fileFormat = format;
		this.model = model;
	}

	@Override
	public void add(Iterable<Statement> triples, String graphURI) {
		model.enterCriticalSection(Lock.WRITE);
		
		try {
			model.add((List<Statement>)triples);
			try {
				model.write(new FileOutputStream(new File(fileLocation)), fileFormat);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} finally { 
			model.leaveCriticalSection();
		}
	}

	@Override
	public void remove(Iterable<Statement> triples, String graphURI) {
		model.enterCriticalSection(Lock.WRITE);
		try {
			model.remove( (List<Statement>)triples );
			try {
				model.write(new FileOutputStream(new File(fileLocation)), fileFormat);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} finally { 
			model.leaveCriticalSection();
		}
	}

	@Override
	public Model getModel() {
		return model;
	}

	@Override
	public Model getModel(String graph) {
		throw new UnsupportedOperationException();
	}

	@Override
	public QueryExecution getQueryExecution(String query, Model model) {
		return QueryExecutionFactory.create(query, model);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		model.close();
	}

	@Override
	public boolean supportsTransaction() {
		return false;
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
	public void delete(String graphURI) {
		model.removeAll();
		try {
			model.write(new FileOutputStream(new File(fileLocation)), fileFormat);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean supportsNamedGraph() {
		return false;
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
