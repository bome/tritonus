/*
 *	MidiMessage.java
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



public abstract class MidiMessage
	implements	Cloneable
{
	/**	All data of the message.
	 */
	private byte[]		m_abData;



	/**	Constructs a MidiMessage.
		The data of the message (m_abData) are initialized by the
		array passed as a parameter. Note that the data is not copied.
		Rather, m_abData is set to point at the passed array.
		This constructor does not use setMessage(byte[], int). So it
		unaffected by overriding setMessage().
	 */
	protected MidiMessage(byte[] abData)
	{
		m_abData = abData;
	}



	/**	Initializes the data of the message.
		This method 
	 */
	protected void setMessage(byte[] abData, int nLength)
		throws	InvalidMidiDataException
	{
		if (nLength < abData.length)
		{
			byte[]	abShortenedData = new byte[nLength];
			System.arraycopy(abData, 0, abShortenedData, 0, nLength);
			abData = abShortenedData;
		}
		m_abData = abData;
	}


	/**	returns the complete message.
		This method makes a copy of the data and returns a
		reference to the copy. Both Sun jdk and Tritonus implement it
		this way. However, it is not clear if this is a requirement
		(Florian).
		Behaviour if getLength() != data.length ?? (Matthias, Florian)
	 */
	public byte[] getMessage()
	{
		byte[]	abData = new byte[m_abData.length];
		System.arraycopy(m_abData, 0, abData, 0, m_abData.length);
		return abData;
	}



	/**	Returns the status byte of the message.
	 *	I.e. the first byte is returned. This has to be overridden
	 *	by subclasses if the status byte is not stored as part of
	 *	the data (like it is for MetaMessages).
	 *	If no message has been set, -1 is returned.
	 */
	public int getStatus()
	{
		if (m_abData != null)
		{
			if (m_abData[0] < 0)
			{
				return (int) m_abData[0] + 256;
			}
			else
			{
				return m_abData[0];
			}
		}
		else
		{
			return -1;
		}

	}



	/**	Returns the length of the whole message in bytes.
		In this implementation, the length returned by this method
		is always equal to the length of the array returned by
		getMessage(). However, this has to be considered
		implementation-specific behaviour. It is not guaranteed for
		other implementations of the Java Sound API. Should be made a
		requirement? (Matthias, Florian)
		If no message has been set, a length of -1 is returned.
	*/
	public int getLength()
	{
		if (m_abData != null)
		{
			return m_abData.length;
		}
		else
		{
			return -1;
		}
	}


	public abstract Object clone();
}



/*** MidiMessage.java ***/
