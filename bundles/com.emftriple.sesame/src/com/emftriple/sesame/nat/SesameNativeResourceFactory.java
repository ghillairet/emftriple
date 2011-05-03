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
package com.emftriple.sesame.nat;

import java.io.File;
import java.util.Map;

import org.openrdf.repository.Repository;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.nativerdf.NativeStore;

import com.emftriple.datasources.IDataSource;
import com.emftriple.resource.ETripleResource;
import com.emftriple.resource.ETripleResourceFactoryImpl;

public class SesameNativeResourceFactory extends ETripleResourceFactoryImpl {

	@Override
	protected IDataSource createDataSource(Map<?, ?> options) {
		Repository repository = null;
		
		if (options != null && options.containsKey(ETripleResource.OPTION_DATASOURCE_LOCATION)) {
			Object file = options.get(ETripleResource.OPTION_DATASOURCE_LOCATION);
			if (file instanceof File) {
				NativeStore store = new NativeStore((File) file);
				repository = new SailRepository(store);
			}
		}
		
		return repository == null ? new SesameNative(new SailRepository(new NativeStore())) : new SesameNative(repository);
	}

}
