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
package org.eclipselabs.emftriple.sesame.junit.tests;

import java.io.File;

import org.eclipselabs.emftriple.junit.tests.EmfTripleBasicTest;
import org.eclipselabs.emftriple.sesame.junit.support.SesameNativeTestSupport;
import org.openrdf.repository.Repository;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.nativerdf.NativeStore;

/**
 * @author ghillairet
 *
 */
public class SesameNativeEmfTripleBasicTest extends EmfTripleBasicTest {

	protected static Repository repository;
	
	static {
		File dataDir = new File("native-basic-test");
		repository = new SailRepository(new NativeStore(dataDir));
	}
	
	public SesameNativeEmfTripleBasicTest() {
		super(new SesameNativeTestSupport("native-basic-test", repository));
	}

}
