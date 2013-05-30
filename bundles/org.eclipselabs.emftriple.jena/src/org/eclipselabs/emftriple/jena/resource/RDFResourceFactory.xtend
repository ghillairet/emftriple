package org.eclipselabs.emftriple.jena.resource

import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl
import org.eclipse.emf.common.util.URI

class RDFResourceFactory extends ResourceFactoryImpl {

	override createResource(URI uri) {
		new RDFResource(uri)
	}

}