/*
 *	TDataSource.java
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

import	java.util.Enumeration;
import	java.util.Hashtable;

import	javax.microedition.media.Control;
import	javax.microedition.media.MediaException;
import	javax.microedition.media.control.MetaDataControl;
import	javax.microedition.media.protocol.ContentDescriptor;
import	javax.microedition.media.protocol.DataSource;
import	javax.microedition.media.protocol.SourceStream;



public abstract class TDataSource
extends DataSource
{
	protected static final Control[]	EMPTY_CONTROL_ARRAY = new Control[0];

	private TControllable	m_controllable;
	private boolean		m_bConnected = false;
	private boolean		m_bStarted = false;
	private String		m_strContentType;



	protected TDataSource(String strLocator)
		throws MediaException
	{
		super(strLocator);
		Control[]	aControls = new Control[]
			{
				new TMetaDataControl("unknown", "unknown", "unknown", "unknown")
			};
		m_controllable = new TDataSourceControllable(aControls);
	}



	// Controllable methods

	public Control getControl(String strType)
	{
		Control	control = m_controllable.getControl(strType);
		return control;
	}



	public Control[] getControls()
	{
		Control[]	aControls = m_controllable.getControls();
		return aControls;
	}



	protected void setContentType(String strContentType)
	{
		m_strContentType = strContentType;
	}




	public String getContentType()
	{
		// The following check may throw an IllegalStateException.
		checkConnected();

		return m_strContentType;
	}



	public void connect()
		throws IOException
	{
		if (! isConnected())
		{
			doConnect();
			setConnected(true);
		}
	}


	protected void doConnect()
		throws IOException
	{
	}




	public void disconnect()
	{
		if (isConnected())
		{
			try
			{
				stop();
				doDisconnect();
				setConnected(false);
			}
			catch (IOException e)
			{
				// IGNORED
			}
		}
	}


	protected void doDisconnect()
		throws IOException
	{
	}




	public void start()
		throws IOException
	{
		// The following check may throw an IllegalStateException.
		checkConnected();

		if (! isStarted())
		{
			doStart();
			setStarted(true);
		}
	}


	protected void doStart()
		throws IOException
	{
	}




	public void stop()
		throws IOException
	{
		if (isStarted())
		{
			doStop();
			setStarted(false);
		}
	}


	protected void doStop()
		throws IOException
	{
	}




	// custom methods


	private synchronized void setConnected(boolean bConnected)
	{
		m_bConnected = bConnected;
	}



	protected boolean isConnected()
	{
		return m_bConnected;
	}



	private synchronized void setStarted(boolean bStarted)
	{
		m_bStarted = bStarted;
	}



	protected boolean isStarted()
	{
		return m_bStarted;
	}



	/**	Checks if the DataSource is connected.
		If this condition is not met,
		an IllegalStateException is throw.

		@throws IllegalStateException Thrown if the DataSource is
		not connected.
	*/
	protected void checkConnected()
	{
		if (! isConnected())
		{
			throw new IllegalStateException("DataSource is not connected.");
		}
	}


	///////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////

	protected class TDataSourceControllable
	extends TControllable
	{
		public TDataSourceControllable(Control[] aControls)
		{
			super(aControls);
		}


		/**
		  For control methods, DataSource has to be connected.
		*/
		protected boolean isStateLegalForControls()
		{
			return TDataSource.this.isConnected();
		}
	}


	protected class TMetaDataControl
	implements MetaDataControl
	{
		private Hashtable	m_data;


		public TMetaDataControl()
		{
			m_data = new Hashtable();
		}



		public TMetaDataControl(String strAuthor,
					String strCopyright,
					String strDate,
					String strTitle)
		{
			this();
			addField(AUTHOR_KEY, strAuthor);
			addField(COPYRIGHT_KEY, strCopyright);
			addField(DATE_KEY, strDate);
			addField(TITLE_KEY, strTitle);
		}



		protected void addField(String strKey, String strValue)
		{
			m_data.put(strKey, strValue);
		}



		public String[] getKeys()
		{
			String[]	astrKeys = new String[m_data.size()];
			Enumeration	enum = m_data.keys();
			int		nIndex = 0;
			while (enum.hasMoreElements())
			{
				astrKeys[nIndex] = (String) enum.nextElement();
				nIndex++;
			}
			return astrKeys;
		}



		public String getKeyValue(String strKey)
		{
			if (strKey == null)
			{
				throw new IllegalArgumentException("key is null");
			}
			String	strValue = (String) m_data.get(strKey);
			if (strValue == null)
			{
				throw new IllegalArgumentException("key is invalid");
			}
			return strValue;
		}
	}
}



/*** TDataSource.java ***/
