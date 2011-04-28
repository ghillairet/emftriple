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

import static com.emftriple.util.ETripleEcoreUtil.getETripleAnnotation;
import static com.emftriple.util.ETripleEcoreUtil.getPackageNamespace;
import static com.emftriple.util.ETripleEcoreUtil.wellFormedURI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.emftriple.transform.Metamodel;
import com.emftriple.util.ETripleEcoreUtil;

/**
 * {@link MetamodelImpl} stores correspondances between model and ontology classes 
 * according to model annotations.
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.5.5
 */
public class MetamodelImpl implements Metamodel {

	private static final Map<EClass, List<String>> cache = new HashMap<EClass, List<String>>();

	private static final Map<EStructuralFeature, String> cacheFeature = new HashMap<EStructuralFeature, String>();
	
	public MetamodelImpl() {}

	@Override
	public EClass getEClass(Class<?> aClass) throws IllegalArgumentException {
		for (EPackage ePackage: Metamodel.Registry.INSTANCE.getRegisteredPackages()) {
			EClassifier eC = ePackage.getEClassifier(aClass.getSimpleName());
			if (eC == null) {
				eC = ePackage.getEClassifier(aClass.getSimpleName()+"Impl");
				if (eC != null) {
					return (EClass) eC;
				}
			} else {
				return (EClass) eC;
			}
		}
		
		return null;
	}

	@Override
	public EClass getEClassByRdfType(String uri) {	
		EClass eC = doGetEClassByRdfType(uri);
		if (eC != null) {
			return eC;
		}
		
		for (Object o: EPackage.Registry.INSTANCE.values()) {
			if (o instanceof EPackage) {
				Metamodel.Registry.INSTANCE.register((EPackage) o);
			}
			
			eC = doGetEClassByRdfType(uri);
			if (eC != null) {
				return eC;
			}
		}
		
		return null;
	}
	
	private EClass doGetEClassByRdfType(String uri) {
		if (Metamodel.Registry.INSTANCE.mapOfEntities().containsKey(uri)) {
			return Metamodel.Registry.INSTANCE.mapOfEntities().get(uri);
		}
		return null;
	}

	@Override
	public EClass getEClassByRdfType(List<String> uris) {
		for (String uri: uris) {
			EClass eC = getEClassByRdfType(uri);
			if (eC != null) {
				return eC;
			}
		}
	
		return null;
	}

	@Override
	public List<String> getRdfTypes(EClass eClass) {
		if (cache.containsKey(eClass)) {
			return cache.get(eClass);
		}
		
		if (!Metamodel.Registry.INSTANCE.hasPackage(eClass.getEPackage())) {
			Metamodel.Registry.INSTANCE.register(eClass.getEPackage());
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
				uris.add( wellFormedURI(ETripleEcoreUtil.getPackageNamespace(eClass.getEPackage())) + eClass.getName() );
			}  catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		
		cache.put(eClass, uris);
		
		return uris;
	}

	@Override
	public String getRdfType(EStructuralFeature feature) {
		if (cacheFeature.containsKey(feature)) {
			return cacheFeature.get(feature);
		}
		
		if (!Metamodel.Registry.INSTANCE.hasPackage(feature.getEContainingClass().getEPackage())) {
			Metamodel.Registry.INSTANCE.register(feature.getEContainingClass().getEPackage());
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
	
}