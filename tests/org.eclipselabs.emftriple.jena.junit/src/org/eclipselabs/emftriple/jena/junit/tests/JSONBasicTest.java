package org.eclipselabs.emftriple.jena.junit.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import org.junit.Assert;
import org.junit.Test;

public class JSONBasicTest {

	@Test
	public void testSaveOne() throws IOException {
		final String front = "{\"http://m.rdf#/\":{";
		final String end = "}}";
		final String type = "\"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\":[{\"type\":\"uri\",\"value\":\"http://www.eclipselabs.org/emf/junit#//Book\"}]";
		final String book = "\"http://www.eclipselabs.org/emf/junit#//Book/title\":[{\"type\":\"literal\",\"value\":\"TheBook\"}]";
		final String delimiter = ",";
		String expected1 = front + book + delimiter + type + end;
		String expected2 = front + type + delimiter + book + end;
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new RDFJSONResourceFactory());
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource r = resourceSet.createResource(URI.createURI("http://m.rdf"));

		Book b = ModelFactory.eINSTANCE.createBook();
		b.setTitle("The Book");

		r.getContents().add(b);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		r.save(out, null);

		final String actual = new String(out.toByteArray()).trim().replaceAll("\\n|\\s", "");
		assertTrue("\n" + actual + "\ndoes not match:\n" + expected1 + "\n" + expected2,
				expected1.equals(actual) || expected2.equals(actual));
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
