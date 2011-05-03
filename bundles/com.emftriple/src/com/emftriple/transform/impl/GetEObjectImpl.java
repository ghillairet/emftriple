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
import static org.eclipse.emf.ecore.util.EcoreUtil.createFromString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
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
import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.INamedGraphDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.datasources.IResultSet.Solution;
import com.emftriple.resource.ETripleResourceImpl;
import com.emftriple.resource.URIBuilder;
import com.emftriple.transform.IGetObject;
import com.emftriple.transform.Metamodel;
import com.emftriple.util.ETripleEcoreUtil;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class GetEObjectImpl implements IGetObject {

	protected final ETripleResourceImpl resource;

	protected final IDataSource dataSource;

	public GetEObjectImpl(ETripleResourceImpl resource, IDataSource dataSource) {
		this.resource = resource;
		this.dataSource = dataSource;
	}

	@Override
	public EObject get(EClass eClass, String key) {
		final EObject returnedObject = EcoreUtil.create(eClass);
		final EAttribute attrId = ETripleEcoreUtil.getId(eClass);
		setIdValue(returnedObject, key.toString(), attrId);

		resource.getContents().add(returnedObject);
		resource.getPrimaryCache().cache(key, returnedObject);

		fillObject(returnedObject, eClass, key);

		return returnedObject;
	}

	@Override
	public EObject resolveProxy(EObject obj, EClass eClass, String key) {
		final EAttribute attrId = ETripleEcoreUtil.getId(eClass);
		setIdValue(obj, key, attrId);
		((InternalEObject)obj).eSetProxyURI(null);
		resource.getPrimaryCache().cache(key, obj);

		fillObject(obj, eClass, key);

		return obj;
	}

	private EObject fillObject(EObject object, EClass eClass, String key) {
		IResultSet resultSet = null; 
		if (resource.getGraph() != null) {
			String query = null;
			try {
				query = selectObjectByClass(eClass, key);
				resultSet = ((INamedGraphDataSource)dataSource).selectQuery(query, resource.getGraph());
			} catch (Exception e) {
				System.out.println(query);
				e.printStackTrace();
			}
		} else {
			resultSet = dataSource.selectQuery(selectObjectByClass(eClass, key));
		}

//		final Map<EStructuralFeature, String> previous = new HashMap<EStructuralFeature, String>();
		final Map<String, Set<Node>> values = new HashMap<String, Set<Node>>();

		for (;resultSet.hasNext();) {
			Solution sol = resultSet.next();
			for (EStructuralFeature feature: eClass.getEAllStructuralFeatures()) {
				Node node = sol.get(feature.getName());
				if (node != null) {
					if (values.containsKey(feature.getName())) {
						Set<Node> newSet = values.get(feature.getName());
						newSet.add(node);
						values.put(feature.getName(), newSet);
					} else {
						Set<Node> newSet = new HashSet<Node>();
						newSet.add(node);
						values.put(feature.getName(), newSet);
					}
				}
			}
		}

//		System.out.println(values);
		
		for (String featureName: values.keySet()) {
			EStructuralFeature feature = eClass.getEStructuralFeature(featureName);
			Set<Node> nodes = values.get(featureName);

			if (feature instanceof EAttribute) {
				if (feature.isMany()) {
					@SuppressWarnings("unchecked")
					final EList<Object> list = (EList<Object>) object.eGet(feature);
					for (Node n: nodes) {
						if (n instanceof Literal) {
							Object o = doEAttribute(object, (EAttribute)feature, (Literal)n);
							if (o != null) {
								list.add(o);
							}
						}
					}
				}
				else {
					if (!nodes.isEmpty()) {
						Node n = nodes.iterator().next();
						if (n instanceof Literal) {
							Object val = doEAttribute(object, (EAttribute)feature, (Literal)n);
							if (val != null) {
								object.eSet(feature, val);								
							}
						}
					}
				}
			}
			else {
				if (feature.isMany()) {
					@SuppressWarnings("unchecked")
					final EList<Object> list = (EList<Object>) object.eGet(feature);
					final List<EObject> tmp = new ArrayList<EObject>();
					for (Node n: nodes) {
						// TODO: fix it!
						// tmp list is necessary to avoid a loop call in EList (proxy resolution)
						if (n instanceof Resource) {
							EObject o = doEReference(object, (EReference)feature, (Resource)n);
							if (o != null) {
								tmp.add(o);
							}
						}
					}
					list.addAll(tmp);
				}
				else {
					if (!nodes.isEmpty()) {
						Node n = nodes.iterator().next();
						if (n instanceof Resource) {
							EObject o = doEReference(object, (EReference)feature, (Resource)n);
							if (o != null) {
								object.eSet(feature, o);
							}
						}
					}
				}
			}
		}

		return object;
	}

	//	if (!previous.containsKey(feature)) {
	//								doEAttribute(object, (EAttribute)feature, (Literal)node);
	//							}
	//							else if (!previous.get(feature).equals(((Literal) node).getLexicalForm())) {
	//								doEAttribute(object, (EAttribute)feature, (Literal)node);
	//							}
	//							previous.put(feature, ((Literal) node).getLexicalForm());
	//						} else {
	//							doEAttribute(object, (EAttribute)feature, (Literal)node);
	//						}
	//					} else if (node instanceof Resource && node instanceof Resource){
	//						if (feature.isMany()) {
	//							if (!previous.containsKey(feature)) {
	//								doEReference(object, (EReference)feature, (Resource)node);
	//							}
	//							else if (!previous.get(feature).equals(((URIElement) node).getURI())) {
	//								doEReference(object, (EReference)feature, (Resource)node);						
	//							}
	//							previous.put(feature, ((URIElement) node).getURI());
	//						} else {
	//							doEReference(object, (EReference)feature, (Resource)node);
	//						}
	//					}
	//				}
	//			}
	//		}
	//		return object;
	//	}

	private EObject doEReference(final EObject returnedObject, EReference feature, Resource node) {
		if (feature.isContainment()) {
			return get(getClass(node, (EClass) feature.getEType()), node.getURI());
		} else {
			return createProxy(node, getClass(node, (EClass) feature.getEType()));
		}
	}

	private Object doEAttribute(EObject returnedObject, EAttribute feature, Literal node) {	
		//			final String aStringValue;
		//			if (isLangSpecific(feature)) {
		//				aStringValue = node.getLexicalForm(); //getValue(node, getLang(feature));
		//			} else {
		//				aStringValue = node.getLexicalForm();
		//			}
		return DatatypeConverter.convert((EDataType) feature.getEType(), node.getLexicalForm());
		//			if (value != null) list.add(value);
		//		} else {
		//			final Object value = DatatypeConverter.convert((EDataType) feature.getEType(), node.getLexicalForm());
		//			if (value != null) returnedObject.eSet(feature, value);
		//		}
	}

	private EObject createProxy(Resource node, EClass eType) {
		if (resource.getPrimaryCache().hasKey(node.getURI())) {
			EObject obj = resource.getPrimaryCache().getObjectByKey(node.getURI());
			if (obj.eResource() == null) {
				resource.getContents().add(obj);
			}
			return obj;
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
		if (eType == null)
			throw new IllegalArgumentException();

		return 	(node instanceof URIElement) ?
				Metamodel.INSTANCE
				.getEClassByRdfType( 
						selectAllTypes(dataSource, ((URIElement) node).getURI(), resource.getGraph()))
						: null;
	}

	protected void setIdValue(EObject returnedObject, String key, EAttribute id) {
		if (id == null)
			return;

		EAnnotation ann = ETripleEcoreUtil.getETripleAnnotation(id, "Id");

		if (ann == null) {
			returnedObject.eSet(id, key);
			return;
		}

		if (ann.getDetails().containsKey("base")) {
			String base = ann.getDetails().get("base");
			if (key.startsWith(base)) {
				String localName = key.substring(base.length(), key.length());
				if (localName != null && localName.length() > 0)
					returnedObject.eSet(id, createFromString((EDataType) id.getEType(), localName));
			}
		}
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
