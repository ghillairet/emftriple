package org.eclipselabs.emftriple.sesame.map

import java.util.List
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EAttribute
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.util.EcoreUtil
import org.openrdf.model.Model
import org.openrdf.model.Value
import org.openrdf.model.ValueFactory
import org.openrdf.model.impl.URIImpl

package class Extensions {

	def getEObject(ResourceSet resourceSet, Value value) {
		resourceSet.getEObject(value.toURI, true)
	}
	
	def toURI(Value value) {
		URI::createURI(value.stringValue)
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

	def add(Model graph, EObject eObject, EAttribute feature, Object value, ValueFactory factory) {
		graph.add(eObject.toURI, feature.toURI, value.toLiteral(feature, factory))
	}
	
	def add(Model graph, EObject eObject, EReference feature, EObject value) {
		graph.add(eObject.toURI, feature.toURI, value.toURI)
	}

	def appendTo(EObject object, List<EObject> objects) {
		if (object != null) objects.add(object)
		objects
	}

}
