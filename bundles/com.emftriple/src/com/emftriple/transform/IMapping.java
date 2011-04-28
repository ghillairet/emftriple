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
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import com.emftriple.transform.impl.ETripleMappingImpl;
import com.emftriple.transform.impl.ETriplePackageRegistryImpl;

/**
 * IMapping
 * 
 */
public interface IMapping {
	
	IMapping INSTANCE = new ETripleMappingImpl();
	
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
	 * 
	 * 
	 *
	 */
	interface ETriplePackageRegistry {
		
		ETriplePackageRegistry INSTANCE = new ETriplePackageRegistryImpl();
	
		/**
		 * 
		 * @return
		 */
		Map<String,EClass> values();
		
		/**
		 * 
		 * @param ePackage
		 */
		void register(EPackage ePackage);

		/**
		 * 
		 * @return
		 */
		Collection<EPackage> getRegisteredPackages();

		/**
		 * 
		 * @param ePackage
		 * @return
		 */
		boolean hasPackage(EPackage ePackage);
		
	}
	
}
