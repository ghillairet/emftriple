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

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;

import com.emftriple.transform.IMapping;

/**
 * {@link ETripleMappingImpl} stores correspondances between model and ontology classes 
 * according to model annotations.
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.5.5
 */
public class ETripleMappingImpl implements IMapping {

	public ETripleMappingImpl() {}

	@Override
	public EClass getEClass(Class<?> aClass) throws IllegalArgumentException {
		for (EPackage ePackage: IMapping.ETriplePackageRegistry.INSTANCE.getRegisteredPackages()) {
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
				IMapping.ETriplePackageRegistry.INSTANCE.register((EPackage) o);
			}
			
			eC = doGetEClassByRdfType(uri);
			if (eC != null) {
				return eC;
			}
		}
		
		return null;
	}
	
	private EClass doGetEClassByRdfType(String uri) {
		if (IMapping.ETriplePackageRegistry.INSTANCE.values().containsKey(uri)) {
			return IMapping.ETriplePackageRegistry.INSTANCE.values().get(uri);
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

}