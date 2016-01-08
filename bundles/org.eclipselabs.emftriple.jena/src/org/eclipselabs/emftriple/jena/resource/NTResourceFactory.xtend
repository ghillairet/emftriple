package org.eclipselabs.emftriple.jena.resource

import org.eclipse.emf.common.util.URI

class NTResourceFactory extends RDFResourceFactory {
	
	override createResource(URI uri) {
		new NTResource(uri)
	}
	
}