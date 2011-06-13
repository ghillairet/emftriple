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
package com.emftriple.resource;

import java.util.HashMap;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * The class {@link ETripleResourceCacheImpl} is an implementation of a cache system for 
 * {@link ETripleResource}. It allows fast retrieval of EObjects available in the associated 
 * {@link Resource}. 
 * 
 * @author guillaume hillairet
 * @since 0.8.0
 */
public class ETripleResourceCacheImpl implements ETripleCache {
	
	private final HashMap<String, EObject> keyToObjects = new HashMap<String, EObject>();
	
	private final HashMap<Integer, String> objectToKey = new HashMap<Integer, String>();

	public ETripleResourceCacheImpl() {}

	@Override
	public boolean hasKey(String key) {
		return keyToObjects.containsKey(key);
	}

	@Override
	public boolean hasObject(EObject obj) {
		return objectToKey.containsKey(obj.hashCode());
	}

	@Override
	public EObject getObjectByKey(String key) {
		return keyToObjects.get(key);
	}

	@Override
	public String getObjectId(EObject obj) {
		return objectToKey.get(obj.hashCode());
	}

	@Override
	public void cache(String key, EObject obj) {
		keyToObjects.put(key, obj);
		objectToKey.put(obj.hashCode(), key);
	}

	public Set<String> getKeys() {
		return keyToObjects.keySet();
	}
	
}
