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
package com.emftriple.jena.util;

import java.util.Iterator;

import com.emftriple.datasources.IResultSet;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * 
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.1
 */
public class JenaResultSet implements IResultSet<RDFNode, Resource, Literal> {
	
	private com.hp.hpl.jena.query.ResultSet resultSet;
	
	public JenaResultSet(com.hp.hpl.jena.query.ResultSet resultSet) {
		this.resultSet = resultSet;
	}
	
	@Override
	public boolean hasNext() {
		try { 
			return resultSet != null && resultSet.hasNext();
		} catch (com.hp.hpl.jena.sparql.resultset.ResultSetException e) {
			return false;
		}
	}

	@Override
	public Solution<RDFNode, Resource, Literal> next() {
		return new JenaSolution(resultSet.next());
	}

	@Override
	public void remove() {
		resultSet.remove();
	}
	
	public static class JenaSolution implements Solution<RDFNode, Resource, Literal> {

		private QuerySolution solution;
		
		public JenaSolution(QuerySolution solution) {
			this.solution = solution;
		}

		@Override
		public RDFNode get(String varName) {
			return solution.get(varName);
		}

		@Override
		public boolean isResource(String varName) {
			if (!solution.contains(varName)) {
				return false;
			}
			
			return solution.get(varName).isResource();
		}

		@Override
		public Resource getResource(String varName) {
			return solution.getResource(varName);
		}

		@Override
		public boolean isLiteral(String varName) {
			return solution.get(varName).isLiteral();
		}

		@Override
		public Literal getLiteral(String varName) {
			return solution.getLiteral(varName);
		}

		@Override
		public Iterable<String> getSolutionNames() {
			return new Iterable<String>() {
				@Override
				public Iterator<String> iterator() {
					return solution.varNames();
				}
			};
		}
		
	}
}
