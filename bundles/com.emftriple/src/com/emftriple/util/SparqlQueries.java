package com.emftriple.util;

import static com.emftriple.util.EntityUtil.getRdfTypes;

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
import com.emftriple.datasources.IDataSourceManager;
import com.emftriple.datasources.IResultSet;
import com.emftriple.datasources.IResultSet.Solution;
import com.google.common.collect.Lists;

/**
 * 
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.7.0
 */
public class SparqlQueries {

	public static String ask(URI from, EClass eClass) {
		final StringBuffer buffer = new StringBuffer("ASK { " + from.toString());
		for (URI aURI: getRdfTypes(eClass)) {
			buffer.append( " <" + RDF.type + "> <" + aURI.toString() + "> ");
		}
		buffer.append(" }");

		return buffer.toString();
	}

	public static String describe(URI from, URI graph) {
		return "DESCRIBE <" + from.toString()+ ">";
	}

	public static List<String> selectAllTypes(IDataSourceManager dataSourceManager, URI from) {
		if (from != null) 
		{
			return selectAllTypes(dataSourceManager, from.toString());
		}
		return Lists.newArrayList();
	}
	
	public static List<String> selectAllTypes(IDataSource dataSource, String key) {
		final List<String> types = Lists.newArrayList();
		final String query = typeOf(key);
		final IResultSet resultSet = dataSource.selectQuery(query);

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
	
	public static List<String> selectAllTypes(IDataSourceManager dataSourceManager, String key) {
		final List<String> types = Lists.newArrayList();
		final String query = typeOf(key);
		final IResultSet resultSet = dataSourceManager.executeSelectQuery(query);

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
		List<URI> uris = getRdfTypes(from);

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

	public static String graphQuery(URI graph) {
		return "CONSTRUCT { ?s ?p ?o } WHERE { GRAPH <" + graph.toString()+ "> { ?s ?p ?o} }";
	}

	public static String typeOf(URI resource, URI uri) {
		return "ASK WHERE { <" + 
		resource.toString() + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <" + uri.toString() + "> }";
	}

	public static String typeOf(URI resource) {
		return "SELECT ?type WHERE { <" + 
		resource.toString() + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type }";
	}

	public static String typeOf(String resourceURI) {
		return "SELECT ?type WHERE { <" + 
		resourceURI + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type }";
	}

	public static String constructSubject(URI key, Object object) {
		return "CONSTRUCT { <" + key + "> ?p ?o } WHERE { <" + key + "> ?p ?o }";
	}

	public static String constructSubject(URI key, EClass eClass) {
		return constructSubject(key, eClass, eClass.getEAllStructuralFeatures()).get(0);
	}

	public static List<String> constructSubjectService(URI key, EClass eClass) {
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

	private static List<String> constructSubject(URI key, EClass eClass, List<? extends EStructuralFeature> feats) {
		final List<String> queries = new ArrayList<String>();
		final StringBuffer constructPattern = new StringBuffer("CONSTRUCT { ");
		final StringBuffer wherePattern = new StringBuffer("WHERE { <" + key + "> a ?o . ");
		constructPattern.append(" <" + key + "> a ?o . ");

		for (EStructuralFeature aFeature: feats) {
			if (EntityUtil.getId(eClass) != null && !EntityUtil.getId(eClass).equals(aFeature)) {
				URI rdfType = EntityUtil.getRdfType(aFeature);
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
		for (final URI type: EntityUtil.getRdfTypes(eClass))
			query+="<"+uri+"> rdf:type <"+type+"> . \n";
		for (final EStructuralFeature feature: eClass.getEAllStructuralFeatures()) {
			if (feature.getLowerBound() < 1) {
				query+=" optional { \n <"+uri+"> <"+EntityUtil.getRdfType(feature)+"> ?"+feature.getName()+" \n } \n";
			} else {
				query+="<"+uri+"> <"+EntityUtil.getRdfType(feature)+"> ?"+feature.getName()+" . \n ";
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
