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
package com.emftriple.transform;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.datasources.IResultSet.Solution;

/**
 * 
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.7.0
 */
public class SparqlQueries {

	private static final Metamodel metamodel = Metamodel.INSTANCE;
	
	public static <G, T, N, U, L> List<String> selectAllTypes(IDataSource<G, T, N, U, L> dataSource, String key, String graph) {
		final List<String> types = new ArrayList<String>();
		final String query = typeOf(key, graph);

		final IResultSet<N, U, L> resultSet = dataSource.selectQuery(query, graph);

		if (resultSet == null) {
			return null;
		}
		
		while (resultSet.hasNext()) {
			Solution<N, U, L> solution = resultSet.next();
			if (solution.isResource("type")) {
				types.add(solution.getResource("type").toString());
			}
		}
		
		return types;
	}

	public static String typeOf(String resourceURI, String graph) {
		String query = "SELECT DISTINCT ?type WHERE ";
		if (graph != null) {
			query+="\n { GRAPH <"+graph+"> ";
		}
		query+=" { <"+resourceURI+"> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type }";
		if (graph != null) {
			return query+=" }";
		}
		return query;
	}
	
	private static final String prefixes = 
		"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
	
	public static String selectObjectByClass(EClass eClass, String uri,
			String graph) {
		StringBuilder sb = new StringBuilder(128);
		sb.append(prefixes);
		sb.append("\n SELECT DISTINCT ");
		sb.append(getVarFrom(eClass.getEAllStructuralFeatures()));
		sb.append(" WHERE { ");
		if (graph != null) {
			sb.append("\n GRAPH <");
			sb.append(graph);
			sb.append("> { ");
		}
		for (final String type : metamodel.getRdfTypes(eClass)) {
			sb.append("<");
			sb.append(uri);
			sb.append("> rdf:type <");
			sb.append(type);
			sb.append("> . \n");
		}

		selectFeatures(sb, eClass, uri);
		sb.append(" }");
		if (graph != null) {
			sb.append(" }");
		}
		return sb.toString();
	}
	
	public static String selectObjectByURI(EClass eClass, String uri,
			String graph) {
		StringBuilder sb = new StringBuilder(128);
		sb.append(prefixes);
		sb.append("\n SELECT DISTINCT ");
		sb.append(getVarFrom(eClass.getEAllStructuralFeatures()));
		sb.append(" WHERE { ");
		if (graph != null) {
			sb.append("\n GRAPH <");
			sb.append(graph);
			sb.append("> { ");
		}

		selectFeatures(sb, eClass, uri);
		sb.append(" }");
		if (graph != null) {
			sb.append(" }");
		}
		return sb.toString();
	}

	private static void selectFeatures(StringBuilder result, EClass eClass,
			String uri) {
		for (final EStructuralFeature feature : eClass
				.getEAllStructuralFeatures()) {
			if (feature.getLowerBound() < 1) {
				result.append(" OPTIONAL { \n <");
				result.append(uri);
				result.append("> <");
				result.append(metamodel.getRdfType(feature));
				result.append("> ?");
				result.append(feature.getName());
				result.append(" \n } \n");
			} else {
				result.append("<");
				result.append(uri);
				result.append("> <");
				result.append(metamodel.getRdfType(feature));
				result.append("> ?");
				result.append(feature.getName());
				result.append(" . \n ");
			}
		}
	}

	private static String getVarFrom(List<EStructuralFeature> list) {
		String vars = "";
		for (int i=0; i<list.size(); i++)
			vars += "?"+list.get(i).getName()+" ";
		
		return vars;
	}

}
