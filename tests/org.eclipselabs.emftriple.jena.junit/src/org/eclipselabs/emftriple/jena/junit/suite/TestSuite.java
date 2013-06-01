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
package org.eclipselabs.emftriple.jena.junit.suite;

import org.eclipselabs.emftriple.jena.junit.tests.BasicTest;
import org.eclipselabs.emftriple.jena.junit.tests.TDBBasicTest;
import org.eclipselabs.emftriple.jena.junit.tests.TTLBasicTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author ghillairet
 * @since 0.9.0
 */
@RunWith(Suite.class)
@SuiteClasses({
	BasicTest.class,
	TDBBasicTest.class,
	TTLBasicTest.class
})
public class TestSuite {}
