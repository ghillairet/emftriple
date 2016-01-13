package org.eclipselabs.emftriple.sesame.handlers

import java.io.IOException
import java.util.Map
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl
import org.eclipselabs.emftriple.sesame.streams.RepositoryInputStream
import org.eclipselabs.emftriple.sesame.streams.RepositoryOutputStream
import org.openrdf.repository.Repository

class RepositoryHandler extends URIHandlerImpl {
	
	private final Repository repository
	
	new(Repository repository) {
		this.repository = repository
	}
	
	override canHandle(URI uri) {
		true
	}
	
	override createOutputStream(URI uri, Map<?, ?> options) throws IOException {
		if (repository == null) {
			throw new IOException("Repository must be defined")
		}
		new RepositoryOutputStream(repository, uri)
	}
	
	override createInputStream(URI uri, Map<?, ?> options) throws IOException {
		if (repository == null) {
			throw new IOException("Repository must be defined")
		}
		new RepositoryInputStream(repository, uri)
	}

}
