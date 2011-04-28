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
import java.util.Map;
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
public class ETripleResourceImpl extends ResourceImpl implements ETripleResource {

	private final IDataSource dataSource;

	private final ETripleResourceCacheImpl primaryCache;

	public ETripleResourceImpl(URI uri, IDataSource dataSource) {
		super(uri);
		this.dataSource = dataSource;
		this.primaryCache = new ETripleResourceCacheImpl();
	}

	public ETripleResourceCacheImpl getPrimaryCache() {
		return primaryCache;
	}

	public IDataSource getDataSource() {
		return dataSource;
	}
	
	public String getGraph() {
		return decodeQueryString(getURI().query()).get("graph");
	}

	@Override
	public void delete(Map<?, ?> options) throws IOException {
		if (getDataSource() instanceof IMutableDataSource) {
			final Map<String, String> queries = decodeQueryString(getURI().query());
			if (queries.containsKey("graph")) {
				((IMutableNamedGraphDataSource)getDataSource()).deleteGraph(queries.get("graph"));
			} else {
				((IMutableDataSource)getDataSource()).delete();
			}
		}
	}

	@Override
	public void save(Map<?, ?> options) throws IOException {
		if (!(getDataSource() instanceof IMutableDataSource)) {
			throw new IllegalStateException("Cannot save in a non mutable RDF Store");
		}
		final Map<String, String> queries = decodeQueryString(getURI().query());
		final IPutObject put = new PutObjectImpl(this);


		boolean inGraph = queries.containsKey("graph");
		if (inGraph && !(getDataSource() instanceof INamedGraphDataSource)) {
			throw new IllegalStateException("RDF Store does not support named graphs");
		}
		
		for (TreeIterator<EObject> it = getAllContents(); it.hasNext();){
			Iterable<Triple> triples = put.put(it.next());
			if (inGraph) {
				((IMutableNamedGraphDataSource)getDataSource()).add(triples, queries.get("graph"));
			} else {
				((IMutableDataSource)getDataSource()).add(triples);
			}
		}
	}

	@Override
	public void load(Map<?, ?> options) throws IOException {
		final Map<String, String> queries = decodeQueryString(getURI().query());

		String query;
		if (queries.containsKey("query")) {
			query = queries.get("query").replaceAll("%20", " ").replaceAll("%23", "#"); 
		} else {
			query ="select ?s where { ?s ?p ?o }";
		}

		final IResultSet rs;
		if (queries.containsKey("graph")) {
			if (getDataSource() instanceof INamedGraphDataSource) {
				rs =((INamedGraphDataSource) getDataSource()).selectQuery(query, queries.get("graph"));
			} else { 
				throw new IllegalArgumentException("RDF Store does not support named graphs"); 
			}
		} else {
			rs = getDataSource().selectQuery(query);
		}

		final IGetObject get = new GetEObjectImpl(this);
		for (;rs.hasNext();) {
			com.emf4sw.rdf.Resource res = rs.next().getResource("s");
			if (!getPrimaryCache().hasKey(res.getURI())) {
				EClass eClass = getMapping().getEClassByRdfType(
						selectAllTypes(getDataSource(), res.getURI(), queries.get("graph")));
				
				if (eClass != null) {
					if (getPrimaryCache().hasKey(res.getURI())) {
						EObject object = getPrimaryCache().getObjectByKey(res.getURI());
						if (object == null) {
							object = get.get(eClass, res.getURI());
							if (object != null) {
								getPrimaryCache().cache(res.getURI(), object);
							}
						} else if (object.eIsProxy()) {
							object = get.resolveProxy(object, eClass, res.getURI());
						}
					} else {
						EObject object = get.get(eClass, res.getURI());
						if (object != null) {
							getPrimaryCache().cache(res.getURI(), object);
						}
					}
				}
			}
		}
	}

	private Metamodel getMapping() {
		return Metamodel.INSTANCE;
	}

	@Override
	public EObject getEObject(String uriFragment) {
		EObject proxy = null;

		if (uriFragment != null && uriFragment.startsWith("uri=")) 
		{
			final URI key = getProxyKey(uriFragment);
			final IGetObject get = new GetEObjectImpl(this);

			if (getPrimaryCache().hasKey(key.toString())) {
				proxy = getPrimaryCache().getObjectByKey(key.toString());
				if (((InternalEObject)proxy).eIsProxy()) {
					proxy = get.resolveProxy(proxy, proxy.eClass(), key.toString());
				}
			} else {
				final EClass eClass = getMapping().getEClassByRdfType(
						selectAllTypes(getDataSource(), key.toString(), null));
				if (eClass != null) {
					proxy = get.get(eClass, key.toString());
					getPrimaryCache().cache(key.toString(), proxy);
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
			final String fieldName = qryPart.substring(0, qryPart.indexOf('='));

			final String fieldValue = qryPart.substring(qryPart.indexOf('=') + 1);
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
	
}
