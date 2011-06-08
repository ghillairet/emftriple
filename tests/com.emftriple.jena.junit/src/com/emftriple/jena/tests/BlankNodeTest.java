package com.emftriple.jena.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Before;
import org.junit.Test;

import com.emftriple.jena.file.FileResourceFactory;
import com.emftriple.jena.file.FileResourceImpl;
import com.emftriple.util.ETripleOptions;
import com.hp.hpl.jena.query.ARQ;
import com.junit.model.Book;
import com.junit.model.ModelPackage;
import com.junit.model.Person;

public class BlankNodeTest {

	Map<String, Object> options = new HashMap<String, Object>();
	
	@Before
	public void tearUp() {
		ARQ.set(ARQ.outputGraphBNodeLabels, true);
		
		Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put("emftriple", new FileResourceFactory());
		EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, ModelPackage.eINSTANCE);
		
		options.put(ETripleOptions.OPTION_DATASOURCE_LOCATION, "bnode.ttl");
		options.put(FileResourceImpl.OPTION_RDF_FORMAT, "TTL");
	}
	
//	@Test
	public void testDelete() throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getLoadOptions().putAll(options);
		
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://data"));
		
		resource.delete(null);
	}
	
	@Test
	public void testLoadResource() throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://data"));
		
		assertNotNull(resource);
		resource.load(options);
		
		assertFalse(resource.getContents().isEmpty());
		System.out.println(resource.getContents().size());
		for (EObject o: resource.getContents())
			System.out.println(o);
		assertTrue(resource.getContents().size() == 3);
		
		Object obj = EcoreUtil.getObjectByType(resource.getContents(), ModelPackage.eINSTANCE.getPerson());
		
		assertTrue(obj instanceof Person);
		assertFalse( ((Person)obj).getBooks().isEmpty() );
		assertEquals( ((Person)obj).getBooks().size(), 2 );
		
		System.out.println(((Person) obj).getName());
		
		for (Book b: ((Person) obj).getBooks()) {
			System.out.println(b.getTitle());
		}
		
		for (EObject o: resource.getContents())
			System.out.println(o);
	}
	
	@Test
	public void testSaveBlankNode() {
		
	}
}
