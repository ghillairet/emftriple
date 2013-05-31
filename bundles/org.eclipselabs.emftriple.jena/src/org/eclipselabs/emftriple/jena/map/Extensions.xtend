package org.eclipselabs.emftriple.jena.map

import com.hp.hpl.jena.rdf.model.Model
import com.hp.hpl.jena.rdf.model.Resource
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EAttribute
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.util.EcoreUtil

package class Extensions {

	def create(EClass eClass) {
		EcoreUtil::create(eClass)
	}

	def URI(EObject eObject) {
		EcoreUtil::getURI(eObject)
	}

	def toURI(EObject eObject) {
		eObject.URI.toURI
	}

	def toURI(URI uri) {
		uri.toString		
	}

	def getResource(EObject eObject, Model graph) {
		graph.getResource(eObject.toURI)
	}

	def getProperty(EStructuralFeature eObject, Model graph) {
		graph.getProperty(eObject.toURI)
	}
	
	def getLiteral(Object value, EAttribute attribute, Model graph) {
		val stringValue = EcoreUtil::convertToString(attribute.EAttributeType, value)
		graph.createLiteral(stringValue)
	}
	
	def getEObject(ResourceSet resourceSet, Resource res) {
		val eURI = URI::createURI(res.URI)
		resourceSet.getEObject(eURI, true)
	}

}
