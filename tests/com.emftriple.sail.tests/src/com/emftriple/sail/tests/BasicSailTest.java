package com.emftriple.sail.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Before;
import org.junit.Test;
import org.openrdf.sail.Sail;

import com.emftriple.sail.SailDataSource;
import com.emftriple.sail.SailResourceFactory;
import com.junit.model.Book;
import com.junit.model.ModelFactory;
import com.junit.model.ModelPackage;
import com.junit.model.Person;
import com.tinkerpop.blueprints.pgm.impls.neo4j.Neo4jGraph;
import com.tinkerpop.blueprints.pgm.oupls.sail.GraphSail;

public class BasicSailTest {
	
	ResourceSet resourceSet;
	
	@Before
	public void tearUp() {
		Sail sail = new GraphSail(new Neo4jGraph("/Users/guillaume/tmp/neo/my_graph"));

		Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put("emftriple", new SailResourceFactory());
		resourceSet = new ResourceSetImpl();
		resourceSet.getLoadOptions().put(SailDataSource.OPTION_SAIL_OBJECT, sail);
	}
	
	@Test
	public void testClearDataSource() throws IOException {
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://sail?graph=http://mygraph"));		
		resource.delete(null);
		
		Resource deleted = resourceSet.createResource(URI.createURI("emftriple://sail?graph=http://mygraph"));		
		deleted.load(null);
		
		assertTrue(deleted.getContents().isEmpty());
	}
	
	@Test
	public void testSaveSail() throws IOException {
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://sail?graph=http://mygraph"));
		
		Person p1 = ModelFactory.eINSTANCE.createPerson();
		p1.setName("John Doe");
		
		Book b1 = ModelFactory.eINSTANCE.createBook();
		b1.setTitle("Some Book");
		
		Book b2 = ModelFactory.eINSTANCE.createBook();
		b2.setTitle("Another Book");
		
		p1.getBooks().add(b1);
		p1.getBooks().add(b2);
		
		resource.getContents().add(p1);
		resource.getContents().add(b1);
		resource.getContents().add(b2);
		resource.save(null);
	}
	
	@Test
	public void testLoadSail() throws IOException {
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://sail?graph=http://mygraph"));
		resource.load(null);

		assertFalse(resource.getContents().isEmpty());
		assertTrue(resource.getContents().size() == 3);
		
		Person p = (Person) EcoreUtil.getObjectByType(resource.getContents(), ModelPackage.eINSTANCE.getPerson());
		System.out.println(p);
		
		System.out.println(p.getBooks());
	}
}
