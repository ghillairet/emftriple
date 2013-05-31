package org.eclipselabs.emftriple.sesame.resource

import org.eclipse.emf.common.util.URI

class TTLResourceFactory extends RDFResourceFactory {
	
	override createResource(URI uri) {
		new TTLResource(uri)
	}
	
}