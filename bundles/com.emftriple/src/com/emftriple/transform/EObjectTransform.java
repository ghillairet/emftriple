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

import static com.emftriple.transform.SparqlQueries.selectObjectByClass;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

public abstract class EObjectTransform<N, U, L> {

	@SuppressWarnings("rawtypes")
	protected final IDataSource dataSource;

	@SuppressWarnings("rawtypes")
	protected ETripleResourceImpl resource;

	@SuppressWarnings("rawtypes")
	public EObjectTransform(ETripleResourceImpl resource, IDataSource dataSource) {
		this.resource = resource;
		this.dataSource = dataSource;
	}

	public EObject loadEObject(EObject object, String key, String graphURI) {
		final IResultSet<N, U, L> resultSet = getResultSet(object.eClass(), key, graphURI, dataSource); 		
		final Map<String, Set<N>> values = createMapOfValues(object.eClass(), resultSet);

		final EAttribute attrId = ETripleEcoreUtil.getId(object.eClass());
		setIdValue(object, key.toString(), attrId);

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
		}

		return object;
	}

	protected abstract void setEReferenceValue(EObject object, EReference reference, N node);

	protected abstract void setEAttributeValue(EObject object, EAttribute reference, N node);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected IResultSet<N, U, L> getResultSet(EClass eClass, String key, String graphURI, IDataSource dataSource) {
		return dataSource.selectQuery(selectObjectByClass(eClass, key, graphURI), null);
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
			
			final URI proxyURI;
//			if (resource.getURI().hasQuery()) {
//				proxyURI = URI.createURI(resource.getURI().trimQuery() + "?uri="+key);
//			} else {
				proxyURI = URI.createURI(resource.getURI()+"#uri="+key);
//			}
			
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
