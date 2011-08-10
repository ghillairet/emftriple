/**
 * 
 */
package org.eclipselabs.emftriple.sesame.junit.suite;

import org.eclipselabs.emftriple.sesame.junit.tests.SesameNativeEmfTripleBasicTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author ghillairet
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
	SesameNativeEmfTripleBasicTest.class
})
public class SesameTestSuite {}
