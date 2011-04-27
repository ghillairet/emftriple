/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple.transform.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;

import com.emftriple.transform.IMapping;

/**
 * {@link AbstractMapping}
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.5.5
 */
public abstract class AbstractMapping implements IMapping {

	protected List<EPackage> ePackages;

	private List<EClass> eClasses;
	
	protected AbstractMapping() {
		this.ePackages = new ArrayList<EPackage>();
	}

	@Override
	public void addEPackage(EPackage ePackage) {
		ePackages.add(ePackage);
		eClasses = computeMappedClasses();
	}
	
	@Override
	public List<EPackage> getEPackages() {
		return ePackages;
	}

	@Override
	public List<EClass> getEClasses() {
		if (eClasses == null) {
			eClasses = computeMappedClasses();
		}
		return eClasses;
	}
		
	private List<EClass> computeMappedClasses() {
		final List<EClass> classes = new ArrayList<EClass>();
		for (EPackage ePackage: getEPackages()) 
		{
			for (EClassifier eClassifier: ePackage.getEClassifiers()) 
			{
				if (eClassifier instanceof EClass) 
				{
					classes.add((EClass) eClassifier);
				}
			}
		}
		return classes;
	}
}
