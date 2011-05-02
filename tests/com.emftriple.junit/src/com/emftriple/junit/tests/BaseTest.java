package com.emftriple.junit.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.Before;
import org.junit.Test;

import com.emftriple.jena.tdb.TDBResourceFactory;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.junit.model.ModelFactory;
import com.junit.model.ModelPackage;
import com.junit.model.Person;

public class BaseTest {
	@Before
	public void tearUp() {
		Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put("emftriple", new TDBResourceFactory());
		EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, ModelPackage.eINSTANCE);
	}
	
//	@Test
	public void createPersonsAndBooks() throws IOException {
		Dataset ds = TDBFactory.createDataset("basetest");
		ds.getDefaultModel().removeAll();
		ds.getDefaultModel().commit();
		
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://basetest"));
		resource.load(null);
		
		assertTrue(resource.getContents().isEmpty());
		
		Person p1 = ModelFactory.eINSTANCE.createPerson();
		p1.setName("John Doe");
		
		Person p2 = ModelFactory.eINSTANCE.createPerson();
		p2.setName("Paul Smith");
		
		Person p3 = ModelFactory.eINSTANCE.createPerson();
		p3.setName("Hector Poe");
		
		resource.getContents().add(p1);
		resource.getContents().add(p2);
		resource.getContents().add(p3);
		resource.save(null);
	}
	
	@Test
	public void testLoadObjects() throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://basetest"));
		resource.load(null);
		
		assertFalse(resource.getContents().isEmpty());
		assertTrue(resource.getContents().size() == 3);
		
		for (EObject o: resource.getContents()) {
			System.out.println(o);
		}
	}
}
