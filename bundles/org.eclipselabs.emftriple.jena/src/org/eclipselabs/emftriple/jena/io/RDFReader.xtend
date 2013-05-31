package org.eclipselabs.emftriple.jena.io

import com.hp.hpl.jena.rdf.model.Model
import com.hp.hpl.jena.rdf.model.ModelFactory
import java.io.InputStream
import org.eclipselabs.emftriple.RDFFormat

class RDFReader {

	static def read(InputStream stream, RDFFormat format) {
		read(stream, ModelFactory::createDefaultModel, format)
	}

	static def read(InputStream stream, Model graph, RDFFormat format) {
		switch format {
			case RDFFormat::TURTLE: readTurtle(stream, graph)
			case RDFFormat::NTRIPLES: readNTriples(stream, graph)
			default: readXML(stream, graph)
		}
	}

	private static def readNTriples(InputStream stream, Model graph) {
		graph.getReader("N-TRIPLES").read(graph, stream, null)
	}

	private static def readTurtle(InputStream stream, Model graph) {
		graph.getReader("TURTLE").read(graph, stream, null)
	}

	private static def readXML(InputStream stream, Model graph) {
		graph.read(stream, null)
	}

}
