package org.eclipselabs.emftriple.sesame.map

import java.util.Collection
import org.eclipse.emf.ecore.EAttribute
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.resource.Resource
import org.openrdf.model.Model
import org.openrdf.model.ValueFactory
import org.openrdf.model.impl.ValueFactoryImpl
import org.openrdf.model.vocabulary.RDF

class Serializer {

	extension Extensions extensions = new Extensions

	def to(Resource resource, Model graph) {
		resource.contents.forEach[to(it, graph, ValueFactoryImpl::instance)]
		graph
	}

	def Model to(EObject eObject, Model graph, ValueFactory factory) {
		createTypeStatement(eObject, graph, factory)

		eObject.eClass.EAllAttributes.forEach[serialize(it, eObject, graph, factory)]
		eObject.eClass.EAllReferences.forEach[serialize(it, eObject, graph, factory)]
		graph
	}

	protected def createTypeStatement(EObject eObject, Model graph, ValueFactory factory) {
		val subject = eObject.toURI		
		val predicate = RDF::TYPE
		val object = eObject.eClass.toURI

		graph.add(subject, predicate, object)
	}

	private def serialize(EAttribute attribute, EObject eObject, Model graph, ValueFactory factory) {
		if (attribute.derived || attribute.transient || !eObject.eIsSet(attribute)) return null
		
		val value = eObject.eGet(attribute)

		if (attribute.many)
			(value as Collection<Object>).forEach[graph.add(eObject, attribute, it, factory)]
		else graph.add(eObject, attribute, value, factory)
	}

	private def serialize(EReference reference, EObject eObject, Model graph, ValueFactory factory) {
		if (reference.derived || reference.transient || !eObject.eIsSet(reference)) return null

		val value = eObject.eGet(reference)
		if (reference.many)
			(value as Collection<Object>).forEach[
				serializeOne(eObject, reference, it as EObject, graph, factory)
			]
		else
			serializeOne(eObject, reference, value as EObject, graph, factory)
	}
	
	private def serializeOne(EObject subject, EReference reference, EObject value, Model graph, ValueFactory factory) {
		if (reference.containment) to(value, graph, factory)

		graph.add(subject, reference, value)
	}

}
