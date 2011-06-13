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
package com.emftriple.query;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Query interface for sparql queries.
 * 
 * @author guillaume hillairet
 * @since 0.8.0
 */
public interface Query {
	
	/**
	 * Returns a URI containing the query. The parameter is the {@link Resource} URI to 
	 * which the query will be associated.
	 * 
	 * @param resourceURI
	 * @return URI with current Query as query parameter.
	 */
	URI toURI(URI resourceURI);
	
	/**
	 * Returns String representation of the query.
	 */
	String get();
	
}