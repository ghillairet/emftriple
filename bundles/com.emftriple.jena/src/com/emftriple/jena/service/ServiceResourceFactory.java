package com.emftriple.jena.service;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;

import com.emftriple.resource.ETripleResource;

public class ServiceResourceFactory extends ResourceFactoryImpl {
	@Override
	public Resource createResource(URI uri) {
		return new ETripleResource(uri, new JenaService("sparql", getLocation(uri)));
	}

	private String getLocation(URI uri) {
		if (!uri.scheme().equals("emftriple"))
			throw new IllegalArgumentException("URI is not well formed.");
		
		return uri.host();
	}
}
