/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple.datasources;

import java.util.List;

import org.eclipse.emf.common.util.URI;

import com.emf4sw.rdf.RDFGraph;

/**
 * The {@link IDataSourceManager} interface represents the set of methods relative to the management of {@link IDataSource}.
 * A {@link IDataSourceManager} is supposed to manage a set of {@link IDataSource}. The implementation of a {@link IDataSourceManager} 
 * must take into account the delivery of SPAQRL queries to the required data sources.
 * 
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.5.5
 */
public interface IDataSourceManager {
	
	/**
	 * Adds an {@link RDFGraph} to a {@link IDataSource}. 
	 * The implementation must locate the {@link IDataSource} according to
	 * the value of the URI attribute of the {@link RDFGraph}.
	 *  
	 * @param graph
	 */
	void add(RDFGraph graph);

	/**
	 * Removes an {@link RDFGraph} to a {@link IDataSource}. 
	 * The implementation must locate the {@link IDataSource} according to
	 * the value of the URI attribute of the {@link RDFGraph}.
	 * 
	 * @param graph
	 */
	void remove(RDFGraph graph);

	/**
	 * Connect the managed {@link IDataSource}.
	 */
	void connect();

	/**
	 * Disconnect the managed {@link IDataSource}
	 */
	void disconnect();
	
	/**
	 * Returns the list of managed {@link IDataSource}
	 * 
	 * @return list of data sources
	 */
	List<IDataSource> getDataSources();
	
	/**
	 * Returns the {@link IDataSource} identify by a URI, or containing a corresponding 
	 * named graph.
	 *  
	 * @param graphURI identifying a data source
	 * 
	 * @return data source
	 */
	IDataSource getDataSourceByGraph(URI graphURI);
	
	/**
	 * Returns the DataSource according to its name.
	 * 
	 * @param name
	 * @return data source
	 */
	IDataSource getDataSource(String name);
	
	/**
	 * Executes a {@link SelectQuery} against a {@link IDataSource}
	 * 
	 * @param aQuery to execute
	 * @return query execution
	 */
	IResultSet executeSelectQuery(String aQuery);

	/**
	 * Executes an {@link AskQuery} against a {@link IDataSource}
	 * 
	 * @param aQuery to execute
	 * @return query execution
	 */
	boolean executeAskQuery(String aQuery);

	/**
	 * Executes a {@link ConstructQuery} against a {@link IDataSource}
	 * 
	 * @param aQuery to execute
	 * @return query execution
	 */
	RDFGraph executeConctructQuery(String aQuery);

	/**
	 * Executes a {@link DescribeQuery} against a {@link IDataSource}
	 * 
	 * @param aQuery to execute
	 * @return query execution
	 */
	RDFGraph executeDescribeQuery(String query);
	
	/**
	 * Executes an {@link UpdateQuery} against a {@link IDataSource}
	 * 
	 * @param aQuery to execute
	 * @return query execution
	 */
	int executeUpdateQuery(String aQuery);

	/**
	 * Returns the default data source
	 * 
	 * @return data source
	 */
	IDataSource getDefaultDataSource();

}
