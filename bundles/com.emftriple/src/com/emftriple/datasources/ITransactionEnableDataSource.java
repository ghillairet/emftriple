/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple.datasources;

/**
 * The {@link ITransactionEnableDataSource} interface represents a {@link IDataSource} that supports 
 * transactions.
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.5.5
 */
public interface ITransactionEnableDataSource extends IDataSource {

	/**
	 * Begin a transaction
	 * 
	 * @throws DataSourceException
	 */
	void begin();
	
	/**
	 * Commit changes to the data source
	 * 
	 * @throws DataSourceException
	 */
	void commit();
	
	/**
	 * Roll back changes 
	 * 
	 * @throws DataSourceException
	 */
	void rollback();
	
}
