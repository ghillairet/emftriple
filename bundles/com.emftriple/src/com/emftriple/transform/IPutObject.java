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

import org.eclipse.emf.ecore.EObject;

import com.emf4sw.rdf.Triple;

/**
 * IPutObject.
 */
public interface IPutObject {

	/**
	 * Put.
	 *
	 * @param from the from
	 * @return the collection
	 */
	Collection<Triple> put(EObject from);
	
}
