package org.eclipselabs.emftriple.sesame.streams

import java.io.ByteArrayOutputStream
import java.io.IOException
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.URIConverter.Saveable
import org.eclipselabs.emftriple.sesame.map.EObjectMapper
import org.openrdf.model.impl.LinkedHashModel
import org.openrdf.model.impl.URIImpl
import org.openrdf.repository.Repository

class RepositoryOutputStream extends ByteArrayOutputStream implements Saveable {

	protected final Repository repository
	protected final URI uri

	new(Repository repository, URI uri) {
		this.uri = uri
		this.repository = repository
	}

	override saveResource(Resource resource) throws IOException {
		val namedGraphURI = uri.toString
		val mapper = new EObjectMapper
		val graph = new LinkedHashModel

		mapper.to(graph, resource)
		val connection = repository.connection
		connection.begin
		try {
			connection.add(graph, new URIImpl(namedGraphURI))
			connection.commit
		} finally {
			connection.close
		}
	}

}
