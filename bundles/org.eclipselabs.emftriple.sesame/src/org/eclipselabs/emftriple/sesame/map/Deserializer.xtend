package org.eclipselabs.emftriple.sesame.map

import java.util.List
import java.util.Map
import org.eclipse.emf.ecore.EAttribute
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipselabs.emftriple.map.IDeserializer
import org.openrdf.model.Model
import org.openrdf.model.Resource
import org.openrdf.model.Statement
import org.openrdf.model.vocabulary.RDF

class Deserializer implements IDeserializer<Model> {
	
	extension Extensions extensions = new Extensions

	override from(Model graph, org.eclipse.emf.ecore.resource.Resource resource) {
		val resourceSet = resource.resourceSet
		val contents = resource.contents
		val Map<Resource, EObject> mapOfObjects = newHashMap()
		val List<EObject> listOfObjects = newArrayList()

		graph.fold(listOfObjects, [list, it |
			deSerialize(it, graph, mapOfObjects, resourceSet).appendTo(list)
		])
		
		mapOfObjects.keySet.forEach[
			val res = it
			val eObject = mapOfObjects.get(it)
			val eClass = eObject.eClass
			val references = eClass.EAllReferences
			references.forEach[
				deSerialize(it, res, eObject, graph, mapOfObjects, resourceSet)
			]
		]

		mapOfObjects.values.forEach[
			if (it.eContainer == null) contents.add(it)
		]
	}

	def deSerialize(Statement statement, Model graph, Map<Resource, EObject> mapOfObjects, ResourceSet resourceSet) {
		val sbj = statement.subject
		if (!mapOfObjects.containsKey(sbj)) {
			val subModel = graph.filter(sbj, null, null)
			val types = subModel.filter(sbj, RDF::TYPE, null)

			return switch eClass: resourceSet.getEObject(types.head.object) {
				EClass: createEObject(eClass, sbj, subModel, mapOfObjects) 
				default: null
			}
		}
		null
	}
	
	def createEObject(EClass eClass, Resource sbj, Model model, Map<Resource, EObject> mapOfObjects) {
		val eObject = EcoreUtil::create(eClass)
		mapOfObjects.put(sbj, eObject)

		eClass.EAllAttributes.forEach[
			deSerialize(it, sbj, eObject, model)
		]
		eObject
	}
	
	def deSerialize(EAttribute attribute, Resource sbj, EObject eObject, Model model) {
		if (attribute.derived || attribute.transient) return null

		val subModel = model.filter(sbj, attribute.toURI, null)
		 
		if (attribute.many) {
			val value = eObject.eGet(attribute) as List<Object>
			subModel.forEach[
				value.add(0, EcoreUtil::createFromString(
					attribute.EAttributeType,
					it.object.stringValue
				))
			]
		}
		else { 
			val value = EcoreUtil::createFromString(
				attribute.EAttributeType, 
				subModel.head.object.stringValue
			)
			if (value != null) eObject.eSet(attribute, value)
		}
	}
	
	def deSerialize(EReference reference, Resource sbj, EObject eObject, Model model, Map<Resource, EObject> mapOfObjects, ResourceSet resourceSet) {
		if (reference.derived || reference.transient) return null;

		val subModel = model.filter(sbj, reference.toURI, null)

		if (reference.many) {
			val values = eObject.eGet(reference) as List<Object>
			subModel.forEach[
				val v = it.object
				val o = mapOfObjects.get(v)
				if (o != null) values.add(0, o)
			]
		} else if (subModel.head != null) {
			val v = subModel.head.object
			val o = mapOfObjects.get(v)
			if (o != null) eObject.eSet(reference, o)
		}
	}

}

