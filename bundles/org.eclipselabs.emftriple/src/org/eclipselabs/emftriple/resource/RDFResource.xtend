package org.eclipselabs.emftriple.resource

import org.eclipse.emf.ecore.resource.impl.ResourceImpl
import java.io.InputStream
import java.util.Map
import java.io.IOException
import java.io.OutputStream
import org.eclipse.emf.common.util.URI

abstract class RDFResource extends ResourceImpl {

	new() {
	}

	new(URI uri) {
		super(uri)
	}

	override protected doLoad(InputStream inputStream, Map<? extends Object,? extends Object> options) throws IOException {
		super.doLoad(inputStream, options)
	}

	override protected doSave(OutputStream outputStream, Map<? extends Object,? extends Object> options) throws IOException {
		super.doSave(outputStream, options)
	}

}