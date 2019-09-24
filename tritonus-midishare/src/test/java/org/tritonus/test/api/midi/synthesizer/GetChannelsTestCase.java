/*
 *	GetChannelsTestCase.java
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

import javax.sound.midi.Synthesizer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.sound.midi.MidiChannel;


/**	Test for javax.sound.midi.Synthesizer.getLatency().
 */
public class GetChannelsTestCase
extends BaseSynthesizerTestCase
{
	protected void checkSynthesizer(Synthesizer synth)
		throws Exception
	{
		MidiChannel[] channels;
		synth.open();
		try
		{
			channels = synth.getChannels();
			assertNotNull(
					channels, constructErrorMessage(synth, "getChannels() result null", true));
			int numChannels = channels.length;
			assertTrue(
					numChannels == 16, constructErrorMessage(synth, "getChannels() result has wrong length", true));
			for (int i = 0; i < channels.length; i++)
			{
				assertNotNull(
						channels[i], constructErrorMessage(synth, "getChannels() result element null", true));
			}
		}
		finally
		{
			synth.close();
		}
	}
}



/*** GetChannelsTestCase.java ***/
