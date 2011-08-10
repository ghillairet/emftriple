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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipselabs.emftriple.internal.util.ETripleEcoreUtil;
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
public abstract class EmfTripleAttributesTest {

	TestSupport support;
	
	@Before
	public void setUp() throws IOException {
		Resource resource = support.createResource(null);
		resource.delete(null);
	}
	
	public EmfTripleAttributesTest(TestSupport support) {
		this.support = support;
		this.support.init();
	}
	
	@Test
	public void testTargetObjectWithSingleAttribute() {
		assertEquals(0L, support.dataStoreSize());
		
		TargetObject target = ModelFactory.eINSTANCE.createTargetObject();
		target.setId(1);
		target.setSingleAttribute("foo");
		
		support.saveObject(target);
		
		assertEquals(3L, support.dataStoreSize());
		
		support.checkObject(target);
	}
	
	@Test
	public void testTargetObjectWithArrayAttribute() {
		assertEquals(0L, support.dataStoreSize());
		
		TargetObject target = ModelFactory.eINSTANCE.createTargetObject();
		target.setId(2);
		target.getArrayAttribute().add("one");
		target.getArrayAttribute().add("two");
		
		support.saveObject(target);
		
		assertEquals(4L, support.dataStoreSize());
		
		support.checkObject(target);
	}
	
	@Test
	public void testTargetObjectUpdateSingleAttribute() throws IOException {
		assertEquals(0L, support.dataStoreSize());
		
		TargetObject target = ModelFactory.eINSTANCE.createTargetObject();
		target.setId(3);
		target.setSingleAttribute("foo");
		
		support.saveObject(target);
		
		assertEquals(3L, support.dataStoreSize());
		
		target.setSingleAttribute("bar");
		target.eResource().save(null);
		
		assertEquals(3L, support.dataStoreSize());
		
		support.checkObject(target);
	}
	
	@Test
	public void testTargetObjectUpdateArrayAttribute() throws IOException {
		assertEquals(0L, support.dataStoreSize());
		
		TargetObject target = ModelFactory.eINSTANCE.createTargetObject();
		target.setId(4);
		target.getArrayAttribute().add("foo");
		target.getArrayAttribute().add("bar");
		
		support.saveObject(target);
		
		assertEquals(4L, support.dataStoreSize());
		
		target.getArrayAttribute().add("foobar");
		target.eResource().save(null);
		
		assertEquals(5L, support.dataStoreSize());
		
		support.checkObject(target);
	}
	
	@Test
	public void testUnsettableAttributeSetToNull() throws IOException {
		assertEquals(0L, support.dataStoreSize());
		
		PrimaryObject object = ModelFactory.eINSTANCE.createPrimaryObject();
		object.setId(5);
		object.setUnsettableAttribute(null);
		
		support.saveObject(object);
		
		assertEquals(2L, support.dataStoreSize());
		
		Resource resource = support.createResource("uri="+ETripleEcoreUtil.getID(object));
		resource.load(null);
		
		PrimaryObject loaded = (PrimaryObject)resource.getContents().get(0);
		
		assertTrue(loaded.isSetUnsettableAttribute());
		assertEquals(loaded.getUnsettableAttribute(), null);
	}
	
	@Test
	public void testFeatureMap() {
		assertEquals(0L, support.dataStoreSize());
		
		PrimaryObject object = ModelFactory.eINSTANCE.createPrimaryObject();
		object.setId(6);
		object.setName("foo");
		
		object.getFeatureMapAttributeType1().add("Hello");
		object.getFeatureMapAttributeType2().add("World");
		
		assertEquals(2, object.getFeatureMapAttributeCollection().size());
		assertEquals(1, object.getFeatureMapAttributeType1().size());
		assertEquals(1, object.getFeatureMapAttributeType2().size());
		
		support.saveObject(object);
		
		assertEquals(5L, support.dataStoreSize());
		
		support.checkObject(object);
	}
}
