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
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.IResultSet;

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

	public ETripleResourceCacheImpl getPrimaryCache() {
		return primaryCache;
	}

	public String getGraph() {
		return decodeQueryString(getURI().query()).get("graph");
	}

	@Override
	public void delete(Map<?, ?> options) throws IOException {
		final IDataSource<G, T, N, U, L> dataSource = options == null ? getDataSource() : getDataSource(options);

		if (dataSource.isMutable()) {
			dataSource.connect();
			final Map<String, String> queries = decodeQueryString(getURI().query());
			dataSource.delete(queries.get("graph"));
			dataSource.disconnect();
		}
	}

	@Override
	public void save(Map<?, ?> options) throws IOException {
//		long startTime = System.currentTimeMillis();

		final IDataSource<G, T, N, U, L> dataSource = options == null ? getDataSource() : getDataSource(options);

		if (!(dataSource.isMutable())) {
			throw new IllegalStateException("Cannot save in a non mutable RDF Store");
		}
		final Map<String, String> queries = decodeQueryString(getURI().query());

		boolean inGraph = queries.containsKey("graph");
		if (inGraph && !(dataSource.supportsNamedGraph())) {
			throw new IllegalStateException("RDF Store does not support named graphs");
		}

		for (TreeIterator<EObject> it = getAllContents(); it.hasNext();){
			save(dataSource, it.next(), queries.get("graph"));
		}

//		long endTime = System.currentTimeMillis();
//		System.out.println("Time to create " + getContents().size() + " objects: " + ((endTime - startTime) / 1000.0) + " sec");
	}

	@Override
	public void load(Map<?, ?> options) throws IOException {
		final IDataSource<G, T, N, U,L> dataSource = this.dataSource = options == null ? getDataSource() : getDataSource(options);

		final Map<String, String> queries = decodeQueryString(getURI().query());

		if (queries.containsKey("uri")) {
			EObject object = load(dataSource, queries.get("uri"), queries.get("graph"));

			if (object != null)
				getContents().add(object);

		} else {
			loadByQuery(dataSource, queries);
		}
	}

	@Override
	public EObject getEObject(String uriFragment) {
		if (dataSource == null) {
			dataSource = getDataSource();
		}

		if (uriFragment != null && uriFragment.startsWith("uri=")) 
		{
			final URI key = getProxyKey(uriFragment);
			return load(dataSource, key.toString(), getGraph());
		}
		return null;
	}

	private void loadByQuery(IDataSource<G, T, N, U, L> dataSource, Map<String, String> queries) {
		final String query;

		if (queries.containsKey("query")) {
			query = queries.get("query").replaceAll("%20", " ").replaceAll("%23", "#"); 
		}
		else {
			query ="select ?s where { ?s ?p ?o }";
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
			loadingEObjectsFromURIs(uris, queries.get("graph"), dataSource);
		}
	}

	protected abstract Set<String> loadingContentFromResultSet(IResultSet<N, U, L> resultSet);

	private void loadingEObjectsFromURIs(Set<String> uris, String graph, IDataSource<G, T, N, U, L> dataSource) {
		for (String uri: uris) {
			EObject object = load(dataSource, uri, graph);
			if (object != null) {
				getContents().add(object);
			}
		}
	}

	private URI getProxyKey(String uriFragment) {
		return URI.createURI(uriFragment.split("=")[1].replaceAll("%23", "#"));
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
		if (getPrimaryCache().hasObject(object)) {
			return URI.createURI(getPrimaryCache().getObjectId(object));
		}
		final String key = EObjectID.getId(object);
		getPrimaryCache().cache(key, object);

		return URI.createURI(key);
	}

	public abstract IDataSource<G, T, N, U, L> getDataSource(Map<?, ?> options);

	public abstract IDataSource<G, T, N, U, L> getDataSource();

	@Override
	public abstract EObject load(@SuppressWarnings("rawtypes") IDataSource dataSource, String uri, String graphURI);

	@Override
	public abstract void save(@SuppressWarnings("rawtypes") IDataSource dataSource, EObject object, String graphURI);
}
