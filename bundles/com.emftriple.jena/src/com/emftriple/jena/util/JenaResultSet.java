package com.emftriple.jena.util;

import java.util.Collection;

import com.emf4sw.rdf.Literal;
import com.emf4sw.rdf.Node;
import com.emf4sw.rdf.RDFFactory;
import com.emf4sw.rdf.Resource;
import com.emf4sw.rdf.URIElement;
import com.emftriple.datasources.IResultSet;
import com.hp.hpl.jena.query.QuerySolution;

/**
 * 
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.1
 */
public class JenaResultSet implements IResultSet {
	
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
	public Solution next() {
		return new JenaSolution(resultSet.next());
	}

	@Override
	public void remove() {
		resultSet.remove();
	}

	@Override
	public Collection<String> getVarNames() {
		return resultSet.getResultVars();
	}
	
	public static class JenaSolution implements Solution {

		private QuerySolution solution;

		private RDFFactory aFactory = RDFFactory.eINSTANCE;
		
		public JenaSolution(QuerySolution solution) {
			this.solution = solution;
		}

		@Override
		public Node get(String varName) {
			if (!solution.contains(varName)) {
				return null;
			}

			Node node = null;
			if (solution.get(varName).isResource()) {
				node = aFactory.createResource();
				((URIElement)node).setURI(solution.get(varName).asResource().getURI());
			} else if (solution.get(varName).isLiteral()) {
				node = aFactory.createLiteral();
				((Literal)node).setLexicalForm(solution.get(varName).asLiteral().getLexicalForm());
			} else {
				node = aFactory.createBlankNode();
			}
			return node;
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
			if (!isResource(varName)) {
				throw new IllegalArgumentException();
			}
			
			final Resource node = aFactory.createResource();
			node.setURI(solution.get(varName).asResource().getURI());

			return node;
		}

		@Override
		public boolean isLiteral(String varName) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Literal getLiteral(String varName) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
