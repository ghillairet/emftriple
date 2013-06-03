package org.eclipselabs.emftriple.jena.resource

import org.eclipse.emf.common.util.URI

class RDFJSONResourceFactory extends RDFResourceFactory {
	
	override createResource(URI uri) {
		new RDFJSONResource(uri)
	}
	
}