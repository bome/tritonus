/*
 *	TDataSource.java
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



public abstract class TDataSource
extends DataSource
{
	private static final Control[]	EMPTY_CONTROL_ARRAY = new Control[0];
	private TControllable	m_controllable;
	private boolean		m_bConnected = false;



	protected TDataSource(String strLocator)
		throws MediaException
	{
		super(strLocator);
		m_controllable = new TDataSourceControllable(EMPTY_CONTROL_ARRAY);
	}



	protected boolean isConnected()
	{
		return m_bConnected;
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
}



/*** TDataSource.java ***/
