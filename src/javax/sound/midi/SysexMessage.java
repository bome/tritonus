/*
 *	SysexMessage.java
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



public class SysexMessage
    extends	MidiMessage
{
	public static final int		SYSTEM_EXCLUSIVE = 0xF0;
	public static final int		SPECIAL_SYSTEM_EXCLUSIVE = 0xF7;


	public SysexMessage()
	{
		super(null);
	}


	protected SysexMessage(byte[] abData)
	{
		super(abData);
	}



	public void setMessage(byte[] abData, int nLength)
		throws	InvalidMidiDataException
	{
		super.setMessage(abData, nLength);
	}



	public void setMessage(int nStatus, byte[] abData, int nLength)
		throws	InvalidMidiDataException
	{
		byte[]	abCompleteData = new byte[nLength + 1];
		abCompleteData[0] = (byte) nStatus;
		System.arraycopy(abData, 0, abCompleteData, 1, nLength);
		setMessage(abCompleteData, abCompleteData.length);
	}



	public byte[] getData()
	{
		byte[] abData = new byte[getLength() - 1];
		System.arraycopy(getMessage(), 1, abData, 0, abData.length);
		return abData;
	}



	public Object clone()
	{
		byte[]	abData = new byte[getLength()];
		System.arraycopy(getMessage(), 0, abData, 0, abData.length);
		SysexMessage	message = new SysexMessage(abData);
		return message;
	}



}



/*** SysexMessage.java ***/
