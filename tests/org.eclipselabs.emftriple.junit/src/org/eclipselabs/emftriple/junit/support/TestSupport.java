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
package org.eclipselabs.emftriple.junit.support;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * @author ghillairet
 *
 */
public interface TestSupport {

	void init();
	
	Resource createResource(String query);
	
	void saveObject(EObject object);
	
	void saveObject(EObject object, URI uri);
	
	boolean existsInDataStore(String resourceURI);
	
	URI createObjectURI(String objectURI, String graphURI);
	
	boolean dataStoreIsEmpty();
	
	void populate() throws IOException;
	
	void populateWithBNode() throws IOException;
	
	ResourceSet getResourceSet();
	
	URI createBaseURI();

	/**
	 * @param target
	 */
	void checkObject(EObject expected);

	/**
	 * @return number of subjects (objects) in the data store.
	 */
	long dataStoreSize();
}
