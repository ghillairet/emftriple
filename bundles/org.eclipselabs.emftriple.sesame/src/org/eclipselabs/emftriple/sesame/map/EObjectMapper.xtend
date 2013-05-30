package org.eclipselabs.emftriple.sesame.map

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.Map
import org.eclipse.emf.ecore.resource.Resource
import org.eclipselabs.emftriple.resource.RDFResource
import org.openrdf.model.Graph
import org.openrdf.model.Model
import org.openrdf.model.impl.GraphImpl
import org.openrdf.model.impl.LinkedHashModel
import org.openrdf.model.impl.ValueFactoryImpl
import org.openrdf.rio.RDFFormat
import org.openrdf.rio.RDFHandlerException
import org.openrdf.rio.RDFParseException
import org.openrdf.rio.Rio
import org.openrdf.rio.helpers.StatementCollector

class EObjectMapper {

	def write(OutputStream stream, RDFResource resource, Map<? extends Object,? extends Object> options) {
		write(stream, to(resource, options));
	}

	def write(OutputStream stream, Graph graph) {
		val writer = Rio::createWriter(RDFFormat::RDFXML, stream)
		try {
			writer.startRDF()
			graph.forEach[writer.handleStatement(it)]
			writer.endRDF()
		} catch (RDFHandlerException e) {
			e.printStackTrace
		}
	}

	def to(Resource resource, Map<? extends Object,? extends Object> options) {
		to(new GraphImpl(ValueFactoryImpl::instance, newArrayList), resource, options)
	}
	
	def to(Graph graph, Resource resource, Map<? extends Object,? extends Object> options) {
		to(graph, resource, options, false)
	}
	
	def to(Graph graph, Resource resource, Map<? extends Object,? extends Object> options, boolean named) {
		val serializer = switch named {
			case true: new NamedGraphSerializer
			default: new Serializer
		}
		println(serializer)
		serializer.to(resource,
			if (graph == null) new GraphImpl(ValueFactoryImpl::instance, newArrayList) else graph
		)
	}
	
	def from(InputStream stream, Resource resource, Map<? extends Object,? extends Object> options) {
		val parser = Rio::createParser(RDFFormat::RDFXML)
		val graph = new LinkedHashModel
		val collector = new StatementCollector(graph)
		parser.setRDFHandler(collector)

		try {
			parser.parse(stream, resource.URI.toString)
			from(graph, resource, options)
		} catch (IOException e) {
			throw e
		} catch (RDFParseException e) {
			throw e
		} catch (RDFHandlerException e) {
			throw e
		}
	}
	
	def from(Model graph, Resource resource, Map<? extends Object,? extends Object> options) {
		val deserializer = new Deserializer
		deserializer.from(graph, resource)		
	}
}