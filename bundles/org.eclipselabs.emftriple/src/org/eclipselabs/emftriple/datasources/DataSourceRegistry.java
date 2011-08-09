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
package org.eclipselabs.emftriple.datasources;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipselabs.emftriple.datasources.IDataSource.Registry;


/**
 * Registry for {@link IDataSource}
 * 
 * The class {@link DataSourceRegistry} registers the currently used data sources.
 *  
 * @author guillaume hillairet
 * @since 0.8.0
 */
public class DataSourceRegistry implements Registry {

	private static final Map<URI, IDataSource<?,?>> map = new HashMap<URI, IDataSource<?,?>>();
	
	@Override
	public IDataSource<?,?> getDataSource(URI uri) {
		return map.get(uri);
	}

	@Override
	public void register(URI uri, IDataSource<?,?> dataSource) {
		map.put(uri, dataSource);
	}

}
