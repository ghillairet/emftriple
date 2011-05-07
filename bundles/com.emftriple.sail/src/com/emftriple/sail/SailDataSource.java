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

import java.util.ArrayList;
import java.util.List;

import org.openrdf.model.Graph;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
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

import com.emf4sw.rdf.NamedGraph;
import com.emf4sw.rdf.RDFGraph;
import com.emf4sw.rdf.Triple;
import com.emf4sw.rdf.sesame.RDFGraph2SesameGraph;
import com.emftriple.datasources.IMutableNamedGraphDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.datasources.ITransactionEnableDataSource;
import com.emftriple.datasources.impl.AbstractNamedGraphDataSource;
import com.emftriple.sail.util.SailResultSet;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class SailDataSource 
extends AbstractNamedGraphDataSource 
implements IMutableNamedGraphDataSource, ITransactionEnableDataSource {

	public static final Object OPTION_SAIL_OBJECT = "OPTION_SAIL_OBJECT";

	protected SailConnection connection;

	private Sail sail;

	public SailDataSource(Sail sail) {
		this.sail = sail;
		
		try {
			this.connection = sail.getConnection();
		} catch (SailException e) {
			e.printStackTrace();
		}
//		connect();
	}

	@Override
	public void add(Iterable<Triple> triples) {
//		checkIsConnected();
		final Graph aGraph = RDFGraph2SesameGraph.extract(triples);
		try {
			System.out.println(sail.isWritable());
		} catch (SailException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		for (Statement stmt: aGraph) {
			try {
				connection.addStatement(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
			} catch (SailException e) {
				try {
					connection.rollback();
				} catch (SailException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		commit();
	}

	@Override
	public void add(Iterable<Triple> triples, String namedGraphURI) {
//		checkIsConnected();
		final Graph aGraph = RDFGraph2SesameGraph.extract(triples, namedGraphURI, sail.getValueFactory());
		System.out.println(aGraph.size());
		int i = 0;
		for (Statement stmt: aGraph) {
			System.out.println(i);
			try {
				System.out.println(sail.getConnection().isOpen());
			} catch (SailException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			i++;
			try {
//				sail.getConnection().addStatement(
//						sail.getValueFactory().createURI("http://tinkerpop.com#1"), 
//						sail.getValueFactory().createURI("http://tinkerpop.com#knows"), 
//						sail.getValueFactory().createURI("http://tinkerpop.com#3"), 
//						sail.getValueFactory().createURI("http://tinkerpop.com"));

				sail.getConnection().addStatement(stmt.getSubject(), stmt.getPredicate(), stmt.getObject(), 
						sail.getValueFactory().createURI(namedGraphURI));
			} catch (SailException e) {
				e.printStackTrace();
			}
		}
		commit();
	}

	@Override
	public void remove(Iterable<Triple> triples) {
		checkIsConnected();
		final Graph aGraph = RDFGraph2SesameGraph.extract(triples);
		for (Statement stmt: aGraph)
			try {
				connection.removeStatements(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
			} catch (SailException e) {
				try {
					connection.rollback();
				} catch (SailException e1) {
					e1.printStackTrace();
				}
			}
			commit();
	}

	@Override
	public void remove(Iterable<Triple> triples, String namedGraphURI) {
		checkIsConnected();
		final Graph aGraph = RDFGraph2SesameGraph.extract(triples);
		for (Statement stmt: aGraph)
			try {
				connection.removeStatements(stmt.getSubject(), stmt.getPredicate(), stmt.getObject(),
						new ValueFactoryImpl().createURI(namedGraphURI));
			} catch (SailException e) {
				try {
					connection.rollback();
				} catch (SailException e1) {
					e1.printStackTrace();
				}
			}
			commit();
	}

	@Override
	public void begin() {
		checkIsConnected();
	}

	@Override
	public void commit() {
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

			try {
				if (!sail.isWritable()) {
					sail.shutDown();
				}
			} catch (SailException e) {
				e.printStackTrace();
			}
			try {
				sail.initialize();
			} catch (SailException e) {
				e.printStackTrace();
			}
			try {
				connection = sail.getConnection();
			} catch (SailException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void delete() {
		checkIsConnected();

		try {
			connection.clear();
		} catch (SailException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SailException e1) {
				e1.printStackTrace();
			}
		}

		commit();
	}

	@Override
	public void deleteGraph(String graph) {
		checkIsConnected();

		try {
			connection.clear(new ValueFactoryImpl().createURI(graph));
		} catch (SailException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SailException e1) {
				e1.printStackTrace();
			}
		}

		commit();
	}

	@Override
	public void disconnect() {
		setConnected(false);

		try {
			connection.close();
			sail.shutDown();
		} catch (SailException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean askQuery(String query, String graph) {
		return askQuery(query);
	}

	@Override
	public boolean askQuery(String query) {
		throw new UnsupportedOperationException();
	}

	@Override
	public RDFGraph describeQuery(String query) {
		//		checkIsConnected();
		//
		//		GraphQueryResult aResult = null;	
		//		try {
		//			aResult = connection.prepareGraphQuery(QueryLanguage.SPARQL, query)
		//			.evaluate();
		//		} catch (QueryEvaluationException e) {
		//			e.printStackTrace();
		//		} catch (RepositoryException e) {
		//			e.printStackTrace();
		//		} catch (MalformedQueryException e) {
		//			e.printStackTrace();
		//		}
		//
		//		return aResult != null ? new SesameGraphResult2RDFGraph(aResult).extract() : null;
		throw new UnsupportedOperationException();
	}

	@Override
	public void describeQuery(String aQuery, RDFGraph aGraph) {
		//		checkIsConnected();
		//
		//		GraphQueryResult aResult = null;	
		//		try {
		//			aResult = connection.prepareGraphQuery(QueryLanguage.SPARQL, aQuery)
		//			.evaluate();
		//		} catch (QueryEvaluationException e) {
		//			e.printStackTrace();
		//		} catch (RepositoryException e) {
		//			e.printStackTrace();
		//		} catch (MalformedQueryException e) {
		//			e.printStackTrace();
		//		}
		//
		//		if (aResult != null) 
		//			new SesameGraphResult2RDFGraph(aResult).extract(aGraph);
		throw new UnsupportedOperationException();
	}

	@Override
	public RDFGraph describeQuery(String query, String graph) {
		return describeQuery(query);
	}

	@Override
	public RDFGraph constructQuery(String query, String graph) {
		return constructQuery(query);
	}

	@Override
	public RDFGraph constructQuery(String query) {
		//		checkIsConnected();
		//
		//		GraphQueryResult aResult = null;
		//		try {
		//			aResult = connection.prepareGraphQuery(QueryLanguage.SPARQL, query)
		//			.evaluate();
		//		} catch (QueryEvaluationException e) {
		//			e.printStackTrace();
		//		} catch (RepositoryException e) {
		//			e.printStackTrace();
		//		} catch (MalformedQueryException e) {
		//			e.printStackTrace();
		//		}
		//
		//		return aResult != null ? new SesameGraphResult2RDFGraph(aResult).extract() : null;
		throw new UnsupportedOperationException();
	}

	@Override
	public void constructQuery(String aQuery, RDFGraph aGraph) {
		//		checkIsConnected();
		//
		//		GraphQueryResult aResult = null;
		//		try {
		//			aResult = connection.prepareGraphQuery(QueryLanguage.SPARQL, aQuery)
		//			.evaluate();
		//		} catch (QueryEvaluationException e) {
		//			e.printStackTrace();
		//		} catch (RepositoryException e) {
		//			e.printStackTrace();
		//		} catch (MalformedQueryException e) {
		//			e.printStackTrace();
		//		}
		//
		//		if (aResult != null) 
		//			new SesameGraphResult2RDFGraph(aResult).extract(aGraph);
		throw new UnsupportedOperationException();
	}

	@Override
	public IResultSet selectQuery(String query, String graph) {
		checkIsConnected();
		SPARQLParser parser = new SPARQLParser();
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
	public IResultSet selectQuery(String query) {
		checkIsConnected();
		SPARQLParser parser = new SPARQLParser();
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

	@Override
	public boolean containsGraph(String graph) {
		try {
			for (CloseableIteration<? extends Resource, SailException> res = connection.getContextIDs(); res.hasNext();) {
				Resource r = res.next();
				if (r.stringValue().equals(graph.toString()))
					return true;
			}
		} catch (SailException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public NamedGraph getNamedGraph(String graphURI) {
		return null;
	}

	@Override
	public Iterable<String> getNamedGraphs() {
		final List<String> list = new ArrayList<String>();
		try {
			for (CloseableIteration<? extends Resource, SailException> res = connection.getContextIDs(); res.hasNext();) {
				Resource r = res.next();
				if (r != null)
					list.add(r.stringValue());
			}
		} catch (SailException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void rollback() {
		checkIsConnected();

		try {
			connection.rollback();
		} catch (SailException e) {
			e.printStackTrace();
		}
	}

	private final void checkIsConnected() {
		try {
			if (connection == null || !connection.isOpen() || !isConnected()) {
				connect();
			}
		} catch (SailException e) {
			e.printStackTrace();
		}
	}
}
