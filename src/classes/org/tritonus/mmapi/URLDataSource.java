/*
 *	URLDataSource.java
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
extends TDataSource
{
	private static final Control[]	EMPTY_CONTROL_ARRAY = new Control[0];
	// has to be set in connect()
	private String		m_strContentType = null;
	// private boolean		m_bConnected = false;
	private boolean		m_bStarted = false;
	private URL		m_url;
	private SourceStream	m_sourceStream;



	public URLDataSource(String strLocator)
		throws MediaException
	{
		super(strLocator);
	}



	// DataSource methods

	public String getContentType()
	{
		return m_strContentType;
	}



	// idempotent??
	public void connect()
		throws IOException
	{
		if (isConnected())
		{
			return;
		}
		String	strLocator = getLocator();
		try
		{
			m_url = new URL(strLocator);
		}
		catch (MalformedURLException e)
		{
			throw new IOException("malformed URL");
		}
		URLConnection	connection = m_url.openConnection();
		InputStream	stream = connection.getInputStream();
		m_strContentType = connection.getContentType();
		// TODO: set controls
	}



	// idempotent??
	public void disconnect()
	{
		if (! isConnected())
		{
			return;
		}
		if (m_bStarted)
		{
			stop();
		}
	}



	// spec says must be connected, but now what happens if not?
	// idempotent??
	public void start()
	{
		if (m_bStarted)
		{
			return;
		}
		if (! isConnected())
		{
			throw new RuntimeException("this DataSource is not connected");
		}
		// TODO: ??
	}



	// idempotent??
	public void stop()
	{
		if (! m_bStarted)
		{
			return;
		}
	}


	// behaviour if not connected or not started?
	public SourceStream[] getStreams()
	{
		if (m_sourceStream != null)
		{
			return new SourceStream[]{m_sourceStream};
		}
		else
		{
			return new SourceStream[0];
		}
	}



	private class URLSourceStream
	extends TControllable
	implements SourceStream
	{
		private InputStream	m_inputStream;
		private String		m_strContentType;
		private long		m_lContentLength;



		public URLSourceStream(InputStream inputStream,
				       String strContentType,
				       long lContentLength)
		{
			super(EMPTY_CONTROL_ARRAY);
			m_inputStream = inputStream;
			m_strContentType = strContentType;
			m_lContentLength = lContentLength;
		}


		// TODO: method does not make sense
		public ContentDescriptor getContentDescriptor()
		{
			return new ContentDescriptor(getContentType());
		}


		// TODO: should become public to replace getContentDescriptor()
		private String getContentType()
		{
			return m_strContentType;
		}


		public long getContentLength()
		{
			return m_lContentLength;
		}


		public int read(byte[] abBuffer, int nOffset, int nLength)
		{
			// TODO:
			return 0;
		}


		// TODO: frame size for PCM? Means that DataSource has to parse headers
		public int getTransferSize()
		{
			return -1;
		}



		public long seek(long lWhere)
			throws IOException
		{
			// TODO:
			return 0;
		}



		public long tell()
		{
			// TODO:
			return 0;
		}



		public int getSeekType()
		{
			// TODO:
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



/*** URLDataSource.java ***/
