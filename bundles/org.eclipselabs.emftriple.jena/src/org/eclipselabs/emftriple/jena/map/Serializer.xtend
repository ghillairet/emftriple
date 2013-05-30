package org.eclipselabs.emftriple.jena.map

import com.hp.hpl.jena.rdf.model.Model
import com.hp.hpl.jena.rdf.model.Resource
import java.util.Collection
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EAttribute
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipselabs.emftriple.map.ISerializer
import org.eclipselabs.emftriple.vocabularies.RDF

class Serializer implements ISerializer<Model> {

	override Model to(org.eclipse.emf.ecore.resource.Resource resource, Model graph) {
		resource.contents.forEach[to(it, graph)]
		graph
	}

	def Model to(EObject eObject, Model graph) {
		val uri = EcoreUtil::getURI(eObject)
		val eClassURI = EcoreUtil::getURI(eObject.eClass)
		val subject = graph.getResource(uri.toString)
		
		createTypeStatement(subject, eClassURI, graph)
		eObject.eClass.EAllAttributes.forEach[serialize(it, eObject, subject, graph)]
		eObject.eClass.EAllReferences.forEach[serialize(it, eObject, subject, graph)]
		
		graph
	}
	
	private def createTypeStatement(Resource subject, URI eClass, Model graph) {
		val predicate = graph.getProperty(RDF::type)
		val object = graph.getResource(eClass.toString)

		graph.add(subject, predicate, object)		
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
		val stringValue = EcoreUtil::convertToString(attribute.EAttributeType, value)
		val propertyURI = EcoreUtil::getURI(attribute)
		graph.add(resource, graph.getProperty(propertyURI.toString), stringValue)
	}

	private def serialize(EReference reference, EObject eObject, Resource resource, Model graph) {
		if (reference.derived || reference.transient || !eObject.eIsSet(reference)) return null;
		
		val value = eObject.eGet(reference)
		if (reference.many)
			(value as Collection<Object>).forEach[
				serializeOne(it, reference, resource, graph)
			]
		else
			serializeOne(value, reference, resource, graph)
	}

	private def serializeOne(Object value, EReference reference, Resource resource, Model graph) {
		val eObject = value as EObject
		val uri = EcoreUtil::getURI(eObject)
		val propertyURI = EcoreUtil::getURI(reference)

		if (reference.containment) to(eObject, graph)

		graph.add(resource, graph.getProperty(propertyURI.toString), graph.getResource(uri.toString))
	}

}
