/*
 *	SunMiscPerfClock.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
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

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.midi.device.java;

/** Sequencer clock based on sun.misc.Perf.
	Sun JDK 1.4.2 or later is required to compile this class.
 */
public class SunMiscPerfClock
implements JavaSequencer.Clock
{
	private long m_nanos;


	public SunMiscPerfClock()
	{
		m_nanos = System.nanoTime();
	}


	/**	Retrieve system time in microseconds.
		This method retrieves the time by calling
		@return the system time in microseconds
	*/
	public long getMicroseconds()
	{
		return (System.nanoTime() - m_nanos) / 1000;
	}
}



/*** SunMiscPerfClock.java ***/
