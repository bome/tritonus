/*
 *	TMidiConfig.java
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


package	org.tritonus.midi;

import	java.io.InputStream;
import	java.io.IOException;

import	java.net.URL;

import	java.util.ArrayList;
import	java.util.Collection;
import	java.util.Iterator;
import	java.util.List;
import	java.util.Set;
import	java.util.HashSet;

import	javax.sound.midi.MidiDevice;
import	javax.sound.midi.Sequencer;
import	javax.sound.midi.Synthesizer;
import	javax.sound.midi.spi.MidiDeviceProvider;
import	javax.sound.midi.spi.MidiFileReader;
import	javax.sound.midi.spi.MidiFileWriter;
import	javax.sound.midi.spi.SoundbankReader;

import	org.tritonus.TDebug;
import	org.tritonus.util.ArraySet;



public class TMidiConfig
{
	private static final String	INIT_CLASS_NAME = "org.tritonus.TInit";

	private static final Set	sm_midiDeviceProviders = new ArraySet();
	private static final Set	sm_midiFileReaders = new ArraySet();
	private static final Set	sm_midiFileWriters = new ArraySet();
	private static final Set	sm_soundbankReaders = new ArraySet();

	private static MidiDevice.Info	sm_defaultMidiDeviceInfo = null;
	private static MidiDevice.Info	sm_defaultSequencerInfo = null;
	private static MidiDevice.Info	sm_defaultSynthesizerInfo = null;



	/**
	 *	This triggers the whole inititalization of the
	 *	(MIDI part of) Tritonus.
	 */
	static
	{
		try
		{
			Class.forName(INIT_CLASS_NAME);
		}
		catch (ClassNotFoundException e)
		{
			TDebug.out(e);
		}
/*
		catch (ExceptionInInitializerError e)
		{
			TDebug.out(e);
			TDebug.out("---> embedded exception:");
			Throwable	t = e.getException();
			TDebug.out(t);
		}
*/
	}





	public static void addMidiDeviceProvider(MidiDeviceProvider reader)
	{
		synchronized (sm_midiDeviceProviders)
		{
			sm_midiDeviceProviders.add(reader);
			if (getDefaultMidiDeviceInfo() == null ||
			    getDefaultSynthesizerInfo() == null ||
			    getDefaultSequencerInfo() == null)
			{
				MidiDevice.Info[]	infos = reader.getDeviceInfo();
				for (int i = 0; i < infos.length; i++)
				{
					MidiDevice	device = null;
					try
					{
						device = reader.getDevice(infos[i]);
					}
					catch (IllegalArgumentException e)
					{
					}
					if (device instanceof Synthesizer)
					{
						if (getDefaultSynthesizerInfo() == null)
						{
							sm_defaultSynthesizerInfo = infos[i];
						}
					}
					else if (device instanceof Sequencer)
					{
						if (getDefaultSequencerInfo() == null)
						{
							sm_defaultSequencerInfo = infos[i];
						}
					}
					else
					{
						if (getDefaultMidiDeviceInfo() == null)
						{
							sm_defaultMidiDeviceInfo = infos[i];
						}
					}
				}
			}
			
		}
	}



	public static void removeMidiDeviceProvider(MidiDeviceProvider reader)
	{
		synchronized (sm_midiDeviceProviders)
		{
			sm_midiDeviceProviders.remove(reader);
			// TODO: change default infos
		}
	}



	public static Iterator getMidiDeviceProviders()
	{
		synchronized (sm_midiDeviceProviders)
		{
			return sm_midiDeviceProviders.iterator();
		}
	}




	public static void addMidiFileReader(MidiFileReader reader)
	{
		synchronized (sm_midiFileReaders)
		{
			if (TDebug.TraceMidiConfig)
			{
				TDebug.out("TMidiConfig.addMidiFileReader(): adding " + reader);
			}
			sm_midiFileReaders.add(reader);
			if (TDebug.TraceMidiConfig)
			{
				TDebug.out("TMidiConfig.addMidiFileReader(): size " + sm_midiFileReaders.size());
			}
		}
	}



	public static void removeMidiFileReader(MidiFileReader reader)
	{
		synchronized (sm_midiFileReaders)
		{
			sm_midiFileReaders.remove(reader);
		}
	}



	public static Iterator getMidiFileReaders()
	{
		synchronized (sm_midiFileReaders)
		{
			if (TDebug.TraceMidiConfig)
			{
				TDebug.out("TMidiConfig.getMidiFileReaders(): called");
			}
			return sm_midiFileReaders.iterator();
		}
	}



	public static void addMidiFileWriter(MidiFileWriter reader)
	{
		synchronized (sm_midiFileWriters)
		{
			sm_midiFileWriters.add(reader);
		}
	}



	public static void removeMidiFileWriter(MidiFileWriter reader)
	{
		synchronized (sm_midiFileWriters)
		{
			sm_midiFileWriters.remove(reader);
		}
	}



	public static Iterator getMidiFileWriters()
	{
		synchronized (sm_midiFileWriters)
		{
			return sm_midiFileWriters.iterator();
		}
	}



	public static void addSoundbankReader(SoundbankReader reader)
	{
		synchronized (sm_soundbankReaders)
		{
			sm_soundbankReaders.add(reader);
		}
	}



	public static void removeSoundbankReader(SoundbankReader reader)
	{
		synchronized (sm_soundbankReaders)
		{
			sm_soundbankReaders.remove(reader);
		}
	}



	public static Iterator getSoundbankReaders()
	{
		synchronized (sm_soundbankReaders)
		{
			return sm_soundbankReaders.iterator();
		}
	}



	public static MidiDevice.Info getDefaultMidiDeviceInfo()
	{
		return sm_defaultMidiDeviceInfo;
	}



	public static MidiDevice.Info getDefaultSynthesizerInfo()
	{
		return sm_defaultSynthesizerInfo;
	}



	public static MidiDevice.Info getDefaultSequencerInfo()
	{
		return sm_defaultSequencerInfo;
	}




}


/*** TMidiConfig.java ***/
