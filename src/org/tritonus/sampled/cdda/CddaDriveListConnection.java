/*
 *	CddaDriveListConnection.java
 */

package	org.tritonus.sampled.cdda;


import	java.io.ByteArrayInputStream;
import	java.io.ByteArrayOutputStream;
import	java.io.InputStream;
import	java.io.IOException;
import	java.io.PrintStream;

import	java.net.URL;
import	java.net.URLConnection;

import	java.util.Iterator;

import	org.tritonus.lowlevel.cdda.CddaMidLevel;
import	org.tritonus.lowlevel.cdda.CddaUtils;

import	org.tritonus.share.TDebug;



public class CddaDriveListConnection
	extends	URLConnection
{
	private CddaMidLevel	m_cddaMidLevel;



	// TODO: m_cdda.close();
	public CddaDriveListConnection(URL url)
	{
		super(url);
		if (TDebug.TraceCdda) { TDebug.out("CddaDriveListConnection.<init>(): begin"); }
		m_cddaMidLevel = CddaUtils.getCddaMidLevel();
		if (TDebug.TraceCdda) { TDebug.out("CddaDriveListConnection.<init>(): end"); }
	}



	public void connect()
	{
		if (TDebug.TraceCdda) { TDebug.out("CddaDriveListConnection.connect(): begin"); }
		if (! connected)
		{
			// m_cdda = new CDDA();
			connected = true;
		}
		if (TDebug.TraceCdda) { TDebug.out("CddaDriveListConnection.connect(): end"); }
	}



	public InputStream getInputStream()
		throws IOException
	{
		if (TDebug.TraceCdda) { TDebug.out("CddaDriveListConnection.getInputStream(): begin"); }
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
