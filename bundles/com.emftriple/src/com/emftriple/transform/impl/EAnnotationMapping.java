/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple.transform.impl;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.emf4sw.rdf.Node;
import com.emf4sw.rdf.Resource;
import com.emftriple.transform.IMapping;
import com.emftriple.util.EntityUtil;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * {@link EAnnotationMapping} stores correspondances between model and ontology classes 
 * according to model annotations.
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.5.5
 */
public class EAnnotationMapping extends AbstractMapping implements IMapping {

	protected Map<URI, EClass> rdfTypeMap;

	protected Map<EClass, List<URI>> classMap;

	protected Map<EStructuralFeature, URI> featureMap;

	protected Map<Class<?>, EClass> mappedClasses;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	//	@Inject
	//	public EAnnotationMapping(@Named("Packages") List packages, @Named("Properties") List properties) {

	public EAnnotationMapping(List<EPackage> packages) {
		super(packages);
		this.rdfTypeMap = Maps.newHashMap();
		this.featureMap = Maps.newHashMap();
		this.mappedClasses = Maps.newHashMap();
		this.classMap = Maps.newHashMap();

		for (EClass eClass: getEClasses()) 
		{
			classMap.put(eClass, EntityUtil.getRdfTypes(eClass));
			for (URI aURI: EntityUtil.getRdfTypes(eClass)) {
				rdfTypeMap.put(aURI, eClass);	
			}

			Class<?> c = eClass.getInstanceClass();
			if (c != null) 
			{
				mappedClasses.put(c, eClass);
			}

			for (EStructuralFeature eFeature: eClass.getEStructuralFeatures()) 
			{
				URI featureURI = EntityUtil.getRdfType(eFeature);
				if (featureURI != null) 
				{
					featureMap.put(eFeature, featureURI);
				}
			}
		}
	}

	@Override
	public EClass getEClassWithRdfType(String uri) {
		return rdfTypeMap.get( URI.createURI(uri) );
	}

	@Override
	public EClass getEClass(Class<?> aClass) throws IllegalArgumentException {
		if (aClass.isInterface()) {
			if (mappedClasses.containsKey(aClass)) {
				return mappedClasses.get(aClass);
			}
		} else {
			if (aClass.getInterfaces().length == 0) {
				if (mappedClasses.containsKey(aClass)) {
					return mappedClasses.get(aClass);
				}
			} else {
				for (Class<?> intface: aClass.getInterfaces()) {
					if (mappedClasses.containsKey(intface)) {
						return mappedClasses.get(intface);
					}
				}
			}
		}
		return null;
	}

	@Override
	public boolean isMappedClass(Class<?> aClass) {
		if (aClass.isInterface()) {
			return mappedClasses.containsKey(aClass);
		} else {
			if (aClass.getInterfaces().length == 0) {
				return mappedClasses.containsKey(aClass);
			} else {
				for (Class<?> intface: aClass.getInterfaces()) {
					if (mappedClasses.containsKey(intface)) {
						return true;
					}
				}
				return false;
			}
		}
	}

	@Override
	public List<URI> getRdfTypes(EClass eClass) {
		return classMap.get(eClass);
	}

	@Override
	public URI getRdfType(EStructuralFeature eFeature) {
		return featureMap.get(eFeature);
	}

	@Override
	public EClass findEClassByRdfType(List<String> types) {
		// TODO
		for (String type: types) {
			URI typeURI = URI.createURI(type);
			if (rdfTypeMap.containsKey(typeURI)) {
				return rdfTypeMap.get(typeURI);
			}
		}
		return null;
	}

	@Override
	public <T> URI getNamedGraph(Class<T> aClass) throws IllegalArgumentException {
		final EClass eClass = getEClass(aClass);
		if (eClass == null) {
			throw new IllegalArgumentException();
		}

		final URI uri = EntityUtil.getNamedGraph(eClass);

		return uri == null ? null : uri;
	}

	@Override
	public EClass findEClassByRdfType(EList<Resource> types) {
		for (Resource node: types) {
			URI typeURI = URI.createURI( ((Resource) node).getURI() );
			if (rdfTypeMap.containsKey(typeURI)) {
				return rdfTypeMap.get(typeURI);
			}
		}
		return null;
	}

}