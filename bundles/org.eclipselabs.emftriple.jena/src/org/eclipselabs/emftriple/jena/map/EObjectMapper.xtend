package org.eclipselabs.emftriple.jena.map

import com.hp.hpl.jena.rdf.model.Model
import com.hp.hpl.jena.rdf.model.ModelFactory
import org.eclipse.emf.ecore.resource.Resource

class EObjectMapper {

	def to(Resource resource) {
		to(ModelFactory::createDefaultModel, resource)
	}

	def to(Model model, Resource resource) {
		val serializer = new Serializer
		serializer.to(resource, if (model == null) ModelFactory::createDefaultModel else model)
	}

	def from(Model graph, Resource resource) {
		val deserializer = new Deserializer
		deserializer.from(graph, resource)
	}

}
