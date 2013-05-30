package org.eclipselabs.emftriple.sesame.junit.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipselabs.emftriple.junit.model.Book;
import org.eclipselabs.emftriple.junit.model.Library;
import org.eclipselabs.emftriple.junit.model.ModelFactory;
import org.eclipselabs.emftriple.junit.model.ModelPackage;
import org.eclipselabs.emftriple.sesame.resource.SesameResourceFactory;
import org.junit.Test;

public class BasicTest {

	@Test
	public void testSaveOne() throws IOException {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new SesameResourceFactory());
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource r = resourceSet.createResource(URI.createURI("http://m.rdf"));
		
		Book b = ModelFactory.eINSTANCE.createBook();
		b.setTitle("The Book");
		b.getTags().add("SciFI");
		b.getTags().add("Fantasy");
		
		r.getContents().add(b);
		
		r.save(System.out, null);
	}
	
	@Test
	public void testSaveOneContainment() throws IOException {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new SesameResourceFactory());
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource r = resourceSet.createResource(URI.createURI("http://m.rdf"));
		
		Library l = ModelFactory.eINSTANCE.createLibrary();
		Book b = ModelFactory.eINSTANCE.createBook();
		b.setTitle("The Book");
		b.getTags().add("SciFI");
		b.getTags().add("Fantasy");
		l.getBooks().add(b);
		
		r.getContents().add(l);
		r.save(System.out, null);
	}
	
	@Test
	public void testSaveManyContainment() throws IOException {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new SesameResourceFactory());
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource r = resourceSet.createResource(URI.createURI("http://m.rdf"));
		
		Library l = ModelFactory.eINSTANCE.createLibrary();
		Book b = ModelFactory.eINSTANCE.createBook();
		b.setTitle("The Book");
		b.getTags().add("SciFI");
		b.getTags().add("Fantasy");
		l.getBooks().add(b);
		
		Book b2 = ModelFactory.eINSTANCE.createBook();
		b2.setTitle("The Other Book");
		b2.getTags().add("Fantasy");
		l.getBooks().add(b2);
		
		Book b3 = ModelFactory.eINSTANCE.createBook();
		b3.setTitle("The Other Other Book");
		b3.getTags().add("SciFI");
		l.getBooks().add(b3);
		
		r.getContents().add(l);
		r.save(System.out, null);
	}
	
	@Test
	public void testLoadOne() throws IOException {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new SesameResourceFactory());
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getURIConverter().getURIMap().put(
				URI.createURI("http://m.rdf"), 
				URI.createURI("platform:/plugin/org.eclipselabs.emftriple.junit/tests/test-load-1.rdf"));

		Resource r = resourceSet.createResource(URI.createURI("http://m.rdf"));
		r.load(null);
		
		assertEquals(1, r.getContents().size());
		assertEquals(ModelPackage.Literals.BOOK, r.getContents().get(0).eClass());
		
		Book b = (Book) r.getContents().get(0);
		assertEquals("The Book", b.getTitle());
		assertEquals(2, b.getTags().size());	
		assertEquals("SciFI", b.getTags().get(0));
		assertEquals("Fantasy", b.getTags().get(1));
	}

	@Test
	public void testLoadOneContainment() throws IOException {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new SesameResourceFactory());
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getURIConverter().getURIMap().put(
				URI.createURI("http://m.rdf"), 
				URI.createURI("platform:/plugin/org.eclipselabs.emftriple.junit/tests/test-load-2.rdf"));

		Resource r = resourceSet.createResource(URI.createURI("http://m.rdf"));
		r.load(null);
		
		assertEquals(1, r.getContents().size());
		assertEquals(ModelPackage.Literals.LIBRARY, r.getContents().get(0).eClass());
		
		Library l = (Library) r.getContents().get(0);
		assertEquals(1, l.getBooks().size());
		
		Book b = l.getBooks().get(0);
		assertEquals("The Book", b.getTitle());
		assertEquals(2, b.getTags().size());	
		assertEquals("SciFI", b.getTags().get(0));
		assertEquals("Fantasy", b.getTags().get(1));
	}
	
	@Test
	public void testLoadManyContainment() throws IOException {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new SesameResourceFactory());
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getURIConverter().getURIMap().put(
				URI.createURI("http://m.rdf"), 
				URI.createURI("platform:/plugin/org.eclipselabs.emftriple.junit/tests/test-load-3.rdf"));

		Resource r = resourceSet.createResource(URI.createURI("http://m.rdf"));
		r.load(null);
		
		assertEquals(1, r.getContents().size());
		assertEquals(ModelPackage.Literals.LIBRARY, r.getContents().get(0).eClass());
		
		Library l = (Library) r.getContents().get(0);
		assertEquals(3, l.getBooks().size());

		Book b = l.getBooks().get(0);
		assertEquals("The Book", b.getTitle());
		assertEquals(2, b.getTags().size());	
		assertEquals("SciFI", b.getTags().get(0));
		assertEquals("Fantasy", b.getTags().get(1));

		Book b2 = l.getBooks().get(1);
		assertEquals("The Other Book", b2.getTitle());
		assertEquals(1, b2.getTags().size());	
		assertEquals("Fantasy", b2.getTags().get(0));

		Book b3 = l.getBooks().get(2);
		assertEquals("The Other Other Book", b3.getTitle());
		assertEquals(1, b3.getTags().size());	
		assertEquals("SciFI", b3.getTags().get(0));
	}
}
