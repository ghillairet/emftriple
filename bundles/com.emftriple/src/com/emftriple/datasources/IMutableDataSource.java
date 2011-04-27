/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple.datasources;

import com.emf4sw.rdf.Triple;

/**
 * {@link IMutableDataSource} extends {@link IDataSource} with support for add and remove operations.
 * 
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.5.5
 */
public interface IMutableDataSource extends IDataSource {
	
	/**
	 * Adds the {@link Triple} from the {@link IDataSource}
	 * 
	 * @param triples to remove
	 */
	void add(Iterable<Triple> triples);
	 
	/**
	 * Removes the {@link Triple} from the {@link IDataSource}
	 * 
	 * @param triples to remove
	 */
	void remove(Iterable<Triple> triples);

	/**
	 * Delete the content of the {@link IDataSource}.
	 */
	void delete();
	
}
