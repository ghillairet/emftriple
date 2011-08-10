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
package org.eclipselabs.emftriple.internal;

import static org.eclipselabs.emftriple.internal.SparqlQueries.selectAllTypes;
import static org.eclipselabs.emftriple.internal.SparqlQueries.selectBlankNodeObject;
import static org.eclipselabs.emftriple.internal.SparqlQueries.selectObjectByClass;
import static org.eclipselabs.emftriple.internal.util.ETripleEcoreUtil.decodeQueryString;
import static org.eclipselabs.emftriple.internal.util.ETripleEcoreUtil.isBlankNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipselabs.emftriple.datasources.IDataSource;
import org.eclipselabs.emftriple.datasources.IResultSet;
import org.eclipselabs.emftriple.datasources.IResultSet.Solution;
import org.eclipselabs.emftriple.internal.util.ETripleEcoreUtil;
import org.eclipselabs.emftriple.query.result.ListResult;
import org.eclipselabs.emftriple.query.result.ResultFactory;
import org.eclipselabs.emftriple.vocabularies.RDF;

/**
 * The {@link ETripleInputStream} abstract class provides methods from creating EObject from RDF. This class needs to
 * be extended to fulfill specific RDF APIs. 
 * 
 * @author ghillairet
 * @since 0.9.0
 */
public abstract class ETripleInputStream 
	extends InputStream
	implements URIConverter.Loadable {

	protected final IDataSource<?,?> dataSource;
	protected final Map<?, ?> options;	
	protected final URI uri;
	protected EObjectBuilder<?, ?> builder;

	public ETripleInputStream(URI uri, Map<?, ?> options, IDataSource<?,?> dataSource) {
		this.uri = uri;
		this.options = options;
		this.dataSource = dataSource;
	}

	@Override
	public void loadResource(Resource resource) throws IOException {
		final Map<String, String> queries = decodeQueryString(uri.query());

		if (queries.containsKey("uri")) {

			dataSource.connect();
			EObject obj = load(dataSource, queries.get("uri"), queries.get("graph"), resource);
			if (obj != null) {
				resource.getContents().add(obj);
			}
			dataSource.disconnect();

		} else {
			
			dataSource.connect();
			Set<String> uris = loadByQuery(dataSource, queries);
			if (!uris.isEmpty()) {
				if (queries.containsKey("query")) {
					loadingQueryResultFromURIs(uris, queries.get("graph"), dataSource, resource);
				} else {
					loadingEObjectsFromURIs(uris, queries.get("graph"), dataSource, resource);
				}
			}
			dataSource.disconnect();

		}
	}

	protected EObject load(IDataSource<?,?> dataSource, String uri, String graphURI, Resource resource) {
		EObject object = null;

		ResourceSet resourceSet = resource.getResourceSet();

		URI objectURI = URI.createURI(resource.getURI().trimFragment().trimQuery()+"#"+uri);
		
		object = resourceSet.getEObject(objectURI, false);
		
		if (object == null) {
			EClass eClass = Metamodel.INSTANCE.getEClassByRdfType(selectAllTypes(dataSource, uri, graphURI));
			if (eClass != null) {
				object = EcoreUtil.create(eClass);
				object = builder.loadEObject(object, uri, graphURI, false);
			}

			if (resource instanceof ResourceImpl) {
				if (((ResourceImpl)resource).getIntrinsicIDToEObjectMap() == null) {
					((ResourceImpl)resource).setIntrinsicIDToEObjectMap(new HashMap<String, EObject>());
				}
				((ResourceImpl)resource).getIntrinsicIDToEObjectMap().put(uri, object);
			}
		}
		
		if (((InternalEObject)object).eIsProxy()) {
			object = builder.loadEObject(object, uri, graphURI, false);
			((InternalEObject)object).eSetProxyURI(null);
		}

		return object;
	}

	protected Set<String> loadByQuery(IDataSource<?,?> dataSource, Map<String, String> queries) {
		final String query;

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

		final IResultSet rs;
		if (queries.containsKey("graph")) {
			if (dataSource.supportsNamedGraph()) {
				rs = dataSource.selectQuery(query, queries.get("graph"));
			} else { 
				throw new IllegalArgumentException("RDF Store does not support named graphs"); 
			}
		} else {
			rs = dataSource.selectQuery(query, null);
		}

		return loadingContentFromResultSet(rs);
	}

	protected abstract Set<String> loadingContentFromResultSet(IResultSet resultSet);

	protected void loadingQueryResultFromURIs(Set<String> uris, String graph, IDataSource<?,?> dataSource, Resource resource) {
		final ListResult result = ResultFactory.eINSTANCE.createListResult();

		for (String uri: uris) {
			EObject object = load(dataSource, uri, graph, resource);
			if (object != null) {
				result.getResult().add(object);
			}
		}

		resource.getContents().add(0, result);
	}

	protected void loadingEObjectsFromURIs(Set<String> uris, String graph, IDataSource<?,?> dataSource, Resource resource) {
		for (String uri: uris) {
			EObject object = load(dataSource, uri, graph, resource);
			if (object != null) {
				resource.getContents().add(object);
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.io.InputStream#read()
	 */
	@Override
	public int read() throws IOException {
		return 0;
	}
	
	/**
	 * 
	 * @author ghillairet
	 *
	 * @param <N> representation of a RDF Node
	 * @param <U> representation of a RDF URI (Label)
	 * @param <L> representation of a RDF Literal
	 */
	protected static abstract class EObjectBuilder<N, G> {

		protected final IDataSource<?,?> dataSource;
		protected final ETripleInputStream inStream;

		public EObjectBuilder(IDataSource<?,?> dataSource, ETripleInputStream inStream) {
			this.dataSource = dataSource;
			this.inStream = inStream;
		}

		public EObject loadEObject(EObject object, String key, String graphURI, boolean isProxy) {
			final IResultSet resultSet = getResultSet(object, key, graphURI, dataSource); 		
			final Map<String, Set<N>> values = createMapOfValues(object.eClass(), resultSet);

			final EAttribute attrId = ETripleEcoreUtil.getID(object.eClass());
			setIdValue(object, key, attrId);

			for (String featureName: values.keySet()) {
				EStructuralFeature feature = object.eClass().getEStructuralFeature(featureName);
				Set<N> nodes = values.get(featureName);
				
				if (feature instanceof EAttribute && feature != attrId) {
					
					if (feature.isMany()) {
						@SuppressWarnings("unchecked")
						N[] array = (N[]) nodes.toArray();
						
						for (int i=array.length;i > 0; --i) {
							setEAttributeValue(object, (EAttribute) feature, array[i-1]);
						}
						
					} else {
						
						if (!nodes.isEmpty()) {
							setEAttributeValue(object, (EAttribute) feature, nodes.iterator().next());
						}
						
					}
					
				} else if (!isProxy && feature instanceof EReference) {
					
					if (isBlankNode(feature)) {
						// having values in nodes mean there is values available, but those wont be used
						// to create Proxies, instead we create and load objects with specific bnode query.
						loadBlankNodeReference(object, key, (EReference)feature, graphURI);
					} else {
						loadProxyReference(object, (EReference)feature, nodes, graphURI);
					}
					
				}
			}

			return object;
		}

		protected List<EObject> loadBlankNodeReference(EObject parent, String key, EReference parentFeature, String graphURI) {
			final List<EObject> objects = new BasicEList<EObject>();

			final String query = selectBlankNodeObject(key, parentFeature, graphURI);

			Map<String, Map<String, Set<N>>> mapOfNodes = createMapOfValuesForBNode(dataSource.selectQuery(query, graphURI));

			for (String bnode: mapOfNodes.keySet()) {
				Map<String, Set<N>> values = mapOfNodes.get(bnode);

				if (values.containsKey(RDF.type)) {
					Set<N> types = values.get(RDF.type);
					EClass eClass = Metamodel.INSTANCE.getEClassByRdfType(asListOfString(types));

					if (eClass != null) {
						EObject object = EcoreUtil.create(eClass);
						//						resource.getContents().add(object);

						for (EStructuralFeature bNodeFeature: eClass.getEAllStructuralFeatures()) {
							String featureURI = Metamodel.INSTANCE.getRdfType(bNodeFeature);
							if (values.containsKey(featureURI)) {
								Set<N> nodes = values.get(featureURI);

								if (bNodeFeature instanceof EAttribute) {
									if (bNodeFeature.isMany()) {
										for (N n: nodes) {
											setEAttributeValue(object, (EAttribute) bNodeFeature, n);
										}
									} else {
										if (!nodes.isEmpty()) {
											setEAttributeValue(object, (EAttribute) bNodeFeature, nodes.iterator().next());
										}
									}
								} else if (bNodeFeature instanceof EReference) {
									if (isBlankNode(bNodeFeature)) {
										// having values in nodes mean there is values available, but those wont be used
										// to create Proxies, instead we create and load objects with specific bnode query.
										loadBlankNodeReference(object, key, (EReference)bNodeFeature, graphURI);
									} else {
										loadProxyReference(object, (EReference)bNodeFeature, nodes, graphURI);
									}
								}
							}
						}

						objects.add(object);
					}
				}
			}

			if (parentFeature.isMany()) {
				@SuppressWarnings("unchecked")
				EList<EObject> values = (EList<EObject>) parent.eGet(parentFeature);
				values.addAll(objects);
			} else {
				parent.eSet(parentFeature, objects.get(0));
			}

			return objects;
		}

		private List<String> asListOfString(Set<N> types) {
			final List<String> uris = new ArrayList<String>();
			for (N n: types) {
				String uri = getURI(n);
				if (uri != null) {
					uris.add(uri);
				}
			}
			return uris;
		}

		protected abstract String getURI(N node);

		// SELECT DISTINCT ?bn_ ?p ?o 
		protected abstract Map<String, Map<String, Set<N>>> createMapOfValuesForBNode(IResultSet resultSet);

		protected void loadProxyReference(EObject object, EReference feature, Set<N> nodes, String graph) {
			if (feature.isMany()) {
				for (N n: nodes) {
					setEReferenceValue(object, (EReference) feature, n, graph);
				}
			} else {
				if (!nodes.isEmpty()) {
					N n = nodes.iterator().next();
					setEReferenceValue(object, (EReference) feature, n, graph);
				}
			}
		}

		protected abstract void setEReferenceValue(EObject object, EReference reference, N node, String graph);

		protected abstract void setEAttributeValue(EObject object, EAttribute reference, N node);

		protected IResultSet getResultSet(EClass eClass, String key, String graphURI, IDataSource<?,?> dataSource) {
			return dataSource.selectQuery(selectObjectByClass(eClass, key, graphURI), null);
		}

		protected IResultSet getResultSet(EObject object, String key, String graphURI, IDataSource<?,?> dataSource) {
			return dataSource.selectQuery(selectObjectByClass(object.eClass(), key, graphURI), null);
		}

		protected Map<String, Set<N>> createMapOfValues(EClass eClass, IResultSet resultSet) {
			final Map<String, Set<N>> values = new HashMap<String, Set<N>>();

			for (;resultSet.hasNext();) {
				Solution sol = resultSet.next();
				for (EStructuralFeature feature: eClass.getEAllStructuralFeatures()) {
					N node = sol.get(feature.getName());
					if (node != null) {
						if (values.containsKey(feature.getName())) {
							Set<N> newSet = values.get(feature.getName());
							newSet.add(node);
							values.put(feature.getName(), newSet);
						} else {
							Set<N> newSet = new HashSet<N>();
							newSet.add(node);
							values.put(feature.getName(), newSet);
						}
					}
				}
			}

			return values;
		}

	}

	public EObject createProxy(EClass eClass, String key) {
		EObject object = EcoreUtil.create(eClass);
		setIdValue(object, key, ETripleEcoreUtil.getID(eClass));

		final URI proxyURI = URI.createURI(uri.trimFragment().trimQuery()+"#"+key);

		((InternalEObject)object).eSetProxyURI(proxyURI);

		return object;
	}

	protected static void setIdValue(EObject returnedObject, String key, EAttribute id) {
		if (id == null)
			return;

		EAnnotation ann = ETripleEcoreUtil.getETripleAnnotation(id, "Id");

		if (ann == null) {
			if (id.isID() && id.getName().equals("URI")) {
				returnedObject.eSet(id, EcoreUtil.createFromString(id.getEAttributeType(), key));	
			} else {
				String ns = returnedObject.eClass().getEPackage().getNsURI();

				int length;
				if (!(ns.endsWith("/") || ns.endsWith("#"))) {
					length = ns.length() + 1;
				} else {
					length = ns.length();
				}
				String value = key.substring(length, key.length());

				returnedObject.eSet(id, EcoreUtil.createFromString(id.getEAttributeType(), value));
			}
		} else {
			if (ann.getDetails().containsKey("base")) {
				String base = ann.getDetails().get("base");
				if (key.startsWith(base)) {
					String localName = key.substring(base.length(), key.length());
					if (localName != null && localName.length() > 0) {
						returnedObject.eSet(id, EcoreUtil.createFromString(id.getEAttributeType(), localName));
					}
				}
			}
		}
	}
}
