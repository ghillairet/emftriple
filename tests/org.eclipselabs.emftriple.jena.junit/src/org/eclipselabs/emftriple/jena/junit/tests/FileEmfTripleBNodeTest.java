/**
 * 
 */
package org.eclipselabs.emftriple.jena.junit.tests;

import org.eclipselabs.emftriple.jena.junit.support.FileTestSupport;

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
