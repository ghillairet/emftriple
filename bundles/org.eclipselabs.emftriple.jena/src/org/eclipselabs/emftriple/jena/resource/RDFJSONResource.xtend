package org.eclipselabs.emftriple.jena.resource

import com.hp.hpl.jena.rdf.model.Model
import java.io.InputStream
import java.io.OutputStream
import org.eclipse.emf.common.util.URI
import org.eclipselabs.emftriple.RDFFormat
import org.eclipselabs.emftriple.jena.io.RDFReader
import org.eclipselabs.emftriple.jena.io.RDFWriter

class RDFJSONResource extends RDFResource {
	
	new() {
		super()
	}
	
	new(URI uri) {
		super(uri)
	}
	
	override protected write(OutputStream stream, Model graph) {
		RDFWriter::write(stream, graph, RDFFormat::RDFJSON)
	}

	override protected read(InputStream stream) {
		RDFReader::read(stream, RDFFormat::RDFJSON)
	}

}