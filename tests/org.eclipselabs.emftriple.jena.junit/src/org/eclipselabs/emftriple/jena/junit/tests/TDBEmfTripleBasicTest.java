/**
 * 
 */
package org.eclipselabs.emftriple.jena.junit.tests;

import org.eclipselabs.emftriple.jena.junit.support.TDBTestSupport;

/**
 * @author ghillairet
 *
 */
public class TDBEmfTripleBasicTest extends EmfTripleBasicTest {

	/**
	 * 
	 */
	public TDBEmfTripleBasicTest() {
		super(new TDBTestSupport("tdb-basic-test"));
	}
	
}
