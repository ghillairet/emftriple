package org.eclipselabs.emftriple.junit.suite;

import org.eclipselabs.emftriple.junit.tests.FakeTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	FakeTest.class
})
public class TestSuite {

}
