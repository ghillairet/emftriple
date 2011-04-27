package com.emftriple.jena.tdb;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;

import com.emftriple.resource.ETripleResource;

/**
 * 
 * @author ehilgui
 *
 */
public class TDBResourceFactory extends ResourceFactoryImpl {
	@Override
	public Resource createResource(URI uri) {
		return new ETripleResource(uri, new JenaTDB("tdb", getLocation(uri)));
	}

	private String getLocation(URI uri) {
		if (!uri.scheme().equals("emftriple"))
			throw new IllegalArgumentException("URI is not well formed.");
		
		return uri.host();
	}
}
