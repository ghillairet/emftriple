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
package com.emftriple;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import com.emftriple.resource.ETripleResource;


/**
 * 
 * @since 0.7.0
 */
public class ETriple {

	/**
	 * 
	 * @param <T>
	 * @param uri
	 * @param aClass
	 * @param resource
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T find(String uri, Class<T> aClass, Resource resource) {
		if (resource instanceof ETripleResource) {
			EObject object = resource.getEObject("uri="+uri);
			if (aClass.isInstance(object)) {
				return (T)object;
			} else {
				throw new ClassCastException();
			}
		}
		return null;
	}
	
}
