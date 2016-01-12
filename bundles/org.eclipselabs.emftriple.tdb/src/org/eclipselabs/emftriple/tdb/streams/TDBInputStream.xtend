package org.eclipselabs.emftriple.tdb.streams

import com.hp.hpl.jena.query.Dataset
import com.hp.hpl.jena.query.ReadWrite
import java.io.IOException
import java.util.Map
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipselabs.emftriple.jena.map.EObjectMapper
import org.eclipselabs.emftriple.jena.streams.RDFInputStream

class TDBInputStream extends RDFInputStream {

	private final Dataset dataset

	new(Dataset dataset, URI uri, Map<?, ?> options) {
		super(uri, options)
		this.dataset = dataset
	}

	override loadResource(Resource resource) throws IOException {
		val namedGraphURI = uri.trimQuery.toString

		dataset.begin(ReadWrite::READ)
		try {
			if (dataset.containsNamedModel(namedGraphURI)) {
				val model = dataset.getNamedModel(namedGraphURI)
				val mapper = new EObjectMapper
				mapper.from(model, resource, options)
			}
		} finally {
			dataset.end 
		}
	}

}