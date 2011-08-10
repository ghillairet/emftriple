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
package org.eclipselabs.emftriple.sail;

import info.aduna.iteration.CloseableIteration;

import org.eclipselabs.emftriple.datasources.AbstractDataSource;
import org.eclipselabs.emftriple.datasources.IDataSource;
import org.eclipselabs.emftriple.datasources.IResultSet;
import org.eclipselabs.emftriple.sail.util.SailResultSet;
import org.openrdf.model.Graph;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
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

/**
 * 
 * @author ghillairet
 * @since 0.8.0
 */
public class SailDataSource 
	extends AbstractDataSource<Graph, Statement>
	implements IDataSource<Graph, Statement> {

	protected SailConnection connection;

	private Sail sail;

	/**
	 * The Sail repository should be initialize before.
	 * 
	 * @param sail
	 */
	public SailDataSource(Sail sail) {
		this.sail = sail;
		try {
			this.connection = sail.getConnection();
		} catch (SailException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		sail.shutDown();
	}

	@Override
	public void add(Iterable<Statement> triples, String namedGraphURI) {
		for (Statement stmt: triples)
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
		
		try {
			connection.commit();
		} catch (SailException e2) {
			e2.printStackTrace();
		}
	}

	@Override
	public void remove(Iterable<Statement> triples, String namedGraphURI) {
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

		try {
			connection.commit();
		} catch (SailException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connect() {
		if (!isConnected()) {
			setConnected(true);
//			try {
//				if (connection != null && connection.isOpen()) {
//					connection.close();
//				}
//				connection = sail.getConnection();
//			} catch (SailException e) {
//				e.printStackTrace();
//			}
		}
	}

	@Override
	public void disconnect() {
		setConnected(false);
//
//		try {
//			if (connection.isOpen()) {
//				connection.close();
//			}
//		} catch (SailException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public boolean askQuery(String query, String graph) {
		throw new UnsupportedOperationException("Ask Queries are not yet supported for Sail.");
	}

	@Override
	public IResultSet selectQuery(String query, String graph) {
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
			sparqlResults = connection.evaluate(
					parsedQuery.getTupleExpr(), 
					parsedQuery.getDataset(), 
					new EmptyBindingSet(), 
					false);
		} catch (SailException e) {
			e.printStackTrace();
		}
		
		return new SailResultSet(sparqlResults);

	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.datasources.IDataSource#update(java.lang.String)
	 */
	@Override
	public void update(String updateQuery) {
		// TODO Auto-generated method stub
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.datasources.IDataSource#contains(java.lang.String)
	 */
	@Override
	public boolean contains(String resourceURI) {
		IResultSet resultSet = 
				selectQuery("SELECT ?o WHERE { <"+resourceURI+"> ?p ?o }", null);
		
		if (resultSet != null) {
			return resultSet.hasNext();
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.datasources.IDataSource#delete(java.lang.String, java.lang.String)
	 */
	@Override
	public void delete(String resourceURI, String graphURI) {
		ValueFactory factory = sail.getValueFactory();
		if (factory == null) {
			factory = new ValueFactoryImpl();
		}
		if (graphURI == null) {
			try {
				connection.removeStatements(factory.createURI(resourceURI), null, null);
			} catch (SailException e) {
				e.printStackTrace();
			}
		} else {
			try {
				connection.removeStatements(factory.createURI(resourceURI), null, null, factory.createURI(graphURI));
			} catch (SailException e) {
				e.printStackTrace();
			}
		}
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
		throw new UnsupportedOperationException("Construct Queries are not yet supported for Sail.");
	}

	@Override
	public void describeQuery(String aQuery, String graphURI, Graph aGraph) {
		throw new UnsupportedOperationException("Describe Queries are not yet supported for Sail.");
	}

	@Override
	public Graph getGraph(String graphURI) {
		throw new UnsupportedOperationException("Not yet supported for Sail.");
	}

	@Override
	public void delete(String graphURI) {
		try {
			if (graphURI == null) {
				connection.clear();
				connection.commit();
			} else {
				connection.clear(new ValueFactoryImpl().createURI(graphURI));
				connection.commit();
			}
		} catch (SailException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SailException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public Graph constructQuery(String query, String graphURI) {
		throw new UnsupportedOperationException("Construct Queries are not yet supported for Sail.");
	}

	@Override
	public Graph describeQuery(String query, String graphURI) {
		throw new UnsupportedOperationException("Describe Queries are not yet supported for Sail.");
	}
}
