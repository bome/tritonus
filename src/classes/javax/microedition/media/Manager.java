/*
 *	Manager.java
 */

/*
 *  Copyright (c) 2002 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
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

package	javax.microedition.media;


import	java.io.IOException;
import	java.io.InputStream;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.microedition.media.protocol.DataSource;

import org.tritonus.mmapi.InputStreamDataSource;
import org.tritonus.mmapi.JavaSoundToneGenerator;
import org.tritonus.mmapi.SystemTimeBase;
import org.tritonus.mmapi.URLDataSource;
import org.tritonus.mmapi.PcmAudioPlayer;
import org.tritonus.mmapi.Mp3AudioPlayer;
import org.tritonus.mmapi.JavaSoundAudioPlayer;
import org.tritonus.mmapi.MidiPlayer;
import org.tritonus.mmapi.ToneGenerator;
import org.tritonus.share.TDebug;


/**	TODO:
*/
public final class Manager
{
	/**	TODO:
	*/
	public static final String	TONE_DEVICE_LOCATOR = "device://tone";


	/**	TODO:
	*/
	public static final String	MIDI_DEVICE_LOCATOR = "device://midi";


	/**
	  Table of protocol names and DataSource classes used to
	  implement the protocols.
	*/
	private static final Object[][]	PROTOCOL_TABLE =
	{
		{ "capture", null },
		{ "rtp", null },
		{ "file", URLDataSource.class },
		{ "http", URLDataSource.class },
		{ "ftp", URLDataSource.class },
		// device: ??
	};


	/**
	  Table of protocol names and DataSource classes used to
	  implement the protocols.
	*/
	private static final Object[][]	CONTENT_TYPE_TABLE =
	{
		{ "audio/ac3", null },
		{ "audio/basic", JavaSoundAudioPlayer.class },
		{ "audio/midi", MidiPlayer.class },
		{ "audio/mpeg", JavaSoundAudioPlayer.class },
		{ "audio/x-aiff", JavaSoundAudioPlayer.class },
		{ "audio/x-gsm", JavaSoundAudioPlayer.class },
		{ "audio/x-it", null },
		{ "audio/x-midi", MidiPlayer.class },
		{ "audio/x-mod", null },
		{ "audio/x-mp3", JavaSoundAudioPlayer.class },
		{ "audio/x-real-audio", null },
		{ "audio/x-s3m", null },
		{ "audio/x-stm", null },
		{ "audio/x-ulaw", JavaSoundAudioPlayer.class },
		{ "audio/x-voc", null },
		{ "audio/x-wav", JavaSoundAudioPlayer.class },
		{ "audio/x-xm", null },
	};


	/**	TODO:
	*/
	private static final TimeBase	sm_systemTimeBase = new SystemTimeBase();


	/**	TODO:
	*/
	private static ToneGenerator	sm_toneGenerator = null;



	/**	TODO:
	*/
	public static String[] getSupportedContentTypes(String strProtocol)
	{
		String[]	astrContentTypes = new String[CONTENT_TYPE_TABLE.length];
		for (int i = 0; i < CONTENT_TYPE_TABLE.length; i++)
		{
			astrContentTypes[i] = (String) CONTENT_TYPE_TABLE[i][0];
		}
		return astrContentTypes;
	}



	/**	TODO:
	*/
	public static String[] getSupportedProtocols(String strContentType)
	{
		String[]	astrProtocols = new String[PROTOCOL_TABLE.length];
		for (int i = 0; i < PROTOCOL_TABLE.length; i++)
		{
			astrProtocols[i] = (String) PROTOCOL_TABLE[i][0];
		}
		return astrProtocols;
	}



	/**	TODO:
	*/
	public static Player createPlayer(String strLocator)
		throws IOException, MediaException
	{
		if (strLocator == null)
		{
			throw new IllegalArgumentException();
		}
 		DataSource	source = createDataSource(strLocator);
 		Player		player = createPlayer(source);
		return player;
	}



	/**	TODO:
	*/
	public static Player createPlayer(DataSource dataSource)
		throws IOException, MediaException
	{
		if (dataSource == null)
		{
			throw new IllegalArgumentException();
		}
		dataSource.connect();
		String	strContentType = dataSource.getContentType();
		Player	player = null;

		for (int i = 0; i < CONTENT_TYPE_TABLE.length; i++)
		{
			if (CONTENT_TYPE_TABLE[i][0].equals(strContentType))
			{
				Class	cls = (Class) CONTENT_TYPE_TABLE[i][1];
				try
				{
					Constructor	constructor = cls.getConstructor(new Class[]{DataSource.class});
					player = (Player) constructor.newInstance(new Object[]{dataSource});
				}
				catch (InstantiationException e)
				{
					// DO NOTHING
				}
				catch (IllegalAccessException e)
				{
					// DO NOTHING
				}
				catch (NoSuchMethodException e)
				{
					// DO NOTHING
				}
				catch (InvocationTargetException e)
				{
					// DO NOTHING
				}
			}
		}
		if (player == null)
		{
			throw new MediaException("unknown content type: " + strContentType);
		}
		return player;
	}



	// TODO:
	/**	TODO:
	*/
	public static Player createPlayer(InputStream stream,
					  String strType)
		throws IOException, MediaException
	{
 		DataSource	dataSource = new InputStreamDataSource(stream, strType);
 		Player		player = createPlayer(dataSource);
		return player;
	}



	/**	TODO:
		@throws IllegalArgumentException Thrown if note or duration value
		are out of range.
	*/
	public static void playTone(int nNote, int nDuration, int nVolume)
		throws MediaException
	{
		if (TDebug.TraceManager) { TDebug.out("Manager.playTone(): begin"); }
		if (nNote < 0 || nNote > 127)
		{
			throw new IllegalArgumentException("note value out of range (must be [0..127])");
		}
		if (nDuration < 0)
		{
			throw new IllegalArgumentException("duration value out of range (must be positive)");
		}
		nVolume = Math.min(100, Math.max(0, nVolume));
		if (sm_toneGenerator == null)
		{
			if (TDebug.TraceManager) { TDebug.out("Manager.playTone(): initializing ToneGenerator"); }
			sm_toneGenerator = new JavaSoundToneGenerator();
		}
		sm_toneGenerator.playTone(nNote, nDuration, nVolume);
		if (TDebug.TraceManager) { TDebug.out("Manager.playTone(): end"); }
	}



	/**	TODO:
	*/
	public static TimeBase getSystemTimeBase()
	{
		return sm_systemTimeBase;
	}


	/**	TODO:
	*/
	private static DataSource createDataSource(String strLocator)
		throws IOException, MediaException
	{
		if (strLocator == null)
		{
			throw new MediaException("media locator is null");
		}
		int	nColonPos = strLocator.indexOf(':');
		if (nColonPos < 0)
		{
			throw new MediaException("no colon found in media locator");
		}
		String	strProtocol = strLocator.substring(0, nColonPos);
		DataSource	dataSource = null;

		for (int i = 0; i < PROTOCOL_TABLE.length; i++)
		{
			if (PROTOCOL_TABLE[i][0].equals(strProtocol))
			{
				Class	cls = (Class) PROTOCOL_TABLE[i][1];
				try
				{
					Constructor	constructor = cls.getConstructor(new Class[]{String.class});
					dataSource = (DataSource) constructor.newInstance(new Object[]{strLocator});
				}
				catch (InstantiationException e)
				{
					// DO NOTHING
				}
				catch (IllegalAccessException e)
				{
					// DO NOTHING
				}
				catch (NoSuchMethodException e)
				{
					// DO NOTHING
				}
				catch (InvocationTargetException e)
				{
					// DO NOTHING
				}
			}
		}
		if (dataSource == null)
		{
			throw new MediaException("unknown protocol: " + strProtocol);
		}
		return dataSource;
	}
}



/*** Manager.java ***/
