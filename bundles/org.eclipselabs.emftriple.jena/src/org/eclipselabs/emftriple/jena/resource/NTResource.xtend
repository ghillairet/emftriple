package org.eclipselabs.emftriple.jena.resource

import com.hp.hpl.jena.rdf.model.Model
import java.io.OutputStream
import org.eclipselabs.emftriple.RDFFormat
import org.eclipse.emf.common.util.URI
import java.io.InputStream
import org.eclipselabs.emftriple.jena.io.RDFReader
import org.eclipselabs.emftriple.jena.io.RDFWriter

class NTResource extends RDFResource {
	
	new() {
		super()
	}
	
	new(URI uri) {
		super(uri)
	}

	override protected write(OutputStream stream, Model graph) {
		RDFWriter::write(stream, graph, RDFFormat::NTRIPLES)
	}

	override protected read(InputStream stream) {
		RDFReader::read(stream, RDFFormat::NTRIPLES)
	}

}
