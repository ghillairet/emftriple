package org.eclipselabs.emftriple.sesame.streams

import info.aduna.iteration.Iterations
import java.io.IOException
import java.io.InputStream
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.URIConverter.Loadable
import org.eclipselabs.emftriple.sesame.map.EObjectMapper
import org.openrdf.model.impl.LinkedHashModel
import org.openrdf.model.impl.URIImpl
import org.openrdf.repository.Repository

class RepositoryInputStream extends InputStream implements Loadable {
	
	protected final Repository repository
	protected final URI uri

	new(Repository repository, URI uri) {
		this.uri = uri
		this.repository = repository
	}

	override read() throws IOException { 0 }

	override loadResource(Resource resource) throws IOException {
		val namedGraphURI = uri.toString
		val mapper = new EObjectMapper

		val connection = repository.connection
		try {
			val stmts = connection.getStatements(null, null, null, true, new URIImpl(namedGraphURI))
			val graph = Iterations::addAll(stmts, new LinkedHashModel)
			mapper.from(graph, resource)
			stmts.close
		} finally {
			connection.close
		}
	}

}
