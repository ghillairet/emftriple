package org.eclipselabs.emftriple.sesame.resource

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.Collections
import java.util.Map
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.URIConverter$Loadable
import org.eclipse.emf.ecore.resource.URIConverter$Saveable
import org.eclipse.emf.ecore.resource.impl.ResourceImpl
import org.eclipselabs.emftriple.sesame.map.EObjectMapper
import org.eclipselabs.emftriple.sesame.io.RDFReader
import org.eclipselabs.emftriple.sesame.io.RDFWriter

class SesameResource extends ResourceImpl {

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
				RDFReader::read(inputStream, null, this.URI),
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
			RDFWriter::write(outputStream, mapper.to(
				this,
				if (options == null) Collections::emptyMap else options
			), null)
		}
	}

}
