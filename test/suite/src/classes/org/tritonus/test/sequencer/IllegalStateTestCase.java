/*
 *	IllegalStateTestCase.java
 */

/*
 *  Copyright (c) 2003 by Matthias Pfisterer
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package org.tritonus.test.sequencer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.InvalidMidiDataException;



/**	Tests for class javax.sound.midi.MidiMessage.
 */
public class IllegalStateTestCase
extends BaseSequencerTestCase
{
	public IllegalStateTestCase(String strName)
	{
		super(strName);
	}



	protected void checkSequencer(Sequencer seq)
		throws Exception
	{
		// Sequencer is closed
		checkStartStop(seq, false);
		checkGetSetSequence(seq, false);

		// sequencer open
		seq.open();

		checkStartStop(seq, true);
		checkGetSetSequence(seq, true);

		// clean up
		seq.close();
	}


	private void checkStartStop(Sequencer seq, boolean bOpen)
		throws Exception
	{
		boolean bExpectingException = ! bOpen;
		checkMethod(seq, "start()", bExpectingException, bOpen);
		checkMethod(seq, "stop()", bExpectingException, bOpen);
		checkMethod(seq, "startRecording()", bExpectingException, bOpen);
		checkMethod(seq, "stopRecording()", bExpectingException, bOpen);
	}



	private void checkGetSetSequence(Sequencer seq, boolean bOpen)
		throws Exception
	{
		boolean bExpectingException = false;
		checkMethod(seq, "setSequence(Sequence)", bExpectingException, bOpen);
		checkMethod(seq, "setSequence(InputStream)", bExpectingException, bOpen);
		checkMethod(seq, "getSequence()", bExpectingException, bOpen);
		checkMethod(seq, "isRunning()", bExpectingException, bOpen);
	}



	private void checkMethod(Sequencer seq, String strMethodName,
							 boolean bExceptionExpected, boolean bOpen)
		throws Exception
	{
		try
		{
			if ("start()".equals(strMethodName))
			{
				seq.start();
			}
			else if ("stop()".equals(strMethodName))
			{
				seq.stop();
			}
			else if ("startRecording()".equals(strMethodName))
			{
				seq.startRecording();
			}
			else if ("stopRecording()".equals(strMethodName))
			{
				seq.stopRecording();
			}
			else if ("setSequence(Sequence)".equals(strMethodName))
			{
				seq.setSequence(createSequence());
			}
			else if ("setSequence(InputStream)".equals(strMethodName))
			{
				seq.setSequence(createSequenceInputStream());
			}
			else if ("getSequence()".equals(strMethodName))
			{
				seq.getSequence();
			}
			else if ("isRunning()".equals(strMethodName))
			{
				seq.isRunning();
			}
			else
			{
				fail("unknown method name");
			}
			if (bExceptionExpected)
			{
				fail(constructErrorMessage(strMethodName, bExceptionExpected, bOpen));
			}
		}
		catch (IllegalStateException e)
		{
			if (! bExceptionExpected)
			{
				fail(constructErrorMessage(strMethodName, bExceptionExpected, bOpen));
			}
		}
	}



	private static Sequence createSequence()
		throws Exception
	{
		Sequence sequence = new Sequence(Sequence.PPQ, 480);
		sequence.createTrack();
		return sequence;
	}



	private static InputStream createSequenceInputStream()
		throws Exception
	{
		Sequence sequence = createSequence();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		MidiSystem.write(sequence, 0, baos);
		byte[] data = baos.toByteArray();
		InputStream inputStream = new ByteArrayInputStream(data);
		return inputStream;
	}



	private static String constructErrorMessage(String strMethodName,
										 boolean bExceptionExpected,
										 boolean bOpen)
	{
		String strMessage = "IllegalStateException ";
		strMessage += (bExceptionExpected ? "not thrown" : "thrown");
		strMessage += " on " + strMethodName + " in ";
		strMessage += (bOpen ? "closed" : "open");
		strMessage += " state";
		return strMessage;
	}
}



/*** IllegalStateTestCase.java ***/
