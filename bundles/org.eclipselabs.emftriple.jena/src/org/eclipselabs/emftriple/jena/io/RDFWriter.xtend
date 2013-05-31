package org.eclipselabs.emftriple.jena.io

import com.hp.hpl.jena.rdf.model.Model
import java.io.OutputStream
import org.eclipselabs.emftriple.RDFFormat

class XMLWriter {

	static def write(OutputStream stream, Model graph, RDFFormat format) {
		switch format {
			case RDFFormat::TURTLE: writeTurtle(stream, graph)
			case RDFFormat::NTRIPLES: writeNTriples(stream, graph)
			default: writeXML(stream, graph) 
		}
	}

	private static def void writeNTriples(OutputStream stream, Model model) {
		model.write(stream, "N-TRIPLES")
	}

	private static def void writeTurtle(OutputStream stream, Model model) {
		model.write(stream, "TTL")
	}

	private static def writeXML(OutputStream stream, Model model) {
		model.write(stream)
	}

}
