package com.emftriple.sesame.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.Before;
import org.junit.Test;

import com.emftriple.sesame.nat.SesameNativeResourceFactory;
import com.emftriple.util.ETripleOptions;
import com.junit.model.Book;
import com.junit.model.ModelFactory;
import com.junit.model.ModelPackage;
import com.junit.model.Person;

public class InterModelReferenceNativeTest {
	ResourceSet resourceSet;
	
	@Before
	public void tearUp() {
		Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put("emftriple", new SesameNativeResourceFactory());
		EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, ModelPackage.eINSTANCE);
		
		resourceSet = new ResourceSetImpl();
		Map<String, Object> options = new HashMap<String, Object>();
		options.put(ETripleOptions.OPTION_DATASOURCE_LOCATION, "/tmp/sesame/native/inter");
		resourceSet.getLoadOptions().putAll(options);
	}
	
	@Test
	public void testDeleteGraphs() throws IOException {
		Resource graph1 = resourceSet.createResource(URI.createURI("emftriple://sesame?graph=http://persons"));
		Resource graph2 = resourceSet.createResource(URI.createURI("emftriple://sesame?graph=http://books"));
		
		graph1.delete(null);
		graph2.delete(null);
		
		graph1.load(null);
		graph2.load(null);
		
		assertTrue(graph1.getContents().isEmpty());
		assertTrue(graph2.getContents().isEmpty());
	}
	
	@Test
	public void testCreateTwoObjectsAndStoreInDifferentResources() throws IOException {
		Resource graph1 = resourceSet.createResource(URI.createURI("emftriple://sesame?graph=http://persons"));
		Resource graph2 = resourceSet.createResource(URI.createURI("emftriple://sesame?graph=http://books"));
		
		graph1.load(null);
		graph2.load(null);
		
		assertTrue(graph1.getContents().isEmpty());
		assertTrue(graph2.getContents().isEmpty());
		
		Person p = ModelFactory.eINSTANCE.createPerson();
		p.setName("John Doe");
		
		Book b = ModelFactory.eINSTANCE.createBook();
		b.setTitle("Game on");
		p.getBooks().add(b);
		
		graph1.getContents().add(p);
		graph1.save(null);
		
		graph2.getContents().add(b);
		graph2.save(null);
	}
	
	@Test
	public void testLoadFromTwoResources() throws IOException {
		Resource graph1 = resourceSet.createResource(URI.createURI("emftriple://sesame?graph=http://persons"));
		Resource graph2 = resourceSet.createResource(URI.createURI("emftriple://sesame?graph=http://books"));
		
		graph1.load(null);
		graph2.load(null);
		
		assertFalse(graph1.getContents().isEmpty());
		assertFalse(graph2.getContents().isEmpty());
	}
}
