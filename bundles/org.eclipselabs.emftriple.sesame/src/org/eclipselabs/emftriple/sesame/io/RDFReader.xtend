package org.eclipselabs.emftriple.sesame.io

import java.io.IOException
import java.io.InputStream
import org.openrdf.model.Model
import org.openrdf.model.impl.LinkedHashModel
import org.openrdf.rio.RDFFormat
import org.openrdf.rio.RDFHandlerException
import org.openrdf.rio.RDFParseException
import org.openrdf.rio.RDFParser
import org.openrdf.rio.Rio
import org.openrdf.rio.helpers.StatementCollector
import org.eclipse.emf.common.util.URI

class RDFReader {
	
	static def read(InputStream stream, org.eclipselabs.emftriple.RDFFormat format, URI uri) {
		read(stream, new LinkedHashModel, format, uri)
	}

	static def read(InputStream stream, Model graph, org.eclipselabs.emftriple.RDFFormat format, URI uri) {
		doRead(getReader(format), stream, graph, uri)		
	}
	
	private static def getReader(org.eclipselabs.emftriple.RDFFormat format) {
		switch format {
			case RDFFormat::TURTLE: Rio::createParser(RDFFormat::TURTLE)
			case RDFFormat::NTRIPLES: Rio::createParser(RDFFormat::NTRIPLES)
			default: Rio::createParser(RDFFormat::RDFXML)
		}
	}

	private static def doRead(RDFParser reader, InputStream stream, Model graph, URI uri) {
		val collector = new StatementCollector(graph)
		reader.setRDFHandler(collector)

		try {
			reader.parse(stream, uri.toString)
		} catch (IOException e) {
			throw e
		} catch (RDFParseException e) {
			throw e
		} catch (RDFHandlerException e) {
			throw e
		}

		graph
	}

}