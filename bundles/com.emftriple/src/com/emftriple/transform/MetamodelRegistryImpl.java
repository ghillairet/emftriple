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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;

import com.emftriple.transform.Metamodel.Registry;


public class MetamodelRegistryImpl implements Registry {

	private static final Map<String,EClass> mapOfRdfTypes = new HashMap<String,EClass>();
	
	private static final Map<String, EPackage> registeredPackages = new HashMap<String, EPackage>();
	
	public MetamodelRegistryImpl() {
	}

	@Override
	public void register(EPackage ePackage) {
		if (!registeredPackages.containsKey(ePackage.getNsURI())) {
			registeredPackages.put(ePackage.getNsURI(), ePackage);
			mapOfRdfTypes.putAll(createMapOfRdfTypes(ePackage));
		}
	}

	private Map<String, EClass> doCreateMapOfRdfTypes(EPackage ePackage) {
		final Map<String, EClass> mapOfRdfTypes = new HashMap<String, EClass>();
		for (EClassifier eClass: ePackage.getEClassifiers()) {
			if (eClass instanceof EClass) {
				for (String s: Metamodel.INSTANCE.getRdfTypes((EClass) eClass)) {
					mapOfRdfTypes.put(s, (EClass) eClass);
				}
			}
		}
		return mapOfRdfTypes;
	}
	
	private Map<String, EClass> createMapOfRdfTypes(EPackage ePackage) {
		if (!ePackage.getESubpackages().isEmpty()) {
			Map<String, EClass> map = doCreateMapOfRdfTypes(ePackage);
			for (EPackage ePackage2: ePackage.getESubpackages()) {
				map.putAll( createMapOfRdfTypes(ePackage2) );
			}
			return map;
		} else {
			return doCreateMapOfRdfTypes(ePackage);
		}
	}

	@Override
	public Collection<EPackage> getRegisteredPackages() {
		return registeredPackages.values();
	}

	@Override
	public Map<String, EClass> mapOfEntities() {
		return mapOfRdfTypes;
	}

	@Override
	public boolean hasPackage(EPackage ePackage) {
		return registeredPackages.containsKey(ePackage.getNsURI());
	}

}
