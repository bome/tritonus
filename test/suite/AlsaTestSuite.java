import	junit.framework.Test;
import	junit.framework.TestSuite;

import	org.tritonus.lowlevel.alsa.AlsaCtl;



public class AlsaTestSuite
{
	public static Test suite()
	{
		TestSuite	suite = new TestSuite();
		suite.addTest(new TestSuite(AlsaCtlTestCase.class));
		suite.addTest(new TestSuite(AlsaMixerTestCase.class));
		return suite;
	}



	public static void main(String[] args)
	{
		Test	suite = suite();
		junit.textui.TestRunner.run(suite);
	}
}

