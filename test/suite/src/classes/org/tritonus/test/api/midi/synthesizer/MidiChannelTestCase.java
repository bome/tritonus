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

import javax.sound.midi.Synthesizer;
import javax.sound.midi.MidiChannel;


/**	Test for javax.sound.midi.Synthesizer.getLatency().
 */
public class MidiChannelTestCase
extends BaseSynthesizerTestCase
{
	public MidiChannelTestCase(String strName)
	{
		super(strName);
	}



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
			assertTrue(constructErrorMessage(synth,
					"poly pressure[" + i + "]", true),
					i == value || value == 0);
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
		assertTrue(constructErrorMessage(synth,
				"channel pressure", true),
				nPressure == value || value == 0);
	}


	private void checkControlChange(Synthesizer synth, MidiChannel channel)
	{
		for (int i = 0; i < 127; i++)
		{
			channel.controlChange(i, i);
			int value = channel.getController(i);
			assertTrue(constructErrorMessage(synth,
					"control change[" + i + "]", true),
					i == value || value == 0);
		}
	}


	private void checkProgramChange(Synthesizer synth, MidiChannel channel)
	{
		for (int i = 0; i < 127; i++)
		{
			channel.programChange(i);
			int value = channel.getProgram();
			assertEquals(constructErrorMessage(synth,
					"program change [" + i + "]", true),
					i, value);
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
			assertTrue(constructErrorMessage(synth,
					"program change [" + nBank + ", " + nProgram + "]: bank", true),
					nBank == bankValue || bankValue == 0);
			assertEquals(constructErrorMessage(synth,
					"program change [" + nBank + ", " + nProgram + "]: program", true),
					nProgram, programValue);
	}


	private void checkPitchbend(Synthesizer synth, MidiChannel channel)
	{
		checkPitchbend(synth, channel, 0);
		checkPitchbend(synth, channel, 127);
		checkPitchbend(synth, channel, 128);
		checkPitchbend(synth, channel, 8192);
		checkPitchbend(synth, channel, 16383);
	}


	private void checkPitchbend(Synthesizer synth, MidiChannel channel,
			int nBend)
	{
		channel.setPitchBend(nBend);
			int value = channel.getPitchBend();
			assertTrue(constructErrorMessage(synth,
					"pitch bend [" + nBend + "]", true),
					nBend == value || value == 8192);
	}
}



/*** MidiChannelTestCase.java ***/
