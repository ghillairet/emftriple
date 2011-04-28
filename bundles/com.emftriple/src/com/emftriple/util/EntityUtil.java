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
package com.emftriple.util;

import static com.emftriple.util.Functions.transform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.emftriple.transform.IMapping;
import com.google.common.base.Function;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.5.5
 */
public class EntityUtil {
	
	private static final Map<EClass, List<String>> cache = new HashMap<EClass, List<String>>();

	private static final Map<EStructuralFeature, String> cacheFeature = new HashMap<EStructuralFeature, String>();
	
	private static final Map<EClass, EAttribute> cacheid = new HashMap<EClass, EAttribute>();
	
	public static EAnnotation getETripleAnnotation(EModelElement element, String name) {
		EAnnotation ann = element.getEAnnotation(name);
		if (ann != null)
		{
			return ann;
		}
		return element.getEAnnotation("etriple." + name);
	}
	
	public static String getNamedGraph(EClass aClass) {
		EAnnotation graphURI = getETripleAnnotation(aClass, "NamedGraph");
		if (graphURI == null) 
		{
			for (EClass eClass: aClass.getEAllSuperTypes()) 
			{
				graphURI = getETripleAnnotation(eClass, "NamedGraph");
				if (graphURI != null) 
				{
					break;
				}
			}
		}
		
		if (graphURI == null) 
		{
			graphURI = getETripleAnnotation(aClass.getEPackage(), "NamedGraph");
		}
		
		return graphURI == null ? null : graphURI.getDetails().get("uri");
	}
	
	public static String getDataSet(EClass eClass) {
		EAnnotation dataSetAnn = getETripleAnnotation(eClass, "DataSet");
		if (dataSetAnn != null) {
			return dataSetAnn.getDetails().get("name");
		}
		return null;
	}

	public static void checkState(Object obj) {
		checkIsSupported(obj);
	}

	public static void checkIsSupported(Object obj) throws IllegalArgumentException {
		if (obj == null) 
		{
			throw new IllegalArgumentException("null objects are not supported.");
		}
		checkIsEntity(obj);
	}

	public static void checkIsEntity(Object obj) throws IllegalArgumentException {
		if (!(obj instanceof EObject)) 
		{
			throw new IllegalArgumentException("only EObject instances are suppported.");
		}
	}

	public static List<String> getRdfTypes(EClass eClass) {
		if (cache.containsKey(eClass)) {
			return cache.get(eClass);
		}
		
		if (!IMapping.ETriplePackageRegistry.INSTANCE.hasPackage(eClass.getEPackage())) {
			IMapping.ETriplePackageRegistry.INSTANCE.register(eClass.getEPackage());
		}
		
		final List<String> uris = new ArrayList<String>();
		
		for (EAnnotation annotation: eClass.getEAnnotations()) {
			if (annotation.getSource().contains("Entity") || annotation.getSource().contains("OWLClass")) {
				try {
					uris.add( annotation.getDetails().get("uri") );
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (uris.isEmpty()) 
		{
			try {
				uris.add( wellFormedURI(getPackageNamespace(eClass.getEPackage())) + eClass.getName() );
			}  catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		
		cache.put(eClass, uris);
		
		return uris;
	}

	public static String getRdfType(EStructuralFeature feature) {
		if (cacheFeature.containsKey(feature)) {
			return cacheFeature.get(feature);
		}
		
		if (!IMapping.ETriplePackageRegistry.INSTANCE.hasPackage(feature.getEContainingClass().getEPackage())) {
			IMapping.ETriplePackageRegistry.INSTANCE.register(feature.getEContainingClass().getEPackage());
		}
		
		EAnnotation ann = getETripleAnnotation(feature, "DataProperty");
		String annotationURI = null;
		if (ann != null)
		{
			annotationURI = ann.getDetails().get("uri");
		}
		if (annotationURI == null)
		{
			ann = getETripleAnnotation(feature, "ObjectProperty");
			if (ann != null)
			{
				annotationURI = ann.getDetails().get("uri");
			}
		}
		if (annotationURI == null)
		{
			ann = getETripleAnnotation(feature, "rdf");
			if (ann != null)
			{
				annotationURI = ann.getDetails().get("uri");
			}
		}
		if (annotationURI == null) 
		{
			annotationURI = wellFormedURI(getPackageNamespace(feature.getEContainingClass().getEPackage())) + feature.getName();
		}
		
		cacheFeature.put(feature, annotationURI);
		
		return annotationURI;
	}
	
	public static URI URI(Object key) {
		return (key instanceof URI) ? (URI) key : URI.createURI(key.toString());
	}

	public static EAttribute getId(EClass eClass) {
		if (cacheid.containsKey(eClass)) {
			return cacheid.get(eClass);
		}
		
		EAttribute theId = eClass.getEIDAttribute();
		if (theId != null) {
			cacheid.put(eClass, theId);
			return theId;
		}

		theId = getId(eClass, eClass.getEAttributes());
		if (theId == null) {
			theId = getId(eClass, eClass.getEAllAttributes());
		}
		
		return theId;
	}
	
	private static EAttribute getId(EClass eClass, EList<EAttribute> attributes) {
		for (EAttribute eAttribute: attributes) {
			for (EAnnotation ann: eAttribute.getEAnnotations()) {
				if (ann.getSource().contains("Id")) { // || ann.getSource().equals("GeneratedId") || ann.getSource().equals("CompositeId")) {
					cacheid.put(eClass, eAttribute);
					return eAttribute;
				}
			}
		}
		return null;
	}
	
	private static String getPackageNamespace(EPackage ePackage) {
		EAnnotation ann = getETripleAnnotation(ePackage, "Ontology");
		String namespace = null;
		if (ann != null)
		{
			namespace = ann.getDetails().get("uri");
		}
		if (namespace == null) 
		{
			namespace = ePackage.getNsURI();
		}
		return namespace;
	}

	/**
	 * Return the namespace of an EObject corresponding to its EPackage nsURI value.
	 */
	public static String namespace(EObject object) {
		String nsURI = null;
		if (object instanceof EPackage) {
			nsURI = ((EPackage) object).getNsURI();
		}
		if (object instanceof EClassifier) {
			nsURI = ((EClassifier) object).getEPackage().getNsURI();
		}
		else if (object instanceof EStructuralFeature) {
			nsURI = ((EStructuralFeature) object).getEContainingClass().getEPackage().getNsURI();
		}
		else {
			nsURI = object.eClass().getEPackage().getNsURI();
		}
		return wellFormedURI(nsURI);
	}

	public static String validNamespace(String namespace) {
		return transform(namespace, new URIValidator());
	}

	public static String wellFormedURI(String namespace) {
		return 
			!((namespace.endsWith("#") || namespace.endsWith("/"))) ? 
					namespace + "#" : 
						namespace;
	}

	public static class URIValidator implements Function<String, String> {
		private static final String HTTP = "http://";
		
		@Override
		public String apply(String from) {
			return ((from.indexOf(":") > -1) ? 
					(from.charAt(from.indexOf(":") + 1 ) == '/') ? from : 
						from.replaceFirst(":", "://") : HTTP + from).replaceAll("\\s", "_");
		}	
	}
}
