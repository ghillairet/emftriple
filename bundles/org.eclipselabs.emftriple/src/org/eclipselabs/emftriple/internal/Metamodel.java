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
package org.eclipselabs.emftriple.internal;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * The Metamodel interface allows access to the mapping information.
 *   
 * 
 * @author guillaume hillairet
 * @since 0.8.0	
 */
public interface Metamodel {
	
	/**
	 * INSTANCE
	 */
	Metamodel INSTANCE = new MetamodelImpl();
	
	/**
	 * 
	 * @param eClass
	 * @return
	 */
	List<String> getRdfTypes(EClass eClass);
	
	/**
	 * 
	 * @param feature
	 * @return
	 */
	String getRdfType(EStructuralFeature feature);
	
	/**
	 * 
	 * @param uri
	 * @return
	 */
	EClass getEClassByRdfType(String uri);
	
	/**
	 * 
	 * @param uris
	 * @return
	 */
	EClass getEClassByRdfType(List<String> uris);
	
	/**
	 * Gets the e class.
	 *
	 * @param aClass the a class
	 * @return the e class
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	EClass getEClass(Class<?> aClass);
	
	/**
	 * The Registry interface represents a registry of model elements.
	 * 
	 * @author guillaume hillairet
	 * @since 0.8.0
	 */
	interface Registry {
		
		Registry INSTANCE = new MetamodelRegistryImpl();
	
		/**
		 * Returns classes currently registered as entity classes.
		 */
		Map<String, EClass> mapOfEntities();
		
		/**
		 * Register a {@link EPackage} in the metamodel registry.
		 * 
		 * @param ePackage
		 */
		void register(EPackage ePackage);

		/**
		 * Returns the collection of registered {@link EPackage}.
		 */
		Collection<EPackage> getRegisteredPackages();

		/**
		 * Tests if the {@link EPackage} is registered.
		 * @param ePackage
		 * @return true if {@link EPackage} is registered.
		 */
		boolean hasPackage(EPackage ePackage);
		
	}

}
