/**
 * 
 */
package org.eclipselabs.emftriple.jena.junit.tests;

import org.eclipselabs.emftriple.jena.junit.support.TDBTestSupport;

/**
 * @author ghillairet
 *
 */
public class TDBEmfTripleQueryResultTest extends EmfTripleQueryResultTest {

	/**
	 * 
	 */
	public TDBEmfTripleQueryResultTest() {
		super(new TDBTestSupport("tdb-query-test"));
	}

}
