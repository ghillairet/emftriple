package org.eclipselabs.emftriple.sesame.map

import org.eclipse.emf.ecore.EAttribute
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.openrdf.model.Graph
import org.openrdf.model.ValueFactory
import org.openrdf.model.vocabulary.RDF

class NamedGraphSerializer extends Serializer {

	override def createTypeStatement(EObject eObject, Graph graph, ValueFactory factory) {
		graph.add(
			eObject.toURI, 
			RDF::TYPE,  
			eObject.eClass.toURI,
			eObject.eResource.URI.toURI
		)
	}

	override def add(Graph graph, EObject eObject, EAttribute feature, Object value, ValueFactory factory) {
		graph.add(
			eObject.toURI,
			feature.toURI,
			value.toLiteral(feature, factory), 
			eObject.eResource.URI.toURI
		)
	}

	override def add(Graph graph, EObject eObject, EReference feature, EObject value) {
		graph.add(
			eObject.toURI,
			feature.toURI,
			value.toURI,
			eObject.eResource.URI.toURI
		)
	}

}