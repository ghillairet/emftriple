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
package com.emftriple.jena;

import com.emf4sw.rdf.RDFGraph;
import com.emf4sw.rdf.jena.NamedGraphInjector;
import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.datasources.impl.AbstractDataSource;
import com.emftriple.jena.util.JenaResultSet;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.shared.Lock;

public abstract class ModelDataSource extends AbstractDataSource implements IDataSource, JenaDataSource {

	public ModelDataSource() {}

	@Override
	public IResultSet selectQuery(String query) {
		IResultSet rs = null;
		final Model model = getModel();

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
	public RDFGraph constructQuery(String query) {
		RDFGraph graph = null;
		final Model model = getModel();

		model.enterCriticalSection(Lock.READ);
		try {
			final QueryExecution queryExec = getQueryExecution(query, model);
			final Model result = queryExec.execConstruct();
			graph = result == null ? null : NamedGraphInjector.inject(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			model.leaveCriticalSection();
		}

		return graph;
	}

	@Override
	public void constructQuery(String query, RDFGraph graph) {
		final Model model = getModel();

		model.enterCriticalSection(Lock.READ);
		try {
			final QueryExecution queryExec = getQueryExecution(query, model);
			final Model result = queryExec.execConstruct();

			if (result != null) {
				NamedGraphInjector.inject(result, graph);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			model.leaveCriticalSection();
		}
	}

	@Override
	public RDFGraph describeQuery(String query) {
		RDFGraph graph = null;
		final Model model = getModel();

		model.enterCriticalSection(Lock.READ);
		try {
			QueryExecution qexec = getQueryExecution(query, model);
			final Model result = qexec.execDescribe();
			graph = result == null ? null : NamedGraphInjector.inject(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			model.leaveCriticalSection();
		};

		return graph;
	}

	@Override
	public void describeQuery(String query, RDFGraph aGraph) {
		final Model model = getModel();

		model.enterCriticalSection(Lock.READ);
		try {
			QueryExecution qexec = getQueryExecution(query, model);
			final Model result = qexec.execDescribe();
			if (result != null) {
				NamedGraphInjector.inject(result, aGraph);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			model.leaveCriticalSection();
		}
	}

	@Override
	public boolean askQuery(String query) {
		boolean result = false;
		final Model model = getModel();

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
	public abstract boolean supportsTransaction();

	@Override
	public abstract void connect();

	@Override
	public abstract void disconnect();

}
