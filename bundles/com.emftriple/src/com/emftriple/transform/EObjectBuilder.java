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
package com.emftriple.transform;

import static com.emftriple.transform.SparqlQueries.selectBlankNodeObject;
import static com.emftriple.transform.SparqlQueries.selectObjectByClass;
import static com.emftriple.util.ETripleEcoreUtil.isBlankNode;

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
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.datasources.IResultSet.Solution;
import com.emftriple.resource.ETripleResourceImpl;
import com.emftriple.util.ETripleEcoreUtil;
import com.emftriple.vocabularies.RDF;

/**
 * The {@link EObjectBuilder} abstract class provides methods from creating EObject from RDF. This class needs to
 * be extended to fulfill specific RDF APIs. 
 * 
 * @author guillaume hillairet
 * @since 0.8.0
 * 
 * @param <N> representation of a RDF Node
 * @param <U> representation of a RDF URI (Label)
 * @param <L> representation of a RDF Literal
 */
public abstract class EObjectBuilder<N, U extends N, L extends N> {

	@SuppressWarnings("rawtypes")
	protected final IDataSource dataSource;

	@SuppressWarnings("rawtypes")
	protected ETripleResourceImpl resource;

	@SuppressWarnings("rawtypes")
	public EObjectBuilder(ETripleResourceImpl resource, IDataSource dataSource) {
		this.resource = resource;
		this.dataSource = dataSource;
	}

	public EObject loadEObject(EObject object, String key, String graphURI) {
		final IResultSet<N, U, L> resultSet = getResultSet(object, key, graphURI, dataSource); 		
		final Map<String, Set<N>> values = createMapOfValues(object.eClass(), resultSet);

		final EAttribute attrId = ETripleEcoreUtil.getId(object.eClass());
		setIdValue(object, key, attrId);

		for (String featureName: values.keySet()) {
			EStructuralFeature feature = object.eClass().getEStructuralFeature(featureName);
			Set<N> nodes = values.get(featureName);
			
			if (feature instanceof EAttribute && feature != attrId) {
				if (feature.isMany()) {
					for (N n: nodes) {
						setEAttributeValue(object, (EAttribute) feature, n);
					}
				} else {
					if (!nodes.isEmpty()) {
						setEAttributeValue(object, (EAttribute) feature, nodes.iterator().next());
					}
				}
			} else if (feature instanceof EReference) {
				if (isBlankNode(feature)) {
					// having values in nodes mean there is values available, but those wont be used
					// to create Proxies, instead we create and load objects with specific bnode query.
					loadBlankNodeReference(object, key, (EReference)feature, graphURI);
				} else {
					loadProxyReference(object, (EReference)feature, nodes);
				}
			}
		}

		return object;
	}

	private List<EObject> loadBlankNodeReference(EObject parent, String key, EReference parentFeature, String graphURI) {
		final List<EObject> objects = new BasicEList<EObject>();
		
		final String query = selectBlankNodeObject(key, parentFeature, graphURI);
		@SuppressWarnings("unchecked")
		Map<String, Map<String, Set<N>>> mapOfNodes = createMapOfValuesForBNode(dataSource.selectQuery(query, graphURI));
		
		for (String bnode: mapOfNodes.keySet()) {
			Map<String, Set<N>> values = mapOfNodes.get(bnode);
			
			if (values.containsKey(RDF.type)) {
				Set<N> types = values.get(RDF.type);
				EClass eClass = Metamodel.INSTANCE.getEClassByRdfType(asListOfString(types));
				
				if (eClass != null) {
					EObject object = EcoreUtil.create(eClass);
					resource.getContents().add(object);
					
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
									loadProxyReference(object, (EReference)bNodeFeature, nodes);
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
	protected abstract Map<String, Map<String, Set<N>>> createMapOfValuesForBNode(IResultSet<N, U, L> resultSet);

	private void loadProxyReference(EObject object, EReference feature, Set<N> nodes) {
		if (feature.isMany()) {
			for (N n: nodes) {
				setEReferenceValue(object, (EReference) feature, n);
			}
		} else {
			if (!nodes.isEmpty()) {
				N n = nodes.iterator().next();
				setEReferenceValue(object, (EReference) feature, n);
			}
		}
	}
	
	protected abstract void setEReferenceValue(EObject object, EReference reference, N node);

	protected abstract void setEAttributeValue(EObject object, EAttribute reference, N node);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected IResultSet<N, U, L> getResultSet(EClass eClass, String key, String graphURI, IDataSource dataSource) {
		return dataSource.selectQuery(selectObjectByClass(eClass, key, graphURI), null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected IResultSet<N, U, L> getResultSet(EObject object, String key, String graphURI, IDataSource dataSource) {
		return dataSource.selectQuery(selectObjectByClass(object.eClass(), key, graphURI), null);
	}

	protected Map<String, Set<N>> createMapOfValues(EClass eClass, IResultSet<N, U, L> resultSet) {
		final Map<String, Set<N>> values = new HashMap<String, Set<N>>();

		for (;resultSet.hasNext();) {
			Solution<N, U, L> sol = resultSet.next();
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
	
	protected EObject createProxy(EClass eClass, String key) {
		if (resource.getPrimaryCache().hasKey(key)) {
			return resource.getPrimaryCache().getObjectByKey(key);
		} else {
			EObject object = EcoreUtil.create(eClass);
			setIdValue(object, key, ETripleEcoreUtil.getId(eClass));
			
			final URI proxyURI = URI.createURI(resource.getURI()+"#uri="+key);
			
			((InternalEObject)object).eSetProxyURI(proxyURI);
			resource.getPrimaryCache().cache(key, object);
			resource.getContents().add(object);

			return object;
		}
	}

	protected void setIdValue(EObject returnedObject, String key, EAttribute id) {
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
