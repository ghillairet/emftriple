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
package org.eclipselabs.emftriple.sail.junit.tests;

import java.io.File;

import org.eclipselabs.emftriple.junit.tests.EmfTripleReferencesTest;
import org.eclipselabs.emftriple.sail.junit.support.SailTestSupport;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailException;
import org.openrdf.sail.nativerdf.NativeStore;

/**
 * @author ghillairet
 *
 */
public class SailEmfTripleReferencesTest extends EmfTripleReferencesTest {

	private static final File folder;
	private static final Sail repository;
	
	static {
		folder = new File("sail-refs-test");
		repository = new SailRepository(new NativeStore(folder)).getSail();
		try {
			repository.initialize();
		} catch (SailException e) {
			e.printStackTrace();
		}
	}
	
	public SailEmfTripleReferencesTest() {
		super(new SailTestSupport("sail-refs-test", repository));
	}
}
