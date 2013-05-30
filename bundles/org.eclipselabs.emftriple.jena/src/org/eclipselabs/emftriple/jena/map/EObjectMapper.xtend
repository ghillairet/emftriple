package org.eclipselabs.emftriple.jena.map

import java.io.OutputStream
import java.util.Map
import org.eclipselabs.emftriple.jena.resource.RDFResource
import com.hp.hpl.jena.rdf.model.Model
import java.io.InputStream
import org.eclipse.emf.ecore.resource.Resource
import com.hp.hpl.jena.rdf.model.ModelFactory

class EObjectMapper {

	def write(OutputStream stream, RDFResource resource, Map<? extends Object,? extends Object> options) {
		write(stream, to(resource, options));
	}

	def write(OutputStream stream, Model graph) {
		graph.write(stream)
	}

	def to(Resource resource, Map<? extends Object,? extends Object> options) {
		to(ModelFactory::createDefaultModel, resource, options)
	}
	
	def to(Model model, Resource resource, Map<? extends Object,? extends Object> options) {
		val serializer = new Serializer
		serializer.to(resource, if (model == null) ModelFactory::createDefaultModel else model)
	}
	
	def from(InputStream stream, Resource resource, Map<? extends Object,? extends Object> options) {
		val graph = ModelFactory::createDefaultModel
		graph.read(stream, null)
		from(graph, resource, options)
	}
	
	def from(Model graph, Resource resource, Map<? extends Object,? extends Object> options) {
		val deserializer = new Deserializer
		deserializer.from(graph, resource)		
	}

}