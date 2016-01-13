package org.eclipselabs.emftriple.jena.resource

import com.hp.hpl.jena.rdf.model.Model
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.Map
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.URIConverter.Loadable
import org.eclipse.emf.ecore.resource.URIConverter.Saveable
import org.eclipse.emf.ecore.resource.impl.ResourceImpl
import org.eclipselabs.emftriple.jena.io.RDFReader
import org.eclipselabs.emftriple.jena.io.RDFWriter
import org.eclipselabs.emftriple.jena.map.EObjectMapper

class RDFResource extends ResourceImpl {

	new() {
	}

	new(URI uri) {
		super(uri)
	}

	override protected doLoad(InputStream inputStream, Map<?, ?> options) throws IOException {
		if (inputStream instanceof Loadable) {
			(inputStream as Loadable).loadResource(this)
		} else {
			val mapper = new EObjectMapper
			mapper.from(read(inputStream), this)
		}
	}

	override protected doSave(OutputStream outputStream, Map<?, ?> options) throws IOException {
		if (outputStream instanceof Saveable) {
			(outputStream as Saveable).saveResource(this)
		} else {
			val mapper = new EObjectMapper
			write(outputStream, mapper.to(this))
		}
	}
	
	protected def read(InputStream stream) {
		RDFReader::read(stream, null)
	}

	protected def write(OutputStream stream, Model graph) {
		RDFWriter::write(stream, graph, null)
	}

}