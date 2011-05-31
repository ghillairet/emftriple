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
package com.emftriple.sesame.mem;

import java.io.File;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.openrdf.model.Graph;
import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.repository.Repository;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.nativerdf.NativeStore;

import com.emftriple.datasources.IDataSource;
import com.emftriple.sail.SailResourceImpl;
import com.emftriple.util.ETripleOptions;

public class MemoryResourceImpl 
	extends SailResourceImpl {

	public MemoryResourceImpl(URI uri) {
		super(uri);
	}
	
	@Override
	public IDataSource<Graph, Statement, Value, org.openrdf.model.URI, Literal> getDataSource() {
		return getDataSource(resourceSet.getLoadOptions());
	}
	
	@Override
	public IDataSource<Graph, Statement, Value, org.openrdf.model.URI, Literal> getDataSource(Map<?, ?> options) {
		if (options != null) {
			if (options.containsKey(ETripleOptions.OPTION_DATASOURCE_LOCATION)) {
				String dir = (String) options.get(ETripleOptions.OPTION_DATASOURCE_LOCATION);
				File file = new File(dir);
				String indexes = "spoc,posc,cosp";
				return new SesameMemory(new SailRepository(new NativeStore(file, indexes)));
			} else if (options.containsKey(ETripleOptions.OPTION_DATASOURCE_OBJECT)) {
				Repository repository = (Repository) options.get(ETripleOptions.OPTION_DATASOURCE_LOCATION);
				return new SesameMemory(repository);
			} else {
				return new SesameMemory(new SailRepository(new NativeStore()));
			}
		}
		return null;
	}

}
