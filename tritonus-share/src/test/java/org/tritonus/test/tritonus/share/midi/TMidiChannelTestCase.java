/*
 *	TDirectSynthesizerTestCase.java
 */

/*
 *  Copyright (c) 2003 by Matthias Pfisterer
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

package org.tritonus.test.tritonus.share.midi;

import org.junit.jupiter.api.Test;
import org.tritonus.share.midi.TMidiChannel;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TMidiChannelTestCase
{
    @Test
    public void testChannelNumber()
	{
		int CHANNEL = 19;
		TestMidiChannel channel = new TestMidiChannel(CHANNEL);
		assertEquals(CHANNEL, channel.getChannelNumber(), "channel number");
	}



    @Test
	public void testNoteOff()
	{
		TestMidiChannel channel = new TestMidiChannel(0);
		int KEY;
		channel.resetCachedValues();
		KEY = 0;
		channel.noteOff(KEY);
		assertEquals(KEY, channel.getNoteOffKey(), "noteOff() key");
		assertEquals(0, channel.getNoteOffVelocity(), "noteOff() velocity");

		channel.resetCachedValues();
		KEY = 11;
		channel.noteOff(KEY);
		assertEquals(KEY, channel.getNoteOffKey(), "noteOff() key");
		assertEquals(0, channel.getNoteOffVelocity(), "noteOff() velocity");

		channel.resetCachedValues();
		KEY = 127;
		channel.noteOff(KEY);
		assertEquals(KEY, channel.getNoteOffKey(), "noteOff() key");
		assertEquals(0, channel.getNoteOffVelocity(), "noteOff() velocity");
	}


    @Test
	public void testProgramChange()
	{
		TestMidiChannel channel = new TestMidiChannel(0);
		doTestProgramChange(channel, 0, 0, 0);
		doTestProgramChange(channel, 127, 127, 127);
	}


	private void doTestProgramChange(TestMidiChannel channel, int nBankHigh,
			int nBankLow, int nProgram)
	{
		channel.resetCachedValues();
		int nBank = (nBankHigh << 7) | nBankLow;
		channel.programChange(nBank, nProgram);
		System.out.println("(c)" + channel.getSetControllerNumber());
		System.out.println("(v)" + channel.getSetControllerValue());
		System.out.println("(c2)" + channel.getSetControllerNumber2());
		System.out.println("(v2)" + channel.getSetControllerValue2());

		assertEquals(0, channel.getSetControllerNumber(), "programChange() bank high (c)");
		assertEquals(nBankHigh, channel.getSetControllerValue(), "programChange() bank high (v)");
		assertEquals(32, channel.getSetControllerNumber2(), "programChange() bank low (c)");
		assertEquals(nBankLow, channel.getSetControllerValue2(), "programChange() bank low (v)");
		assertEquals(nProgram, channel.getProgramChangeValue(), "programChange() program");
	}


    @Test
	public void testResetAllControllers()
	{
		TestMidiChannel channel = new TestMidiChannel(0);
		channel.resetAllControllers();
		assertEquals(121, channel.getSetControllerNumber(), "resetAllControllers(): controller");
		assertEquals(0, channel.getSetControllerValue(), "resetAllControllers(): value");
	}


    @Test
	public void testAllNotesOff()
	{
		TestMidiChannel channel = new TestMidiChannel(0);
		channel.allNotesOff();
		assertEquals(123, channel.getSetControllerNumber(), "allNotesOff(): controller");
		assertEquals(0, channel.getSetControllerValue(), "allNotesOff(): value");
	}


    @Test
	public void testAllSoundOff()
	{
		TestMidiChannel channel = new TestMidiChannel(0);
		channel.allSoundOff();
		assertEquals(120, channel.getSetControllerNumber(), "allSoundOff(): controller");
		assertEquals(0, channel.getSetControllerValue(), "allSoundOff(): value");
	}


    @Test
	public void testLocalControl()
	{
		TestMidiChannel channel = new TestMidiChannel(0);
		channel.localControl(true);
		assertEquals(122, channel.getSetControllerNumber(), "localControl(true): controller");
		assertEquals(127, channel.getSetControllerValue(), "localControl(true): value");
		channel.resetCachedValues();

		channel.localControl(false);
		assertEquals(122, channel.getSetControllerNumber(), "localControl(false): controller");
		assertEquals(0, channel.getSetControllerValue(), "localControl(false): value");
}


	private static class TestMidiChannel
	extends TMidiChannel
	{
		private int m_nNoteOffKey;
		private int m_nNoteOffVelocity;
		private int m_nSetControllerNumber;
		private int m_nSetControllerValue;
		private int m_nSetControllerNumber2;
		private int m_nSetControllerValue2;
//		private int m_nGetControllerNumber;
		private int m_nProgramChangeValue;


		public TestMidiChannel(int nChannel)
		{
			super(nChannel);
			resetCachedValues();
		}


		/**
		 * Used to obtain the return value of the protected super class method.
		 * @return
		 */
		public int getChannelNumber()
		{
			return getChannel();
		}

		public void resetCachedValues()
		{
			m_nNoteOffKey = -1;
			m_nNoteOffVelocity = -1;
			m_nSetControllerNumber = -1;
			m_nSetControllerValue = -1;
			m_nSetControllerNumber2 = -1;
			m_nSetControllerValue2 = -1;
//			m_nGetControllerNumber = -1;
			m_nProgramChangeValue = -1;
		}

		public int getNoteOffKey()
		{
			return m_nNoteOffKey;
		}

		public int getNoteOffVelocity()
		{
			return m_nNoteOffVelocity;
		}

		public int getSetControllerNumber()
		{
			return m_nSetControllerNumber;
		}

		public int getSetControllerValue()
		{
			return m_nSetControllerValue;
		}

		public int getSetControllerNumber2()
		{
			return m_nSetControllerNumber2;
		}

		public int getSetControllerValue2()
		{
			return m_nSetControllerValue2;
		}

		public int getProgramChangeValue()
		{
			return m_nProgramChangeValue;
		}

		/**
		 * Records the passed values.
		 */
		public void controlChange(int nController, int nValue)
		{
			System.out.println("CC: " + nController + ": " + nValue);
			if (m_nSetControllerNumber != -1)
			{
				m_nSetControllerNumber2 = nController;
				m_nSetControllerValue2 = nValue;
			}
			else
			{
				m_nSetControllerNumber = nController;
				m_nSetControllerValue = nValue;
			}
		}

		public int getChannelPressure()
		{
			return 0;
		}

		public int getController(int nController)
		{
			return 0;
		}

		public boolean getMute()
		{
			return false;
		}

		public int getPitchBend()
		{
			return 0;
		}

		public int getPolyPressure(int nNoteNumber)
		{
			return 0;
		}

		public int getProgram()
		{
			return 0;
		}

		public boolean getSolo()
		{
			return false;
		}

		public void noteOff(int nNoteNumber, int nVelocity)
		{
			m_nNoteOffKey = nNoteNumber;
			m_nNoteOffVelocity = nVelocity;
		}

		public void noteOn(int nNoteNumber, int nVelocity)
		{
		}

		public void programChange(int nProgram)
		{
			m_nProgramChangeValue = nProgram;
		}

		public void setChannelPressure(int nPressure)
		{
		}

		public void setMute(boolean bMute)
		{
		}

		public void setPitchBend(int nBend)
		{
		}

		public void setPolyPressure(int nNoteNumber, int nPressure)
		{
		}

		public void setSolo(boolean bSolo)
		{
		}
	}
}



/*** TDirectSynthesizerTestCase.java ***/
