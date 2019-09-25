/*
 *	SequenceLengthTestCase.java
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

import java.io.InputStream;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import static org.junit.jupiter.api.Assertions.assertEquals;



/**	Tests for class javax.sound.midi.MidiMessage.
 */
public class SequenceLengthTestCase
extends BaseSequencerTestCase
{
	private static final String MIDI_FILENAME = "trippygaia1.mid";


	protected void checkSequencer(Sequencer seq)
		throws Exception
	{
		seq.open();

		Sequence sequence = MidiSystem.getSequence(getMediaFile(MIDI_FILENAME));
		seq.setSequence(sequence);
		assertEquals(sequence.getTickLength(),
					 seq.getTickLength(), getMessagePrefix(seq) + ": tick length");
		assertEquals(sequence.getMicrosecondLength(),
					 seq.getMicrosecondLength(), getMessagePrefix(seq) + ": time length");

		// clean up
		seq.close();
	}


	private static InputStream getMediaFile(String strFilename)
	{
		return SequenceLengthTestCase.class.getResourceAsStream("/sounds/" + strFilename);
	}
}



/*** SequenceLengthTestCase.java ***/
