/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple.jena.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.emf4sw.rdf.Triple;
import com.emf4sw.rdf.jena.TripleExtractor;
import com.emftriple.datasources.ISparqlUpdateDataSource;
import com.emftriple.jena.JenaDataSourceFactory;
import com.emftriple.jena.ModelDataSource;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.update.GraphStore;
import com.hp.hpl.jena.update.GraphStoreFactory;
import com.hp.hpl.jena.update.UpdateAction;

/**
 * {@link JenaDataSourceFactory}
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class JenaFile extends ModelDataSource implements ISparqlUpdateDataSource {

	private final String fileLocation;

	private final Model model;

	private final String fileFormat;

	protected JenaFile(String name, Model model, String fileLocation, String format) {
		super(name);
		this.fileLocation = fileLocation;
		this.fileFormat = format;
		this.model = model;
	}

	@Override
	public void add(Iterable<Triple> triples) {
		model.enterCriticalSection(Lock.WRITE);
		TripleExtractor.extract(triples, model);
		try {
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
	public void remove(Iterable<Triple> triples) {
		model.enterCriticalSection(Lock.WRITE);
		try {
			Model removeModel = ModelFactory.createDefaultModel(); 
			TripleExtractor.extract(triples, removeModel);
			
			if (!removeModel.isEmpty()) {
				model.remove( removeModel );
			}
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
	public void update(String query) {
		final Model model = getModel();

		model.enterCriticalSection(Lock.WRITE);
		try {
			GraphStore graphStore = GraphStoreFactory.create(model);
			UpdateAction.parseExecute( query, graphStore);
		} finally {
			model.leaveCriticalSection();
		}
		try {
			model.write(new FileOutputStream(new File(fileLocation)), fileFormat);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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
	public void delete() {
		model.removeAll();
		try {
			model.write(new FileOutputStream(new File(fileLocation)), fileFormat);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
