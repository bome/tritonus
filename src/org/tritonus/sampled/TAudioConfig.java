/*
 *	TAudioConfig.java
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


package	org.tritonus.sampled;


import	java.util.Set;
import	java.util.Iterator;

import	javax.sound.sampled.Mixer;

import	javax.sound.sampled.spi.AudioFileWriter;
import	javax.sound.sampled.spi.AudioFileReader;
import	javax.sound.sampled.spi.FormatConversionProvider;
import	javax.sound.sampled.spi.MixerProvider;

import	org.tritonus.TDebug;
import	org.tritonus.util.ArraySet;



public class TAudioConfig
{
	private static Set		sm_audioFileProviders = new ArraySet();
	private static Set		sm_audioInputStreamProviders = new ArraySet();
	private static Set		sm_formatConversionProviders = new ArraySet();
	private static Set		sm_mixerProviders = new ArraySet();

	// private static InputDevice	sm_defaultInputDevice;
	private static Mixer.Info	sm_defaultMixerInfo;
	// private static OutputDevice	sm_defaultOutputDevice;

	// TODO: merge herein
	private static final String	INIT_CLASS_NAME = "org.tritonus.TInit";


	/**
	 *	This triggers the whole inititalization of the
	 *	audio part of Tritonus.
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
	}



	public static void addAudioFileWriter(AudioFileWriter provider)
	{
		synchronized (sm_audioFileProviders)
		{
			sm_audioFileProviders.add(provider);
		}
	}



	public static void removeAudioFileWriter(AudioFileWriter provider)
	{
		synchronized (sm_audioFileProviders)
		{
			sm_audioFileProviders.remove(provider);
		}
	}



	public static Iterator getAudioFileWriters()
	{
		synchronized (sm_audioFileProviders)
		{
			return sm_audioFileProviders.iterator();
		}
	}



	public static void addAudioFileReader(AudioFileReader provider)
	{
		synchronized (sm_audioInputStreamProviders)
		{
			sm_audioInputStreamProviders.add(provider);
		}
	}



	public static void removeAudioFileReader(AudioFileReader provider)
	{
		synchronized (sm_audioInputStreamProviders)
		{
			sm_audioInputStreamProviders.remove(provider);
		}
	}



	public static Iterator getAudioFileReaders()
	{
		synchronized (sm_audioInputStreamProviders)
		{
			return sm_audioInputStreamProviders.iterator();
		}
	}



	public static void addFormatConversionProvider(FormatConversionProvider provider)
	{
		synchronized (sm_formatConversionProviders)
		{
			sm_formatConversionProviders.add(provider);
		}
	}



	public static void removeFormatConversionProvider(FormatConversionProvider provider)
	{
		synchronized (sm_formatConversionProviders)
		{
			sm_formatConversionProviders.remove(provider);
		}
	}



	public static Iterator getFormatConversionProviders()
	{
		return sm_formatConversionProviders.iterator();
	}



	public static void addMixerProvider(MixerProvider provider)
	{
		synchronized (sm_mixerProviders)
		{
			sm_mixerProviders.add(provider);
		}
	}



	public static void removeMixerProvider(MixerProvider provider)
	{
		synchronized (sm_mixerProviders)
		{
			sm_mixerProviders.remove(provider);
		}
	}



	public static Iterator getMixerProviders()
	{
		synchronized (sm_mixerProviders)
		{
			return sm_mixerProviders.iterator();
		}
	}


	// TODO: a way to set the default mixer
	public static Mixer.Info getDefaultMixerInfo()
	{
		return sm_defaultMixerInfo;
	}

}



/*** TAudioConfig.java ***/
