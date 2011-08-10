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
package org.eclipselabs.emftriple.jena.junit.tests;

import org.eclipselabs.emftriple.jena.junit.support.FileTestSupport;
import org.eclipselabs.emftriple.junit.tests.EmfTripleBNodeTest;

/**
 * @author ghillairet
 *
 */
public class FileEmfTripleBNodeTest extends EmfTripleBNodeTest {

	/**
	 * 
	 */
	public FileEmfTripleBNodeTest() {
		super(new FileTestSupport("bnode.ttl", "TTL"));
	}
}
