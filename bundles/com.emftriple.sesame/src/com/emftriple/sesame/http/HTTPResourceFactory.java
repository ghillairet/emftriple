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
package com.emftriple.sesame.http;

import java.util.Map;

import org.openrdf.repository.http.HTTPRepository;

import com.emftriple.datasources.IDataSource;
import com.emftriple.resource.ETripleResource;
import com.emftriple.resource.ETripleResourceFactoryImpl;

public class HTTPResourceFactory extends ETripleResourceFactoryImpl {

	@Override
	protected IDataSource createDataSource(Map<?, ?> options) {
		if (options == null) {
			throw new IllegalArgumentException();
		}
		if (!options.containsKey(ETripleResource.OPTION_DATASOURCE_LOCATION)) {
			throw new IllegalArgumentException();
		}
		
		return new SesameHTTP(new HTTPRepository((String) options.get(ETripleResource.OPTION_DATASOURCE_LOCATION)));
	}
	
}
