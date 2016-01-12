package org.eclipselabs.emftriple.jena.streams

import com.hp.hpl.jena.rdf.model.Model
import com.hp.hpl.jena.rdf.model.ModelFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.Map
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.URIConverter$Saveable
import org.eclipselabs.emftriple.jena.map.EObjectMapper

class RDFOutputStream extends ByteArrayOutputStream implements Saveable {

	protected final URI uri
	protected final Map<?, ?> options
	protected Model model

	new(URI uri, Map<?, ?> options) {
		this.uri = uri
		this.options = options
	}

	override saveResource(Resource resource) throws IOException {
		val mapper = new EObjectMapper
		this.model = ModelFactory::createDefaultModel
		mapper.to(
			model,
			resource,
			options
		)
	}

	override close() throws IOException {
		if (model != null) {
			// do something
		}
	}

}