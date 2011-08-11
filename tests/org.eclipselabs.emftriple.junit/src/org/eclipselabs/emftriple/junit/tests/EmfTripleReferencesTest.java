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
package org.eclipselabs.emftriple.junit.tests;

import java.io.IOException;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipselabs.emftriple.junit.model.ModelFactory;
import org.eclipselabs.emftriple.junit.model.PrimaryObject;
import org.eclipselabs.emftriple.junit.model.TargetObject;
import org.eclipselabs.emftriple.junit.support.TestSupport;
import org.junit.Before;
import org.junit.Test;

/**
 * @author ghillairet
 *
 */
public class EmfTripleReferencesTest {

	TestSupport support;
	
	public EmfTripleReferencesTest(TestSupport support) {
		this.support = support;
		this.support.init();
	}
	
	@Before
	public void setUp() {
		Resource resource = support.createResource(null);
		try {
			resource.delete(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPrimaryObject() {
		PrimaryObject object = ModelFactory.eINSTANCE.createPrimaryObject();
		object.setId(1);
		object.setName("foo");
		
		support.saveObject(object);
		
		support.checkObject(object);
	}
	
	@Test
	public void testUnsettableReferenceToNull() {
		
	}
	
	@Test
	public void testUnsettableReferenceUnset() {
		
	}
	
	@Test
	public void testPrimaryObjectWithSingleContainmentReferenceNoProxies() {
		PrimaryObject primary = ModelFactory.eINSTANCE.createPrimaryObject();
		primary.setId(1);
		primary.setName("foo");
		
		TargetObject target = ModelFactory.eINSTANCE.createTargetObject();
		target.setId(1);
		target.setSingleAttribute("bar");
		
		primary.setSingleContainmentReferenceNoProxies(target);
		
		support.saveObject(primary);
		
		support.checkObject(primary);
	}
	
	@Test
	public void testPrimaryObjectWithManyContainmentReferenceNoProxies() {
		PrimaryObject primary = ModelFactory.eINSTANCE.createPrimaryObject();
		primary.setId(1);
		primary.setName("foo");
		
		TargetObject t1 = ModelFactory.eINSTANCE.createTargetObject();
		t1.setId(1);
		t1.setSingleAttribute("bar");
		
		TargetObject t2 = ModelFactory.eINSTANCE.createTargetObject();
		t2.setId(2);
		t2.setSingleAttribute("barbar");
		
		primary.getMultipleContainmentReferenceNoProxies().add(t1);
		primary.getMultipleContainmentReferenceNoProxies().add(t2);
		
		support.saveObject(primary);
		
		support.checkObject(primary);
	}
	
	@Test
	public void testPrimaryObjectWithSingleNoContainmentReference() {
		PrimaryObject primary = ModelFactory.eINSTANCE.createPrimaryObject();
		primary.setId(1);
		primary.setName("foo");
		
		TargetObject t1 = ModelFactory.eINSTANCE.createTargetObject();
		t1.setId(1);
		t1.setSingleAttribute("bar");
		
		primary.setSingleNonContainmentReference(t1);
		
		support.saveObject(primary);
		support.saveObject(t1);
		
		support.checkObject(primary);
	}
	
	@Test
	public void testPrimaryObjectWithManyNoContainmentReference() {
		PrimaryObject primary = ModelFactory.eINSTANCE.createPrimaryObject();
		primary.setId(1);
		primary.setName("foo");
		
		TargetObject t1 = ModelFactory.eINSTANCE.createTargetObject();
		t1.setId(1);
		t1.setSingleAttribute("bar");
		
		TargetObject t2 = ModelFactory.eINSTANCE.createTargetObject();
		t2.setId(2);
		t2.setSingleAttribute("barbar");
		
		primary.getMultipleNonContainmentReference().add(t1);
		primary.getMultipleNonContainmentReference().add(t2);
		
		support.saveObject(primary);
		support.saveObject(t1);
		support.saveObject(t2);
		
		support.checkObject(primary);
	}
	
	@Test
	public void testPrimaryObjectWithSingleContainmentReferenceProxies() {
//		ResourceSet resourceSet = support.getResourceSet();
	}
	
	@Test
	public void testPrimaryObjectWithManyContainmentReferenceProxies() {
//		ResourceSet resourceSet = support.getResourceSet();
	}
}
