/*
 *	TDirectSynthesizerTestCase.java
 */

/*
 *  Copyright (c) 2003 by Matthias Pfisterer
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

package org.tritonus.test.tritonus;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.Patch;
import javax.sound.midi.Instrument;
import javax.sound.midi.Soundbank;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.VoiceStatus;

import org.tritonus.share.midi.TDirectSynthesizer;




public class TDirectSynthesizerTestCase
extends TestCase
{
	public TDirectSynthesizerTestCase(String strName)
	{
		super(strName);
	}



	public void testEmptyMap()
	{
	}



	public void testCopying()
	{
	}


	public void testUnmodifiable()
	{
	}


	public void testGet()
	{
	}


	private static class TestSynthesizer
	extends TDirectSynthesizer
	{
		private MidiChannel[]	m_channels;


		public TestSynthesizer()
		{
			// no MidiDevice.Info
			super(null);
// 			m_channels = new MidiChannel[16];
// 			for (int i = 0; i < 16; i++)
// 			{
// 				m_channels[i] = new TestChannel(i);
// 			}
		}


		public int getMaxPolyphony()
		{
			return 16;
		}

		public long getLatency()
		{
			return 0;
		}


		public MidiChannel[] getChannels()
		{
			return null;
		}


		public VoiceStatus[] getVoiceStatus()
		{
			return null;
		}


		public boolean isSoundbankSupported(Soundbank soundbank)
		{
			return false;
		}


		public boolean loadInstrument(Instrument instrument)
		{
			return false;
		}


		public void unloadInstrument(Instrument instrument)
		{
		}


		public boolean remapInstrument(Instrument from,
									   Instrument to)
		{
			return false;
		}


		public Soundbank getDefaultSoundbank()
		{
			return null;
		}


		public Instrument[] getAvailableInstruments()
		{
			return null;
		}


		public Instrument[] getLoadedInstruments()
		{
			return null;
		}


		public boolean loadAllInstruments(Soundbank soundbank)
		{
			return false;
		}


		public void unloadAllInstruments(Soundbank soundbank)
		{
		}


		public boolean loadInstruments(Soundbank soundbank,
									   Patch[] patchList)
		{
			return false;
		}


		public void unloadInstruments(Soundbank soundbank,
									  Patch[] patchList)
		{
		}


// 		private class TestChannel
// 		implements MidiChannel
// 		{
// 			public TestChannel(int nChannel)
// 			{
// 			}
// 		}
	}
}



/*** TDirectSynthesizerTestCase.java ***/
