/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple.datasources;

import java.util.Map;


/**
 * The {@link IDataSourceFactory} interface contains necessary methods to create {@link IDataSource} according 
 * to a given configuration. This interface is supposed to be implemented by data source adapters. This interface 
 * is called during {@link EntityManagerFactory} initialisation to create the set of data sources associated to the 
 * persistence unit. 
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.5.5
 */
public interface IDataSourceFactory {

	/**
	 * Returns True if the {@link IDataSourceFactory} can create a {@link IDataSource} with the given options.
	 * 
	 * @param descriptor
	 * @return true if can create
	 */
	boolean canCreate(Map<String, Object> options);
	
	/**
	 * Returns a newly created {@link IDataSource} instance corresponding to the given options.
	 * 
	 * @param options
	 * 
	 * @return the data source
	 * 
	 * @throws DataSourceException
	 */
	IDataSource create(Map<String, Object> options);

}
