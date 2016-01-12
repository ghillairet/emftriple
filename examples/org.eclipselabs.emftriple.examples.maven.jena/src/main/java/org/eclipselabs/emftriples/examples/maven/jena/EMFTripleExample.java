package org.eclipselabs.emftriples.examples.maven.jena;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipselabs.emftriple.jena.resource.RDFResourceFactory;
import org.eclipselabs.emftriple.junit.model.Book;
import org.eclipselabs.emftriple.junit.model.ModelFactory;

public class EMFTripleExample {
	public static void main(final String[] args) throws IOException {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new RDFResourceFactory());

		final ResourceSet resourceSet = new ResourceSetImpl();
		final Resource r = resourceSet.createResource(URI.createURI("file:///tmp/my.rdf"));

		final Book b = ModelFactory.eINSTANCE.createBook();
		b.setTitle("The Book");
		b.getTags().add("SciFI");
		b.getTags().add("Fantasy");

		r.getContents().add(b);
		r.save(null);
	}
}
