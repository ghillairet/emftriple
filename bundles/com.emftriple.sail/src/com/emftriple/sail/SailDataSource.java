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
package com.emftriple.sail;

import info.aduna.iteration.CloseableIteration;

import org.openrdf.model.Graph;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.impl.EmptyBindingSet;
import org.openrdf.query.parser.ParsedQuery;
import org.openrdf.query.parser.sparql.SPARQLParser;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;

import com.emftriple.datasources.AbstractDataSource;
import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.sail.util.SailResultSet;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class SailDataSource 
extends AbstractDataSource<Graph, Statement, Value, URI, Literal>
implements IDataSource<Graph, Statement, Value, URI, Literal> {

	protected SailConnection connection;

	private Sail sail;

	public SailDataSource(Sail sail) {
		this.sail = sail;
		try {
			sail.initialize();
		} catch (SailException e1) {
			e1.printStackTrace();
		}

		//		try {
		//			this.connection = sail.getConnection();
		//		} catch (SailException e) {
		//			e.printStackTrace();
		//		}

	}

	@Override
	protected void finalize() throws Throwable {
		sail.shutDown();
	}

	@Override
	public void add(Iterable<Statement> triples, String namedGraphURI) {
		SailConnection connection = null;
		try {
			connection = sail.getConnection();
		} catch (SailException e2) {
			e2.printStackTrace();
		}
		for (Statement stmt: triples) {
			try {
				if (namedGraphURI == null) {
					connection.addStatement(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
				} else {
					final URI graph = new ValueFactoryImpl().createURI(namedGraphURI);
					connection.addStatement(stmt.getSubject(), stmt.getPredicate(), stmt.getObject(), graph);
				} 
			} catch (SailException e) {
				try {
					connection.rollback();
				} catch (SailException e1) {
					e1.printStackTrace();
				}
			}
		}
		try {
			connection.commit();
			connection.close();
		} catch (SailException e2) {
			e2.printStackTrace();
		}
	}

	@Override
	public void remove(Iterable<Statement> triples, String namedGraphURI) {
		connect();

		for (Statement stmt: triples)
			try {
				if (namedGraphURI == null) {
					connection.removeStatements(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
				} else {
					connection.removeStatements(stmt.getSubject(), stmt.getPredicate(), stmt.getObject(),
							new ValueFactoryImpl().createURI(namedGraphURI));
				}
			} catch (SailException e) {
				try {
					connection.rollback();
				} catch (SailException e1) {
					e1.printStackTrace();
				}
			}

		disconnect();
	}

	@Override
	public void connect() {
		if (!isConnected()) {
			setConnected(true);
			try {
				connection = sail.getConnection();
			} catch (SailException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void disconnect() {
		setConnected(false);

		try {
			connection.close();
			//			sail.shutDown();
		} catch (SailException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean askQuery(String query, String graph) {
		//		return askQuery(query);
		throw new UnsupportedOperationException();
	}

	@Override
	public IResultSet<Value, URI, Literal> selectQuery(String query, String graph) {
		connect();

		final SPARQLParser parser = new SPARQLParser();

		CloseableIteration<? extends BindingSet, QueryEvaluationException> sparqlResults = null;
		ParsedQuery parsedQuery = null;

		try {
			parsedQuery = parser.parseQuery(query, null);
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		}
		try {
			sparqlResults = sail.getConnection().evaluate(
					parsedQuery.getTupleExpr(), 
					parsedQuery.getDataset(), 
					new EmptyBindingSet(), 
					false);
		} catch (SailException e) {
			e.printStackTrace();
		}

		return new SailResultSet(sparqlResults);

	}

	@Override
	public boolean supportsTransaction() {
		return true;
	}

	protected boolean containsGraph(String graph) {
		connect();

		try {
			for (CloseableIteration<? extends Resource, SailException> res = connection.getContextIDs(); res.hasNext();) {
				Resource r = res.next();
				if (r.stringValue().equals(graph.toString()))
					return true;
			}
		} catch (SailException e) {
			e.printStackTrace();
		}

		disconnect();

		return false;
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
		return false;
	}

	@Override
	public void constructQuery(String aQuery, String graphURI, Graph aGraph) {
		// TODO Auto-generated method stub

	}

	@Override
	public void describeQuery(String aQuery, String graphURI, Graph aGraph) {
		// TODO Auto-generated method stub

	}

	@Override
	public Graph getGraph(String graphURI) {

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(String graphURI) {
		connect();

		try {
			if (graphURI == null) {
				connection.clear();	
			} else {
				connection.clear(new ValueFactoryImpl().createURI(graphURI));
			}
		} catch (SailException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SailException e1) {
				e1.printStackTrace();
			}
		}

		disconnect();
	}

	@Override
	public Graph constructQuery(String query, String graphURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Graph describeQuery(String query, String graphURI) {
		// TODO Auto-generated method stub
		return null;
	}
}
