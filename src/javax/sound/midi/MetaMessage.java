/*
 *	MetaMessage.java
 */

/*
 *  Copyright (c) 1999 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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

package	javax.sound.midi;


import	org.tritonus.share.TDebug;
import	org.tritonus.share.midi.MidiUtils;



/**	Container for meta messages.
	This class is used to represent meta events that are commonly stored
	in MIDI files.
	Note that normally, meta messages are not meaningful when sent over the
	wire to a MIDI instrument.<p>

	The way
	these messages are separated into bytes for storing in
	{@link MidiMessage#data data} is the same as specified in the
	Standard MIDI File Format, except that no delta time in ticks is stored
	here.
*/
public class MetaMessage
extends MidiMessage
{
	/**	Status byte for meta messages (value 255, 0xff).
	 */
	public static final int		META = 0xFF;



	/**	TODO:
	*/
	private static final byte[]	DEFAULT_MESSAGE = {(byte) META, 0x2f, 0};



	/**	TODO:
	*/
	/**	Create a container for a MIDI meta message.
		This constructor does not create an object containing a legal
		MIDI message. You have to use one of the setMessage() methods.
		Before calling one of these methods, calling retrieval methods
		(getLength(), getMessage(), getStatus(), getType(),
		getData()) may have
		undesired results.

		@see #setMessage(int)
		@see #setMessage(int, int, int)
		@see #setMessage(int, int, int, int)
	*/
	public MetaMessage()
	{
		super(null);
	}



	/**	TODO:
	*/
	protected MetaMessage(byte[] abData)
	{
		// TODO: set dataLength, ...
		super(abData);
	}



	/**	TODO:
	*/
	public void setMessage(int nType, byte[] abData, int nDataLength)
		throws	InvalidMidiDataException
	{
		if (nType > 127)
		{
			throw new InvalidMidiDataException("type must not exceed 127");
		}
		byte[]	abLength = MidiUtils.getVariableLengthQuantity(nDataLength);
		int	nCompleteLength = 2 + abLength.length + nDataLength;
		int	nDataStart = 2 + abLength.length;
		byte[]	abCompleteData = new byte[nCompleteLength];
		abCompleteData[0] = (byte) META;
		abCompleteData[1] = (byte) nType;
		System.arraycopy(abLength, 0, abCompleteData, 2, abLength.length);
		System.arraycopy(abData, 0, abCompleteData, nDataStart, nDataLength);
		super.setMessage(abCompleteData, nCompleteLength);
	}



	/**	TODO:
	*/
	public int getType()
	{
		int	nType = MidiUtils.getUnsignedInteger(getMessage()[1]);
		return nType;
	}



	/**	TODO:
	*/
	public byte[] getData()
	{
		int	nDataLength = getDataLength();
		int	nDataStartIndex = getDataStartIndex();
		byte[] abData = new byte[nDataLength];
		System.arraycopy(getMessage(), nDataStartIndex, abData, 0, nDataLength);
		return abData;
	}



	/**	TODO:
	*/
	public Object clone()
	{
		// TODO: re-check
		byte[]	abData = getMessage();
		MetaMessage	message = new MetaMessage(abData);
		return message;
	}



	/**	Get the start of the "data" of the meta message.
		The returned value is an index into the array
		{@link MidiMessage#data data}.
		It is the index of the first byte considered as "data".
		"data" is the part of the message without status byte, type
		byte and length (as variable length quantity).
	*/
	private int getDataStartIndex()
	{
		byte[]	abData = getMessage();
		int	nIndex = 2;	// start of length information
		// search for the last byte of the variable length quantity representation
		while ((abData[nIndex] & 0x80) != 0)
		{
			nIndex++;
		}
		// one more is the start of the data
		nIndex++;
		return nIndex;
	}



	/**	Get the length of the "data" of the meta message.
		The returned value is the number of bytes considered as "data".
		"data" is the part of the message without status byte, type
		byte and length (as variable length quantity).
	*/
	private int getDataLength()
	{
		byte[]	abData = getMessage();
		int	nDataStartIndex = getDataStartIndex();
		int	nDataLength = abData.length - nDataStartIndex;
		return nDataLength;
	}
}



/*** MetaMessage.java ***/
