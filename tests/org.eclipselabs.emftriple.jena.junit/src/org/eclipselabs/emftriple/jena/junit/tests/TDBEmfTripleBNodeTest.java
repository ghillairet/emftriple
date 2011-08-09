/**
 * 
 */
package org.eclipselabs.emftriple.jena.junit.tests;

import java.io.IOException;

import org.eclipselabs.emftriple.jena.junit.support.TDBTestSupport;

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
