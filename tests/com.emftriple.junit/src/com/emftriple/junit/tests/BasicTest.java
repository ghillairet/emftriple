package com.emftriple.junit.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Before;
import org.junit.Test;

import com.emftriple.ETriple;
import com.emftriple.jena.tdb.TDBResourceFactory;
import com.emftriple.query.ETripleQueryImpl;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.junit.model.Book;
import com.junit.model.ModelFactory;
import com.junit.model.ModelPackage;
import com.junit.model.Person;

public class BasicTest {

	@Before
	public void tearUp() {
		Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put("emftriple", new TDBResourceFactory());
		ETriple.Registry.INSTANCE.register(ModelPackage.eINSTANCE);
	}
	
	@Test
	public void testDelete() throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://data?graph=http://graph"));
		
		resource.delete(null);
		
		Dataset ds = TDBFactory.createDataset("data");
		assertTrue(ds.getDefaultModel().isEmpty());
		ds.close();
	}
	
	@Test
	public void testCreateAndStore() throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://data?graph=http://graph"));
		resource.load(null);
		
		assertTrue(resource.getContents().isEmpty());
		
		Person person = ModelFactory.eINSTANCE.createPerson();
		person.setName("John Doe");
		
		Book book = ModelFactory.eINSTANCE.createBook();
		book.setTitle("Valley Of Thing");
		
		person.getBooks().add(book);
		
		resource.getContents().add(person);
		resource.getContents().add(book);
		resource.save(null);
		
		Dataset ds = TDBFactory.createDataset("data");
		assertTrue(ds.getDefaultModel().isEmpty());
		Model m = ds.getNamedModel("http://graph");
		assertFalse(m.isEmpty());
		
//		for (com.hp.hpl.jena.rdf.model.Statement r: m.listStatements().toList())
//			System.out.println(r.getSubject().getURI());
		
//		System.out.println("res: "+m.listSubjects().toList());
		assertTrue(m.listSubjects().toList().size() == 2);
		ds.close();
	}
	
	@Test
	public void testLoadResource() throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://data?graph=http://graph"));
		
		assertNotNull(resource);
		resource.load(null);
		
		assertFalse(resource.getContents().isEmpty());
		assertTrue(resource.getContents().size() == 2);
		
		Object obj = EcoreUtil.getObjectByType(resource.getContents(), ModelPackage.eINSTANCE.getPerson());
		
		assertTrue(obj instanceof Person);
		assertFalse( ((Person)obj).getBooks().isEmpty() );
		assertEquals( ((Person)obj).getBooks().size(), 1 );
		
//		System.out.println(((Person) obj).getBooks());
		
		Book b = ((Person)obj).getBooks().get(0);
		
		assertFalse(b.eIsProxy());
		assertNotNull(b.getTitle());
		assertEquals(b.getTitle(), "Valley Of Thing");
	}
	
	@Test
	public void testBasicQuery() throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		
		Resource query = resourceSet.createResource(
				new ETripleQueryImpl(
						"prefix m: <http://www.eclipselabs.org/emf/junit#> " +
						"select ?s where { ?s a m:Person }",
				URI.createURI("emftriple://data?graph=http://graph")).toURI());
		query.load(null);
		
		assertFalse(query.getContents().isEmpty());
		assertTrue(query.getContents().size() == 2);
		
		EObject obj = query.getContents().get(0);
		assertTrue(obj instanceof Person);
		
		assertEquals(((Person) obj).getName(), "John Doe");
	}
	
}
