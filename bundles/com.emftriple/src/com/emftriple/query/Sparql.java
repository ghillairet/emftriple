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

public class Sparql implements ETripleQuery {
	
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
		for (String s: select)
			res+=s+" ";
		res+="where { ";
		for (GraphPattern g: where)
			res+=g.get();
		res+="}";
		return res;
	}
		
	public interface GraphPattern {
		String get();
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
		
		public static GraphPattern triple(String s, String p, String o) {
			return new TripleGraphPattern(s, p, o);
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
		
		public static GraphPattern optional(GraphPattern... patterns) {
			return new OptionalGraphPattern(patterns);
		}
		
		public String get() {
			String res = "optional { ";
				for (GraphPattern p: patterns)
					res+=p.get();
			res+=" } ";
			return res;
		}
	}
	
	@Override
	public URI toURI(URI resourceURI) {
		String query = get().replaceAll(" ", "%20").replaceAll("#", "%23");
		return URI.createURI(resourceURI+"&query="+query);
	}

}
