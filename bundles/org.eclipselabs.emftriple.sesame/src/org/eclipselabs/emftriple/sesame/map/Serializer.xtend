package org.eclipselabs.emftriple.sesame.map

import java.util.Collection
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EAttribute
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipselabs.emftriple.map.ISerializer
import org.openrdf.model.Graph
import org.openrdf.model.ValueFactory
import org.openrdf.model.impl.URIImpl
import org.openrdf.model.impl.ValueFactoryImpl
import org.openrdf.model.vocabulary.RDF

class Serializer implements ISerializer<Graph> {
	
	override to(Resource resource, Graph graph) {
		resource.contents.forEach[to(it, graph, ValueFactoryImpl::instance)]
		graph
	}
	
	def toURI(EObject eObject) {
		EcoreUtil::getURI(eObject).toURI
	}
	
	def toURI(URI uri) {
		new URIImpl(uri.toString)
	}
	
	def toLiteral(Object value, EAttribute attribute, ValueFactory factory) {
		val stringValue = EcoreUtil::convertToString(attribute.EAttributeType, value)
		factory.createLiteral(stringValue)
	}

	def add(Graph graph, EObject eObject, EAttribute feature, Object value, ValueFactory factory) {
		graph.add(eObject.toURI, feature.toURI, value.toLiteral(feature, factory))
	}
	
	def add(Graph graph, EObject eObject, EReference feature, EObject value) {
		graph.add(eObject.toURI, feature.toURI, value.toURI)
	}

	def Graph to(EObject eObject, Graph graph, ValueFactory factory) {
		createTypeStatement(eObject, graph, factory)

		eObject.eClass.EAllAttributes.forEach[serialize(it, eObject, graph, factory)]
		eObject.eClass.EAllReferences.forEach[serialize(it, eObject, graph, factory)]
		graph
	}

	protected def createTypeStatement(EObject eObject, Graph graph, ValueFactory factory) {
		val subject = eObject.toURI		
		val predicate = RDF::TYPE
		val object = eObject.eClass.toURI

		graph.add(subject, predicate, object)
	}

	private def serialize(EAttribute attribute, EObject eObject, Graph graph, ValueFactory factory) {
		if (attribute.derived || attribute.transient || !eObject.eIsSet(attribute)) return null
		
		val value = eObject.eGet(attribute)

		if (attribute.many)
			(value as Collection<Object>).forEach[graph.add(eObject, attribute, it, factory)]
		else graph.add(eObject, attribute, value, factory)
	}

	private def serialize(EReference reference, EObject eObject, Graph graph, ValueFactory factory) {
		if (reference.derived || reference.transient || !eObject.eIsSet(reference)) return null

		val value = eObject.eGet(reference)
		println(value)
		if (reference.many)
			(value as Collection<Object>).forEach[
				serializeOne(eObject, reference, it as EObject, graph, factory)
			]
		else
			serializeOne(eObject, reference, value as EObject, graph, factory)
	}
	
	private def serializeOne(EObject subject, EReference reference, EObject value, Graph graph, ValueFactory factory) {
		println(subject)
		if (reference.containment) to(value, graph, factory)

		graph.add(subject, reference, value)
	}

}