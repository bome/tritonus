/*
 *	Manager.java
 */

package	javax.microedition.media;


import	java.io.IOException;
import	java.io.InputStream;

import	javax.microedition.media.protocol.DataSource;

import	org.tritonus.micro.SystemTimeBase;
import	org.tritonus.micro.URLDataSource;
import	org.tritonus.micro.PcmAudioPlayer;
import	org.tritonus.micro.Mp3AudioPlayer;
import	org.tritonus.micro.MidiPlayer;


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
	};


	/**
	  Table of protocol names and DataSource classes used to
	  implement the protocols.
	*/
	private static final Object[][]	CONTENT_TYPE_TABLE =
	{
		{ "audio/x-wav", PcmAudioPlayer.class },
		{ "audio/basic", PcmAudioPlayer.class },
		{ "audio/mpeg", Mp3AudioPlayer.class },
		{ "audio/x-mid", MidiPlayer.class },
	};


	/**	TODO:
	*/
	private static final TimeBase	sm_systemTimeBase = new SystemTimeBase();




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
 		DataSource	source = createDataSource(strLocator);
 		Player		player = createPlayer(source);
		return player;
	}


	// TODO:
	/**	TODO:
	*/
	public static Player createPlayer(DataSource source)
		throws IOException, MediaException
	{
		if (source == null)
		{
			throw new MediaException();
		}
		return null;
	}



	// TODO:
	// IDEA: use a class InputStreamDataSource
	/**	TODO:
	*/
	public static Player createPlayer(InputStream stream,
					  String strType)
		throws IOException, MediaException
	{
// 		DataSource	source = createDataSource(stream, strType);
// 		Player		player = createPlayer(source);
		return null;	// player;
	}



	// TODO:
	/**	TODO:
	*/
	public static void playTone(int nNote, int nDuration, int nVolume)
	{
	}



	/**	TODO:
	*/
	public static TimeBase getSystemTimeBase()
	{
		return sm_systemTimeBase;
	}


	/**	TODO:
	*/
	// TODO:
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
					dataSource = (DataSource) cls.newInstance();
				}
				catch (InstantiationException e)
				{
					// DO NOTHING
				}
				catch (IllegalAccessException e)
				{
					// DO NOTHING
				}
				dataSource.setLocator(strLocator);
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
