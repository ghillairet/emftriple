/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple.sesame;

import java.util.List;

import org.openrdf.model.Graph;
import org.openrdf.model.Resource;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;

import com.emf4sw.rdf.NamedGraph;
import com.emf4sw.rdf.RDFGraph;
import com.emf4sw.rdf.Triple;
import com.emf4sw.rdf.sesame.RDFGraph2SesameGraph;
import com.emftriple.datasources.IMutableNamedGraphDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.datasources.ITransactionEnableDataSource;
import com.emftriple.datasources.impl.AbstractNamedGraphDataSource;
import com.emftriple.sesame.util.SesameGraphResult2RDFGraph;
import com.emftriple.sesame.util.SesameResultSet;
import com.google.common.collect.Lists;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public abstract class SailDataSource extends AbstractNamedGraphDataSource implements IMutableNamedGraphDataSource, ITransactionEnableDataSource {

	protected RepositoryConnection connection;

	protected final Repository repository;

	protected SailDataSource(String name, Repository repository) {
		super(name);
		this.repository = repository;
	}
	
	@Override
	public void delete() {
		try {
			repository.getConnection().clear();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void add(Iterable<Triple> triples) {
		checkIsConnected();

		final Graph aGraph = RDFGraph2SesameGraph.extract(triples);
		try {
			connection.add(aGraph);
		} catch (RepositoryException e) {
			try {
				connection.rollback();
			} catch (RepositoryException re) {
				re.printStackTrace();
			}
		}
	}
	
	@Override
	public void add(Iterable<Triple> triples, String namedGraphURI) {
		// TODO Auto-generated method stub
		
	}
		
	@Override
	public void remove(Iterable<Triple> triples) {
		checkIsConnected();

		Graph aGraph = RDFGraph2SesameGraph.extract(triples);
		try {
			connection.remove(aGraph);
		} catch (RepositoryException e) {
			try {
				connection.rollback();
			} catch (RepositoryException re) {
				re.printStackTrace();
			}
		}	
	}
	
	@Override
	public void remove(Iterable<Triple> triples, String namedGraphURI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RDFGraph constructQuery(String query, String graph) {
		return constructQuery(query);
	}

	@Override
	public RDFGraph constructQuery(String query) {
		checkIsConnected();

		GraphQueryResult aResult = null;
		try {
			aResult = connection.prepareGraphQuery(QueryLanguage.SPARQL, query)
			.evaluate();
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		}

		return aResult != null ? new SesameGraphResult2RDFGraph(aResult).extract() : null;
	}

	@Override
	public void constructQuery(String aQuery, RDFGraph aGraph) {
		checkIsConnected();

		GraphQueryResult aResult = null;
		try {
			aResult = connection.prepareGraphQuery(QueryLanguage.SPARQL, aQuery)
			.evaluate();
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		}

		if (aResult != null) 
			new SesameGraphResult2RDFGraph(aResult).extract(aGraph);
	}
	
	@Override
	public IResultSet selectQuery(String query, String graph) {
		checkIsConnected();

		IResultSet aResult = null;
		try {
			TupleQuery aQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, query);
			aResult = new SesameResultSet(aQuery.evaluate());
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		}
		return aResult;
	}

	@Override
	public IResultSet selectQuery(String query) {
		checkIsConnected();

		IResultSet aResult = null;
		try {
			TupleQuery aQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, query);
			aResult = new SesameResultSet( aQuery.evaluate() );
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		}
		finally {

		}
		return aResult;
	}

	@Override
	public RDFGraph describeQuery(String query) {
		checkIsConnected();

		GraphQueryResult aResult = null;	
		try {
			aResult = connection.prepareGraphQuery(QueryLanguage.SPARQL, query)
			.evaluate();
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		}

		return aResult != null ? new SesameGraphResult2RDFGraph(aResult).extract() : null;
	}

	@Override
	public void describeQuery(String aQuery, RDFGraph aGraph) {
		checkIsConnected();

		GraphQueryResult aResult = null;	
		try {
			aResult = connection.prepareGraphQuery(QueryLanguage.SPARQL, aQuery)
			.evaluate();
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		}

		if (aResult != null) 
			new SesameGraphResult2RDFGraph(aResult).extract(aGraph);
	}
	
	@Override
	public RDFGraph describeQuery(String query, String graph) {
		return describeQuery(query);
	}

	@Override
	public boolean askQuery(String query, String graph) {
		return askQuery(query);
	}

	@Override
	public boolean askQuery(String query) {
		checkIsConnected();

		try {
			return connection.prepareBooleanQuery(QueryLanguage.SPARQL, query).evaluate();
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean supportsTransaction() {
		return true;
	}
	
	@Override
	public boolean containsGraph(String graph) {
		try {
			for (RepositoryResult<Resource> res = repository.getConnection().getContextIDs(); res.hasNext();) {
				Resource r = res.next();
				if (r.stringValue().equals(graph.toString()))
					return true;
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
			
		return false;
	}

	@Override
	public void deleteGraph(String graph) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public NamedGraph getNamedGraph(String graphURI) {
		return null;
	}
	
	@Override
	public Iterable<String> getNamedGraphs() {
		List<String> list = Lists.newArrayList();
		try {
			for (RepositoryResult<Resource> res = repository.getConnection().getContextIDs(); res.hasNext();) {
				Resource r = res.next();
				list.add(r.stringValue());
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void begin() {
		checkIsConnected();
	}

	@Override
	public void commit() {
		checkIsConnected();

		try {
			connection.commit();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void rollback() {
		checkIsConnected();

		try {
			connection.rollback();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connect() {
		if (!isConnected()) {
			setConnected(true);
			try {
				connection = repository.getConnection();
				connection.setAutoCommit(true);
			} catch (RepositoryException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void disconnect() {
		setConnected(false);
		try {
			connection.close();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}

	private final void checkIsConnected() {
		try {
			if (!(connection != null && connection.isOpen() && isConnected()))
				throw new IllegalStateException();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}
}
