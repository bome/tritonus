/*
 *	ShortMessage.java
 */

/*
 *  Copyright (c) 1999 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
 *
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
 *
 */


package	javax.sound.midi;



public class ShortMessage
	extends	MidiMessage
{
	/*
	 *	If true, for messages with one data byte it is checked that
	 *	the second one is zero.
	 */
	private static final boolean	CHECK_SECOND_BYTE = false;

	public static final int	NOTE_OFF = 0x80;
	public static final int	NOTE_ON = 0x90;
	public static final int	POLY_PRESSURE = 0xA0;
	public static final int	CONTROL_CHANGE = 0xB0;
	public static final int	PROGRAM_CHANGE = 0xC0;
	public static final int	CHANNEL_PRESSURE = 0xD0;
	public static final int	PITCH_BEND = 0xE0;

	// system
	// public static final int	SYSTEM_EXCLUSIVE = 0xF0;
	public static final int	MIDI_TIME_CODE = 0xF1;
	public static final int	SONG_POSITION_POINTER = 0xF2;
	public static final int	SONG_SELECT = 0xF3;
	// not defined: 0xF4
	// not defined: 0xF5
	public static final int	TUNE_REQUEST = 0xF6;
	public static final int	END_OF_EXCLUSIVE = 0xF7;

	// real-time messages
	public static final int	TIMING_CLOCK = 0xF8;
	// not defined: 0xF9
	public static final int	START = 0xFA;
	public static final int	CONTINUE = 0xFB;
	public static final int	STOP = 0xFC;
	// not defined: 0xFD
	public static final int	ACTIVE_SENSING = 0xFE;
	public static final int	SYSTEM_RESET = 0xFF;


	/*
	 *	Tables for getDataLength().
	 */

	private static final int	sm_anChannelMessageLength[] =
	{ 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 1, 1, 2 };
	/*                       8x 9x Ax Bx Cx Dx Ex */

	private static final int	sm_anSystemMessageLength[] = 
	{   0, 2, 3, 2, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1 };
	/* F0 F1 F2 F3 F4 F5 F6 F7 F8 F9 FA FB FC FD FE FF */
	/* 0 for undefined (or unknown for F0) */



	public ShortMessage()
	{
		super(null);
	}



	protected ShortMessage(byte[] abData)
	{
		super(abData);
	}



	public Object clone()
	{
		byte[] abData = new byte[getLength()];
		System.arraycopy(getMessage(), 0, abData, 0, getLength());
		ShortMessage	message = new ShortMessage(abData);
		return message;
	}



	public void setMessage(int nStatus)
		throws	InvalidMidiDataException
	{
		if (getDataLength(nStatus) != 0)
		{
			throw new InvalidMidiDataException("not a one-byte command");
		}
		byte[]	abData = new byte[1];
		abData[0] = (byte) nStatus;
		setMessage(abData, 1);
	}



	public void setMessage(int nStatus, int nData1, int nData2)
		throws	InvalidMidiDataException
	{
		int	nDataLength = getDataLength(nStatus);
		if (nDataLength < 1)
		{
			throw new InvalidMidiDataException("not a two- or three-byte command");
		}
		byte[]	abData = new byte[nDataLength + 1];
		abData[0] = (byte) nStatus;
		abData[1] = (byte) nData1;
		if (nDataLength == 2)
		{
			abData[2] = (byte) nData2;
		}
		else if (nData2 != 0 && CHECK_SECOND_BYTE)
		{
			throw new InvalidMidiDataException("data2 should be 0 for a two-byte command");
		}
		setMessage(abData, nDataLength + 1);
	}


	public void setMessage(int nCommand, int nChannel, int nData1, int nData2)
		throws	InvalidMidiDataException
	{
		setMessage((nCommand & 0xF0) | (nChannel & 0x0F), nData1, nData2);
	}



	public int getChannel()
	{
		return (getStatus() & 0xF);
	}



	public int getCommand()
	{
		return getStatus() & 0xF0;
	}



	public int getData1()
	{
		return getDataByte(1);
	}



	public int getData2()
	{
		return getDataByte(2);
	}



	private int getDataByte(int nNumber)
	{
		if (getLength() >= nNumber)
		{
			return getMessage()[nNumber];
		}
		else
		{
			return 0;
		}
	}


	protected final int getDataLength(int nStatus)
		throws	InvalidMidiDataException
	{
		if (nStatus < 0xF0)	// channel voice message
		{
			return sm_anChannelMessageLength[(nStatus >> 4) & 0xF];
		}
		else
		{
			return sm_anSystemMessageLength[nStatus & 0xF];
		}
	}
}



/*** ShortMessage.java ***/
