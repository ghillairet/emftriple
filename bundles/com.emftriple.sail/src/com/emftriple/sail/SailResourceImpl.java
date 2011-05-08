package com.emftriple.sail;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.openrdf.model.Graph;
import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.sail.Sail;

import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.datasources.IResultSet.Solution;
import com.emftriple.resource.ETripleResourceImpl;
import com.emftriple.transform.Metamodel;
import com.emftriple.util.ETripleOptions;
import com.emftriple.util.SparqlQueries;

public class SailResourceImpl 
	extends ETripleResourceImpl<Graph, Statement, Value, URI, Literal>{

	public SailResourceImpl(org.eclipse.emf.common.util.URI uri) {
		super(uri);
	}

	@Override
	protected Set<String> loadingContentFromResultSet(IResultSet<Value, URI, Literal> resultSet) {
		final Set<String> uris = new HashSet<String>();
		
		for (;resultSet.hasNext();) {
			Solution<Value, URI, Literal> s = resultSet.next();
			for (String var: s.getSolutionNames()) {
				if (s.isResource(var)) {
					URI res = s.getResource(var);
					if (!uris.contains(res.stringValue())) {
						uris.add(res.stringValue());
					}
				}
			}
		}

		return uris;
	}

	@Override
	public IDataSource<Graph, Statement, Value, URI, Literal> getDataSource(Map<?, ?> options) {
		SailDataSource dataSource = null;
		if (options.containsKey(ETripleOptions.OPTION_DATASOURCE_OBJECT)) {
			dataSource = new SailDataSource((Sail) options.get(ETripleOptions.OPTION_DATASOURCE_OBJECT));
		}
		return dataSource;
	}

	@Override
	public IDataSource<Graph, Statement, Value, URI, Literal> getDataSource() {
		return getDataSource(getResourceSet().getLoadOptions());
	}

	@Override
	public EObject load(@SuppressWarnings("rawtypes") IDataSource dataSource, String uri, String graphURI) {
EObject object;
		
		if (getPrimaryCache().hasKey(uri)) {
			object = getPrimaryCache().getObjectByKey(uri);
			if (((InternalEObject)object).eIsProxy()) {
				object = new SailEObjectBuilder(this, dataSource).loadEObject(object, uri, graphURI);
			}
		} else {
			@SuppressWarnings("unchecked")
			EClass eClass = Metamodel.INSTANCE.getEClassByRdfType(
				SparqlQueries.selectAllTypes(dataSource, uri, graphURI));
			
			object = EcoreUtil.create(eClass);
			getPrimaryCache().cache(uri, object);
			object = new SailEObjectBuilder(this, dataSource).loadEObject(object, uri, graphURI);
		}
		
		return object;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void save(@SuppressWarnings("rawtypes") IDataSource dataSource, EObject object, String graphURI) {
		final org.eclipse.emf.common.util.URI key = this.getID(object);
		final Collection<Statement> triples = new SailRdfBuilder().createTriples(object, key.toString(), 
				(Graph) dataSource.getGraph(graphURI));
		
		dataSource.add(triples, graphURI);
	}
	
	
}
