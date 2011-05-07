package com.emftriple.sail.util;

import info.aduna.iteration.CloseableIteration;

import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryEvaluationException;

import com.emf4sw.rdf.Node;
import com.emf4sw.rdf.RDFFactory;
import com.emf4sw.rdf.Resource;
import com.emftriple.datasources.IResultSet;

public class SailResultSet implements IResultSet {

	private final CloseableIteration<? extends BindingSet, QueryEvaluationException> result;

	public SailResultSet(CloseableIteration<? extends BindingSet, QueryEvaluationException> result) {
		this.result = result;
	}

	@Override
	public boolean hasNext() {
		try {
			return result != null && result.hasNext();
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Solution next() {
		try {
			return new SailSolution(result.next());
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

	public static class SailSolution implements Solution {

		private final BindingSet solution;

		public SailSolution(BindingSet solution) {
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
//				System.out.println(value.getClass());
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

		@Override
		public Iterable<String> getSolutionNames() {
			return solution.getBindingNames();
		}

	}

}
