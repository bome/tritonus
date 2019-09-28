/*
 *	MidiMessageTestCase.java
 */

/*
 *  Copyright (c) 2001 - 2002 by Matthias Pfisterer
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

package org.tritonus.test.api.midi.message;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;

import org.junit.jupiter.api.Test;
import org.tritonus.test.Util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**	Tests for class javax.sound.midi.MidiMessage.
 */
public class MidiMessageTestCase
{
	/**	Checks the constructor.
		The test checks for four things:
		<ol>
		<li>if the content of data follows the passed array (note that
		it is legal for data to be longer than the passed array).</li>
		<li>if the value of length follows the length of the passed array.</li>
		<li>if the constructor makes a copy of the passed array.</li>
		<li>if the constructor does (not) use setMessage().</li>
		</ol>
	*/
    @Test
	public void testConstructor()
		throws Exception
	{
		byte[]		abData = new byte[]{(byte) 144, 127, 0};
		TestMidiMessage	message = new TestMidiMessage(abData);
 		assertTrue(Util.compareByteArrays(abData, 0, message.getDataField(), 0, abData.length), "data content");
		assertEquals(abData.length, message.getLengthField(), "length field");
		assertTrue(abData == message.getDataField(), "array copying"); // not copied!
		assertEquals(false, message.getSetMessageUsed(), "setMessage() usage");
	}



	/**	Checks setMessage(byte[], int).
		The test checks for three things:
		<ol>
		<li>if the content of data follows the passed array (note that
		it is legal for data to be longer than the passed array).</li>
		<li>if the value of length follows the length of the passed array.</li>
		<li>if the method makes a copy of the passed array.</li>
		</ol>
	*/
    @Test
	public void testSetMessage()
		throws Exception
	{
		byte[]		abData = new byte[]{(byte) 144, 127, 0};
		TestMidiMessage	message = new TestMidiMessage(abData);
		byte[]		abData2 = new byte[]{(byte) 128, 31, 1};
		message.setMessage(abData2, abData2.length);
 		assertTrue(Util.compareByteArrays(abData2, 0, message.getDataField(), 0, abData.length), "data content");
		assertEquals(abData2.length, message.getLengthField(), "length field");
		assertTrue(abData2 != message.getDataField(), "array copying");
		byte[]		abData3 = new byte[]{(byte) 128, 31, 1, 55, 55, 55};
		int		nDesiredLength = 3;
		message.setMessage(abData3, nDesiredLength);
 		assertTrue(Util.compareByteArrays(abData3, 0, message.getDataField(), 0, nDesiredLength), "data content");
		assertEquals(nDesiredLength, message.getLengthField(), "length field");
	}



	/**	Checks getMessage().
		The test checks for three things:
		<ol>
		<li>if the returned array has the correct length.</li>
		<li>if the returned array has the correct content (note that
		it is legal for stored data to be longer than the returned array).</li>
		<li>if the returned array is a copy of the stored array.</li>
		</ol>
	*/
    @Test
	public void testGetMessage()
		throws Exception
	{
		byte[]		abData = new byte[]{(byte) 144, 127, 0};
		TestMidiMessage	message = new TestMidiMessage(abData);
		byte[]		abReturned = message.getMessage();
		assertEquals(abData.length, abReturned.length, "length");
 		assertTrue(Util.compareByteArrays(abData, 0, abReturned, 0, abData.length), "data content");
		assertTrue(abReturned != message.getDataField(), "array copying");
	}



	/**	Checks getStatus().
		The test checks if the returned status byte is correct.
	*/
    @Test
	public void testGetStatus()
		throws Exception
	{
		int		nStatus = 144;
		byte[]		abData = new byte[]{(byte) nStatus, 127, 0};
		TestMidiMessage	message = new TestMidiMessage(abData);
		int		nReturnedStatus = message.getStatus();
		assertEquals(nStatus, nReturnedStatus, "status byte");
	}



	/**	Checks setMessage(byte[], int).
		The test checks if the returned length is correct.
	*/
    @Test
	public void testGetLength()
		throws Exception
	{
		byte[]		abData = new byte[]{(byte) 144, 127, 0};
		TestMidiMessage	message = new TestMidiMessage(abData);
		int		nReturnedLength = message.getLength();
		assertEquals(abData.length, nReturnedLength, "length");
	}



	/**	Inner class used to access protected fields of MidiMessage.
	 */
	private class TestMidiMessage
	extends MidiMessage
	{
		private boolean		m_bSetMessageUsed;



		public TestMidiMessage(byte[] abData)
		{
			super(abData);
		}



		public byte[] getDataField()
		{
			return data;
		}



		public int getLengthField()
		{
			return length;
		}



		public boolean getSetMessageUsed()
		{
			return m_bSetMessageUsed;
		}



		protected void setMessage(byte[] abData, int nLength)
			throws InvalidMidiDataException
		{
			super.setMessage(abData, nLength);
			m_bSetMessageUsed = true;
		}



		/**	Not used here.
		 */
		public Object clone()
		{
			return null;
		}
	}
}



/*** MidiMessageTestCase.java ***/
