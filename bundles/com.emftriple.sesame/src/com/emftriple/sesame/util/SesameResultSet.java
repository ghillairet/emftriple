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
package com.emftriple.sesame.util;

import java.util.Collection;

import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;

import com.emf4sw.rdf.Node;
import com.emf4sw.rdf.RDFFactory;
import com.emf4sw.rdf.Resource;
import com.emftriple.datasources.IResultSet;

public class SesameResultSet implements IResultSet {

	private final TupleQueryResult result;

	public SesameResultSet(TupleQueryResult result)
	{
		this.result = result ;
	}

	@Override
	public boolean hasNext() {
		try {
			return result.hasNext();
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Solution next() {
		try {
			return new SesameSolution(result.next());
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void remove() {
		try {
			result.remove();
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Collection<String> getVarNames() {
		return result.getBindingNames();
	}

	public static class SesameSolution implements Solution {

		private final BindingSet solution;

		public SesameSolution(BindingSet solution) {
			this.solution = solution;
		}

		@Override
		public Node get(String varName) {
			RDFFactory aFactory = RDFFactory.eINSTANCE;
			Node node = null;
			Value value = solution.getValue(varName);

			if (value == null) {
				return null;
			}
			else if ( value instanceof Literal )
			{
				node = aFactory.createLiteral();
				((com.emf4sw.rdf.Literal)node).setLexicalForm(((Literal) value).getLabel().split("^^")[0]);
			}  
			else if ( value instanceof URI )
			{
				node = aFactory.createResource();
				((Resource)node).setURI( ((URI) value).getNamespace() + ((URI) value).getLocalName() );
			}
			else if ( value instanceof BNode )
			{
				node = aFactory.createBlankNode();
			}
			else
			{
				throw new IllegalArgumentException("Not a concrete value "+value) ;	
			}
			return node;
		}

		@Override
		public boolean isResource(String varName) {
			return solution.getValue(varName) instanceof URI;
		}

		@Override
		public Resource getResource(String varName) {
			final Value value = solution.getValue(varName);
			final Resource node = RDFFactory.eINSTANCE.createResource();
			((Resource)node).setURI( ((URI) value).getNamespace() + ((URI) value).getLocalName() );
			
			return node;
		}

		@Override
		public boolean isLiteral(String varName) {
			return solution.getValue(varName) instanceof Literal;
		}

		@Override
		public com.emf4sw.rdf.Literal getLiteral(String varName) {
			final Value value = solution.getValue(varName);
			final com.emf4sw.rdf.Literal node = RDFFactory.eINSTANCE.createLiteral();
			((com.emf4sw.rdf.Literal)node).setLexicalForm(((Literal) value).getLabel().split("^^")[0]);
			
			return node;
		}

	}

}
