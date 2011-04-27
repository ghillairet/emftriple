package com.emftriple.datasources;


/**
 * {@link ISparqlUpdateDataSource} extends {@link IDataSource} with support for SPARQL 1.1 Update Queries.
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */	
public interface ISparqlUpdateDataSource extends IMutableDataSource {

	/**
	 * Executes the update query.
	 * 
	 * @param query to execute
	 */
	void update(String query);
	
}
