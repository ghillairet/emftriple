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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * The Interface IGetObject.
 */
public interface IGetObject {

	/**
	 * Gets the.
	 *
	 * @param <T> the generic type
	 * @param entityClass the entity class
	 * @param key the key
	 * @return the t
	 */
	<T> T get(Class<T> entityClass, URI key);
	
	/**
	 * Gets the.
	 *
	 * @param eClass the e class
	 * @param key the key
	 * @return the e object
	 */
	EObject get(EClass eClass, URI key);
	
	/**
	 * Gets the proxy.
	 *
	 * @param proxy the proxy
	 * @param eClass the e class
	 * @param key the key
	 * @return the proxy
	 */
	EObject getProxy(EObject proxy, EClass eClass, URI key);
	
}
