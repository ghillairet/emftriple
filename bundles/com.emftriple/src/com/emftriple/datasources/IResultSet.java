package com.emftriple.datasources;

import java.util.Collection;
import java.util.Iterator;

import com.emf4sw.rdf.Literal;
import com.emf4sw.rdf.Node;
import com.emf4sw.rdf.Resource;
import com.emftriple.datasources.IResultSet.Solution;

/**
 * Wrapper class for ResultSet.
 * 
 * ResultSet corresponds to Select query result.
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public interface IResultSet extends Iterator<Solution> {

	Collection<String> getVarNames();
	
	public static interface Solution {
		
		Node get(String varName);
		
		boolean isResource(String varName);
		
		Resource getResource(String varName);
		
		boolean isLiteral(String varName);
		
		Literal getLiteral(String varName);
		
	}
	
}
