package org.eclipselabs.emftriple.jena.map

import com.hp.hpl.jena.rdf.model.Model
import com.hp.hpl.jena.rdf.model.Resource
import java.util.Collection
import org.eclipse.emf.ecore.EAttribute
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipselabs.emftriple.vocabularies.RDF

class Serializer {

	extension Extensions = new Extensions

	def Model to(org.eclipse.emf.ecore.resource.Resource resource, Model graph) {
		resource.contents.forEach[to(it, graph)]
		graph
	}

	def Model to(EObject eObject, Model graph) {
		val subject = eObject.getResource(graph)

		eObject.createTypeStatement(graph)
		eObject.eClass.EAllAttributes.forEach[serialize(it, eObject, subject, graph)]
		eObject.eClass.EAllReferences.forEach[serialize(it, eObject, subject, graph)]
		
		graph
	}
	
	private def createTypeStatement(EObject eObject, Model graph) {
		val predicate = graph.getProperty(RDF::type)
		val object = eObject.eClass.getResource(graph)

		graph.add(eObject.getResource(graph), predicate, object)		
	}

	private def serialize(EAttribute attribute, EObject eObject, Resource resource, Model graph) {
		if (attribute.derived || attribute.transient || !eObject.eIsSet(attribute)) return null;

		val value = eObject.eGet(attribute)
		if (attribute.many) 
			(value as Collection<Object>).forEach[
				serializeOne(it, attribute, resource, graph)
			]
		else
			serializeOne(value, attribute, resource, graph)
	}

	private def serializeOne(Object value, EAttribute attribute, Resource resource, Model graph) {
		graph.add(resource, attribute.getProperty(graph), value.getLiteral(attribute, graph))
	}

	private def serialize(EReference reference, EObject eObject, Resource resource, Model graph) {
		if (reference.derived || reference.transient || !eObject.eIsSet(reference)) return null;
		
		val value = eObject.eGet(reference)
		if (reference.many)
			(value as Collection<Object>).forEach[
				serializeOne(it as EObject, reference, resource, graph)
			]
		else
			serializeOne(value as EObject, reference, resource, graph)
	}

	private def serializeOne(EObject value, EReference reference, Resource resource, Model graph) {
		if (reference.containment) to(value, graph)

		graph.add(resource, reference.getProperty(graph), value.getResource(graph))
	}

}
