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

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;

import com.emftriple.datasources.IDataSource;

public abstract class ETripleResourceFactoryImpl extends ResourceFactoryImpl {

	@Override
	public Resource createResource(URI uri) {
		return new ETripleResourceImpl(uri) {
			@Override
			public IDataSource getDataSource(Map<?, ?> options) {
				return createDataSource(options);
			}
			@Override
			public IDataSource getDataSource() {
				return createDataSource(getResourceSet().getLoadOptions());
			}
		};
	}
	
	protected abstract IDataSource createDataSource(Map<?, ?> options);
		
}
