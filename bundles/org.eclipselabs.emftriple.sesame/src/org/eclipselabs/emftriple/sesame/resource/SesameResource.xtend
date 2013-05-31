package org.eclipselabs.emftriple.sesame.resource

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.Collections
import java.util.Map
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.URIConverter$Loadable
import org.eclipse.emf.ecore.resource.URIConverter$Saveable
import org.eclipselabs.emftriple.resource.RDFResource
import org.eclipselabs.emftriple.sesame.map.EObjectMapper

class SesameResource extends RDFResource {

	new() {
	}

	new(URI uri) {
		super(uri)
	}

	override protected doLoad(InputStream inputStream, Map<? extends Object,? extends Object> options) throws IOException {
		if (inputStream instanceof Loadable) {
			(inputStream as Loadable).loadResource(this)
		} else {
			val mapper = new EObjectMapper
			mapper.from(
				inputStream,
				this,
				if (options == null) Collections::emptyMap else options
			)
		}
	}

	override protected doSave(OutputStream outputStream, Map<? extends Object,? extends Object> options) throws IOException {
		if (outputStream instanceof Saveable) {
			(outputStream as Saveable).saveResource(this)
		} else {
			val mapper = new EObjectMapper
			mapper.write(
				outputStream, 
				this, 
				if (options == null) Collections::emptyMap else options
			)
		}
	}

}
