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

import java.io.IOException;

import org.eclipselabs.emftriple.jena.junit.support.TDBTestSupport;
import org.eclipselabs.emftriple.junit.tests.EmfTripleBNodeTest;

/**
 * @author ghillairet
 *
 */
public class TDBEmfTripleBNodeTest extends EmfTripleBNodeTest {
	/**
	 * 
	 */
	public TDBEmfTripleBNodeTest() {
		super(new TDBTestSupport("tdb-bnode-test"));
		try {
			this.support.populateWithBNode();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
