package org.eclipselabs.emftriple.sesame.streams

import java.io.IOException
import java.io.InputStream
import java.util.Map
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.URIConverter$Loadable
import org.openrdf.repository.Repository

class RepositoryInputStream extends InputStream implements Loadable {
	
	protected final Repository repository
	protected final URI uri
	protected final Map<? extends Object,? extends Object> options 

	new(Repository repository, URI uri, Map<? extends Object,? extends Object> options) {
		this.uri = uri
		this.options = options
		this.repository = repository
	}
	
	override read() throws IOException { 0 }
	
	override loadResource(Resource resource) throws IOException {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
}