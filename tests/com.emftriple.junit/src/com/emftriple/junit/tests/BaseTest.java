package com.emftriple.junit.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.Before;
import org.junit.Test;

import com.emftriple.jena.tdb.TDBResourceFactory;
import com.emftriple.resource.ETripleResource;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.junit.model.ModelFactory;
import com.junit.model.ModelPackage;
import com.junit.model.PrimaryObject;

public class BaseTest {
	
	ResourceSet resourceSet;
	
	@Before
	public void tearUp() {
		Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put("emftriple", new TDBResourceFactory());
		EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, ModelPackage.eINSTANCE);
		
		resourceSet = new ResourceSetImpl();
		resourceSet.getLoadOptions().put(ETripleResource.OPTION_DATASOURCE_LOCATION, "rest-test");
	}
	
	@Test
	public void testPost() throws IOException {
		Dataset ds = TDBFactory.createDataset("rest-test");
		ds.getDefaultModel().removeAll();
		ds.getDefaultModel().commit();
		
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://tdb"));
		resource.load(null);
		
		assertTrue(resource.getContents().isEmpty());
		
		PrimaryObject p1 = ModelFactory.eINSTANCE.createPrimaryObject();
		p1.setId(1);
		p1.setName("Foo");
		
		PrimaryObject p2 = ModelFactory.eINSTANCE.createPrimaryObject();
		p2.setId(2);
		p2.setName("Bar");
		
		resource.getContents().add(p1);
		resource.getContents().add(p2);
		resource.save(null);
	}
	
	@Test
	public void testGet() throws IOException {
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://tdb?uri=http://junit.org/primary/1"));
		resource.load(null);
		
		assertFalse(resource.getContents().isEmpty());
		assertTrue(resource.getContents().size() == 1);
		
		assertTrue(resource.getContents().get(0) instanceof PrimaryObject);
		PrimaryObject p = (PrimaryObject) resource.getContents().get(0);
		
		assertEquals(p.getId(), 1);
		assertEquals(p.getName(), "Foo");
	}
	
	@Test
	public void testDelete() {
		
	}
}
