package org.eclipselabs.emftriple.sesame.io

import java.io.OutputStream
import org.openrdf.model.Model
import org.openrdf.rio.RDFFormat
import org.openrdf.rio.RDFHandlerException
import org.openrdf.rio.Rio

class RDFWriter {

	static def write(OutputStream stream, Model graph, org.eclipselabs.emftriple.RDFFormat format) {
		doWrite(getWriter(format, stream), graph)
	}

	private static def org.openrdf.rio.RDFWriter getWriter(org.eclipselabs.emftriple.RDFFormat format, OutputStream stream) {
		switch format {
			case org::eclipselabs::emftriple::RDFFormat::TURTLE: Rio::createWriter(RDFFormat::TURTLE, stream)
			case org::eclipselabs::emftriple::RDFFormat::NTRIPLES: Rio::createWriter(RDFFormat::NTRIPLES, stream)
			default: Rio::createWriter(RDFFormat::RDFXML, stream)
		}
	}

	private static def doWrite(org.openrdf.rio.RDFWriter writer, Model graph) {
		try {
			writer.startRDF()
			graph.forEach[writer.handleStatement(it)]
			writer.endRDF()
		} catch (RDFHandlerException e) {
			e.printStackTrace
		}
	}

}