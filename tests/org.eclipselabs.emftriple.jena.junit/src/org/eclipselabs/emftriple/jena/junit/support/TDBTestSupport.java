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
package org.eclipselabs.emftriple.jena.junit.support;

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
import org.eclipselabs.emftriple.jena.tdb.TDBURIHandlerImpl;
import org.eclipselabs.emftriple.junit.model.ModelPackage;
import org.eclipselabs.emftriple.junit.support.TestSupport;
import org.eclipselabs.emftriple.junit.support.TestSupportImpl;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import com.hp.hpl.jena.tdb.TDBFactory;

/**
 * @author ghillairet
 *
 */
public class TDBTestSupport extends TestSupportImpl implements TestSupport {
	
	protected String tdbFolder;

	public TDBTestSupport(String folder) {
		this.tdbFolder = folder;
	}
	
	public void init() {
		EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, ModelPackage.eINSTANCE);
		
		Map<String, Object> options = new HashMap<String, Object>();
		options.put(ETripleOptions.OPTION_DATASOURCE_LOCATION, tdbFolder);
		
		StoreOptionsRegistry.INSTANCE.put(tdbFolder, options);
		
		resourceSet = new ResourceSetImpl();
		resourceSet.getURIConverter().getURIHandlers().add(0, new TDBURIHandlerImpl());	
	}
	
	protected Dataset getDataSet() {
		return TDBFactory.createDataset(tdbFolder);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupport#existsInDataStore(java.lang.String)
	 */
	@Override
	public boolean existsInDataStore(String resourceURI) {
		Dataset ds = getDataSet();
		return ds.getDefaultModel().containsResource(new ResourceImpl(resourceURI));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupportImpl#createObjectURI(java.lang.String, java.lang.String)
	 */
	@Override
	public URI createObjectURI(String objectURI, String graphURI) {
		if (objectURI == null)
			throw new IllegalArgumentException();
		
		return URI.createURI("tdb://"+tdbFolder+"?uri="+objectURI+
					(graphURI == null ? "" : "&graph="+graphURI));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupport#dataStoreIsEmpty()
	 */
	@Override
	public boolean dataStoreIsEmpty() {
		Dataset ds = getDataSet();
		return ds.getDefaultModel().isEmpty();
	}
	
	/**
	 * 
	 */
	@Override
	public Resource createResource(String query) {
		URI uri = URI.createURI("tdb://"+tdbFolder+(query==null ? "" : "?"+query));
		
		return resourceSet.createResource(uri);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupport#createBaseURI()
	 */
	@Override
	public URI createBaseURI() {
		return URI.createURI("tdb://"+tdbFolder);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupportImpl#getObject(org.eclipse.emf.common.util.URI)
	 */
	@Override
	protected EObject getObject(URI key) {
		Resource resource = resourceSet.createResource(URI.createURI("tdb://"+tdbFolder+"?uri="+key));
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
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupportImpl#dataStoreSize()
	 */
	@Override
	public long dataStoreSize() {
		return getDataSet().getDefaultModel().size();
	}
}
