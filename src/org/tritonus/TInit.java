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


import	javax.sound.midi.spi.MidiDeviceProvider;
import	javax.sound.midi.spi.MidiFileReader;
import	javax.sound.midi.spi.MidiFileWriter;
import	javax.sound.midi.spi.SoundbankReader;

import	javax.sound.sampled.spi.AudioFileWriter;
import	javax.sound.sampled.spi.AudioFileReader;
import	javax.sound.sampled.spi.FormatConversionProvider;
import	javax.sound.sampled.spi.MixerProvider;

import	org.tritonus.TConfiguration;
import	org.tritonus.TDebug;
import	org.tritonus.midi.TMidiConfig;
import	org.tritonus.sampled.TAudioConfig;




public class TInit
{
	private static interface ClassRegistrationAction
	{
		public void register(Class clazz)
			throws	Exception;
	}



	static
	{
		// MidiDeviceProviders
		registerClasses("midiDeviceProviders", new ClassRegistrationAction()
				{
					public void register(Class cls)
						throws	Exception
						{
							MidiDeviceProvider	provider = (MidiDeviceProvider) cls.newInstance();
							TMidiConfig.addMidiDeviceProvider(provider);
						}
				});



		// MidiFileReaders
		registerClasses("midiFileReaders", new ClassRegistrationAction()
				{
					public void register(Class cls)
						throws	Exception
						{
							MidiFileReader	provider = (MidiFileReader) cls.newInstance();
							TMidiConfig.addMidiFileReader(provider);
						}
				});

		if (TDebug.TraceInit)
		{
			TDebug.out("TInit.<clinit>(): registered all midiFileReaders");
		}

		// MidiFileWriters
		registerClasses("midiFileWriters", new ClassRegistrationAction()
				{
					public void register(Class cls)
						throws	Exception
						{
							MidiFileWriter	provider = (MidiFileWriter) cls.newInstance();
							TMidiConfig.addMidiFileWriter(provider);
						}
				});




		if (TDebug.TraceInit)
		{
			TDebug.out("TInit.<clinit>(): registered all midiFileWriters");
		}

		// SoundbankReaders
		registerClasses("soundbankReaders", new ClassRegistrationAction()
				{
					public void register(Class cls)
						throws	Exception
						{
							SoundbankReader	provider = (SoundbankReader) cls.newInstance();
							TMidiConfig.addSoundbankReader(provider);
						}
				});




		if (TDebug.TraceInit)
		{
			TDebug.out("TInit.<clinit>(): registered all soundbankReaders");
		}

		// AudioFileReaders
		registerClasses("audioFileReaders", new ClassRegistrationAction()
				{
					public void register(Class cls)
						throws	Exception
						{
							AudioFileReader	provider = (AudioFileReader) cls.newInstance();
							TAudioConfig.addAudioFileReader(provider);
						}
				});


		// AudioFileWriters
		registerClasses("audioFileWriters", new ClassRegistrationAction()
				{
					public void register(Class cls)
						throws	Exception
						{
							AudioFileWriter	provider = (AudioFileWriter) cls.newInstance();
							TAudioConfig.addAudioFileWriter(provider);
						}
				});


		// FormatConversionProviders
		registerClasses("formatConversionProviders", new ClassRegistrationAction()
				{
					public void register(Class cls)
						throws	Exception
						{
							FormatConversionProvider	provider = (FormatConversionProvider) cls.newInstance();
							TAudioConfig.addFormatConversionProvider(provider);
						}
				});

		// MixerProviders
		registerClasses("mixerProviders", new ClassRegistrationAction()
				{
					public void register(Class cls)
						throws	Exception
						{
							MixerProvider	provider = (MixerProvider) cls.newInstance();
							TAudioConfig.addMixerProvider(provider);
						}
				});

		// additional classes
		String	strInitClasses = TConfiguration.getResourceString("initClasses");
		String[]	astrInitClasses = TConfiguration.tokenize(strInitClasses);
		for (int i = 0; i < astrInitClasses.length; i++)
		{
			try
			{
				Class.forName(astrInitClasses[i]);
			}
			catch (ClassNotFoundException e)
			{
				if (TDebug.TraceInit)
				{
					TDebug.out(e);
				}
			}
		}
	}



	private static void registerClasses(String strResourceKey,
					    ClassRegistrationAction action)
	{
/*
		if (TDebug.TraceInit)
		{
			TDebug.out("TInit.registerClasses(): before getting of resource string");
		}
*/
		String		strClassNames = TConfiguration.getResourceString(strResourceKey);
		if (TDebug.TraceInit)
		{
			TDebug.out("TInit.registerClasses(): class names (" + strResourceKey + "): " + strClassNames);
		}
		if (strClassNames != null)
		{
			String[]	astrClassNames = TConfiguration.tokenize(strClassNames);
			for (int i = 0; i < astrClassNames.length; i++)
			{
				try
				{
					Class	cls = Class.forName(astrClassNames[i]);
					if (TDebug.TraceInit)
					{
						TDebug.out("TInit.registerClasses(): class " + astrClassNames[i] + " loaded");
					}
					action.register(cls);
/*
					if (TDebug.TraceInit)
					{
						TDebug.out("TInit.registerClasses(): class registered");
					}
*/
				}
				catch (ClassNotFoundException e)
				{
					if (TDebug.TraceInit)
					{
						TDebug.out(e);
					}
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
