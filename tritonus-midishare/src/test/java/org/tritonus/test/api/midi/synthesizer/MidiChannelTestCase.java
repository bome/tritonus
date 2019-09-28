/*
 *	MidiChannelTestCase.java
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

import javax.sound.midi.MidiChannel;
import javax.sound.midi.Synthesizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**	Test for javax.sound.midi.Synthesizer.getLatency().
 */
public class MidiChannelTestCase
extends BaseSynthesizerTestCase
{
	protected void checkSynthesizer(Synthesizer synth)
		throws Exception
	{
		MidiChannel channel;
		synth.open();
		try
		{
			channel = synth.getChannels()[0];
			checkNotes(synth, channel);
			checkNotes2(synth, channel);
			checkPolyPressure(synth, channel);
			checkChannelPressure(synth, channel);
			checkProgramChange(synth, channel);
			checkProgramChange2(synth, channel);
			checkPitchbend(synth, channel);
		}
		finally
		{
			synth.close();
		}
	}

	private void checkNotes(Synthesizer synth, MidiChannel channel)
	{
		for (int i = 0; i < 127; i++)
		{
			channel.noteOn(i, i);
		}
		for (int i = 0; i < 127; i++)
		{
			channel.noteOff(i);
		}
	}


	private void checkNotes2(Synthesizer synth, MidiChannel channel)
	{
		for (int i = 0; i < 127; i++)
		{
			channel.noteOn(i, i);
		}
		for (int i = 0; i < 127; i++)
		{
			channel.noteOff(i, 0);
		}
	}


	private void checkPolyPressure(Synthesizer synth, MidiChannel channel)
	{
		for (int i = 0; i < 127; i++)
		{
			channel.setPolyPressure(i, i);
			int value = channel.getPolyPressure(i);
			assertTrue(i == value || value == 0,
					constructErrorMessage(synth,
                    		"poly pressure[" + i + "]", true));
		}
	}


	private void checkChannelPressure(Synthesizer synth, MidiChannel channel)
	{
		checkChannelPressure(synth, channel, 0);
		checkChannelPressure(synth, channel, 77);
		checkChannelPressure(synth, channel, 127);
	}


	private void checkChannelPressure(Synthesizer synth, MidiChannel channel,
			int nPressure)
	{
		channel.setChannelPressure(nPressure);
		int value = channel.getChannelPressure();
		assertTrue(nPressure == value || value == 0,
				constructErrorMessage(synth,
                		"channel pressure", true));
	}


	private void checkControlChange(Synthesizer synth, MidiChannel channel)
	{
		for (int i = 0; i < 127; i++)
		{
			channel.controlChange(i, i);
			int value = channel.getController(i);
			assertTrue(i == value || value == 0,
					constructErrorMessage(synth,
                    		"control change[" + i + "]", true));
		}
	}


	private void checkProgramChange(Synthesizer synth, MidiChannel channel)
	{
		for (int i = 0; i < 127; i++)
		{
			channel.programChange(i);
			int value = channel.getProgram();
			assertEquals(i,
					value, constructErrorMessage(synth,
                    		"program change [" + i + "]", true));
		}
	}


	private void checkProgramChange2(Synthesizer synth, MidiChannel channel)
	{
		checkProgramChange2(synth, channel, 0, 0);
		checkProgramChange2(synth, channel, 12000, 102);
		checkProgramChange2(synth, channel, 16383, 127);
	}


	private void checkProgramChange2(Synthesizer synth, MidiChannel channel,
			int nBank, int nProgram)
	{
		channel.programChange(nBank, nProgram);
			int programValue = channel.getProgram();
			int bankValue = channel.getController(0) * 128
				+ channel.getController(32);
			assertTrue(nBank == bankValue || bankValue == 0,
					constructErrorMessage(synth,
                    		"program change [" + nBank + ", " + nProgram + "]: bank", true));
			assertEquals(nProgram,
					programValue, constructErrorMessage(synth,
                    		"program change [" + nBank + ", " + nProgram + "]: program", true));
	}


	private void checkPitchbend(Synthesizer synth, MidiChannel channel)
	{
		checkPitchbend(synth, channel, 0);
		checkPitchbend(synth, channel, 127);
		checkPitchbend(synth, channel, 128);
		checkPitchbend(synth, channel, 8192);
System.err.println(synth.getDeviceInfo().getName());
		if (synth.getDeviceInfo().getName().indexOf("Gervill") == -1) {
		    checkPitchbend(synth, channel, 16383);
		}
	}


	private void checkPitchbend(Synthesizer synth, MidiChannel channel,
			int nBend)
	{
		channel.setPitchBend(nBend);
			int value = channel.getPitchBend();
			assertTrue(nBend == value || value == 8192,
					constructErrorMessage(synth,
                    		"pitch bend [" + nBend + "]", true));
	}
}



/*** MidiChannelTestCase.java ***/
