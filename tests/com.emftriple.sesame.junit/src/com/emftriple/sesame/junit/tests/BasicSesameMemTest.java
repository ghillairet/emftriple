package com.emftriple.sesame.junit.tests;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.Before;
import org.junit.Test;

import com.emftriple.sesame.mem.MemoryResourceFactory;
import com.junit.model.ModelPackage;

public class BasicSesameMemTest {
	@Before
	public void tearUp() {
		Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put("emftriple", new MemoryResourceFactory());
		EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, ModelPackage.eINSTANCE);
	}
	
	@Test
	public void testDelete() throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://data"));
		
		resource.delete(null);
		
//		Dataset ds = TDBFactory.createDataset("data");
//		assertTrue(ds.getDefaultModel().isEmpty());
//		ds.close();
	}
	
	@Test
	public void testDeleteGraph() throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://data?graph=http://graph"));
		
		resource.delete(null);
		
//		Dataset ds = TDBFactory.createDataset("data");
//		assertTrue(ds.getDefaultModel().isEmpty());
//		assertTrue(ds.getNamedModel("http://graph").isEmpty());
//		ds.close();
	}
}
