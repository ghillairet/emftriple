/*******************************************************************************
 * Copyright (c) 2011 Guillaume Hillairet.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Guillaume Hillairet - initial API and implementation
 *    Moritz Hoffmann - bnode handling
 *******************************************************************************/
package com.emftriple.transform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

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
	
	/**
	 * Generate a SPARQL pattern to get the value of a blank node. The access
	 * pattern consists of chained access to blank nodes up to the first object
	 * with a URI.
	 * 
	 * @param object
	 *            The parent object.
	 * @param reference
	 *            The reference pointing to the current bnode.
	 * @param pre
	 *            A {@link StringBuilder} that will contain the first part of
	 *            the query.
	 * @param post
	 *            A {@link StringBuilder} containing the latter part of the
	 *            query.
	 */
	private static void generateAccessPattern(final EObject object, EReference reference, StringBuilder pre, StringBuilder post){
		InternalEObject current = (InternalEObject) object;
		LinkedList<InternalEObject> objects = new LinkedList<InternalEObject>();
		
		while (EcoreUtil.getID(current) == null) {
			objects.add(current);
			current = (InternalEObject) current.eContainer();
		}
		
		pre.append('<');
		pre.append(EcoreUtil.getID(current));
		pre.append('>');
		int braces = 0;
		Iterator<InternalEObject> descendingIterator = objects
				.descendingIterator();
		while (descendingIterator.hasNext()) {
			InternalEObject next = descendingIterator.next();
			pre.append(" <");
			pre.append(metamodel.getRdfType(next.eContainmentFeature()));
			pre.append("> [");
			braces = braces + 1;
		}
		pre.append(" <");
		pre.append(metamodel.getRdfType(reference));
		pre.append("> [ ");
		post.ensureCapacity(post.length() + braces * 2);
		while (braces >= 0) {
			braces--;
			post.append(" ]");
		}

	}
	
	public static String typeOf(final EObject object, EReference reference, String graph) {
		StringBuilder pre = new StringBuilder();
		StringBuilder post = new StringBuilder();
		generateAccessPattern(object, reference, pre, post);
		String query = "SELECT DISTINCT ?type WHERE ";
		if (graph != null) {
			query+="\n { GRAPH <"+graph+"> ";
		}
		query+=" { "+pre.toString()+"<http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type" + post.toString()+" }";
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

		for (final EStructuralFeature feature : eClass
				.getEAllStructuralFeatures()) {
			if (feature.getLowerBound() < 1) {
				sb.append(" OPTIONAL { \n <");
				sb.append(uri);
				sb.append("> <");
				sb.append(metamodel.getRdfType(feature));
				sb.append("> ?");
				sb.append(feature.getName());
				sb.append(" \n } \n");
			} else {
				sb.append("<");
				sb.append(uri);
				sb.append("> <");
				sb.append(metamodel.getRdfType(feature));
				sb.append("> ?");
				sb.append(feature.getName());
				sb.append(" . \n ");
			}
		}
		sb.append(" }");
		if (graph != null) {
			sb.append(" }");
		}
		return sb.toString();
	}
	
	public static String selectObjectByURI(EClass eClass, EObject parent,
			EReference reference, String graph) {
		StringBuilder pre = new StringBuilder();
		StringBuilder post = new StringBuilder();
		generateAccessPattern(parent, reference, pre, post);

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
		for (final EStructuralFeature feature : eClass
				.getEAllStructuralFeatures()) {
			boolean optional = feature.getLowerBound() < 1;
			if (optional) {
				sb.append(" OPTIONAL { \n ");
			}
			sb.append(pre);
			sb.append(" <");
			sb.append(metamodel.getRdfType(feature));
			sb.append("> ?");
			sb.append(feature.getName());
			sb.append(post);
			if (optional) {
				sb.append(" \n } \n");
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

	public static <G, T, N, U, L> List<String> selectAllTypes(
			IDataSource<G, T, N, U, L> dataSource, EObject object,
			EReference reference, String graph) {
		final List<String> types = new ArrayList<String>();
		final String query = typeOf(object, reference, graph);

		final IResultSet<N, U, L> resultSet = dataSource.selectQuery(query,
				graph);

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

}
