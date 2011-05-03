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
package com.emftriple.jena.tdb;

import java.util.Map;

import com.emftriple.datasources.IDataSource;
import com.emftriple.resource.ETripleResource;
import com.emftriple.resource.ETripleResourceFactoryImpl;

/**
 * 
 * @author ehilgui
 *
 */
public class TDBResourceFactory extends ETripleResourceFactoryImpl {

	@Override
	protected IDataSource createDataSource(Map<?, ?> options) {
		if (!options.containsKey(ETripleResource.OPTION_DATASOURCE_LOCATION)) {
			return new JenaTDB();
		} else {
			return new JenaTDB((String) options.get(ETripleResource.OPTION_DATASOURCE_LOCATION));
		}
	}
}
