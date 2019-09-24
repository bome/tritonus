/*
 *	JavaSequencerClockTestCase.java
 */

/*
 *  Copyright (c) 2003 by Matthias Pfisterer
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.tritonus.test.sequencer;

import org.tritonus.midi.device.java.JavaSequencer;
import org.tritonus.midi.device.java.SystemCurrentTimeMillisClock;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.tritonus.midi.device.java.SunMiscPerfClock;

/**	Base class for testsof javax.sound.midi.Sequencer.
 */
public class JavaSequencerClockTestCase
{
	private static final boolean IGNORE_SUN_SEQUENCER = true;


	public void testSunMiscPerfClock()
		throws Exception
	{
		checkClock(new SunMiscPerfClock());
	}



	public void testSystemCurrentTimeMillisClock()
		throws Exception
	{
		checkClock(new SystemCurrentTimeMillisClock());
	}



	private void checkClock(JavaSequencer.Clock clock)
		throws Exception
	{
		long lSystemStartTime = System.currentTimeMillis() * 1000;
		long lClockStartTime = clock.getMicroseconds();
		for (int i = 1; i <= 4; i++)
		{
			System.out.println("testing at " + System.currentTimeMillis());
			long lSystemElapsedTime = System.currentTimeMillis() * 1000
				- lSystemStartTime;
			long lClockElapsedTime = clock.getMicroseconds() - lClockStartTime;
			long lTimeDelta = lClockElapsedTime - lSystemElapsedTime;
			/* Here, we allow 1 millisecond difference. This test is
			   supposed to fail on platforms that have a low-resolution
			   implementation of System.currentTimeMillis().
			*/
			assertTrue(Math.abs(lTimeDelta) <= 1000, "clock precision");
			Thread.sleep((long) Math.pow(10, i));
		}
	}


	public void testSetGetClock()
		throws Exception
	{
		JavaSequencer seq = getSequencer();
		assertNotNull(seq.getClock(), "initial clock");
		JavaSequencer.Clock clock = new TestClock();
		seq.setClock(clock);
		assertSame(clock, seq.getClock(), "setClock");
	}


	// TODO: setClock() in open state throws IllegalStateException

	private JavaSequencer getSequencer()
	{
		return new JavaSequencer(null);
	}


	private static class TestClock
	implements JavaSequencer.Clock
	{
		public long getMicroseconds()
		{
			return -1;
		}
	}

}



/*** JavaSequencerClockTestCase.java ***/
