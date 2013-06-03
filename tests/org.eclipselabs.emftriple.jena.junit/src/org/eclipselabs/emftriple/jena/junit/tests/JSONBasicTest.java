package org.eclipselabs.emftriple.jena.junit.tests;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipselabs.emftriple.jena.resource.RDFJSONResourceFactory;
import org.eclipselabs.emftriple.junit.model.Book;
import org.eclipselabs.emftriple.junit.model.ModelFactory;
import org.eclipselabs.emftriple.junit.model.ModelPackage;
import org.junit.Test;

public class JSONBasicTest {

	@Test
	public void testSaveOne() throws IOException {
		String expected = 
				"{\"http://m.rdf#/\":{" +
				"\"http://www.eclipselabs.org/emf/junit#//Book/title\":[{\"type\":\"literal\",\"value\":\"TheBook\"}]," +
				"\"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\":[{\"type\":\"uri\",\"value\":\"http://www.eclipselabs.org/emf/junit#//Book\"}]}}";
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new RDFJSONResourceFactory());
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource r = resourceSet.createResource(URI.createURI("http://m.rdf"));

		Book b = ModelFactory.eINSTANCE.createBook();
		b.setTitle("The Book");

		r.getContents().add(b);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		r.save(out, null);
		assertEquals(expected, new String(out.toByteArray()).trim().replaceAll("\\n|\\s", ""));
	}

	@Test
	public void testLoadOne() throws IOException {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new RDFJSONResourceFactory());
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource r = resourceSet.createResource(URI.createURI("platform:/plugin/org.eclipselabs.emftriple.junit/tests/test-rdf-json-1.json"));

		r.load(null);
		assertEquals(1, r.getContents().size());

		assertEquals(ModelPackage.Literals.BOOK, r.getContents().get(0).eClass());

		Book root = (Book) r.getContents().get(0);
		assertEquals("The Book", root.getTitle());
	}
}
