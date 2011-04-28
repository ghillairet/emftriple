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
package com.emftriple.datasources;

import com.emf4sw.rdf.Triple;

/**
 * The {@link IMutableNamedGraphDataSource} interface represents {@link IDataSource} being both mutable 
 * and that supports named graphs.
 * 
 * @see IMutableDataSource
 * @see INamedGraphDataSource
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.5.5
 */
public interface IMutableNamedGraphDataSource extends INamedGraphDataSource, IMutableDataSource {

	/**
	 * Deletes the content of the named graph.
	 */
	void deleteGraph(String graph);
	
	/**
	 * Adds triples to a named graph.
	 * 
	 * @param list of triples
	 * @param named graph URI
	 */	
	void add(Iterable<Triple> triples, String namedGraphURI);
	
	/**
	 * Deletes triples from a named graph.
	 * 
	 * @param list of triples
	 * @param named graph URI
	 */
	void remove(Iterable<Triple> triples, String namedGraphURI);
	
}
