package org.eclipselabs.emftriple.jena.map

import com.hp.hpl.jena.rdf.model.Model
import com.hp.hpl.jena.rdf.model.ModelFactory
import java.util.Map
import org.eclipse.emf.ecore.resource.Resource

class EObjectMapper {

	def to(Resource resource, Map<?, ?> options) {
		to(ModelFactory::createDefaultModel, resource, options)
	}

	def to(Model model, Resource resource, Map<?, ?> options) {
		val serializer = new Serializer
		serializer.to(resource, if (model == null) ModelFactory::createDefaultModel else model)
	}

	def from(Model graph, Resource resource, Map<?, ?> options) {
		val deserializer = new Deserializer
		deserializer.from(graph, resource)
	}

}
