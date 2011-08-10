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

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.emftriple.transform.Metamodel;

/**
 * The class {@link ETripleUtil} provides methods to easily retrieve objects from the content of a 
 * {@link Resource}.
 * 
 * @author guillaume hillairet
 * @since 0.8.0
 */
public class ETripleUtil {

	@SuppressWarnings("unchecked")
	public static <T> List<T> findAll(Class<T> klass, Collection<EObject> objects) {
		final EClass eClass = Metamodel.INSTANCE.getEClass(klass);
		if (eClass == null) {
			throw new IllegalArgumentException(klass+" is not part of the model.");
		}
		return (List<T>) EcoreUtil.getObjectsByType(objects, eClass);
	}
	
}
