package org.eclipselabs.emftriple.sesame.resource

import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl
import org.eclipse.emf.common.util.URI

class SesameResourceFactory extends ResourceFactoryImpl {
	
	override createResource(URI uri) {
		new SesameResource(uri)
	}
	
}