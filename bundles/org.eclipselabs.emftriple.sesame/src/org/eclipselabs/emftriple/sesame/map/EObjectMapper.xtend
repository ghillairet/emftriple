package org.eclipselabs.emftriple.sesame.map

import java.util.Map
import org.eclipse.emf.ecore.resource.Resource
import org.openrdf.model.Model
import org.openrdf.model.impl.LinkedHashModel

class EObjectMapper {

	def to(Resource resource, Map<?, ?> options) {
		to(new LinkedHashModel(newArrayList), resource, options)
	}
	
	def to(Model graph, Resource resource, Map<?, ?> options) {
		val serializer = new Serializer
		serializer.to(resource,
			if (graph == null) new LinkedHashModel(newArrayList) else graph
		)
	}

	def from(Model graph, Resource resource, Map<?, ?> options) {
		val deserializer = new Deserializer
		deserializer.from(graph, resource)		
	}

}
