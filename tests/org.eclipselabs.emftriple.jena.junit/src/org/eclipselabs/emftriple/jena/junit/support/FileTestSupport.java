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
import org.eclipselabs.emftriple.jena.file.FileURIHandlerImpl;
import org.eclipselabs.emftriple.jena.file.FileUtil;
import org.eclipselabs.emftriple.junit.model.ModelPackage;
import org.eclipselabs.emftriple.junit.support.TestSupportImpl;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;

/**
 * @author ghillairet
 *
 */
public class FileTestSupport extends TestSupportImpl {
	
	protected String fileLocation;
	protected String format;
	protected String storeName = "file";
	
	/**
	 * @param fileLocation
	 * @param format
	 */
	public FileTestSupport(String fileLocation, String format) {
		this.fileLocation = fileLocation;
		this.format = format;
	}

	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupportImpl#createObjectURI(java.lang.String, java.lang.String)
	 */
	@Override
	public URI createObjectURI(String objectURI, String graphURI) {
		if (objectURI == null) 
			throw new IllegalArgumentException();
		
		return URI.createURI("rdf://"+storeName+"?uri="+objectURI);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupportImpl#createResource(java.lang.String)
	 */
	@Override
	public Resource createResource(String query) {
		return resourceSet.createResource(URI.createURI("rdf://"+storeName));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupport#dataStoreIsEmpty()
	 */
	@Override
	public boolean dataStoreIsEmpty() {
		Model model = FileUtil.getModel(fileLocation, format);
		
		return model.isEmpty();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupportImpl#existsInDataStore(java.lang.String)
	 */
	@Override
	public boolean existsInDataStore(String resourceURI) {
		Model model = FileUtil.getModel(fileLocation, format);
		
		return model.containsResource(new ResourceImpl(resourceURI));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupportImpl#init()
	 */
	@Override
	public void init() {
		EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, ModelPackage.eINSTANCE);
		
		Map<String, Object> options = new HashMap<String, Object>();
		options.put(ETripleOptions.OPTION_DATASOURCE_LOCATION, fileLocation);
		options.put(FileUtil.OPTION_RDF_FORMAT, format);
		
		StoreOptionsRegistry.INSTANCE.put(storeName, options);
		
		resourceSet = new ResourceSetImpl();
		resourceSet.getURIConverter().getURIHandlers().add(0, new FileURIHandlerImpl());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupport#createBaseURI()
	 */
	@Override
	public URI createBaseURI() {
		return URI.createURI("rdf://"+storeName);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.jena.junit.support.TestSupportImpl#getObject(org.eclipse.emf.common.util.URI)
	 */
	@Override
	protected EObject getObject(URI key) {
		Resource resource = resourceSet.createResource(URI.createURI("rdf://"+storeName+"?uri="+key));
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
		Model model = FileUtil.getModel(fileLocation, format);
		
		return model.size();
	}
}
