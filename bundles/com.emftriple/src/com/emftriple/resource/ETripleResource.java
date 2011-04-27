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
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;

import com.emf4sw.rdf.RDFFactory;
import com.emf4sw.rdf.RDFGraph;
import com.emf4sw.rdf.Triple;
import com.emf4sw.rdf.notify.ModelAdapterImpl;
import com.emf4sw.rdf.resource.RDFResource;
import com.emf4sw.rdf.resource.TTLResource;
import com.emftriple.ETriple;
import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.IDataSourceFactoryModule;
import com.emftriple.datasources.IMutableDataSource;
import com.emftriple.datasources.IMutableNamedGraphDataSource;
import com.emftriple.datasources.INamedGraphDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.transform.IGetObject;
import com.emftriple.transform.IMapping;
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
	
	public String getGraph() {
		return decodeQueryString(getURI().query()).get("graph");
	}

	@Override
	public void delete(Map<?, ?> options) throws IOException {
		if (dataSource instanceof IMutableDataSource) {
			final Map<String, String> queries = decodeQueryString(getURI().query());
			if (queries.containsKey("graph")) {
				((IMutableNamedGraphDataSource)dataSource).deleteGraph(queries.get("graph"));
			} else {
				((IMutableDataSource)dataSource).delete();
			}
		}
	}

	@Override
	public void save(Map<?, ?> options) throws IOException {
		if (!(dataSource instanceof IMutableDataSource)) {
			throw new IllegalStateException("Cannot save in a non mutable RDF Store");
		}
		final Map<String, String> queries = decodeQueryString(getURI().query());
		final IPutObject put = new PutObjectImpl(this);


		boolean inGraph = queries.containsKey("graph");
		if (inGraph && !(dataSource instanceof INamedGraphDataSource)) {
			throw new IllegalStateException("RDF Store does not support named graphs");
		}
		
		for (TreeIterator<EObject> it = getAllContents(); it.hasNext();){
			Iterable<Triple> triples = put.put(it.next());
			if (inGraph) {
				((IMutableNamedGraphDataSource)dataSource).add(triples, queries.get("graph"));
			} else {
				((IMutableDataSource)dataSource).add(triples);
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
			if (dataSource instanceof INamedGraphDataSource) {
				rs =((INamedGraphDataSource) dataSource).selectQuery(query, queries.get("graph"));
			} else { 
				throw new IllegalArgumentException("RDF Store does not support named graphs"); 
			}
		} else {
			rs = dataSource.selectQuery(query);
		}

		final IGetObject get = new GetEObjectImpl(this);
		for (;rs.hasNext();) {
			com.emf4sw.rdf.Resource res = rs.next().getResource("s");
			if (!primaryCache.hasKey(res.getURI())) {
				EClass eClass = getMapping().findEClassByRdfType(
						selectAllTypes(dataSource, res.getURI(), queries.get("graph")));
				
				if (eClass != null) {
					EObject object = get.get(eClass, URI.createURI(res.getURI()));
					if (object != null) {
						this.getContents().add(object);
						primaryCache.cache(res.getURI(), object);
					}
				}
			}
		}
	}

	private IMapping getMapping() {
		return ETriple.Registry.INSTANCE.getMapping();
	}

	@Override
	public EObject getEObject(String uriFragment) {
		EObject proxy = null;

		if (uriFragment != null && uriFragment.startsWith("uri=")) 
		{
			final URI key = getProxyKey(uriFragment);
			final IGetObject get = new GetEObjectImpl(this);

			if (primaryCache.hasKey(key.toString())) {
				proxy = primaryCache.getObjectByKey(key.toString());
				if (((InternalEObject)proxy).eIsProxy()) {
					proxy = get.getProxy(proxy, proxy.eClass(), key); 
				}
			} else {
				final EClass eClass = getMapping().findEClassByRdfType(
						selectAllTypes(dataSource, key.toString(), null));
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

			final String fieldValue = qryPart.substring(qryPart.indexOf('=') + 1);
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
