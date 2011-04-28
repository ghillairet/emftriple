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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.emf4sw.rdf.Node;
import com.emf4sw.rdf.URIElement;
import com.emf4sw.rdf.vocabulary.RDF;
import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.INamedGraphDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.datasources.IResultSet.Solution;
import com.emftriple.transform.Metamodel;
import com.google.common.collect.Lists;

/**
 * 
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.7.0
 */
public class SparqlQueries {

	private static final Metamodel metamodel = Metamodel.INSTANCE;
	
	public static String ask(URI from, EClass eClass) {
		final StringBuffer buffer = new StringBuffer("ASK { " + from.toString());
		for (String aURI: metamodel.getRdfTypes(eClass)) {
			buffer.append( " <" + RDF.type + "> <" + aURI + "> ");
		}
		buffer.append(" }");

		return buffer.toString();
	}

	public static String describe(URI from, URI graph) {
		return "DESCRIBE <" + from.toString()+ ">";
	}
	
	public static List<String> selectAllTypes(IDataSource dataSource, String key, String graph) {
		final List<String> types = Lists.newArrayList();
		final String query = typeOf(key);
		final IResultSet resultSet = graph!= null?
				((INamedGraphDataSource)dataSource).selectQuery(query, graph): 
				dataSource.selectQuery(query);

		if (resultSet == null) {
			return null;
		}
		
		while (resultSet.hasNext()) {
			Solution solution = resultSet.next();
			Node node = solution.get("type");
			if (node instanceof URIElement)
			{
				types.add( ((URIElement) node).getURI() );
			}
		}
		
		return types;
	}

	public static Integer countObjectsByType(IDataSource source, EClass from) {
		String query = "SELECT ?n WHERE { ";
		List<String> uris = metamodel.getRdfTypes(from);

		query += "{ ?n <" + RDF.type + "> <" + uris.get(0) + "> } ";
		for (int i=1;i<uris.size();i++) {
			query += (i % 2 == 0) ? "" : "UNION " + "{ ?n <" + RDF.type + "> <" + uris.get(i) + "> } ";
		}
		query += "}";

		IResultSet rs = source.selectQuery(query);
		int i=0;
		for (;rs.hasNext();)
			i += rs.next() != null ? 1 : 0;

		return i;
	}

	public static String graphQuery(String graph) {
		return "CONSTRUCT { ?s ?p ?o } WHERE { GRAPH <" +graph+ "> { ?s ?p ?o} }";
	}

	public static String typeOf(String resource, String uri) {
		return "ASK WHERE { <" + 
		resource + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <" + uri + "> }";
	}

	public static String typeOf(String resourceURI) {
		return "SELECT ?type WHERE { <" + 
		resourceURI + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type }";
	}

	public static String constructSubject(String key, Object object) {
		return "CONSTRUCT { <" + key + "> ?p ?o } WHERE { <" + key + "> ?p ?o }";
	}

	public static String constructSubject(String key, EClass eClass) {
		return constructSubject(key, eClass, eClass.getEAllStructuralFeatures()).get(0);
	}

	public static List<String> constructSubjectService(String key, EClass eClass) {
		final List<String> queries = new ArrayList<String>();
		final List<EAttribute> attrs = eClass.getEAllAttributes();
		final List<EReference> refs = eClass.getEAllReferences();

		if (!attrs.isEmpty()) {
			final List<List<EAttribute>> sublists = split(attrs, new ArrayList<List<EAttribute>>());
			for (List<EAttribute> list: sublists) {
				queries.addAll(constructSubject(key, eClass, list));
			}
		}

		if (!refs.isEmpty())
			queries.addAll(constructSubject(key, eClass, refs));

		return queries;
	}

	private static List<List<EAttribute>> split(List<EAttribute> attrs, final List<List<EAttribute>> ret) {
		final int LIMIT = 2;
		int start = 0;
		
		for (int i=0; i < attrs.size(); i++) {
			if (((i+1)% LIMIT) == 0 || (i+1 == attrs.size())) {
				final List<EAttribute> other = new ArrayList<EAttribute>();
				for (int j=start; j<i+1; ++j) {
					other.add(attrs.get(j));
				}
				ret.add(other);
				start = i + 1;
			}
		}

		return ret;
	}

	private static List<String> constructSubject(String key, EClass eClass, List<? extends EStructuralFeature> feats) {
		final List<String> queries = new ArrayList<String>();
		final StringBuffer constructPattern = new StringBuffer("CONSTRUCT { ");
		final StringBuffer wherePattern = new StringBuffer("WHERE { <" + key + "> a ?o . ");
		constructPattern.append(" <" + key + "> a ?o . ");

		for (EStructuralFeature aFeature: feats) {
			if (ETripleEcoreUtil.getId(eClass) != null && !ETripleEcoreUtil.getId(eClass).equals(aFeature)) {
				String rdfType = metamodel.getRdfType(aFeature);
				constructPattern.append(" <" + key + "> <" + rdfType + "> ?" + aFeature.getName() + " . ");
				wherePattern.append(" OPTIONAL { <" + key + "> <" + rdfType + "> ?" + aFeature.getName() + " } ");
			}
		}
		constructPattern.append(" } ");
		wherePattern.append(" } ");
		constructPattern.append(wherePattern);

		queries.add(constructPattern.toString());

		return queries;
	}

	public static String constructSubject(URI key, Object object, Integer limit) {
		return "CONSTRUCT { <" + key + "> ?p ?o } WHERE { <" + key + "> ?p ?o }" + ((limit != null) ? " LIMIT " + limit : "");
	}
	
	private static final String prefixes = 
		"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
	
	public static String selectObjectByClass(EClass eClass, String uri) {
		String query = prefixes + "\n select " + getVarFrom(eClass.getEAllStructuralFeatures());
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
