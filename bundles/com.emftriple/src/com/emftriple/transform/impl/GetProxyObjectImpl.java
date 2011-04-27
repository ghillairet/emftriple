/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple.transform.impl;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.emftriple.ETriple;
import com.emftriple.resource.ETripleResource;
import com.emftriple.resource.URIBuilder;
import com.emftriple.transform.IGetObject;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class GetProxyObjectImpl extends AbstractGetObject implements IGetObject {

	public GetProxyObjectImpl(ETripleResource resource) {
		super(resource);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> entityClass, URI key) {
		T proxy = null;
		
		if (cache.hasKey(key.toString())) {
			proxy = (T) cache.getObjectByKey(key.toString());
		}

		if (proxy == null)
		{
			proxy = (T) get(ETriple.Registry.INSTANCE.getMapping().getEClass(entityClass), key);
		}
		
		return proxy;
	}

	@Override
	public EObject get(EClass eClass, URI key) {
		EObject proxy = null;
		if (cache.hasKey(key.toString())) {
			proxy = (EObject) cache.getObjectByKey(key.toString());
		}
		
		proxy = EcoreUtil.create(eClass);
		URI uri = URIBuilder.build(resource, key);
		if (!uri.fragment().startsWith("uri=")) {
			throw new IllegalArgumentException();
		}
		((InternalEObject)proxy).eSetProxyURI(uri);
		resource.getContents().add(proxy);
		resource.getPrimaryCache().cache(key.toString(), proxy);
		
		return proxy;
	}

	@Override
	public EObject getProxy(EObject proxy, EClass eClass, URI key) {
		// TODO Auto-generated method stub
		return null;
	}

}
