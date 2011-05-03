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

import static com.emftriple.util.SparqlQueries.selectAllTypes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

import com.emf4sw.rdf.Triple;
import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.IMutableDataSource;
import com.emftriple.datasources.IMutableNamedGraphDataSource;
import com.emftriple.datasources.INamedGraphDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.datasources.IResultSet.Solution;
import com.emftriple.transform.IGetObject;
import com.emftriple.transform.IPutObject;
import com.emftriple.transform.Metamodel;
import com.emftriple.transform.impl.GetEObjectImpl;
import com.emftriple.transform.impl.PutObjectImpl;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public abstract class ETripleResourceImpl extends ResourceImpl implements ETripleResource {

	private final ETripleResourceCacheImpl primaryCache;
	
	protected IDataSource dataSource;
	
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
		final IDataSource dataSource = options == null ? getDataSource() : getDataSource(options);
		
		if (dataSource instanceof IMutableDataSource) {
			dataSource.connect();
			final Map<String, String> queries = decodeQueryString(getURI().query());
			if (queries.containsKey("graph")) {
				((IMutableNamedGraphDataSource)dataSource).deleteGraph(queries.get("graph"));
			} else {
				((IMutableDataSource)dataSource).delete();
			}
			dataSource.disconnect();
		}
	}

	@Override
	public void save(Map<?, ?> options) throws IOException {
		final IDataSource dataSource = options == null ? getDataSource() : getDataSource(options);
		
		if (!(dataSource instanceof IMutableDataSource)) {
			throw new IllegalStateException("Cannot save in a non mutable RDF Store");
		}
		final Map<String, String> queries = decodeQueryString(getURI().query());
		final IPutObject put = new PutObjectImpl(this);

		boolean inGraph = queries.containsKey("graph");
		if (inGraph && !(dataSource instanceof INamedGraphDataSource)) {
			throw new IllegalStateException("RDF Store does not support named graphs");
		}

		final List<Triple> triples = new ArrayList<Triple>();
		for (TreeIterator<EObject> it = getAllContents(); it.hasNext();){
			triples.addAll(put.put(it.next()));
		}
		
		dataSource.connect();
		if (inGraph) {
			((IMutableNamedGraphDataSource)dataSource).add(triples, queries.get("graph"));
		} else {
			((IMutableDataSource)dataSource).add(triples);
		}
		dataSource.disconnect();
	}

	@Override
	public void load(Map<?, ?> options) throws IOException {
		final IDataSource dataSource = this.dataSource = options == null ? getDataSource() : getDataSource(options);
		dataSource.connect();
		
		final Map<String, String> queries = decodeQueryString(getURI().query());
		if (queries.containsKey("uri")) {
			loadOne(dataSource, queries.get("uri"), queries.get("graph"));
		} else {
			loadByQuery(dataSource, queries);
		}
		
		dataSource.disconnect();
	}

	private void loadByQuery(IDataSource dataSource, Map<String, String> queries) {
		final String query;
		if (queries.containsKey("query")) {
			query = queries.get("query").replaceAll("%20", " ").replaceAll("%23", "#"); 
		}
		else {
			query ="select ?s where { ?s ?p ?o }";
		}

		final IResultSet rs;
		if (queries.containsKey("graph")) {
			if (dataSource instanceof INamedGraphDataSource) {
				rs =((INamedGraphDataSource) dataSource).selectQuery(query, queries.get("graph"));
			} else { 
				throw new IllegalArgumentException("RDF Store does not support named graphs"); 
			}
		} else {
			rs = dataSource.selectQuery(query);
		}

		final Set<String> uris = loadingContentFromResultSet(rs);
		if (!uris.isEmpty()) {
			loadingEObjectsFromURIs(uris, queries.get("graph"), dataSource);
		}
	}

	private void loadOne(IDataSource dataSource, String uri, String graph) {
		IGetObject get = new GetEObjectImpl(this, dataSource);
		if (getPrimaryCache().hasKey(uri)) {
			EObject obj = getPrimaryCache().getObjectByKey(uri);
			if (obj.eIsProxy()) {
				get.resolveProxy(obj, obj.eClass(), uri);
			}
		} else {
			EClass eClass = getMapping().getEClassByRdfType(selectAllTypes(dataSource, uri, graph));
			if (eClass != null) {
				get.get(eClass, uri);
			}
		}
	}

	private Set<String> loadingContentFromResultSet(IResultSet resultSet) {
		final Set<String> uris = new HashSet<String>();
		for (;resultSet.hasNext();) {
			Solution s = resultSet.next();
			for (String var: resultSet.getVarNames()) {
				if (s.isResource(var)) {
					com.emf4sw.rdf.Resource res = s.getResource(var);
					if (!uris.contains(res.getURI())) {
						uris.add(res.getURI());
					}
				}
			}
		}

		return uris;
	}

	private void loadingEObjectsFromURIs(Set<String> uris, String graph, IDataSource dataSource) {
		for (String uri: uris) {
			loadOne(dataSource, uri, graph);
		}
	}

	private Metamodel getMapping() {
		return Metamodel.INSTANCE;
	}

	@Override
	public EObject getEObject(String uriFragment) {
		EObject proxy = null;

		if (dataSource == null) {
			dataSource = getDataSource();
		}
		
		if (uriFragment != null && uriFragment.startsWith("uri=")) 
		{
			final URI key = getProxyKey(uriFragment);
			final IGetObject get = new GetEObjectImpl(this, dataSource);

			if (getPrimaryCache().hasKey(key.toString())) {
				proxy = getPrimaryCache().getObjectByKey(key.toString());

				if (((InternalEObject)proxy).eIsProxy()) {
					proxy = get.resolveProxy(proxy, proxy.eClass(), key.toString());
				}
			} else {
				final EClass eClass = getMapping().getEClassByRdfType(selectAllTypes(dataSource, key.toString(), null));
				if (eClass != null) {
					proxy = get.get(eClass, key.toString());
				}
			}
			return proxy;
		}
		return null;
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

	public abstract IDataSource getDataSource(Map<?, ?> options);
	
	public abstract IDataSource getDataSource();
	
}
