/*
 *	AlsaTestSuite.java
 */

/*
 *  Copyright (c) 2001 - 2002 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

import	junit.framework.Test;
import	junit.framework.TestSuite;



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



/*** AlsaTestSuite.java ***/
