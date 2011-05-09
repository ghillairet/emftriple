package com.emftriple.jena;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.datasources.IResultSet.Solution;
import com.emftriple.resource.ETripleResource;
import com.emftriple.resource.ETripleResourceImpl;
import com.emftriple.transform.Metamodel;
import com.emftriple.util.SparqlQueries;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

public abstract class JenaResourceImpl 
	extends ETripleResourceImpl<Model, Statement, RDFNode, Resource, Literal> 
	implements ETripleResource<Model, Statement, RDFNode, Resource, Literal> {

	public JenaResourceImpl(URI uri) {
		super(uri);
	}

	@Override
	public abstract IDataSource<Model, Statement, RDFNode, Resource, Literal> getDataSource(Map<?, ?> options);

	@Override
	public abstract IDataSource<Model, Statement, RDFNode, Resource, Literal> getDataSource();

	@Override
	public EObject load(@SuppressWarnings("rawtypes") IDataSource dataSource, String uri, String graphURI) {
		EObject object;
		
		if (getPrimaryCache().hasKey(uri)) {
			object = getPrimaryCache().getObjectByKey(uri);
			if (((InternalEObject)object).eIsProxy()) {
				object = new JenaEObjectBuilder(this, dataSource).loadEObject(object, uri, graphURI);
			}
		} else {
			@SuppressWarnings("unchecked")
			EClass eClass = Metamodel.INSTANCE.getEClassByRdfType(
				SparqlQueries.selectAllTypes(dataSource, uri, graphURI));
			
			object = EcoreUtil.create(eClass);
			getPrimaryCache().cache(uri, object);
			object = new JenaEObjectBuilder(this, dataSource).loadEObject(object, uri, graphURI);
		}
		
		return object;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void save(final Collection<Statement> triples, final IDataSource dataSource, final String graphURI) {
		dataSource.add(triples, graphURI);
	}
	
	@Override
	protected Collection<Statement> getTriples(EObject object) {
		final URI key = this.getID(object);
		final Collection<Statement> triples = new JenaRdfBuilder().createTriples(object, key.toString(), 
				ModelFactory.createDefaultModel());
		
		return triples;
	}
	
	@Override
	protected Set<String> loadingContentFromResultSet(IResultSet<RDFNode, Resource, Literal> resultSet) {
		final Set<String> uris = new HashSet<String>();
		
		for (;resultSet.hasNext();) {
			Solution<RDFNode, Resource, Literal> s = resultSet.next();
			for (String var: s.getSolutionNames()) {
				if (s.isResource(var)) {
					Resource res = s.getResource(var);
					if (!uris.contains(res.getURI())) {
						uris.add(res.getURI());
					}
				}
			}
		}

		return uris;
	}

}
