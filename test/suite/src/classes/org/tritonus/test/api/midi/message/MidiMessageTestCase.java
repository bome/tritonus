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

import junit.framework.TestCase;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.InvalidMidiDataException;

import org.tritonus.test.Util;


/**	Tests for class javax.sound.midi.MidiMessage.
 */
public class MidiMessageTestCase
extends TestCase
{
	public MidiMessageTestCase(String strName)
	{
		super(strName);
	}



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
	public void testConstructor()
		throws Exception
	{
		byte[]		abData = new byte[]{(byte) 144, 127, 0};
		TestMidiMessage	message = new TestMidiMessage(abData);
 		assertTrue("data content", Util.compareByteArrays(abData, 0, message.getDataField(), 0, abData.length));
		assertEquals("length field", abData.length, message.getLengthField());
		assertTrue("array copying", abData != message.getDataField());
		assertEquals("setMessage() usage", false, message.getSetMessageUsed());
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
	public void testSetMessage()
		throws Exception
	{
		byte[]		abData = new byte[]{(byte) 144, 127, 0};
		TestMidiMessage	message = new TestMidiMessage(abData);
		byte[]		abData2 = new byte[]{(byte) 128, 31, 1};
		message.setMessage(abData2, abData2.length);
 		assertTrue("data content", Util.compareByteArrays(abData2, 0, message.getDataField(), 0, abData.length));
		assertEquals("length field", abData2.length, message.getLengthField());
		assertTrue("array copying", abData2 != message.getDataField());
		byte[]		abData3 = new byte[]{(byte) 128, 31, 1, 55, 55, 55};
		int		nDesiredLength = 3;
		message.setMessage(abData3, nDesiredLength);
 		assertTrue("data content", Util.compareByteArrays(abData3, 0, message.getDataField(), 0, nDesiredLength));
		assertEquals("length field", nDesiredLength, message.getLengthField());
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
	public void testGetMessage()
		throws Exception
	{
		byte[]		abData = new byte[]{(byte) 144, 127, 0};
		TestMidiMessage	message = new TestMidiMessage(abData);
		byte[]		abReturned = message.getMessage();
		assertEquals("length", abData.length, abReturned.length);
 		assertTrue("data content", Util.compareByteArrays(abData, 0, abReturned, 0, abData.length));
		assertTrue("array copying", abReturned != message.getDataField());
	}



	/**	Checks getStatus().
		The test checks if the returned status byte is correct.
	*/
	public void testGetStatus()
		throws Exception
	{
		int		nStatus = 144;
		byte[]		abData = new byte[]{(byte) nStatus, 127, 0};
		TestMidiMessage	message = new TestMidiMessage(abData);
		int		nReturnedStatus = message.getStatus();
		assertEquals("status byte", nStatus, nReturnedStatus);
	}



	/**	Checks setMessage(byte[], int).
		The test checks if the returned length is correct.
	*/
	public void testGetLength()
		throws Exception
	{
		byte[]		abData = new byte[]{(byte) 144, 127, 0};
		TestMidiMessage	message = new TestMidiMessage(abData);
		int		nReturnedLength = message.getLength();
		assertEquals("length", abData.length, nReturnedLength);
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
