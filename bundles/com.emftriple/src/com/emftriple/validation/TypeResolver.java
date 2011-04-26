/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple.validation;

import static com.emftriple.util.ETripleEcoreUtil.getESubClasses;
import static com.emftriple.util.EntityUtil.getRdfTypes;
import static com.emftriple.util.Functions.transform;
import static com.google.common.collect.Lists.newArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;

import com.emf4sw.rdf.RDFGraph;
import com.emf4sw.rdf.Resource;
import com.emf4sw.rdf.Triple;
import com.emf4sw.rdf.vocabulary.RDF;
import com.emftriple.transform.IMapping;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.internal.Maps;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class TypeResolver implements Function<URI, EClass> {

	private final Class<?> requestedTypes;

	private final RDFGraph aGraph;

	private final IMapping mapping;

	public TypeResolver(Class<?> entityClass, IMapping mapping, RDFGraph aGraph) {
		this.requestedTypes = entityClass;
		this.aGraph = aGraph;
		this.mapping = mapping;
	}

	@Override
	public EClass apply(URI from) {
		if (aGraph == null)
			return null;
		
		try {
			return identifyCandidateEClass(transform(aGraph, new GetListOfTypes()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static class GetListOfTypes implements Function<RDFGraph, List<Resource>> {

		@Override
		public List<Resource> apply(RDFGraph from) {
			final List<Resource> resources = newArrayList();
			for (Triple triple: from.getTriples()) {
				if (triple.getPredicate().getURI().equals(RDF.type)) {
					if (triple.getObject() instanceof Resource) {
						resources.add((Resource) triple.getObject());
					}
				}
			}
			return resources;
		}

	}

	private EClass identifyCandidateEClass(List<Resource> listOfTypes) throws IllegalArgumentException {
		if (listOfTypes.isEmpty()) {
			throw new IllegalArgumentException("Cannot load object from resource that does not specify any types.");
		}

		final EClass target = mapping.getEClass( requestedTypes );
		final Map<String, EClass> mapOfClasses = computeSubTypes( target );
		final List<Resource> validTypes = removeUnsatisfableTypes( target, listOfTypes, mapOfClasses );

		if (validTypes.isEmpty()) {
			throw new IllegalArgumentException("Cannot load object for the specified type.");
		}

		return accurateType(validTypes, mapOfClasses);
	}

	private Map<String, EClass> computeSubTypes(EClass eClass) {
		final Map<String, EClass> map = Maps.newLinkedHashMap();
		final String requestedURI = getRdfTypes(eClass).toString();
		final EList<EClass> subClasses = getESubClasses(eClass, mapping.getEClasses());
		map.put(requestedURI, eClass);

		for (EClass subClass: subClasses) {
			String subClassURI = getRdfTypes(subClass).toString();
			if (subClassURI != null) {
				map.put(subClassURI, subClass);
			}
		}

		return Collections.synchronizedMap(map);
	}

	private List<Resource> removeUnsatisfableTypes(EClass target, List<Resource> listOfTypes, Map<String, EClass> mapOfClasses) {
		final List<Resource> resultList = Lists.newLinkedList();
		for (Resource resource: listOfTypes) {
			if (mapOfClasses.containsKey(resource.getURI())) {
				EClass eClass = mapOfClasses.get(resource.getURI());
				if (eClass.equals(target) || eClass.getEAllSuperTypes().contains(target)){
					resultList.add(resource);
				}
			}
		}
		return resultList;
	}

	// Should choose the most accurate type according to the current state (data associated) to the resource. This 
	// is done by a integrity constraint checker (construct query generated for each EClass).
	private EClass accurateType(List<Resource> listOfTypes, Map<String, EClass> mapOfClasses) {
		final List<EClass> eClasses = Lists.newLinkedList();
		for (Resource resource: listOfTypes) {
			EClass eClass = mapOfClasses.get(resource.getURI());
			if (eClass != null) {
				eClasses.add( eClass );
			}
		}

		Collections.sort(eClasses, new Comparator<EClass>() {
			@Override
			public int compare(EClass o1, EClass o2) {
				if (o1 != null && o2 != null) {
					if (o2.getEAllSuperTypes().contains(o1)) {
						return 1;
					}
				}
				return 0;
			}});

		return eClasses.isEmpty() ? null : eClasses.get(0); 
	}
}
