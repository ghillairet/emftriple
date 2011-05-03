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
package com.emftriple.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.emf4sw.rdf.Node;
import com.emf4sw.rdf.URIElement;
import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.INamedGraphDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.datasources.IResultSet.Solution;
import com.emftriple.transform.Metamodel;

/**
 * 
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.7.0
 */
public class SparqlQueries {

	private static final Metamodel metamodel = Metamodel.INSTANCE;
	
	public static List<String> selectAllTypes(IDataSource dataSource, String key, String graph) {
		final List<String> types = new ArrayList<String>();
		final String query = typeOf(key);
		final IResultSet resultSet = graph != null ?
				((INamedGraphDataSource)dataSource).selectQuery(query, graph) : 
				dataSource.selectQuery(query);

		if (resultSet == null) {
			return null;
		}
		
		while (resultSet.hasNext()) {
			Solution solution = resultSet.next();
			Node node = solution.get("type");
			if (node instanceof URIElement) {
				types.add( ((URIElement) node).getURI() );
			}
		}
		
		return types;
	}

	public static String typeOf(String resourceURI) {
		return "SELECT distinct ?type WHERE { <" + 
		resourceURI + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type }";
	}
	
	private static final String prefixes = 
		"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
	
	public static String selectObjectByClass(EClass eClass, String uri) {
		String query = prefixes + "\n select distinct " + getVarFrom(eClass.getEAllStructuralFeatures());
		query+= "\n where { ";
		for (final String type: metamodel.getRdfTypes(eClass))
			query+="<"+uri+"> rdf:type <"+type+"> . \n";
		for (final EStructuralFeature feature: eClass.getEAllStructuralFeatures()) {
			if (feature.getLowerBound() < 1) {
				query+=" optional { \n <"+uri+"> <"+metamodel.getRdfType(feature)+"> ?"+feature.getName()+" \n } \n";
			} else {
				query+="<"+uri+"> <"+metamodel.getRdfType(feature)+"> ?"+feature.getName()+" . \n ";
			}
		}
		query+=" }";
		return query;
	}

	private static String getVarFrom(List<EStructuralFeature> list) {
		String vars = "";
		for (int i=0; i<list.size(); i++)
			vars += "?"+list.get(i).getName()+" ";
		
		return vars;
	}

}
