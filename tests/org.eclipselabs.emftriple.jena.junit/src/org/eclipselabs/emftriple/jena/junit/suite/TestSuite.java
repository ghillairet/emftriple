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

import org.eclipselabs.emftriple.jena.junit.tests.FileEmfTripleBNodeTest;
import org.eclipselabs.emftriple.jena.junit.tests.FileEmfTripleBasicTest;
import org.eclipselabs.emftriple.jena.junit.tests.FileEmfTripleQueryResultTest;
import org.eclipselabs.emftriple.jena.junit.tests.TDBEmfTripleBNodeTest;
import org.eclipselabs.emftriple.jena.junit.tests.TDBEmfTripleBasicTest;
import org.eclipselabs.emftriple.jena.junit.tests.TDBEmfTripleQueryResultTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TDBEmfTripleBasicTest.class, TDBEmfTripleQueryResultTest.class, TDBEmfTripleBNodeTest.class,
	FileEmfTripleBasicTest.class, FileEmfTripleQueryResultTest.class, FileEmfTripleBNodeTest.class
})
public class TestSuite {}
