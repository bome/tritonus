/*
 *	TempoTestCase.java
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

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;

import static org.junit.jupiter.api.Assertions.assertEquals;
//import javax.sound.midi.InvalidMidiDataException;



/**	Tests for class javax.sound.midi.MidiMessage.
 */
public class TempoTestCase
extends BaseSequencerTestCase
{
	private static final float DELTA = 1.0E-9F;
	private static final float MPQ0 = 500000;
	private static final float BPM0 = 120;
	private static final float MPQ1 = 600000;
	private static final float BPM1 = 100;
	private static final float MPQ2 = 416666.66F;
	private static final float BPM2 = 144;

	private static final byte[] TEMPOTEXT =
	{
		't', 'e', 'm', 'p', 'o'
	};


	protected void checkSequencer(Sequencer seq)
		throws Exception
	{
System.err.println(seq.getDeviceInfo().getName());
		// initial tempo
		checkTempoValues("initial", seq, MPQ0, BPM0, 1.0F);

		// setting values in closed state
		seq.setTempoInMPQ(MPQ1);
		checkTempoValues("closed: setMPQ", seq, MPQ1, BPM1, 1.0F);
		seq.setTempoInBPM(BPM2);
		checkTempoValues("closed: setBPM", seq, MPQ2, BPM2, 1.0F);
		seq.setTempoFactor(2.0F);
	    checkTempoValues("closed: setFactor", seq, MPQ2, BPM2, 2.0F);

		seq.setSequence(createSequence());

        if (seq.getDeviceInfo().getName().indexOf("Real Time Sequencer") != -1) {
            checkTempoValues("closed: after setSequence()", seq, MPQ2, BPM2, 2.0F); // TODO ???
        } else {
            checkTempoValues("closed: after setSequence()", seq, MPQ2, BPM2, 1.0F);
        }

		seq.open();

        if (seq.getDeviceInfo().getName().indexOf("Real Time Sequencer") != -1) {
            checkTempoValues("after open()", seq, 0, 6.0E7f, 1.0F); // TODO ???
        } else {
            checkTempoValues("after open()", seq, MPQ2, BPM2, 1.0F);
        }

		// setting values in open state
		seq.setTempoInMPQ(MPQ1);
		checkTempoValues("open: setMPQ", seq, MPQ1, BPM1, 1.0F);
		seq.setTempoInBPM(BPM2);
		checkTempoValues("open: setBPM", seq, MPQ2, BPM2, 1.0F);
		seq.setTempoFactor(3.0F);
		checkTempoValues("open: setFactor", seq, MPQ2, BPM2, 3.0F);

		seq.start();

		checkTempoValues("after start()", seq, MPQ2, BPM2, 3.0F);

		// setting values in start state
		seq.setTempoInMPQ(MPQ1);
		checkTempoValues("started: setMPQ", seq, MPQ1, BPM1, 3.0F);
		seq.setTempoInBPM(BPM2);
		checkTempoValues("started: setBPM", seq, MPQ2, BPM2, 3.0F);
		seq.setTempoFactor(2.0F);
		checkTempoValues("started: setFactor", seq, MPQ2, BPM2, 2.0F);

		seq.stop();

		checkTempoValues("after stop()", seq, MPQ2, BPM2, 2.0F);

		// setting values in start state
		seq.setTempoInMPQ(MPQ1);
		checkTempoValues("stopped: setMPQ", seq, MPQ1, BPM1, 2.0F);
		seq.setTempoInBPM(BPM2);
		checkTempoValues("stopped: setBPM", seq, MPQ2, BPM2, 2.0F);
		seq.setTempoFactor(3.0F);
		checkTempoValues("stopped: setFactor", seq, MPQ2, BPM2, 3.0F);

		seq.close();

        if (seq.getDeviceInfo().getName().indexOf("Real Time Sequencer") != -1) {
            checkTempoValues("after close()", seq, 500000.0f, 120.0f, 1.0F); // TODO ???
        } else {
            checkTempoValues("after close()", seq, MPQ2, BPM2, 3.0F);
        }
	}


	private void checkTempoValues(String strMessagePrefix,
								  Sequencer seq,
								  float fExpectedMPQ,
								  float fExpectedBPM,
								  float fExpectedFactor)
	{
		assertEquals(fExpectedMPQ, seq.getTempoInMPQ(), DELTA, strMessagePrefix + " tempo in MPQ");
		assertEquals(fExpectedBPM, seq.getTempoInBPM(), DELTA, strMessagePrefix + " tempo in BPM");
		assertEquals(fExpectedFactor, seq.getTempoFactor(), DELTA, strMessagePrefix + " tempo factor");
	}



	private static Sequence createSequence()
		throws Exception
	{
		Sequence sequence = new Sequence(Sequence.PPQ, 480);
		Track track = sequence.createTrack();
		for (long lTick = 0; lTick < 100000; lTick += 1000)
		{
			MetaMessage mm = new MetaMessage();
			mm.setMessage(6, TEMPOTEXT, TEMPOTEXT.length);
			MidiEvent me = new MidiEvent(mm, lTick);
			track.add(me);
		}
		return sequence;
	}


	private static class TempoDetector
	implements MetaEventListener
	{
		private long[] m_alArrivalTimes;


		public void meta(MetaMessage message)
		{
			if (message.getType() == 6)
			{
				System.arraycopy(m_alArrivalTimes, 0, m_alArrivalTimes, 1, 9);
				m_alArrivalTimes[0] = System.currentTimeMillis();
			}
		}


		public float getTempoInMPQ()
		{
			return 0.0F;
		}
	}
}



/*** TempoTestCase.java ***/
