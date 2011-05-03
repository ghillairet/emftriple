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

import com.emf4sw.rdf.NamedGraph;
import com.emf4sw.rdf.RDFGraph;
import com.emf4sw.rdf.jena.NamedGraphInjector;
import com.emftriple.datasources.IMutableNamedGraphDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.jena.util.JenaResultSet;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.shared.Lock;

public abstract class ModelNamedGraphDataSource extends ModelDataSource implements IMutableNamedGraphDataSource {

	public ModelNamedGraphDataSource() {}

	@Override
	public abstract NamedGraph getNamedGraph(String graphURI);

	@Override
	public abstract Iterable<String> getNamedGraphs();

	@Override
	public abstract boolean containsGraph(String graph);

	@Override
	public IResultSet selectQuery(String query, String graphURI) {
		IResultSet rs = null;
		final Model model = getModel(graphURI);
		
		model.enterCriticalSection(Lock.READ);
		try {
			final QueryExecution qexec = QueryExecutionFactory.create(query, model);
			if (qexec != null)
				rs = new JenaResultSet(qexec.execSelect());
		} catch (QueryParseException e) {
			System.out.println("query= "+query);
			System.out.println("graph= "+graphURI);
			e.printStackTrace();
		} catch (Exception e) {
			
		} finally {
			model.leaveCriticalSection();
		}
		return rs;
	}

	@Override
	public RDFGraph constructQuery(String query, String graphURI) {
		RDFGraph res = null;
		final Model model = getModel(graphURI);
		
		model.enterCriticalSection(Lock.READ);
		try {
			final QueryExecution queryExec = QueryExecutionFactory.create(query, model);
			final Model result = queryExec.execConstruct();
			res = result == null ? null : NamedGraphInjector.inject(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			model.leaveCriticalSection();
		}
		
		return res;
	}

	@Override
	public RDFGraph describeQuery(String query, String graphURI) {
		RDFGraph graph = null;
		final Model model = getModel(graphURI);
		
		model.enterCriticalSection(Lock.READ);
		try {
			QueryExecution qexec = QueryExecutionFactory.create(query, model);
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
	public boolean askQuery(String query, String graph) {
		boolean result = false;
		final Model model = getModel(graph);
		
		model.enterCriticalSection(Lock.READ);
		try {
			result = QueryExecutionFactory.create(query, model).execAsk();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			model.leaveCriticalSection();
		}
		return result;
	}	
}
