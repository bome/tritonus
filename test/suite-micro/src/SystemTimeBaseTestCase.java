/*
 *	SystemTimeBaseTestCase.java
 */

/*
 *  Copyright (c) 2002 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
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

import	junit.framework.TestCase;

import	javax.microedition.media.Manager;
import	javax.microedition.media.TimeBase;



/**	TestCase for the system time base.
	This class tests the javax.microedition.media.TimeBase
	that is returned as the system time base from
	Manager.getSystemTimeBase().
*/
public class SystemTimeBaseTestCase
extends TestCase
{
	public SystemTimeBaseTestCase(String strName)
	{
		super(strName);
	}


	/**	Checks that the system time base's time is
		non-negative and non-decreasing.
	*/
	public void testGetTime()
		throws Exception
	{
		TimeBase	timeBase = Manager.getSystemTimeBase();
		long		lTime0 = timeBase.getTime();
		assertTrue("time non-negative", lTime0 >= 0);
		long		lTime1 = timeBase.getTime();
		assertTrue("time non-decreasing", lTime1 >= lTime0);
	}


	/**	Checks the accuracy of the system time base's
		time. As a reference time, the system time is used.
	*/
	public void testAccuracy()
		throws Exception
	{
		long		lTestMilliseconds = 1000;
		// 0.01 means 1 %
		double		dAcceptableDifferenceFactor = 0.01;
		TimeBase	timeBase = Manager.getSystemTimeBase();
		long		lSystemTime0 = System.currentTimeMillis();
		long		lTimeBaseTime0 = timeBase.getTime();
		Thread.sleep(lTestMilliseconds);
		long		lSystemTime1 = System.currentTimeMillis();
		long		lTimeBaseTime1 = timeBase.getTime();

		long		lSystemTimeSpan = (lSystemTime1 - lSystemTime0) * 1000;
		long		lTimeBaseTimeSpan = lTimeBaseTime1 - lTimeBaseTime0;
		long		lDifference = Math.abs(lTimeBaseTimeSpan - lSystemTimeSpan);
		long		lAcceptableDifference = (long) (lSystemTimeSpan * dAcceptableDifferenceFactor);
		// System.out.println("difference: " + lDifference);
		// System.out.println("acc. difference: " + lAcceptableDifference);
		assertTrue("TimeBase accuracy", lDifference < lAcceptableDifference);
	}
}



/*** SystemTimeBaseTestCase.java ***/
