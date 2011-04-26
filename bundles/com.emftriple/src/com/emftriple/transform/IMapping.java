/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple.transform;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.emf4sw.rdf.NamedGraph;
import com.emf4sw.rdf.Node;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.5.5
 */
public interface IMapping {

	/**
	 * @return the EPackage currently mapped.
	 */
	List<EPackage> getEPackages();
		
    /**
     * @return the List of EClass being part of the mapping.
     */
    List<EClass> getEClasses();
    
	/**
	 * 
	 * @param aClass
	 * @return
	 */
	boolean isMappedClass(Class<?> aClass);
	
	/**
	 * 
	 * @param aClass
	 * @return
	 */
	EClass getEClass(Class<?> aClass) throws IllegalArgumentException;

	/**
	 * 
	 * @param string
	 * @return
	 */
	EClass getEClassWithRdfType(String URI);
	
	/**
	 * 
	 * @param eClass
	 * @return
	 */
	List<URI> getRdfTypes(EClass eClass);
	
	/**
	 * 
	 * @param eFeature
	 * @return
	 */
	URI getRdfType(EStructuralFeature eFeature);
	
	/**
	 * 
	 * @param aClass
	 * @return URI of the {@link NamedGraph} or null
	 */
	<T> URI getNamedGraph(Class<T> aClass) throws IllegalArgumentException;

	/**
	 * Returns the EClass corresponding to a list of rdf types. The most relevant type in the list 
	 * of types is selected. 
	 * 
	 * @param types
	 * @return
	 */
	EClass findEClassByRdfType(List<String> types);

	/**
	 * 
	 * @param types
	 * @return
	 */
	EClass findEClassByRdfType(EList<com.emf4sw.rdf.Resource> types);
	
}
