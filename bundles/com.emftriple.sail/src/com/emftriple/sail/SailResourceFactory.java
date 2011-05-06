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
package com.emftriple.sail;

import java.util.Map;

import org.openrdf.sail.Sail;

import com.emftriple.datasources.IDataSource;
import com.emftriple.resource.ETripleResourceFactoryImpl;

public class SailResourceFactory extends ETripleResourceFactoryImpl {

	@Override
	protected IDataSource createDataSource(Map<?, ?> options) {
		IDataSource ds = null;
		if (options == null) {
			throw new IllegalArgumentException();
		}
		if (options.containsKey(SailDataSource.OPTION_SAIL_OBJECT)) {
			Object sail = options.get(SailDataSource.OPTION_SAIL_OBJECT);
			if (sail instanceof Sail) {
				ds = new SailDataSource((Sail) sail);
			} else {
				throw new ClassCastException();
			}
		}
		return ds;
	}
	
}
