package com.emftriple.jena;

import java.io.File;

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
		
		System.out.println(uri);
		System.out.println(uri.host());
//		if (!new File(uri.host()).canWrite())
//			throw new IllegalArgumentException("URI should contain writable folder or file location.");
		
		return uri.host();
	}
}
