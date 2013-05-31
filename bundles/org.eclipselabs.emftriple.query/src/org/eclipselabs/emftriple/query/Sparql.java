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
package org.eclipselabs.emftriple.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;

/**
 * Sparql, fluent API to create SPARQL queries. 
 * 
 * @since 0.8.0
 */
public class Sparql implements Query {

	private List<String> select;
	private GraphPattern[] where;
	private Map<String, String> prefixes;
	private Integer limit;
	private String orderBy;
	private int offset;

	public Sparql() {
		select = new ArrayList<String>();
		prefixes = new HashMap<String, String>();
	}

	public Sparql prefix(String prefix, String uri) {
		prefixes.put(prefix, uri);
		return this;
	}

	public Sparql select(String select) {
		this.select.add(select);
		return this;
	}

	public Sparql where(GraphPattern... patterns) {
		if (this.where != null) {
			throw new IllegalArgumentException();
		}
		this.where = patterns;
		return this;
	}

	public Sparql limit(int limit) {
		this.limit = limit;
		return this;
	}

	public String get() {
		String res = "";
		for (String p: prefixes.keySet()) {
			res+="prefix "+p+": <"+prefixes.get(p)+"> ";
		}
		res+= "select ";
		if (select.isEmpty()) {
			res+="* ";
		} else {
			for (String s: select) {
				res+=getVariable(s)+" ";
			}
		}
		res+="where { ";
		for (GraphPattern g: where) {
			res+=g.get();
		}
		res+="}";
		if (orderBy != null) {
			res+= " order by ?"+orderBy;
		}
		if (offset > -1) {
			res+= " offset "+offset;
		}
		if (limit != null) {
			res+=" limit "+limit;
		}

		return res;
	}

	//	public static GraphPattern triple(Node s, EStructuralFeature feature, Node o) {
	//		final String rdf = MetamodelImpl.INSTANCE.getRdfType(feature);
	//		
	//		return new TripleGraphPattern(s.get(), iri(rdf).get(), o.get());
	//	}
	//	
	//	public static GraphPattern triple(Node s, Node p, EClass eClass) {
	//		final String rdf = MetamodelImpl.INSTANCE.getRdfTypes(eClass).get(0);
	//		
	//		return new TripleGraphPattern(s.get(), p.get(), iri(rdf).get());
	//	}

	public static GraphPattern triple(Node s, Node p, Node o) {
		return new TripleGraphPattern(s.get(), p.get(), o.get());
	}

	public static IRI iri(String iri) {
		return new IRI(getIRI(iri));
	}

	public static Literal literal(String lit) {
		return new Literal(lit, null, null);
	}

	public static Literal literal(String lit, String type) {
		return new Literal(lit, null, type);
	}

	public static Var var(String var) {
		return new Var(getVariable(var));
	}

	public static Filter filter(String expression) {
		return new Filter(expression);
	}

	public static GraphPattern regex(String expression) {
		return new RegEx(expression);
	}

	public static GraphPattern optional(GraphPattern... patterns) {
		return new OptionalGraphPattern(patterns);
	}

	public static GraphPattern union(GraphPattern... patterns) {
		return new UnionGraphPattern(patterns);
	}

	private static String getVariable(String var) {
		return var.startsWith("?") ? var : "?"+var;	
	}

	private static String getIRI(String iri) {
		return iri.startsWith("<") ? iri : "<"+iri+">";
	}

	/**
	 * SPARQL Graph Pattern.
	 * 
	 * @author guillaume hillairet
	 * @since 0.8.0
	 */
	public interface GraphPattern {
		String get();
	}

	/**
	 * SPARQL Node.
	 * 
	 * @author guillaume hillairet
	 * @since 0.8.0
	 */
	public interface Node {
		String get();
	}

	/**
	 * 
	 * @author guillaume hillairet
	 * @since 0.8.0
	 */
	public static class IRI implements Node {
		private String node;

		IRI(String node) {
			this.node = node;
		}

		@Override
		public String get() {
			return node;
		}

	}

	/**
	 * 
	 * @author guillaume hillairet
	 * @since 0.8.0
	 */
	public static class Var implements Node {
		private String node;

		Var(String node) {
			this.node = node;
		}

		@Override
		public String get() {
			return node;
		}

	}

	/**
	 * 
	 * @author guillaume hillairet
	 * @since 0.8.0
	 */
	public static class Literal implements Node {
		private String node;
		@SuppressWarnings("unused")
		private String lang;
		@SuppressWarnings("unused")
		private String type;

		Literal(String node, String lang, String type) {
			this.node = node;
			this.lang = lang;
			this.type = type;
		}

		@Override
		public String get() {
			String var = "?"+node;
			String literal = var + " . filter (str("+var+") = \""+node+"\")";
			//			if (type != null) {
			//				literal += "^^"+type;
			//			}
			//			if (lang != null) {
			//				literal += "@@"+lang;
			//			}
			return literal;
		}

	}

	/**
	 * 
	 * @author guillaume hillairet
	 * @since 0.8.0
	 */
	public static class TripleGraphPattern implements GraphPattern {
		private String s;
		private String p;
		private String o;

		TripleGraphPattern(String s, String p, String o) {
			this.s = s;
			this.p = p;
			this.o = o;
		}

		public String get() {
			return s+" "+p+" "+o+" . ";
		}
	}

	/**
	 * 
	 * @author guillaume hillairet
	 * @since 0.8.0
	 */
	public static class OptionalGraphPattern implements GraphPattern {
		private GraphPattern[] patterns;

		OptionalGraphPattern(GraphPattern... patterns) {
			this.patterns = patterns;
		}

		public String get() {
			String res = "optional { ";
			for (GraphPattern p: patterns)
				res+=p.get();
			res+=" } ";
			return res;
		}
	}

	public static class UnionGraphPattern implements GraphPattern {
		private GraphPattern[] patterns;

		UnionGraphPattern(GraphPattern... patterns) {
			this.patterns = patterns;
		}

		@Override
		public String get() {
			String res = " { "+patterns[0].get()+" } ";
			if (patterns.length > 1) {
				for (int i=1; i<patterns.length;i++) {
					res+=" union { "+patterns[i].get()+" } ";
				}
			}
			return res;
		}

	}

	public static class Filter implements GraphPattern {
		protected String filter;

		Filter(String filter) {
			this.filter = filter;
		}

		@Override
		public String get() {
			return "filter ("+filter+")";
		}
	}

	public static class RegEx extends Filter {

		RegEx(String filter) {
			super(filter);
		}

		@Override
		public String get() {
			return "filter regex("+filter+")";
		}

	}

	@Override
	public URI toURI(URI resourceURI) {
		final String query = get().replaceAll(" ", "%20").replaceAll("#", "%23");
		final URI uri = resourceURI.hasQuery() ?
				URI.createURI(resourceURI+"&query="+query) :
					URI.createURI(resourceURI+"?query="+query);

				return uri;
	}

	public Sparql orderBy(String orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	public Sparql offset(int offset) {
		this.offset = offset;
		return this;
	}
}
