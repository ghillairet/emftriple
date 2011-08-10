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
package org.eclipselabs.emftriple.sail.junit.suite;

import org.eclipselabs.emftriple.sail.junit.tests.SailEmfTripleAttributesTest;
import org.eclipselabs.emftriple.sail.junit.tests.SailEmfTripleBasicTest;
import org.eclipselabs.emftriple.sail.junit.tests.SailEmfTripleQueryResultTest;
import org.eclipselabs.emftriple.sail.junit.tests.SailEmfTripleReferencesTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author ghillairet
 * @since 0.9.0
 */
@RunWith(Suite.class)
@SuiteClasses({
	SailEmfTripleBasicTest.class, SailEmfTripleAttributesTest.class,
	SailEmfTripleQueryResultTest.class, SailEmfTripleReferencesTest.class
})
public class SailTestSuite {}

