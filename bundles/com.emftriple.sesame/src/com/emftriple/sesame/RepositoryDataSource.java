package com.emftriple.sesame;

import org.openrdf.model.Graph;
import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.GraphImpl;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import com.emftriple.datasources.AbstractDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.sail.util.SesameResultSet;

public class RepositoryDataSource 
extends AbstractDataSource<Graph, Statement, Value, URI, Literal> {

	protected RepositoryConnection connection;

	protected final Repository repository;

	protected RepositoryDataSource(Repository repository) {
		this.repository = repository;
		try {
			repository.initialize();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		try {
			connection = repository.getConnection();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		try {
			connection.setAutoCommit(true);
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void add(Iterable<Statement> triples, String namedGraphURI) {
		connect();

		try {
			if (namedGraphURI == null) {
				connection.add(triples);
			} else {
				connection.add(triples, new ValueFactoryImpl().createURI(namedGraphURI));
			}
		} catch (RepositoryException e) {
			try {
				connection.rollback();
			} catch (RepositoryException re) {
				re.printStackTrace();
			}
		}
	}

	@Override
	public void remove(Iterable<Statement> triples, String namedGraphURI) {
		connect();

		try {
			if (namedGraphURI == null) {
				connection.remove(triples);
			} else {
				connection.remove(triples, new ValueFactoryImpl().createURI(namedGraphURI));
			}
		} catch (RepositoryException e) {
			try {
				connection.rollback();
			} catch (RepositoryException re) {
				re.printStackTrace();
			}
		}
	}

	@Override
	public void delete(String graphURI) {
		connect();
		
		if (graphURI == null) {
			try {
				connection.clear();
			} catch (RepositoryException e) {
				e.printStackTrace();
			}
		} else {
			try {
				connection.clear(new ValueFactoryImpl().createURI(graphURI));
			} catch (RepositoryException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Graph getGraph(String graphURI) {
		return constructQuery("construct { ?s ?p ?o } where { ?s ?p ?o }", graphURI);
	}

	@Override
	public void connect() {
		if (!isConnected()) {
			setConnected(true);
			try {
				if (!repository.isWritable()) {
					repository.shutDown();
				}
//				repository.initialize();
				connection = repository.getConnection();
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
//			repository.shutDown();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean askQuery(String query, String graphURI) {
		connect();

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
	public Graph constructQuery(String query, String graphURI) {
		connect();

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

		Graph g = new GraphImpl();
		try {
			for(;aResult.hasNext();)
				g.add(aResult.next());
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		}

		return g;
	}

	@Override
	public void constructQuery(String query, String graphURI, Graph aGraph) {
		connect();

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

		try {
			for(;aResult.hasNext();)
				aGraph.add(aResult.next());
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void describeQuery(String query, String graphURI, Graph aGraph) {
		connect();

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

		try {
			for(;aResult.hasNext();)
				aGraph.add(aResult.next());
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Graph describeQuery(String query, String graph) {
		connect();

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

		Graph g = new GraphImpl();
		try {
			for(;aResult.hasNext();)
				g.add(aResult.next());
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		}

		return g;
	}

	@Override
	public IResultSet<Value, URI, Literal> selectQuery(String query, String graph) {
		connect();
		
		IResultSet<Value, URI, Literal> aResult = null;
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
	public boolean supportsTransaction() {
		return true;
	}

//	private final void checkIsConnected() {
//		try {
//			if (connection == null || !connection.isOpen() || !isConnected()) {
//				connect();
//			}
//		} catch (RepositoryException e) {
//			e.printStackTrace();
//		}
//	}

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

}
