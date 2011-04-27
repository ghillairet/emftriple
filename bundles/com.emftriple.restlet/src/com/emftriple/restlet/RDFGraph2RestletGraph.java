package com.emftriple.restlet;

import java.util.Collection;

import org.restlet.data.Language;
import org.restlet.data.Reference;
import org.restlet.ext.rdf.Graph;

import com.emf4sw.rdf.Literal;
import com.emf4sw.rdf.Triple;
import com.emf4sw.rdf.URIElement;

/**
 * 
 * @author ghillairet
 * @since 0.1.0
 */
final class RDFGraph2RestletGraph {

	private RDFGraph2RestletGraph() {}
	
	public static Graph toGraph(Collection<Triple> triples) {
		final Graph rGraph = new Graph();
		
		for (Triple t: triples) {
			Reference sbj = new Reference(((URIElement) t.getSubject()).getURI());
			Reference prop = new Reference(t.getPredicate().getURI());
			if (t.getObject() instanceof Literal)
				rGraph.add(sbj, prop, literal((Literal) t.getObject()));
			else rGraph.add(sbj, prop, new Reference(((URIElement) t.getObject()).getURI()));
		}
		
		return rGraph;
	}

	private static org.restlet.ext.rdf.Literal literal(Literal object) {
		org.restlet.ext.rdf.Literal lit = new org.restlet.ext.rdf.Literal(object.getLexicalForm());
		if (object.getDatatype() != null)
			lit.setDatatypeRef(new Reference(object.getDatatype().getURI()));
		if (object.getLang() != null)
			lit.setLanguage(Language.valueOf(object.getLang()));
		
		return lit;
	}
}
