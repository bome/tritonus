/*
 *	BaseMidiDeviceTestCase.java
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

package org.tritonus.test.api.midi.device;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;

/**	Base class for tests of javax.sound.midi.MidiDevice.
 */
public abstract class BaseMidiDeviceTestCase
{
	/**	Iterate over all available MidiDevices.
	*/
	protected void checkMidiDevice(Check check)
		throws Exception
	{
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < infos.length; i++)
		{
			MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
			System.out.println("testing device: " + device);
			check.check(device);
		}
	}



	/** Get the prefix for error messages (containing the sequencer's name).
	 */
	protected static String getMessagePrefix(MidiDevice device)
	{
		return device.getDeviceInfo().getName();
	}


	protected interface Check
	{
		public void check(MidiDevice device)
			throws Exception;
	}
}



/*** BaseMidiDeviceTestCase.java ***/
