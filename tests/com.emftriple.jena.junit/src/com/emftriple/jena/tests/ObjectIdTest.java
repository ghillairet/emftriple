/*******************************************************************************
 * Copyright (c) 2011 Guillaume Hillairet.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Guillaume Hillairet - initial API and implementation
 *******************************************************************************/
package com.emftriple.jena.tests;

import static org.junit.Assert.assertNotNull;
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
import com.emftriple.util.ETripleOptions;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.junit.model.ModelFactory;
import com.junit.model.ModelPackage;
import com.junit.model.PrimaryObject;
import com.junit.model.TargetObject;

public class ObjectIdTest {
	@Before
	public void tearUp() {
		Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put("emftriple", new TDBResourceFactory());
		EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, ModelPackage.eINSTANCE);
	}
	
	@Test
	public void createPersonsAndBooks() throws IOException {
		Dataset ds = TDBFactory.createDataset("id-test");
		ds.getDefaultModel().removeAll();
		ds.getDefaultModel().commit();
		
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getLoadOptions().put(ETripleOptions.OPTION_DATASOURCE_LOCATION, "id-test");
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://idtest"));
		resource.load(null);
		
		assertTrue(resource.getContents().isEmpty());
		
		PrimaryObject p1 = ModelFactory.eINSTANCE.createPrimaryObject();
		p1.setId(1);
		p1.setName("name");
		
		TargetObject t1 = ModelFactory.eINSTANCE.createTargetObject();
		t1.setId(1);
		
		p1.setSingleNonContainmentReference(t1);
		
		resource.getContents().add(p1);
		resource.getContents().add(t1);
		
		resource.save(null);
		
		ds.getDefaultModel().write(System.out);
	}
	
	@Test
	public void testLoadPrimaryObject() {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://idtest"));
		
		EObject o = resource.getEObject("uri=http://junit.org/primary/1");
		System.out.println(o);
		
		assertNotNull(o);
		assertTrue(o instanceof PrimaryObject);
		
		PrimaryObject p = ((PrimaryObject)o);
		System.out.println(resource.getContents());
		System.out.println(p.getSingleNonContainmentReference());
		
		
		assertNotNull(p.getSingleNonContainmentReference());
//		assertTrue(p.getSingleNonContainmentReference() instanceof EObject);
//		assertTrue(p.getSingleNonContainmentReference().eIsProxy());
	}
}
