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
package com.emftriple.resource;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import com.emftriple.datasources.IDataSource;

/**
 * 
 * 
 *
 */
public interface ETripleResource<G, T, N, U, L> extends Resource {
	
	/**
	 * Returns the {@link IDataSource} interface to the RDF repository.
	 */
	IDataSource<G, T, N, U, L> getDataSource(Map<?, ?> options);
	
	IDataSource<G, T, N, U, L> getDataSource();
	
	/**
	 * Returns the object ID.
	 * If the object has not been cached yet and object is contained by the resource, then an ID is created 
	 * and return.
	 * 
	 * @param object
	 * @return object's unique ID
	 */
	URI getID(EObject object);
	
//	void save(IDataSource<G, T, N, U, L> dataSource, EObject object, String graphURI);
	
//	EObject load(IDataSource<G, T, N, U, L> dataSource, String uri, String graphURI);
	
}
