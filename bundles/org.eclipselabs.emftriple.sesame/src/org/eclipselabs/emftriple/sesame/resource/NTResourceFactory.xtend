package org.eclipselabs.emftriple.sesame.resource

import org.eclipse.emf.common.util.URI

class NTResourceFactory extends RDFResourceFactory {
	
	override createResource(URI uri) {
		new NTResource(uri)
	}
	
}