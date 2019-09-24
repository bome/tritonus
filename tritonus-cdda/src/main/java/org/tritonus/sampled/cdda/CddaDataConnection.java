/*
 *	CddaDataConnection.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2001 - 2002 by Matthias Pfisterer
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
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.sampled.cdda;

import java.io.InputStream;
import java.io.IOException;

import java.net.URL;
import java.net.URLConnection;

import javax.sound.sampled.AudioFormat;

import org.tritonus.lowlevel.cdda.CddaMidLevel;
import org.tritonus.lowlevel.cdda.CddaUtils;
import org.tritonus.share.TDebug;



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
		m_strDevice = url.getFile();
		String	strTrack = url.getRef();
		m_nTrack = Integer.parseInt(strTrack);
		if (TDebug.TraceCdda) { TDebug.out("CddaDataConnection.<init>(): end"); }
	}



	public void connect()
	{
		if (TDebug.TraceCdda) { TDebug.out("CddaDataConnection.connect(): begin"); }
		if (! connected)
		{
			m_cddaMidLevel = CddaUtils.getCddaMidLevel();
			if (m_strDevice.equals(""))
			{
				m_strDevice = m_cddaMidLevel.getDefaultDevice();
			}
			connected = true;
		}
		if (TDebug.TraceCdda) { TDebug.out("CddaDataConnection.connect(): end"); }
	}




	public InputStream getInputStream()
		throws IOException
	{
		if (TDebug.TraceCdda) { TDebug.out("CddaDataConnection.getInputStream(): begin"); }
		connect();
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
