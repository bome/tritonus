/*
 *	TInit.java
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


package	org.tritonus;


import	java.util.Iterator;

import	javax.sound.midi.spi.MidiDeviceProvider;
import	javax.sound.midi.spi.MidiFileReader;
import	javax.sound.midi.spi.MidiFileWriter;
import	javax.sound.midi.spi.SoundbankReader;

import	javax.sound.sampled.spi.AudioFileWriter;
import	javax.sound.sampled.spi.AudioFileReader;
import	javax.sound.sampled.spi.FormatConversionProvider;
import	javax.sound.sampled.spi.MixerProvider;

import	org.tritonus.TDebug;
import	org.tritonus.midi.TMidiConfig;
import	org.tritonus.sampled.TAudioConfig;
import	org.tritonus.util.Service;



public class TInit
{
/*
	private static interface ClassRegistrationAction
	{
		public void register(Class clazz)
			throws	Exception;
	}
*/


	private static interface ProviderRegistrationAction
	{
		public void register(Object provider)
			throws	Exception;
	}



	static
	{
		// MidiDeviceProviders
		registerClasses(MidiDeviceProvider.class, new ProviderRegistrationAction()
				{
					public void register(Object obj)
						throws	Exception
						{
							MidiDeviceProvider	midiDeviceProvider = (MidiDeviceProvider) obj;
							TMidiConfig.addMidiDeviceProvider(midiDeviceProvider);
						}
				});



		// MidiFileReaders
		registerClasses(MidiFileReader.class, new ProviderRegistrationAction()
				{
					public void register(Object obj)
						throws	Exception
						{
							MidiFileReader	provider = (MidiFileReader) obj;
							TMidiConfig.addMidiFileReader(provider);
						}
				});

		if (TDebug.TraceInit)
		{
			TDebug.out("TInit.<clinit>(): registered all midiFileReaders");
		}

		// MidiFileWriters
		registerClasses(MidiFileWriter.class, new ProviderRegistrationAction()
				{
					public void register(Object obj)
						throws	Exception
						{
							MidiFileWriter	provider = (MidiFileWriter) obj;
							TMidiConfig.addMidiFileWriter(provider);
						}
				});




		if (TDebug.TraceInit)
		{
			TDebug.out("TInit.<clinit>(): registered all midiFileWriters");
		}

		// SoundbankReaders
		registerClasses(SoundbankReader.class, new ProviderRegistrationAction()
				{
					public void register(Object obj)
						throws	Exception
						{
							SoundbankReader	provider = (SoundbankReader) obj;
							TMidiConfig.addSoundbankReader(provider);
						}
				});




		if (TDebug.TraceInit)
		{
			TDebug.out("TInit.<clinit>(): registered all soundbankReaders");
		}

		// AudioFileReaders
		registerClasses(AudioFileReader.class, new ProviderRegistrationAction()
				{
					public void register(Object obj)
						throws	Exception
						{
							AudioFileReader	provider = (AudioFileReader) obj;
							TAudioConfig.addAudioFileReader(provider);
						}
				});


		// AudioFileWriters
		registerClasses(AudioFileWriter.class, new ProviderRegistrationAction()
				{
					public void register(Object obj)
						throws	Exception
						{
							AudioFileWriter	provider = (AudioFileWriter) obj;
							TAudioConfig.addAudioFileWriter(provider);
						}
				});


		// FormatConversionProviders
		registerClasses(FormatConversionProvider.class, new ProviderRegistrationAction()
				{
					public void register(Object obj)
						throws	Exception
						{
							FormatConversionProvider	provider = (FormatConversionProvider) obj;
							TAudioConfig.addFormatConversionProvider(provider);
						}
				});

		// MixerProviders
		registerClasses(MixerProvider.class, new ProviderRegistrationAction()
				{
					public void register(Object obj)
						throws	Exception
						{
							MixerProvider	provider = (MixerProvider) obj;
							TAudioConfig.addMixerProvider(provider);
						}
				});
	}






	private static void registerClasses(Class providerClass,
					    ProviderRegistrationAction action)
	{
		if (TDebug.TraceInit)
		{
			TDebug.out("TInit.registerClasses(): before getting of resource string");
		}
		Iterator	providers = Service.providers(providerClass);
		if (providers != null)
		{
			while (providers.hasNext())
			{
				Object	provider = providers.next();
				try
				{
					action.register(provider);
				}
				catch (Throwable e)
				{
					if (TDebug.TraceInit)
					{
						TDebug.out(e);
					}
				}
			}
		}
	}

}


/*** TInit.java ***/
