/*
 *	MetaMessage.java
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



public class MetaMessage
    extends	MidiMessage
{
	public static final int		META = 0xFF;

	public int			m_nType;


	public MetaMessage()
	{
		super(null);
	}



	protected MetaMessage(byte[] abData)
	{
		super(abData);
	}



	public int getStatus()
	{
		return META;
	}



	public void setMessage(int nType, byte[] abData, int nLength)
		throws	InvalidMidiDataException
	{
		m_nType = nType;
		byte[]	abCompleteData = new byte[nLength];
		System.arraycopy(abData, 0, abCompleteData, 0, nLength);
		setMessage(abCompleteData, nLength);
	}



	public int getType()
	{
		return m_nType;
	}



	public byte[] getData()
	{
		byte[] abData = new byte[getLength()];
		System.arraycopy(getMessage(), 0, abData, 0, getLength());
		return abData;
	}



	public Object clone()
	{
		byte[]	abData = new byte[getLength()];
		System.arraycopy(getMessage(), 0, abData, 0, abData.length);
		MetaMessage	message = new MetaMessage();
		try
		{
			message.setMessage(getType(), abData, abData.length);
		}
		catch (InvalidMidiDataException e)
		{
		}
		return message;
	}



}



/*** MetaMessage.java ***/
