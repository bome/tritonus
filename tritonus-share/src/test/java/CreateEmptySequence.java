/*
 *	CreateEmptySequence.java
 *
 *	TODO: short description
 */

/*
 *  Copyright (c) 2000 by Matthias Pfisterer
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


import	java.io.File;
import	java.io.IOException;

import	javax.sound.midi.Sequence;
import	javax.sound.midi.MetaMessage;
import	javax.sound.midi.MidiEvent;
import	javax.sound.midi.MidiSystem;
import	javax.sound.midi.Track;
import	javax.sound.midi.InvalidMidiDataException;



/**	Creates a Sequence with only a end-of-track message.
 */
public class CreateEmptySequence
{
	public static void main(String[] args)
	{
		if (args.length != 4)
		{
			out("usage:");
			out("java CreateEmptySequence <duration> <tempo_in_MPQ> <resolution> <midifile>");
			System.exit(1);
		}
		long	lDuration = Long.parseLong(args[0]);
		int	nTempoInMPQ = Integer.parseInt(args[1]);
		int	nResolution = Integer.parseInt(args[2]);
 		String	strFilename = args[3];
		out("Clock distance (µs): " + nTempoInMPQ / 24);
		out("Tick distance (µs): " + nTempoInMPQ / nResolution);
		Sequence	sequence = null;
		try
		{
			sequence = new Sequence(Sequence.PPQ,
						nResolution);
			Track	track = sequence.createTrack();
			MetaMessage	mm = null;
			MidiEvent	me = null;

			mm = new MetaMessage();
			byte[]	abTempo = new byte[3];
			abTempo[0] = (byte) ((nTempoInMPQ >> 16) & 0xFF);
			abTempo[1] = (byte) ((nTempoInMPQ >> 8) & 0xFF);
			abTempo[2] = (byte) ((nTempoInMPQ) & 0xFF);
			mm.setMessage(0x51, abTempo, 3);
			me = new MidiEvent(mm, 0);
			track.add(me);
			mm = new MetaMessage();
			mm.setMessage(0x2F, new byte[0], 0);
			me = new MidiEvent(mm, lDuration);
			track.add(me);
		}
		catch (InvalidMidiDataException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		try
		{
			MidiSystem.write(sequence, 0, new File(strFilename));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}

		/*
		 *	This is only necessary because of a bug in the Sun jdk1.3
		 */
		System.exit(0);
	}



	private static void out(String strMessage)
	{
		System.out.println(strMessage);
	}
}



/*** CreateEmptySequence.java ***/
