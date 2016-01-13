package org.eclipselabs.emftriple.sesame.resource

import java.io.OutputStream
import org.eclipselabs.emftriple.RDFFormat
import org.eclipselabs.emftriple.sesame.io.RDFWriter
import org.openrdf.model.Model
import org.eclipse.emf.common.util.URI

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

}
