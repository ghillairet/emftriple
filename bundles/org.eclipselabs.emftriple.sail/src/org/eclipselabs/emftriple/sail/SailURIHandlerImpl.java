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
package org.eclipselabs.emftriple.sail;

import static org.eclipselabs.emftriple.internal.util.ETripleEcoreUtil.decodeQueryString;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipselabs.emftriple.ETripleOptions;
import org.eclipselabs.emftriple.ETripleURIHandlerImpl;
import org.eclipselabs.emftriple.StoreOptionsRegistry;
import org.eclipselabs.emftriple.datasources.IDataSource;
import org.openrdf.sail.Sail;

/**
 * 
 * @author ghillairet
 * @since 0.9.0
 */
public class SailURIHandlerImpl 
	extends ETripleURIHandlerImpl {
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.impl.URIHandlerImpl#canHandle(org.eclipse.emf.common.util.URI)
	 */
	@Override
	public boolean canHandle(URI uri) {
		return "sail".equalsIgnoreCase(uri.scheme());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.impl.URIHandlerImpl#createInputStream(org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	@Override
	public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException {
		final IDataSource<?,?> dataSource = getDataSource(uri);
		if (dataSource == null) {
			throw new IOException();
		}
		return new SailInputStream(uri, options, dataSource);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.impl.URIHandlerImpl#createOutputStream(org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	@Override
	public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException {
		final IDataSource<?,?> dataSource = getDataSource(uri);
		if (dataSource == null) {
			throw new IOException();
		}
		return new SailOutputStream(uri, options, dataSource);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.ETripleURIHandlerImpl#getDataSource(org.eclipse.emf.common.util.URI)
	 */
	@Override
	protected IDataSource<?, ?> getDataSource(org.eclipse.emf.common.util.URI uri) {
		Map<String, Object> options = StoreOptionsRegistry.INSTANCE.get(getStoreName(uri));
		SailDataSource dataSource = null;
		
		if (options.containsKey(ETripleOptions.OPTION_DATASOURCE_OBJECT)) {
			
			Object obj = options.get(ETripleOptions.OPTION_DATASOURCE_OBJECT);
			
			if (obj instanceof Sail) {
				Sail sail = (Sail) obj;
				
				if (!mapOfDataSources.containsKey(sail)) {
					dataSource = new SailDataSource(sail);
					mapOfDataSources.put(sail, dataSource);
				} else {
					dataSource = (SailDataSource) mapOfDataSources.get(sail);
				}
			}
		}
		
		return dataSource;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.ETripleURIHandlerImpl#exists(org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	@Override
	public boolean exists(URI uri, Map<?, ?> options) {
		final IDataSource<?,?> dataSource = getDataSource(uri);
		if (dataSource == null) {
			return false;
		}
		
		final Map<String, String> queries = decodeQueryString(uri.query());
		
		if (queries.containsKey("uri")) {
			return dataSource.contains(queries.get("uri")); 
		}
		
		return true;
	}
	
}
