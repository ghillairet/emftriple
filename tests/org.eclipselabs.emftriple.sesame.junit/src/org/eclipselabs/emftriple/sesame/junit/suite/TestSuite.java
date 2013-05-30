/**
 * 
 */
package org.eclipselabs.emftriple.sesame.junit.suite;

import org.eclipselabs.emftriple.sesame.junit.tests.BasicTest;
import org.eclipselabs.emftriple.sesame.junit.tests.RepositoryBasicTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	BasicTest.class,
	RepositoryBasicTest.class
})
public class TestSuite {}
