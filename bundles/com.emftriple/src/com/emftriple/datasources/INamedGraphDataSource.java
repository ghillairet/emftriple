/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple.datasources;

import org.eclipse.emf.common.util.URI;

import com.emf4sw.rdf.NamedGraph;
import com.emf4sw.rdf.RDFGraph;

/**
 * {@link INamedGraphDataSource} extends {@link IDataSource} with support for Named Graphs.
 * 
 * @see IDataSource
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.5.5
 */
public interface INamedGraphDataSource extends IDataSource {

	/**
	 * Returns the graph identified by the URI.
	 * 
	 * @param graphURI
	 * @return NamedGraph.
	 */
	NamedGraph getNamedGraph(URI graphURI);
	
	/**
	 * Returns all Named Graphs URIs.
	 */
	Iterable<String> getNamedGraphs();

	/**
	 * Returns true if the {@link IDataSource} contains the named graph.
	 * 
	 * @param graph
	 * @return true if graph is created.
	 */
	boolean containsGraph(URI graph);
	
	/**
	 * Executes the select query on the specified named graph.
	 * 
	 * @param query
	 * @param graph uri
	 * @return query result.
	 */
	IResultSet selectQuery(String query, URI graph);
	
	/**
	 * Executes the construct query on the specified named graph.
	 * 
	 * @param query
	 * @param graph
	 * @return query result.
	 * @throws MalformedQueryException 
	 */
	RDFGraph constructQuery(String query, URI graph);
	
	/**
	 * Executes the describe query on the specified named graph.
	 * 
	 * @param query
	 * @param graph
	 * @return query result.
	 */
	RDFGraph describeQuery(String query, URI graph);
	
	/**
	 * Executes the ask query on the specified named graph.
	 * 
	 * @param query
	 * @param graph
	 * @return query result.
	 */
	boolean askQuery(String query, URI graph);
	
}
