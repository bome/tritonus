/*
 *	CddaTocConnection.java
 */

package	org.tritonus.sampled.cdda;


import	java.io.InputStream;
import	java.io.IOException;

import	java.net.URL;
import	java.net.URLConnection;

import	org.tritonus.lowlevel.cdda.CddaMidLevel;
import	org.tritonus.lowlevel.cdda.CddaUtils;

import	org.tritonus.share.TDebug;



public class CddaTocConnection
	extends	URLConnection
{
	/**	The cdda device name to read from.
	 */
	private String		m_strDevice;

	private CddaMidLevel	m_cddaMidLevel;



	// TODO: m_cdda.close();
	public CddaTocConnection(URL url)
	{
		super(url);
		if (TDebug.TraceCdda) { TDebug.out("CddaTocConnection.<init>(): begin"); }
		// TODO: do it in connect(), but: someone has to call connect()
		m_cddaMidLevel = CddaUtils.getCddaMidLevel();
		m_strDevice = url.getPath();
		if (m_strDevice.equals(""))
		{
			m_strDevice = m_cddaMidLevel.getDefaultDevice();
		}
		if (TDebug.TraceCdda) { TDebug.out("CddaTocConnection.<init>(): end"); }
	}



	public void connect()
	{
		if (TDebug.TraceCdda) { TDebug.out("CddaTocConnection.connect(): begin"); }
		if (! connected)
		{
			// m_cdda = new CDDA();
			connected = true;
		}
		if (TDebug.TraceCdda) { TDebug.out("CddaTocConnection.connect(): end"); }
	}



	public InputStream getInputStream()
		throws IOException
	{
		if (TDebug.TraceCdda) { TDebug.out("CddaTocConnection.getInputStream(): begin"); }
		String	strDevice = getDevice();
		InputStream	inputStream = m_cddaMidLevel.getTocAsXml(strDevice);
		if (TDebug.TraceCdda) { TDebug.out("CddaTocConnection.getInputStream(): end"); }
		return inputStream;
	}



	private String getDevice()
	{
		return m_strDevice;
	}
}



/*** CddaTocConnection.java ****/
