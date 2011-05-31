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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.query.result.ListResult;
import com.emftriple.query.result.ResultFactory;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public abstract class ETripleResourceImpl<G, T, N, U, L> 
extends ResourceImpl 
implements ETripleResource<G, T, N, U, L> {

	protected final ETripleResourceCacheImpl primaryCache;

	protected IDataSource<G, T, N, U, L> dataSource;

	public ETripleResourceImpl(URI uri) {
		super(uri);
		this.primaryCache = new ETripleResourceCacheImpl();
	}

	@Override
	public void delete(Map<?, ?> options) throws IOException {
		if (dataSource == null) {
			dataSource = getByRegistryOrCreateDataSource(options);
		}

		if (dataSource.isMutable()) {
			dataSource.connect();
			final Map<String, String> queries = decodeQueryString(getURI().query());
			dataSource.delete(queries.get("graph"));
			getContents().clear();
			dataSource.disconnect();
		}
	}

	@Override
	public void save(Map<?, ?> options) throws IOException {
		if (dataSource == null) {
			dataSource = getByRegistryOrCreateDataSource(options);
		}

		if (!(dataSource.isMutable())) {
			throw new IllegalStateException("Cannot save in a non mutable RDF Store");
		}
		
		final Map<String, String> queries = decodeQueryString(getURI().query());

		boolean inGraph = queries.containsKey("graph");
		if (inGraph && !(dataSource.supportsNamedGraph())) {
			throw new IllegalStateException("RDF Store does not support named graphs");
		}
		
		final Collection<T> triples = new ArrayList<T>();;
		for (TreeIterator<EObject> it = getAllContents(); it.hasNext();) {
			EObject obj = it.next();
			if (!obj.eIsProxy()) {
				triples.addAll(getTriples(obj));
				((InternalEObject)obj).eSetProxyURI(URI.createURI(getURI()+"#uri="+getID(obj)));
			}
		}
		save(triples, dataSource, queries.get("graph"));
	}

	@Override
	public void load(Map<?, ?> options) throws IOException {
		if (dataSource == null) {
			dataSource = getByRegistryOrCreateDataSource(options);
		}
		
		final Map<String, String> queries = decodeQueryString(getURI().query());

		if (queries.containsKey("uri")) {
			dataSource.connect();
			
			EObject object = load(dataSource, queries.get("uri"), queries.get("graph"));
			
			if (object != null) {
				getContents().add(object);
			}
			dataSource.disconnect();
		} else {
			if (dataSource.isConnected()) {
				dataSource.disconnect();
			}
			dataSource.connect();
			loadByQuery(dataSource, queries);
			dataSource.disconnect();
		}
	}

	@Override
	public EObject getEObject(String uriFragment) {
		if (dataSource == null) {
			dataSource = getByRegistryOrCreateDataSource(null);
		}
		
		if (uriFragment != null) 
		{
			final String key = getProxyKey(uriFragment);
			if (key != null) {
				return load(dataSource, key, getGraph());
			}
		}
		return null;
	}

	private void loadByQuery(IDataSource<G, T, N, U, L> dataSource, Map<String, String> queries) {
		final String query;
		dataSource.connect();
		
		final boolean isQuery = queries.containsKey("query");
		
		if (isQuery) {
			query = queries.get("query").replaceAll("%20", " ").replaceAll("%23", "#"); 
		} else {
			if (queries.containsKey("graph")) {
				query = "select ?s where { graph <"+queries.get("graph")+"> { ?s ?p ?o } }";
			} else {
				query = "select ?s where { ?s ?p ?o }";
			}
		}

		final IResultSet<N, U, L> rs;
		if (queries.containsKey("graph")) {
			if (dataSource.supportsNamedGraph()) {
				rs = dataSource.selectQuery(query, queries.get("graph"));
			} else { 
				throw new IllegalArgumentException("RDF Store does not support named graphs"); 
			}
		} else {
			rs = dataSource.selectQuery(query, null);
		}

		final Set<String> uris = loadingContentFromResultSet(rs);

		if (!uris.isEmpty()) {
			if (isQuery) {
				loadingQueryResultFromURIs(uris, queries.get("graph"), dataSource);
			} else {
				loadingEObjectsFromURIs(uris, queries.get("graph"), dataSource);
			}
		}
		
		dataSource.disconnect();
	}

	protected abstract Set<String> loadingContentFromResultSet(IResultSet<N, U, L> resultSet);
	
	private void loadingQueryResultFromURIs(Set<String> uris, String graph, IDataSource<G, T, N, U, L> dataSource) {
		final ListResult result = ResultFactory.eINSTANCE.createListResult();
		
		for (String uri: uris) {
			EObject object = load(dataSource, uri, graph);
			if (object != null) {
				result.getResult().add(object);
			}
		}
		
		getContents().add(0, result);
	}
	
	private void loadingEObjectsFromURIs(Set<String> uris, String graph, IDataSource<G, T, N, U, L> dataSource) {
		for (String uri: uris) {
			EObject object = load(dataSource, uri, graph);
			if (object != null) {
				getContents().add(object);
			}
		}
	}

	private String getProxyKey(String uriFragment) {
		if (uriFragment.startsWith("uri=")) {
			return uriFragment.split("=")[1].replaceAll("%23", "#");
		} else  return null;
	}

	private Map<String, String> decodeQueryString(String qryStr) {
		final TreeMap<String, String> result = new TreeMap<String, String>();

		if (qryStr == null) {
			return result;
		}

		final String[] qryParts = qryStr.split("&");
		for (final String qryPart : qryParts) {
			final String fieldName = qryPart.substring(0, qryPart.indexOf('=')).trim();
			final String fieldValue = qryPart.substring(qryPart.indexOf('=') + 1).trim();
			result.put(fieldName, fieldValue);
		}

		return result;
	}

	@Override
	public URI getID(EObject object) {
		String key = null;
		if (getPrimaryCache().hasObject(object)) {
			key = getPrimaryCache().getObjectId(object);
		}
		
		if (key == null && object.eIsProxy()) {
			URI uri = ((InternalEObject)object).eProxyURI();
			if (uri.hasFragment()) {
				key = getProxyKey(uri.fragment());
			}
		}
		
		if (key == null) {
			key = EObjectID.getId(object);
			getPrimaryCache().cache(key, object);
		}
		
		return URI.createURI(key);
	}
	
	private IDataSource<G, T, N, U, L> getByRegistryOrCreateDataSource(Map<?, ?> options) {
		dataSource = IDataSource.Registry.INSTANCE.getDataSource(this.getURI().trimQuery());
		if (dataSource == null) {
			dataSource = options == null ? getDataSource() : getDataSource(options);
			IDataSource.Registry.INSTANCE.register(getURI().trimQuery(), dataSource);
		}
		return dataSource;
	}

	public String getGraph() {
		return decodeQueryString(getURI().query()).get("graph");
	}

	public ETripleResourceCacheImpl getPrimaryCache() {
		return primaryCache;
	}

	public abstract IDataSource<G, T, N, U, L> getDataSource(Map<?, ?> options);

	public abstract IDataSource<G, T, N, U, L> getDataSource();

	protected abstract EObject load(@SuppressWarnings("rawtypes") IDataSource dataSource, String uri, String graphURI);

	protected abstract void save(Collection<T> triples, @SuppressWarnings("rawtypes") IDataSource dataSource, String graphURI);
	
	protected abstract Collection<T> getTriples(EObject object);
}
