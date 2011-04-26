package com.emftriple.query;

import java.util.Collection;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import com.emftriple.datasources.IDataSource;
import com.emftriple.resource.ETripleResource;
import com.emftriple.util.SparqlQueries;
import com.google.inject.internal.Lists;

public class ETripleQueryImpl implements ETripleQuery {

	private final ETripleResource resource;
	
	private final IDataSource dataSource;

	public ETripleQueryImpl(Resource resource) {
		this.resource = (ETripleResource)resource;
		this.dataSource = this.resource.getDataSource();
	}

	@Override
	public EObject node(String key) {
		if (resource.getPrimaryCache().containsKey(key)) {
			return resource.getPrimaryCache().get(key);
		}
		
//		SparqlQueries.selectAllTypes(dataSource, key);
		
//		EObject obj = dataSource.selectQuery(
//				SparqlQueries.selectObjectByClass(eClass, uri));
		
		return null;
	}

	@Override
	public EObject node(URI key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterable<EObject> select(EClass eClass) {
		return Lists.newArrayList();
	}
	
	public Iterable<EObject> all() {
		return Lists.newArrayList();
	}
	
	@Override
	public Collection<EObject> by(EClass eClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Collection<T> by(Class<T> eClass) {
		// TODO Auto-generated method stub
		return null;
	}
}
