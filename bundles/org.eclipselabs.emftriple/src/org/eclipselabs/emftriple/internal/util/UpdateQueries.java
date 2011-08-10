/**
 * 
 */
package org.eclipselabs.emftriple.internal.util;

import java.util.Collection;

/**
 * @author ghillairet
 *
 */
public abstract class UpdateQueries<T> {

	/**
	 * Returns the Update Query to Update An Object.
	 * 
	 * @param string
	 * @param triples
	 * @param string2
	 * @return
	 */
	public String createUpdateQuery(String key, Collection<T> triples, String graph) {
		String query = (graph == null ? "" : "WITH <"+graph+"> ");
		query+="DELETE { <"+key+"> ?p ?o } ";
		query+="INSERT { ";
		String tt = "";
		for (T triple: triples) {
			tt+=getTriples(triple);
		}
		query+=tt;
		
		
		query+=" } WHERE { <"+key+"> ?p ?o }";
		
		return query;
	}

	protected abstract String getTriples(T triple);
}
