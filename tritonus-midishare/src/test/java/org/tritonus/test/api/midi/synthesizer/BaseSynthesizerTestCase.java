/*
 *	BaseSynthesizerTestCase.java
 */

/*
 *  Copyright (c) 2006 by Matthias Pfisterer
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

package org.tritonus.test.api.midi.synthesizer;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**	Base class for testsof javax.sound.midi.Synthesizer.
 */
@Disabled
public abstract class BaseSynthesizerTestCase
{
	private static final boolean IGNORE_SUN_SYNTHESIZER = false;


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
			if (device instanceof Synthesizer &&
				! (IGNORE_SUN_SYNTHESIZER &&
				   device.getDeviceInfo().getVendor().indexOf("Sun") != -1))
			{
				System.out.println("testing synth: " + device);
				checkSynthesizer((Synthesizer) device);
			}
		}
	}



	protected abstract void checkSynthesizer(Synthesizer seq)
		throws Exception;

	protected static String constructErrorMessage(Synthesizer synth,
						String strMessage,
						boolean bOpen)
	{
		String strAll = getMessagePrefix(synth) + strMessage;
		strAll += " in " + (bOpen ? "open" : "closed") + " state";
		return strAll;
	}


	/** Get the prefix for error messages (containing the Synthesizer's name).
	 */
	protected static String getMessagePrefix(Synthesizer seq)
	{
		return seq.getDeviceInfo().getName() + ": ";
	}
}



/*** BaseSynthesizerTestCase.java ***/
