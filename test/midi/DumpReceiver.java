/*
 *	DumpReceiver.java
 *
 *	Displays the file format information of a MIDI file.
 *	This file is part of the JavaSound Examples.
 */

/*
 *  Copyright (c) 1999, 2000 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
 *
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */

import	java.io.PrintStream;

import	javax.sound.midi.MidiSystem;
import	javax.sound.midi.InvalidMidiDataException;
import	javax.sound.midi.Sequence;
import	javax.sound.midi.Track;
import	javax.sound.midi.MidiEvent;
import	javax.sound.midi.MidiMessage;
import	javax.sound.midi.ShortMessage;
import	javax.sound.midi.MetaMessage;
import	javax.sound.midi.SysexMessage;
import	javax.sound.midi.Receiver;



public class DumpReceiver
	implements	Receiver
{
	private static String[]		sm_astrKeyNames = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

	private PrintStream		m_printStream;
	private boolean			m_bDebug;
	private boolean			m_bPrintTimeStampAsTicks;



	public DumpReceiver(PrintStream printStream)
	{
		this(printStream, false);
	}


	public DumpReceiver(PrintStream printStream, boolean bPrintTimeStampAsTicks)
	{
		m_printStream = printStream;
		// TODO: should be false for production version
		m_bDebug = true;
		m_bPrintTimeStampAsTicks = bPrintTimeStampAsTicks;
	}



	public void close()
	{
	}



	public void send(MidiMessage message, long lTimeStamp)
	{
		// System.out.println("DumpReceiver.send(): called");
		// m_printStream.println("Class of Message: "+message.getClass().getName());
		// TODO: rework
/*
  if (isDebug())
  {
  // TODO: must be message-specific
  String	d = "";
  }
*/
/*
  m_printStream.println("(DummyReceiver says:) Received a MidiMessage: " + i.toHexString(message.getStatus()) +
  " Type: " + message.getType() +
  //+" "+message.getData1()+" "+message.getData2
  ", Length: " + message.getLength() +
  " at " + message.getTick());
*/
		String	strMessage = null;
		if (message instanceof ShortMessage)
		{
			strMessage = decodeMessage((ShortMessage) message);
		}
		else if (message instanceof SysexMessage)
		{
			strMessage = decodeMessage((SysexMessage) message);
		}
		else if (message instanceof MetaMessage)
		{
			strMessage = decodeMessage((MetaMessage) message);
		}
		else
		{
			strMessage = "unknown message type";
		}
		String	strTimeStamp = null;
		if (m_bPrintTimeStampAsTicks)
		{
			strTimeStamp = "tick " + lTimeStamp + ": ";
		}
		else
		{
			if (lTimeStamp == -1L)
			{
				strTimeStamp = "timestamp [unknown]: ";
			}
			else
			{
				strTimeStamp = "timestamp " + lTimeStamp + "ms: ";
			}
		}
		m_printStream.println(strTimeStamp + strMessage);
	}



	public String decodeMessage(ShortMessage message)
	{
		String	strMessage = null;
		switch (message.getCommand())
		{
		case 0x80:
			strMessage = "note Off " + getKeyName(message.getData1()) + " velocity: " + message.getData2();
			break;

		case 0x90:
			strMessage = "note On " + getKeyName(message.getData1()) + " velocity: " + message.getData2();
			break;

		case 0xa0:
			strMessage = "polyphonic key pressure " + getKeyName(message.getData1()) + " pressure: " + message.getData2();
			break;

		case 0xb0:
			strMessage = "control change " + message.getData1() + " value: " + message.getData2();
			break;

		case 0xc0:
			strMessage = "program change " + message.getData1();
			break;

		case 0xd0:
			strMessage = "key pressure " + getKeyName(message.getData1()) + " pressure: " + message.getData2();
			break;

		case 0xe0:
			strMessage = "pitch wheel change " + get14bitValue(message.getData1(), message.getData2());
			break;

		case 0xF0:
			switch (message.getChannel())
			{
			case 0x0:
				strMessage = "System Exclusive (should not be in ShortMessage!)";
				break;

			case 0x1:
				strMessage = "Undefined";
				break;

			case 0x2:
				strMessage = "Song Position: " + get14bitValue(message.getData1(), message.getData2());
				break;

			case 0x3:
				strMessage = "Song Select: " + message.getData1();
				break;

			case 0x4:
				strMessage = "Undefined";
				break;

			case 0x5:
				strMessage = "Undefined";
				break;

			case 0x6:
				strMessage = "Tune Request";
				break;

			case 0x7:
				strMessage = "End of SysEx (should not be in ShortMessage!)";
				break;

			case 0x8:
				strMessage = "Timing clock";
				break;

			case 0x9:
				strMessage = "Undefined";
				break;

			case 0xA:
				strMessage = "Start";
				break;

			case 0xB:
				strMessage = "Continue";
				break;

			case 0xC:
				strMessage = "Stop";
				break;

			case 0xD:
				strMessage = "Undefined";
				break;

			case 0xE:
				strMessage = "Active Sensing";
				break;

			case 0xF:
				strMessage = "System Reset";
				break;

			}
			break;

		default:
			strMessage = "unknown message: status = " + message.getStatus() + ", byte1 = " + message.getData1() + ", byte2 = " + message.getData2();
			break;
		}
		if (message.getCommand() != 0xF0)
		{
			int	nChannel = message.getChannel() + 1;
			String	strChannel = "channel " + nChannel + ": ";
			strMessage = strChannel + strMessage;
		}
		return strMessage;
	}



	public String decodeMessage(SysexMessage message)
	{
		String	strMessage = "sysex message";
		return strMessage;
	}



	public String decodeMessage(MetaMessage message)
	{
		String	strMessage = null;
		switch (message.getType())
		{
		case 0:
			strMessage = "Sequence Number";
			break;

		case 1:
			strMessage = "Text Event";
			break;

		case 2:
			strMessage = "Copyright Notice";
			break;

		case 3:
			strMessage = "Sequence/Track Name: " + "" ;
			break;

		case 4:
			strMessage = "Instrument Name";
			break;

		case 5:
			strMessage = "Lyric";
			break;

		case 6:
			strMessage = "Marker";
			break;

		case 7:
			strMessage = "Cue Point";
			break;

		case 0x20:
			strMessage = "MIDI Channel Prefix";
			break;

		case 0x2F:
			strMessage = "End of Track";
			break;

		case 0x51:
			strMessage = "Set Tempo";
			break;

		case 0x54:
			strMessage = "SMTPE Offset";
			break;

		case 0x58:
			strMessage = "Time Signature";
			break;

		case 0x59:
			strMessage = "Key Signature";
			break;

		case 0x7F:
			strMessage = "Sequencer-Specific Meta-event";
			break;

		}
		return strMessage;
	}



	public static String getKeyName(int nKeyNumber)
	{
		if (nKeyNumber > 127)
		{
			return "illegal value";
		}
		else
		{
			int	nNote = nKeyNumber % 12;
			int	nOctave = nKeyNumber / 12;
			return sm_astrKeyNames[nNote] + (nOctave - 1);
		}
	}



	public static int get14bitValue(int nLowerPart, int nHigherPart)
	{
		return (nLowerPart & 0x7F) | ((nHigherPart & 0x7F) << 7);
	}
}



/*** DumpReceiver.java ***/
