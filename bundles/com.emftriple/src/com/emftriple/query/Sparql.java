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
package com.emftriple.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.emftriple.transform.MetamodelImpl;

public class Sparql implements Query {
	
	private List<String> select;
	private GraphPattern[] where;
	private Map<String, String> prefixes;
	
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
	
	public String get() {
		String res = "";
		for (String p: prefixes.keySet()) {
			res+="prefix "+p+": <"+prefixes.get(p)+"> ";
		}
		res+= "select ";
		if (select.isEmpty())
			res+="* ";
		else
			for (String s: select)
				res+=getVariable(s)+" ";
		res+="where { ";
		for (GraphPattern g: where)
			res+=g.get();
		res+="}";
		return res;
	}

	public static GraphPattern triple(Node s, EStructuralFeature feature, Node o) {
		final String rdf = MetamodelImpl.INSTANCE.getRdfType(feature);
		
		return new TripleGraphPattern(s.get(), iri(rdf).get(), o.get());
	}
	
	public static GraphPattern triple(Node s, Node p, EClass eClass) {
		final String rdf = MetamodelImpl.INSTANCE.getRdfTypes(eClass).get(0);
		
		return new TripleGraphPattern(s.get(), p.get(), iri(rdf).get());
	}
	
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

	public static GraphPattern optional(GraphPattern... patterns) {
		return new OptionalGraphPattern(patterns);
	}

	private static String getVariable(String var) {
		return var.startsWith("?") ? var : "?"+var;	
	}
	
	private static String getIRI(String iri) {
		return iri.startsWith("<") ? iri : "<"+iri+">";
	}
	
	public interface GraphPattern {
		String get();
	}
	
	public interface Node {
		String get();
	}
	
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
	
	public static class Filter implements GraphPattern {
		private String filter;

		Filter(String filter) {
			this.filter = filter;
		}

		@Override
		public String get() {
			return "filter ("+filter+")";
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

}
