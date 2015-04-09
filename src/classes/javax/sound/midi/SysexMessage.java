/*
 *	SysexMessage.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 by Matthias Pfisterer
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

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package javax.sound.midi;



/**	Holds the data of a MIDI System Exclusive event.
	Sysex event are tricking me over and over again. I give my
	understanding of the expected behaviour for
	review:
*/
public class SysexMessage
extends MidiMessage
{
	/**	TODO:
	*/
	public static final int		SYSTEM_EXCLUSIVE = 0xF0;

	/**	TODO:
	*/
	public static final int		SPECIAL_SYSTEM_EXCLUSIVE = 0xF7;


	/**	Create a container for a MIDI system exclusive message.
		This constructor does not create an object containing a legal
		MIDI message. You have to use one of the setMessage() methods.
		Before calling one of these methods, calling retrieval methods
		(getStatus(), getLength(), getMessage(), getData()) may have
		undesired results.

		@see #setMessage(byte[], int)
		@see #setMessage(int, byte[], int)
	*/
	public SysexMessage()
	{
		this(null);
	}



	/**	TODO:
	*/
	protected SysexMessage(byte[] abData)
	{
		super(abData);
	}



	/**	TODO:
	*/
	public void setMessage(byte[] abData, int nLength)
		throws InvalidMidiDataException
	{
		super.setMessage(abData, nLength);
	}



	/**	TODO:
	*/
	public void setMessage(int nStatus, byte[] abData, int nLength)
		throws InvalidMidiDataException
	{
		byte[]	abCompleteData = new byte[nLength + 1];
		abCompleteData[0] = (byte) nStatus;
		System.arraycopy(abData, 0, abCompleteData, 1, nLength);
		setMessage(abCompleteData, abCompleteData.length);
	}



	/**	TODO:
	*/
	public byte[] getData()
	{
		byte[] abData = new byte[getLength() - 1];
		System.arraycopy(getMessage(), 1, abData, 0, abData.length);
		return abData;
	}



	/**	TODO:
	*/
	public Object clone()
	{
		byte[]	abData = getMessage();
		SysexMessage	message = new SysexMessage(abData);
		return message;
	}
}



/*** SysexMessage.java ***/
