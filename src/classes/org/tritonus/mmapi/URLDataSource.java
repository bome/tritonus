/*
 *	URLDataSource.java
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

package	org.tritonus.mmapi;


import	java.io.IOException;
import	java.io.InputStream;

import	java.net.URL;
import	java.net.URLConnection;
import	java.net.MalformedURLException;

import	javax.microedition.media.Control;
import	javax.microedition.media.MediaException;
import	javax.microedition.media.protocol.ContentDescriptor;
import	javax.microedition.media.protocol.DataSource;
import	javax.microedition.media.protocol.SourceStream;



public class URLDataSource
extends TSingleStreamDataSource
{
	private URL		m_url;



	public URLDataSource(String strLocator)
		throws MediaException
	{
		super(strLocator);
	}


	public void doConnect()
		throws IOException
	{
		m_url = new URL(getLocator());
		URLConnection	connection = m_url.openConnection();
		String	strContentType = connection.getContentType();
		setContentType(strContentType);
		InputStream	inputStream = connection.getInputStream();
		setInputStream(inputStream);
		long	lContentLength = connection.getContentLength();
		SourceStream	sourceStream = new URLSourceStream(inputStream,
								   lContentLength);
		setSourceStream(sourceStream);
	}



	public void doDisconnect()
		throws IOException
	{
		super.doDisconnect();
		m_url = null;
	}



	private URL getURL()
	{
		return m_url;
	}


	///////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////




	protected class URLSourceStream
	extends InputStreamSourceStream
	{
		public URLSourceStream(InputStream inputStream,
					       long lContentLength)
		{
			super(inputStream,
			      lContentLength);
		}



		public URL getURL()
		{
			return URLDataSource.this.getURL();
		}
	}
}



/*** URLDataSource.java ***/
