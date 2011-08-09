/**
 * 
 */
package org.eclipselabs.emftriple.jena.junit.tests;

import org.eclipselabs.emftriple.jena.junit.support.FileTestSupport;

/**
 * @author ghillairet
 *
 */
public class FileEmfTripleQueryResultTest extends EmfTripleQueryResultTest {
	
	public FileEmfTripleQueryResultTest() {
		super(new FileTestSupport("file-query-test.ttl", "TTL"));
	}
}
