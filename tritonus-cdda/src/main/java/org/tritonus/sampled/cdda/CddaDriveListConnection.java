/*
 *	CddaDriveListConnection.java
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;

import java.net.URL;
import java.net.URLConnection;

import java.util.Iterator;

import org.tritonus.lowlevel.cdda.CddaMidLevel;
import org.tritonus.lowlevel.cdda.CddaUtils;

import org.tritonus.share.TDebug;



public class CddaDriveListConnection
extends	URLConnection
{
	private CddaMidLevel	m_cddaMidLevel;



	// TODO: m_cdda.close();
	public CddaDriveListConnection(URL url)
	{
		super(url);
		if (TDebug.TraceCdda) { TDebug.out("CddaDriveListConnection.<init>(): begin"); }
		if (TDebug.TraceCdda) { TDebug.out("CddaDriveListConnection.<init>(): end"); }
	}



	public void connect()
	{
		if (TDebug.TraceCdda) { TDebug.out("CddaDriveListConnection.connect(): begin"); }
		if (! connected)
		{
			m_cddaMidLevel = CddaUtils.getCddaMidLevel();
			connected = true;
		}
		if (TDebug.TraceCdda) { TDebug.out("CddaDriveListConnection.connect(): end"); }
	}



	public InputStream getInputStream()
		throws IOException
	{
		if (TDebug.TraceCdda) { TDebug.out("CddaDriveListConnection.getInputStream(): begin"); }
		connect();
		Iterator	drivesIterator = m_cddaMidLevel.getDevices();
		ByteArrayOutputStream	baos = new ByteArrayOutputStream();
		PrintStream		out = new PrintStream(baos);
		while (drivesIterator.hasNext())
		{
			String	strDrive = (String) drivesIterator.next();
			out.print(strDrive + "\n");
		}
		byte[]	abData = baos.toByteArray();
		baos.close();
		ByteArrayInputStream	bais = new ByteArrayInputStream(abData);
		if (TDebug.TraceCdda) { TDebug.out("CddaDriveListConnection.getInputStream(): end"); }
		return bais;
	}
}



/*** CddaDriveListConnection.java ****/
