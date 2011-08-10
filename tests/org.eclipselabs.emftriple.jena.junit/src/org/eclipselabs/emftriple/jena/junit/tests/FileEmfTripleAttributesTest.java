/**
 * 
 */
package org.eclipselabs.emftriple.jena.junit.tests;

import org.eclipselabs.emftriple.jena.junit.support.FileTestSupport;
import org.eclipselabs.emftriple.junit.tests.EmfTripleAttributesTest;

/**
 * @author ghillairet
 *
 */
public class FileEmfTripleAttributesTest extends EmfTripleAttributesTest {

	/**
	 * @param support
	 */
	public FileEmfTripleAttributesTest() {
		super(new FileTestSupport("file-attrs-test.ttl", "TTL"));
	}
	
}
