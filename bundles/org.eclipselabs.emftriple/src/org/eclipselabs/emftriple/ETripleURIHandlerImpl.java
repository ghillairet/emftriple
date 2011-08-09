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
package org.eclipselabs.emftriple;

import static org.eclipselabs.emftriple.internal.util.ETripleEcoreUtil.decodeQueryString;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;
import org.eclipselabs.emftriple.datasources.IDataSource;

/**
 * Handles request.
 * 
 * examples URIs:
 *  - store_handler_scheme://store_name?uri=http://www.example.org/persons/1
 *  - store_handler_scheme://store_name?graph=http://ex.org/data/1
 *  - store_handler_scheme://store_name?uri=http://www.example.org/persons/1&graph=http://ex.org/data/1
 *  
 * @author ghillairet
 * @since 0.9.0
 */
public abstract class ETripleURIHandlerImpl extends URIHandlerImpl {
	
	public ETripleURIHandlerImpl() {}
	
	public String getStoreName(URI uri) {
		if (uri.hasAuthority()) {
			return uri.authority();
		}
		return null;
	}
	
	protected abstract IDataSource<?, ?> getDataSource(URI uri);
	
	@Override
	public void delete(URI uri, Map<?, ?> options) throws IOException {
		final IDataSource<?,?> dataSource = getDataSource(uri);
		final Map<String, String> queries = decodeQueryString(uri.query());
		
		if (dataSource != null) {
			dataSource.delete(queries.get("graph"));
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.impl.URIHandlerImpl#exists(org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	@Override
	public boolean exists(URI uri, Map<?, ?> options) {
		final IDataSource<?,?> dataSource = getDataSource(uri);
		if (dataSource == null) {
			return false;
		}
		
		final Map<String, String> queries = decodeQueryString(uri.query());
		
		if (queries.containsKey("uri")) {
			return dataSource.askQuery("ask { <"+queries.get("uri")+"> ?p ?o }", queries.get("graph"));
		} else if (queries.containsKey("graph")) {
			return dataSource.askQuery("ask { graph <"+queries.get("graph")+"> { ?s ?p ?o } }", queries.get("graph"));
		}
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.impl.URIHandlerImpl#contentDescription(org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	@Override
	public Map<String, ?> contentDescription(URI uri, Map<?, ?> options) throws IOException {
		return decodeQueryString(uri.toString());
	}
}
