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
package com.emftriple.transform.impl;

import static com.emftriple.util.SparqlQueries.selectAllTypes;
import static com.emftriple.util.SparqlQueries.selectObjectByClass;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.emf4sw.rdf.Literal;
import com.emf4sw.rdf.Node;
import com.emf4sw.rdf.Resource;
import com.emf4sw.rdf.URIElement;
import com.emf4sw.rdf.operations.DatatypeConverter;
import com.emftriple.datasources.INamedGraphDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.datasources.IResultSet.Solution;
import com.emftriple.resource.ETripleResourceImpl;
import com.emftriple.resource.URIBuilder;
import com.emftriple.transform.IGetObject;
import com.emftriple.transform.Metamodel;
import com.emftriple.util.ETripleEcoreUtil;
import com.google.inject.internal.Maps;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class GetEObjectImpl extends AbstractGetObject implements IGetObject {

	public GetEObjectImpl(ETripleResourceImpl resource) {
		super(resource);
	}

	@Override
	public EObject get(EClass eClass, String key) {
		final EObject returnedObject = EcoreUtil.create(eClass);
		final EAttribute attrId = ETripleEcoreUtil.getId(eClass);
		setIdValue(returnedObject, key.toString(), attrId);
		
		resource.getContents().add(returnedObject);

		fillObject(returnedObject, eClass, key);
		
		return returnedObject;
	}
	
	@Override
	public EObject resolveProxy(EObject obj, EClass eClass, String key) {
		final EAttribute attrId = ETripleEcoreUtil.getId(eClass);
		setIdValue(obj, key, attrId);
		((InternalEObject)obj).eSetProxyURI(null);

		fillObject(obj, eClass, key);

		return obj;
	}

	private EObject fillObject(EObject object, EClass eClass, String key) {
		final IResultSet resultSet; 
		if (resource.getGraph() != null) {
			resultSet = ((INamedGraphDataSource)dataSource).selectQuery(
					selectObjectByClass(eClass, key), resource.getGraph());
		} else {
			resultSet = dataSource.selectQuery(selectObjectByClass(eClass, key));
		}
		
		final Map<EStructuralFeature, String> previous = Maps.newHashMap();

		for (;resultSet.hasNext();) {
			Solution sol = resultSet.next();
			for (EStructuralFeature feature: eClass.getEAllStructuralFeatures()) {
				Node node = sol.get(feature.getName());
				if (node != null) {
					if (feature instanceof EAttribute && node instanceof Literal) {
						if (feature.isMany()) {
							if (!previous.containsKey(feature)) {
								doEAttribute(object, (EAttribute)feature, (Literal)node);
							}
							else if (!previous.get(feature).equals(((Literal) node).getLexicalForm())) {
								doEAttribute(object, (EAttribute)feature, (Literal)node);
							}
							previous.put(feature, ((Literal) node).getLexicalForm());
						} else {
							doEAttribute(object, (EAttribute)feature, (Literal)node);
						}
					} else if (node instanceof Resource && node instanceof Resource){
						if (feature.isMany()) {
							if (!previous.containsKey(feature)) {
								doEReference(object, (EReference)feature, (Resource)node);
							}
							else if (!previous.get(feature).equals(((URIElement) node).getURI())) {
								doEReference(object, (EReference)feature, (Resource)node);								
							}
							previous.put(feature, ((URIElement) node).getURI());
						} else {
							doEReference(object, (EReference)feature, (Resource)node);
						}
					}
				}
			}
		}
		return object;
	}
	
	private void doEReference(final EObject returnedObject, EReference feature, Resource node) {
		if (feature.isMany()) {
			@SuppressWarnings("unchecked")
			final EList<Object> list = (EList<Object>) returnedObject.eGet(feature);

			if (feature.isContainment()) {
				list.add( get(getClass(node, (EClass) feature.getEType()), node.getURI()) );
			} else {
				EObject prox = createProxy(node, getClass(node, (EClass) feature.getEType()));
				list.add( prox );
			}
		} else {
			if (feature.isContainment()) {
				returnedObject.eSet(feature, get(getClass(node, (EClass) feature.getEType()), node.getURI()));
			} else {
				returnedObject.eSet(feature, createProxy(node, getClass(node, (EClass) feature.getEType())));
			}
		}
	}

	private void doEAttribute(EObject returnedObject, EAttribute feature, Literal node) {	
		if (feature.isMany()) {
			@SuppressWarnings("unchecked")
			final EList<Object> list = (EList<Object>) returnedObject.eGet(feature);
//			final String aStringValue;
//			if (isLangSpecific(feature)) {
//				aStringValue = node.getLexicalForm(); //getValue(node, getLang(feature));
//			} else {
//				aStringValue = node.getLexicalForm();
//			}
			final Object value = DatatypeConverter.convert((EDataType) feature.getEType(), node.getLexicalForm());
			if (value != null) list.add(value);
		} else {
			final Object value = DatatypeConverter.convert((EDataType) feature.getEType(), node.getLexicalForm());
			if (value != null) returnedObject.eSet(feature, value);
		}
	}

	private EObject createProxy(Resource node, EClass eType) {
		if (resource.getPrimaryCache().hasKey(node.getURI())) {
			return resource.getPrimaryCache().getObjectByKey(node.getURI());
		} else {
			EObject proxy = EcoreUtil.create(eType);
			URI uri = URIBuilder.build(resource, node.getURI());
			if (!uri.fragment().startsWith("uri=")) {
				throw new IllegalArgumentException();
			}
			((InternalEObject)proxy).eSetProxyURI(uri);
			resource.getContents().add(proxy);
			resource.getPrimaryCache().cache(node.getURI(), proxy);
			
			return proxy;
		}
	}

	private EClass getClass(Node node, EClass eType) {
		checkNotNull(eType);
	
		return 	(node instanceof URIElement) ?
				Metamodel.INSTANCE
					.getEClassByRdfType( 
							selectAllTypes(dataSource, ((URIElement) node).getURI(), resource.getGraph()))
				: null;
	}

//	private boolean isLangSpecific(EAttribute attribute) {
//		EAnnotation ann = EntityUtil.getETripleAnnotation(attribute, "DataProperty");
//		if (ann != null) {
//			return ann.getDetails().containsKey("lang");
//		}
//		ann = EntityUtil.getETripleAnnotation(attribute, "rdf");
//		if (ann != null) {
//			return ann.getDetails().containsKey("lang");
//		}
//		return false;
//	}
//
//	private String getLang(EAttribute attribute) {
//		EAnnotation ann = EntityUtil.getETripleAnnotation(attribute, "DataProperty");
//		if (ann != null) {
//			return ann.getDetails().get("lang");
//		}
//		ann = EntityUtil.getETripleAnnotation(attribute, "rdf");
//		if (ann != null) {
//			return ann.getDetails().get("lang");
//		}
//		return null;
//	}

}
