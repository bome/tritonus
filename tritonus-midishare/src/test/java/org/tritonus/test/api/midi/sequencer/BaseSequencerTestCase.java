/*
 *	BaseSequencerTestCase.java
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

package org.tritonus.test.api.midi.sequencer;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;

import org.junit.jupiter.api.Test;


/**	Base class for testsof javax.sound.midi.Sequencer.
 */
public abstract class BaseSequencerTestCase
{
	private static final boolean IGNORE_SUN_SEQUENCER = true;


	/**	Iterate over all available Sequencers.
	*/
    @Test
	public void testSeqencer()
		throws Exception
	{
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < infos.length; i++)
		{
			MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
			if (device instanceof Sequencer &&
				! (IGNORE_SUN_SEQUENCER &&
				   device.getDeviceInfo().getVendor().indexOf("Sun") != -1))
			{
				System.out.println("testing seq: " + device);
				checkSequencer((Sequencer) device);
			}
		}
	}



	protected abstract void checkSequencer(Sequencer seq)
		throws Exception;

	/** Get the prefix for error messages (containing the sequencer's name).
	 */
	protected static String getMessagePrefix(Sequencer seq)
	{
		return seq.getDeviceInfo().getName();
	}
}



/*** BaseSequencerTestCase.java ***/
