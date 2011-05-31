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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.emftriple.transform.Metamodel;

public class ETripleUtil {

	@SuppressWarnings("unchecked")
	public static <T> List<T> findAll(Class<T> klass, Resource resource) {
		final EClass eClass = Metamodel.INSTANCE.getEClass(klass);
		if (eClass == null) {
			throw new IllegalArgumentException();
		}
		final Collection<Object> col = EcoreUtil.getObjectsByType(resource.getContents(), eClass);
		
		return (List<T>) col;
	}
}
