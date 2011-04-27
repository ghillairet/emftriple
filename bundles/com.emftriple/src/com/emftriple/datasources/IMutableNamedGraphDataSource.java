/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple.datasources;

import com.emf4sw.rdf.NamedGraph;
import com.emf4sw.rdf.RDFGraph;
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
	 * 
	 * @param graph
	 */
	void deleteGraph(String graph);
	
	/**
	 * 
	 * @param graph
	 * @throws DataSourceException
	 */
	void add(RDFGraph graph, String namedGraphURI);
	
	
	void add(Iterable<Triple> triples, String namedGraphURI);
	
	/**
	 * 
	 * @param graph
	 * @throws DataSourceException
	 */
	void remove(NamedGraph graph);
	
}
