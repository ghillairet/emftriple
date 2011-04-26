/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple.resource;

import static com.emftriple.util.SparqlQueries.selectAllTypes;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;

import com.emf4sw.rdf.RDFFactory;
import com.emf4sw.rdf.RDFGraph;
import com.emf4sw.rdf.notify.ModelAdapterImpl;
import com.emf4sw.rdf.resource.RDFResource;
import com.emf4sw.rdf.resource.TTLResource;
import com.emftriple.ETriple;
import com.emftriple.cache.ETripleResourceCacheImpl;
import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.IDataSourceFactoryModule;
import com.emftriple.datasources.IMutableNamedGraphDataSource;
import com.emftriple.datasources.INamedGraphDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.transform.IGetObject;
import com.emftriple.transform.IPutObject;
import com.emftriple.transform.impl.GetEObjectImpl;
import com.emftriple.transform.impl.PutObjectImpl;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class ETripleResource extends ResourceImpl implements Resource {

	private final IDataSource dataSource;

	private final ETripleResourceCacheImpl primaryCache;

	//	private ETripleQuery find;

	public ETripleResource(URI uri, IDataSource dataSource) {
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

	@Override
	public void delete(Map<?, ?> options) throws IOException {
		super.delete(options);
	}

	@Override
	public void save(Map<?, ?> options) throws IOException {
		final Map<String, String> queries = decodeQueryString(getURI().query());
		if (queries.containsKey("graph")) {
			final IPutObject put = new PutObjectImpl(ETriple.mapping);
			final RDFGraph graph = getGraph(queries.get("graph"));

			for (TreeIterator<EObject> it = getAllContents(); it.hasNext();){
				EObject obj = it.next();
				put.put(obj, graph);
			}
			((IMutableNamedGraphDataSource)dataSource).add(graph);
		}
	}

	@Override
	public void load(Map<?, ?> options) throws IOException {
		final Map<String, String> queries = decodeQueryString(getURI().query());
		String query;
		if (queries.containsKey("query")) {
			query = queries.get("query"); 
		} else {
			query ="select ?s where { ?s ?p ?o }";
		}
		final IResultSet rs;
		if (queries.containsKey("graph")) {
			if (dataSource instanceof INamedGraphDataSource) {
				rs =((INamedGraphDataSource) dataSource).selectQuery(query, URI.createURI(queries.get("graph")));
			} else { 
				throw new IllegalArgumentException(); 
			}
		} else {
			rs = dataSource.selectQuery(query);
		}
		
		final IGetObject get = new GetEObjectImpl(this);
		for (;rs.hasNext();) {
			com.emf4sw.rdf.Resource res = rs.next().getResource("s");
			EClass eClass = ETriple.mapping.findEClassByRdfType(selectAllTypes(dataSource, res.getURI()));
			if (eClass != null) {
				EObject object = get.get(eClass, URI.createURI(res.getURI()));
				if (object != null) {
					this.getContents().add(object);
				}
			}
		}
	}

	@Override
	public EObject getEObject(String uriFragment) {
		EObject proxy = null;

		if (uriFragment != null && uriFragment.startsWith("uri=")) 
		{
			final URI key = getProxyKey(uriFragment);

			if (primaryCache.containsKey(key.toString())) {
				proxy = primaryCache.get(key);
			} else {
				final IGetObject get = new GetEObjectImpl(this);
				final EClass eClass = ETriple.mapping.findEClassByRdfType(selectAllTypes(dataSource, key.toString()));
				if (eClass != null) {
					proxy = get.get(eClass, key);
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
			final String fieldValue = URI.decode(qryPart.substring(qryPart.indexOf('=') + 1));
			result.put(fieldName, fieldValue);
		}

		return result;
	}

	public static class ETripleURIHandler extends URIHandlerImpl implements URIHandler {
		@Override
		public boolean canHandle(URI uri) {
			if (uri.scheme().equals("emftriple")) {
				return true;
			}
			return super.canHandle(uri);
		}
	}

	protected RDFGraph getGraph(String graphURI) {
		final RDFGraph graph;
		if (graphURI != null) {
			graph = RDFFactory.eINSTANCE.createDocumentGraph();
			graph.setURI(graphURI);
		} else {
			graph = RDFFactory.eINSTANCE.createDocumentGraph();
		}

		final RDFResource aResource = ETriple.inject(ETriple.get(IDataSourceFactoryModule.class)).getInstance(TTLResource.class);
		aResource.getContents().add(graph);
		graph.eAdapters().add(new ModelAdapterImpl());

		return graph;
	}
}
