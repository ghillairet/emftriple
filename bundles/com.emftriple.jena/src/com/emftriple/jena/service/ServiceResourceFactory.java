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
package com.emftriple.jena.service;

import java.util.Map;

import com.emftriple.datasources.IDataSource;
import com.emftriple.resource.ETripleResource;
import com.emftriple.resource.ETripleResourceFactoryImpl;

public class ServiceResourceFactory extends ETripleResourceFactoryImpl {

	@Override
	protected IDataSource createDataSource(Map<?, ?> options) {
		if (!options.containsKey(ETripleResource.OPTION_DATASOURCE_LOCATION)) {
			throw new IllegalArgumentException("Service location must be specified.");
		}
		return new JenaService((String) options.get(ETripleResource.OPTION_DATASOURCE_LOCATION));
	}
}
