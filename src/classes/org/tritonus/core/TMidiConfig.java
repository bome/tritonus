/*
 *	TMidiConfig.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 - 2004 by Matthias Pfisterer
 *
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
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.core;

import java.util.Iterator;
import java.util.Set;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.spi.MidiDeviceProvider;
import javax.sound.midi.spi.MidiFileReader;
import javax.sound.midi.spi.MidiFileWriter;
import javax.sound.midi.spi.SoundbankReader;

import org.tritonus.share.TDebug;
import org.tritonus.share.ArraySet;
import org.tritonus.core.TInit.ProviderRegistrationAction;


/** TODO:
 */
public class TMidiConfig
{
	private static Set<MidiDeviceProvider>	sm_midiDeviceProviders = null;
	private static Set<MidiFileReader>		sm_midiFileReaders = null;
	private static Set<MidiFileWriter>		sm_midiFileWriters = null;
	private static Set<SoundbankReader>		sm_soundbankReaders = null;

	private static MidiDevice.Info	sm_defaultMidiInDeviceInfo = null;
	private static MidiDevice.Info	sm_defaultMidiOutDeviceInfo = null;
	private static MidiDevice.Info	sm_defaultSequencerInfo = null;
	private static MidiDevice.Info	sm_defaultSynthesizerInfo = null;


	static
	{
		init();
	}

	/** Constructor to prevent instantiation.
	 */
	private TMidiConfig()
	{
	}


	/** Initialize the collections of providers and the default devices.
	 */
	private static void init()
	{
		// init providers from scanning the class path
		// note: this already sets the default devices
		getMidiDeviceProvidersImpl();
		getMidiFileReadersImpl();
		getMidiFileWritersImpl();
		getSoundbankReadersImpl();
		// now check properties for default devices
		// ... TODO:
	}



	private static void registerMidiDeviceProviders()
	{
		ProviderRegistrationAction	action = null;
		action = new ProviderRegistrationAction()
			{
				public void register(Object obj)
					throws Exception
				{
					MidiDeviceProvider	midiDeviceProvider = (MidiDeviceProvider) obj;
					TMidiConfig.addMidiDeviceProvider(midiDeviceProvider);
				}
			};
		TInit.registerClasses(MidiDeviceProvider.class, action);
	}


	
	private static void registerMidiFileReaders()
	{
		ProviderRegistrationAction	action = null;
		action = new ProviderRegistrationAction()
			{
				public void register(Object obj)
					throws Exception
				{
					MidiFileReader	provider = (MidiFileReader) obj;
					TMidiConfig.addMidiFileReader(provider);
				}
			};
		TInit.registerClasses(MidiFileReader.class, action);
	}



	private static void registerMidiFileWriters()
	{
		ProviderRegistrationAction	action = null;
		action = new ProviderRegistrationAction()
			{
				public void register(Object obj)
					throws Exception
				{
					MidiFileWriter	provider = (MidiFileWriter) obj;
					TMidiConfig.addMidiFileWriter(provider);
				}
			};
		TInit.registerClasses(MidiFileWriter.class, action);
	}



	private static void registerSoundbankReaders()
	{
		ProviderRegistrationAction	action = null;
		action = new ProviderRegistrationAction()
			{
				public void register(Object obj)
					throws Exception
				{
					SoundbankReader	provider = (SoundbankReader) obj;
					TMidiConfig.addSoundbankReader(provider);
				}
			};
		TInit.registerClasses(SoundbankReader.class, action);
	}


	//////////////////////////////////////////////////////////////////


	public static synchronized void addMidiDeviceProvider(MidiDeviceProvider provider)
	{
		// TDebug.out("MidiDeviceProvider: " + provider);
		getMidiDeviceProvidersImpl().add(provider);
		if (getDefaultMidiInDeviceInfo() == null ||
			getDefaultMidiOutDeviceInfo() == null ||
		    getDefaultSynthesizerInfo() == null ||
		    getDefaultSequencerInfo() == null)
		{
			MidiDevice.Info[]	infos = provider.getDeviceInfo();
			// TDebug.out("#infos: " + infos.length);
			for (int i = 0; i < infos.length; i++)
			{
				MidiDevice.Info info = infos[i];
				MidiDevice	device = null;
				try
				{
					device = provider.getDevice(info);
				}
				catch (IllegalArgumentException e)
				{
					if (TDebug.TraceAllExceptions)
					{
						TDebug.out(e);
					}
				}
				if (device instanceof Synthesizer)
				{
					if (getDefaultSynthesizerInfo() == null)
					{
						sm_defaultSynthesizerInfo = info;
					}
				}
				else if (device instanceof Sequencer)
				{
					if (getDefaultSequencerInfo() == null)
					{
						sm_defaultSequencerInfo = info;
					}
				}
				else if (device.getMaxTransmitters() != 0)
				{
					if (getDefaultMidiInDeviceInfo() == null)
					{
						sm_defaultMidiInDeviceInfo = info;
					}
				}
				else if (device.getMaxReceivers() != 0)
				{
					if (getDefaultMidiOutDeviceInfo() == null)
					{
						sm_defaultMidiOutDeviceInfo = info;
					}
				}
			}
		}
	}



	public static synchronized void removeMidiDeviceProvider(MidiDeviceProvider provider)
	{
		getMidiDeviceProvidersImpl().remove(provider);
		// TODO: change default infos
	}



	public static synchronized Iterator<MidiDeviceProvider> getMidiDeviceProviders()
	{
		return getMidiDeviceProvidersImpl().iterator();
	}




	private static synchronized Set<MidiDeviceProvider> getMidiDeviceProvidersImpl()
	{
		if (sm_midiDeviceProviders == null)
		{
			sm_midiDeviceProviders = new ArraySet<MidiDeviceProvider>();
			registerMidiDeviceProviders();
		}
		return sm_midiDeviceProviders;
	}




	public static synchronized void addMidiFileReader(MidiFileReader reader)
	{
		if (TDebug.TraceMidiConfig) TDebug.out("TMidiConfig.addMidiFileReader(): adding " + reader);
		getMidiFileReadersImpl().add(reader);
		if (TDebug.TraceMidiConfig) TDebug.out("TMidiConfig.addMidiFileReader(): size " + sm_midiFileReaders.size());
	}



	public static synchronized void removeMidiFileReader(MidiFileReader reader)
	{
		getMidiFileReadersImpl().remove(reader);
	}



	public static synchronized Iterator<MidiFileReader> getMidiFileReaders()
	{
		return getMidiFileReadersImpl().iterator();
	}



	private static synchronized Set<MidiFileReader> getMidiFileReadersImpl()
	{
		if (sm_midiFileReaders == null)
		{
			sm_midiFileReaders = new ArraySet<MidiFileReader>();
			registerMidiFileReaders();
		}
		return sm_midiFileReaders;
	}



	public static synchronized void addMidiFileWriter(MidiFileWriter reader)
	{
		getMidiFileWritersImpl().add(reader);
	}



	public static synchronized void removeMidiFileWriter(MidiFileWriter reader)
	{
		getMidiFileWritersImpl().remove(reader);
	}



	public static synchronized Iterator<MidiFileWriter> getMidiFileWriters()
	{
		return getMidiFileWritersImpl().iterator();
	}


	private static synchronized Set<MidiFileWriter> getMidiFileWritersImpl()
	{
		if (sm_midiFileWriters == null)
		{
			sm_midiFileWriters = new ArraySet<MidiFileWriter>();
			registerMidiFileWriters();
		}
		return sm_midiFileWriters;
	}




	public static synchronized void addSoundbankReader(SoundbankReader reader)
	{
		getSoundbankReadersImpl().add(reader);
	}



	public static synchronized void removeSoundbankReader(SoundbankReader reader)
	{
		getSoundbankReadersImpl().remove(reader);
	}



	public static synchronized Iterator<SoundbankReader> getSoundbankReaders()
	{
		return getSoundbankReadersImpl().iterator();
	}



	private static synchronized Set<SoundbankReader> getSoundbankReadersImpl()
	{
		if (sm_soundbankReaders == null)
		{
			sm_soundbankReaders = new ArraySet<SoundbankReader>();
			registerSoundbankReaders();
		}
		return sm_soundbankReaders;
	}



	public static MidiDevice.Info getDefaultMidiInDeviceInfo()
	{
		return sm_defaultMidiInDeviceInfo;
	}



	public static MidiDevice.Info getDefaultMidiOutDeviceInfo()
	{
		return sm_defaultMidiOutDeviceInfo;
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
