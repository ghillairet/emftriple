package org.eclipselabs.emftriple.tdb.streams

import com.hp.hpl.jena.query.Dataset
import com.hp.hpl.jena.query.ReadWrite
import com.hp.hpl.jena.rdf.model.ModelFactory
import java.io.IOException
import java.util.Map
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipselabs.emftriple.jena.map.EObjectMapper
import org.eclipselabs.emftriple.jena.streams.RDFOutputStream

class TDBOutputStream extends RDFOutputStream {

	private Dataset dataset

	new(Dataset dataset, URI uri, Map<? extends Object,? extends Object> options) {
		super(uri, options)
		this.dataset = dataset
	}

	override saveResource(Resource resource) throws IOException {
		val namedGraphURI = uri.toString

		dataset.begin(ReadWrite::WRITE)
		try {
			if (!dataset.containsNamedModel(namedGraphURI)) { 
				dataset.addNamedModel(namedGraphURI, ModelFactory::createDefaultModel)
			}
			
			val model = dataset.getNamedModel(namedGraphURI)
			val mapper = new EObjectMapper
			mapper.to(model, resource, options) 
			dataset.commit
		} finally {
			dataset.end 
		}
	}

}