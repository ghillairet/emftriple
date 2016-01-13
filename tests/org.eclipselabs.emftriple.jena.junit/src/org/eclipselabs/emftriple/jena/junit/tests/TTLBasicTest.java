package org.eclipselabs.emftriple.jena.junit.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipselabs.emftriple.jena.resource.TTLResourceFactory;
import org.eclipselabs.emftriple.junit.model.Book;
import org.eclipselabs.emftriple.junit.model.ModelFactory;
import org.eclipselabs.emftriple.junit.model.ModelPackage;
import org.eclipselabs.emftriple.junit.model.PrimaryObject;
import org.eclipselabs.emftriple.junit.model.TargetObject;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class TTLBasicTest {

	@Test
	public void testSaveOne() throws IOException {
		String expected = "<http://m.rdf#/>a<http://www.eclipselabs.org/emf/junit#//Book>.";
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new TTLResourceFactory());
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource r = resourceSet.createResource(URI.createURI("http://m.rdf"));
		
		Book b = ModelFactory.eINSTANCE.createBook();

		r.getContents().add(b);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		r.save(out, null);
		assertEquals(expected, new String(out.toByteArray()).trim().replaceAll("\\n|\\s", ""));
	}

	@Test
	public void testLoadOne() throws IOException {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new TTLResourceFactory());
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource r = resourceSet.createResource(URI.createURI("platform:/plugin/org.eclipselabs.emftriple.junit/tests/file-test-1.ttl"));
		
		r.load(null);
		assertEquals(1, r.getContents().size());
		
		assertEquals(ModelPackage.Literals.TARGET_OBJECT, r.getContents().get(0).eClass());
		
		TargetObject root = (TargetObject) r.getContents().get(0);
		assertEquals(4, root.getId());
		assertEquals(3, root.getArrayAttribute().size());
		assertTrue(root.getArrayAttribute().contains("foo"));
		assertTrue(root.getArrayAttribute().contains("foobar"));
		assertTrue(root.getArrayAttribute().contains("bar"));
	}

	@Test
	public void testLoadTwo() throws IOException {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new TTLResourceFactory());
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource r = resourceSet.createResource(URI.createURI("platform:/plugin/org.eclipselabs.emftriple.junit/tests/file-test-2.ttl"));

		r.load(null);
		assertEquals(2, r.getContents().size());
		
		assertEquals(ModelPackage.Literals.PRIMARY_OBJECT, r.getContents().get(0).eClass());
		assertEquals(ModelPackage.Literals.PRIMARY_OBJECT, r.getContents().get(1).eClass());
		
		PrimaryObject root1 = (PrimaryObject) r.getContents().get(0);
		PrimaryObject root2 = (PrimaryObject) r.getContents().get(1);

		Map<Integer, String> contents = ImmutableMap.<Integer, String>builder().
				put(root1.getId(), root1.getName()).
				put(root2.getId(), root2.getName()).
				build();

		assertEquals("Foo", contents.get(1));
		assertEquals("Bar", contents.get(2));
	}

}
