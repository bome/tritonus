/*
 *	TSingleStreamDataSource.java
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



public abstract class TSingleStreamDataSource
extends TDataSource
{
	private InputStream	m_inputStream;
	private SourceStream	m_sourceStream;


	/**
	*/
	protected TSingleStreamDataSource(String strLocator)
		throws MediaException
	{
		super(strLocator);
	}



	protected void setInputStream(InputStream inputStream)
	{
		m_inputStream = inputStream;
	}



	protected InputStream getInputStream()
	{
		return m_inputStream;
	}



	protected void setSourceStream(SourceStream sourceStream)
	{
		m_sourceStream = sourceStream;
	}



	public void doDisconnect()
		throws IOException
	{
		super.doDisconnect();
		setSourceStream(null);
		m_inputStream.close();
		setInputStream(null);
	}



	// behaviour if not started?
	public SourceStream[] getStreams()
	{
		// The following check may throw an IllegalStateException.
		checkConnected();

		if (m_sourceStream != null)
		{
			return new SourceStream[]{m_sourceStream};
		}
		else
		{
			return new SourceStream[0];
		}
	}



	///////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////




	protected class InputStreamSourceStream
	extends TControllable
	implements SourceStream
	{
		private InputStream	m_inputStream;
		private long		m_lContentLength;
		private ContentDescriptor	m_contentDescriptor;



		public InputStreamSourceStream(InputStream inputStream,
					       long lContentLength)
		{
			super(EMPTY_CONTROL_ARRAY);
			m_inputStream = inputStream;
			m_lContentLength = lContentLength;
		}




		public ContentDescriptor getContentDescriptor()
		{
			if (m_contentDescriptor == null)
			{
				m_contentDescriptor = new ContentDescriptor(getContentType());
			}
			return m_contentDescriptor;
		}



		private String getContentType()
		{
			return TSingleStreamDataSource.this.getContentType();
		}


		public long getContentLength()
		{
			return m_lContentLength;
		}


		public int read(byte[] abBuffer, int nOffset, int nLength)
			throws IOException
		{
			int	nBytesRead = m_inputStream.read(abBuffer, nOffset, nLength);
			return nBytesRead;
		}



		public int getTransferSize()
		{
			return -1;
		}



		public long seek(long lWhere)
			throws IOException
		{
			throw new IOException("seek() not supported");
		}



		public long tell()
		{
			// TODO:
			return 0;
		}



		public int getSeekType()
		{
			return NOT_SEEKABLE;
		}



		/**
		   controls can be queried at any time.
		 */
		protected boolean isStateLegalForControls()
		{
			return true;
		}

	}

}



/*** TSingleStreamDataSource.java ***/
