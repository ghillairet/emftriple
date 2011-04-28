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
package com.emftriple.datasources;


/**
 * {@link ISparqlUpdateDataSource} extends {@link IDataSource} with support for SPARQL 1.1 Update Queries.
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */	
public interface ISparqlUpdateDataSource extends IMutableDataSource {

	/**
	 * Executes the update query.
	 * 
	 * @param query to execute
	 */
	void update(String query);
	
}
