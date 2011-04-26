package com.emftriple.jena;

import org.eclipse.emf.common.util.URI;

import com.emf4sw.rdf.NamedGraph;
import com.emf4sw.rdf.RDFGraph;
import com.emf4sw.rdf.jena.NamedGraphInjector;
import com.emftriple.datasources.IMutableNamedGraphDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.jena.util.JenaResultSet;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.shared.Lock;

public abstract class ModelNamedGraphDataSource extends ModelDataSource implements IMutableNamedGraphDataSource {

	ModelNamedGraphDataSource(String name) {
		super(name);
	}

	@Override
	public abstract NamedGraph getNamedGraph(URI graphURI);

	@Override
	public abstract Iterable<String> getNamedGraphs();

	@Override
	public abstract boolean containsGraph(URI graph);

	@Override
	public IResultSet selectQuery(String query, URI graphURI) {
		IResultSet rs = null;
		final Model model = getModel(graphURI);
		
		model.enterCriticalSection(Lock.READ);
		try {
			final QueryExecution qexec = QueryExecutionFactory.create(query, getModel());
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
	public RDFGraph constructQuery(String query, URI graphURI) {
		RDFGraph res = null;
		final Model model = getModel(graphURI);
		
		model.enterCriticalSection(Lock.READ);
		try {
			final QueryExecution queryExec = QueryExecutionFactory.create( QueryFactory.create( query ), model );
			final Model result = queryExec.execConstruct();
			res = result == null ? null : new NamedGraphInjector(result).inject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			model.leaveCriticalSection();
		}
		
		return res;
	}

	@Override
	public RDFGraph describeQuery(String query, URI graphURI) {
		RDFGraph graph = null;
		final Model model = getModel(graphURI);
		
		model.enterCriticalSection(Lock.READ);
		try {
			QueryExecution qexec = QueryExecutionFactory.create(QueryFactory.create( query ), model);
			final Model result = qexec.execDescribe();
			graph = result == null ? null : new NamedGraphInjector(result).inject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			model.leaveCriticalSection();
		};
		
		return graph;
	}

	@Override
	public boolean askQuery(String query, URI graph) {
		boolean result = false;
		final Model model = getModel(graph);
		
		model.enterCriticalSection(Lock.READ);
		try {
			result = QueryExecutionFactory.create(QueryFactory.create(query), model).execAsk();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			model.leaveCriticalSection();
		}
		return result;
	}

	@Override
	public abstract void add(RDFGraph graph);

	@Override
	public abstract void remove(RDFGraph graph);

	@Override
	public abstract void deleteGraph(URI graph);

	@Override
	public abstract void add(NamedGraph graph);

	@Override
	public abstract void remove(NamedGraph graph);
	
}
