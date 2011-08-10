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
package org.eclipselabs.emftriple.sesame.junit.support;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipselabs.emftriple.ETripleOptions;
import org.eclipselabs.emftriple.StoreOptionsRegistry;
import org.eclipselabs.emftriple.junit.model.ModelPackage;
import org.eclipselabs.emftriple.junit.support.TestSupportImpl;
import org.eclipselabs.emftriple.sesame.nat.SesameNativeURIHandlerImpl;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;

/**
 * @author ghillairet
 *
 */
public class SesameNativeTestSupport extends TestSupportImpl {
	
	private Repository repository;
	private String storeName;
	
	/**
	 * 
	 */
	public SesameNativeTestSupport(String storeName, Repository repository) {
		this.repository = repository;
		this.storeName = storeName;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.junit.support.TestSupport#dataStoreIsEmpty()
	 */
	@Override
	public boolean dataStoreIsEmpty() {
		try {
			return repository.getConnection().isEmpty();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.junit.support.TestSupport#createBaseURI()
	 */
	@Override
	public URI createBaseURI() {
		return URI.createURI("native://"+storeName);
	}

	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.junit.support.TestSupportImpl#init()
	 */
	@Override
	public void init() {
		EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, ModelPackage.eINSTANCE);
		
		Map<String, Object> options = new HashMap<String, Object>();
		options.put(ETripleOptions.OPTION_DATASOURCE_OBJECT, repository);
		
		StoreOptionsRegistry.INSTANCE.put(storeName, options);
		
		resourceSet = new ResourceSetImpl();
		resourceSet.getURIConverter().getURIHandlers().add(0, new SesameNativeURIHandlerImpl());
	}

	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.junit.support.TestSupportImpl#createResource(java.lang.String)
	 */
	@Override
	public Resource createResource(String query) {
		URI uri = URI.createURI("native://"+storeName+(query==null ? "" : "?"+query));
		
		return resourceSet.createResource(uri);
	}

	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.junit.support.TestSupportImpl#existsInDataStore(java.lang.String)
	 */
	@Override
	public boolean existsInDataStore(String resourceURI) {
		ValueFactory factory = repository.getValueFactory();
		if (factory == null) {
			factory = new ValueFactoryImpl();
		}
		
		try {
			return repository.getConnection().getStatements(
					factory.createURI(resourceURI), null, null, false).hasNext();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.junit.support.TestSupportImpl#createObjectURI(java.lang.String, java.lang.String)
	 */
	@Override
	public URI createObjectURI(String objectURI, String graphURI) {
		if (objectURI == null)
			throw new IllegalArgumentException();
		
		return URI.createURI("native://"+storeName+"?uri="+objectURI+
					(graphURI == null ? "" : "&graph="+graphURI));
	}

	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.junit.support.TestSupportImpl#getObject(org.eclipse.emf.common.util.URI)
	 */
	@Override
	protected EObject getObject(URI key) {
		Resource resource = resourceSet.createResource(
				URI.createURI("native://"+storeName+"?uri="+key));
		try {
			resource.load(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertFalse(resource.getContents().isEmpty());
		assertTrue(resource.getContents().size() == 1);
		
		return resource.getContents().get(0);
	}

	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.junit.support.TestSupportImpl#dataStoreSize()
	 */
	@Override
	public long dataStoreSize() {
		try {
			return repository.getConnection().size();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return 0L;
	}

}
