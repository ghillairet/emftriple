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
package org.eclipselabs.emftriple.jena;

import java.util.List;

import org.eclipselabs.emftriple.datasources.AbstractDataSource;
import org.eclipselabs.emftriple.datasources.IDataSource;
import org.eclipselabs.emftriple.datasources.IResultSet;
import org.eclipselabs.emftriple.jena.util.JenaResultSet;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.shared.Lock;

/**
 * 
 * @author ghillairet
 * @since 0.8.0
 */
public abstract class ModelDataSource 
	extends AbstractDataSource<Model, Statement>
	implements IDataSource<Model, Statement>, JenaDataSource {
	
	public ModelDataSource() {}

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
	public IResultSet selectQuery(String query, String graphURI) {
		IResultSet rs = null;
		final Model model = getModel(graphURI);
		
		model.enterCriticalSection(Lock.READ);
		try {
			final QueryExecution qexec = getQueryExecution(query, model);
			if (qexec != null)
				rs = new JenaResultSet(qexec.execSelect());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			model.leaveCriticalSection();
		}
		return rs;
	}

	@Override
	public Model constructQuery(String query, String graphURI) {
		final Model model = getModel(graphURI);
		Model result = null;
		
		model.enterCriticalSection(Lock.READ);
		try {
			final QueryExecution queryExec = getQueryExecution(query, model);
			result = queryExec.execConstruct();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			model.leaveCriticalSection();
		}

		return result;
	}
	
	@Override
	public void constructQuery(String query, String graphURI, Model graph) {
		final Model model = getModel(graphURI);
		Model result = null;
		
		model.enterCriticalSection(Lock.READ);
		try {
			final QueryExecution queryExec = getQueryExecution(query, model);
			result = queryExec.execConstruct();
			graph.add(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			model.leaveCriticalSection();
		}
	}

	@Override
	public Model describeQuery(String query, String graphURI) {
		Model graph = null;
		final Model model = getModel(graphURI);

		model.enterCriticalSection(Lock.READ);
		try {
			QueryExecution qexec = getQueryExecution(query, model);
			graph = qexec.execDescribe();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			model.leaveCriticalSection();
		};

		return graph;
	}

	@Override
	public void describeQuery(String query, String graphURI, Model graph) {
		final Model model = getModel(graphURI);

		model.enterCriticalSection(Lock.READ);
		try {
			QueryExecution qexec = getQueryExecution(query, model);
			final Model result = qexec.execDescribe();
			if (result != null) {
				graph.add(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			model.leaveCriticalSection();
		}
	}

	@Override
	public boolean askQuery(String query, String graphURI) {
		boolean result = false;
		final Model model = getModel(graphURI);

		model.enterCriticalSection(Lock.READ);
		try {
			result = getQueryExecution(query, model).execAsk();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			model.leaveCriticalSection();
		}
		return result;
	}

	@Override
	public Model getGraph(String graphURI) {
		return getModel(graphURI);
	}
	
	@Override
	public abstract boolean supportsTransaction();

	@Override
	public abstract void connect();

	@Override
	public abstract void disconnect();

}
