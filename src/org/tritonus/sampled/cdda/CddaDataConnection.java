/*
 *	CddaDataConnection.java
 */

package	org.tritonus.sampled.cdda;


import	java.io.InputStream;
import	java.io.IOException;

import	java.net.URL;
import	java.net.URLConnection;

import	javax.sound.sampled.AudioFormat;

import	org.tritonus.lowlevel.cdda.CddaMidLevel;
import	org.tritonus.lowlevel.cdda.CddaUtils;
import	org.tritonus.share.TDebug;
import	org.tritonus.share.sampled.convert.TAsynchronousFilteredAudioInputStream;



public class CddaDataConnection
	extends	URLConnection
{
	private static int		PCM_FRAMES_PER_CDDA_FRAME = 588;
	private static AudioFormat	CDDA_FORMAT = new AudioFormat(
		AudioFormat.Encoding.PCM_SIGNED,
		44100.0F, 16, 2, 4, 44100.0F, false);

	/**	The cdda device name to read from.
	 */
	private String		m_strDevice;

	/**	Track to read from the CD.
	 */
	private int		m_nTrack;

	private CddaMidLevel	m_cddaMidLevel;



	public CddaDataConnection(URL url)
	{
		super(url);
		if (TDebug.TraceCdda) { TDebug.out("CddaDataConnection.<init>(): begin"); }
		// TODO: should be done in connect()
		m_cddaMidLevel = CddaUtils.getCddaMidLevel();
		m_strDevice = url.getFile();
		if (m_strDevice.equals(""))
		{
			m_strDevice = m_cddaMidLevel.getDefaultDevice();
		}
		String	strTrack = url.getRef();
		m_nTrack = Integer.parseInt(strTrack);
		if (TDebug.TraceCdda) { TDebug.out("CddaDataConnection.<init>(): end"); }
	}



	public void connect()
	{
		if (TDebug.TraceCdda) { TDebug.out("CddaDataConnection.connect(): begin"); }
		if (! connected)
		{
			// TODO: move here from constructor
			// m_cdda = new CDDA();
			connected = true;
		}
		if (TDebug.TraceCdda) { TDebug.out("CddaDataConnection.connect(): end"); }
	}



	public InputStream getInputStream()
		throws IOException
	{
		if (TDebug.TraceCdda) { TDebug.out("CddaDataConnection.getInputStream(): begin"); }
		String	strDevice = getDevice();
		int	nTrack = getTrack();
		InputStream	inputStream = m_cddaMidLevel.getTrack(strDevice, nTrack);
		if (TDebug.TraceCdda) { TDebug.out("CddaDataConnection.getInputStream(): end"); }
		return inputStream;
	}



	private String getDevice()
	{
		return m_strDevice;
	}



	private int getTrack()
	{
		return m_nTrack;
	}
}


/*** CddaDataConnection.java ****/
