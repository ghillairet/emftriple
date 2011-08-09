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
package org.eclipselabs.emftriple.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipselabs.emftriple.datasources.IDataSource;
import org.eclipselabs.emftriple.datasources.IResultSet;
import org.eclipselabs.emftriple.datasources.IResultSet.Solution;


/**
 * The {@link SparqlQueries} class provides utility methods to create Sparql Queries needed to retrieve EObjects
 * from RDF data sources.
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.7.0
 */
public class SparqlQueries {

	private static final Metamodel metamodel = Metamodel.INSTANCE;
	
	public static List<String> selectAllTypes(IDataSource<?,?> dataSource, String key, String graph) {
		final List<String> types = new ArrayList<String>();
		final String query = typeOf(key, graph);

		final IResultSet resultSet = dataSource.selectQuery(query, graph);
		
		if (resultSet == null) {
			return null;
		}
		
		while (resultSet.hasNext()) {
			Solution solution = resultSet.next();
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
	
	public static String selectBlankNodeObject(String parentURI, EStructuralFeature containerFeature, String graph) {
		String query = prefixes+" SELECT DISTINCT ?bn_ ?p ?o WHERE { \n";
		
		if (graph != null) {
			query+=" GRAPH { <"+graph+"> { \n";
		}
		query+="<"+parentURI+"> <"+metamodel.getRdfType(containerFeature)+"> ?bn_ . ?bn_ ?p ?o . \n";

		if (graph != null)
			query+=" } \n";
		
		return query + " } ";
	}
	
	public static String selectObjectByClass(EClass eClass, String uri, String graph) {
		final StringBuilder sb = new StringBuilder(128);
		sb.append(prefixes)
		.append("\n SELECT DISTINCT ")
		.append(getVarFrom(eClass.getEAllStructuralFeatures()))
		.append(" WHERE { ");
		
		if (graph != null) {
			sb.append("\n GRAPH <").append(graph).append("> { ");
		}
		
		for (final String type : metamodel.getRdfTypes(eClass)) {
			sb.append("<").append(uri)
			.append("> rdf:type <")
			.append(type).append("> . \n");
		}

		for (final EStructuralFeature feature : eClass.getEAllStructuralFeatures()) {
			if (feature.getLowerBound() < 1) {
				sb.append(" OPTIONAL { \n <"+uri+"> <")
				.append(metamodel.getRdfType(feature)).append("> ?")
				.append(feature.getName()).append(" \n } \n");
			} else {
				sb.append("<"+uri+"> <")
				.append(metamodel.getRdfType(feature)+"> ?")
				.append(feature.getName())
				.append(" . \n");
			}
		}
		sb.append(" }");
		if (graph != null) {
			sb.append(" }");
		}
		return sb.toString();
	}
	
	private static String getVarFrom(List<EStructuralFeature> list) {
		String vars = "";
		for (int i=0; i<list.size(); i++)
			vars += "?"+list.get(i).getName()+" ";
		
		return vars;
	}
	
}
