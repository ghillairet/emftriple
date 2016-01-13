package org.eclipselabs.emftriple.jena.streams

import java.io.InputStream
import java.io.IOException
import org.eclipse.emf.ecore.resource.URIConverter$Loadable
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.common.util.URI
import java.util.Map

class RDFInputStream extends InputStream implements Loadable {

	protected final URI uri

	new(URI uri) {
		this.uri = uri
	}

	override read() throws IOException { 0 }

	override loadResource(Resource resource) throws IOException {}

}