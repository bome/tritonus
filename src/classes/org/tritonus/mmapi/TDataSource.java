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
