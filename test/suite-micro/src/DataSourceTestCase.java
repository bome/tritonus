/*
 *	DataSourceTestCase.java
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

import junit.framework.TestCase;

import java.io.IOException;
import javax.microedition.media.Control;
import javax.microedition.media.protocol.DataSource;
import javax.microedition.media.protocol.SourceStream;



public class DataSourceTestCase
extends TestCase
{
	public DataSourceTestCase(String strName)
	{
		super(strName);
	}



	public void testLocator()
		throws Exception
	{
		String		strLocator = "protocol:body";
		DataSource	source = new TestDataSource(strLocator);
		String		strReturnedLocator = source.getLocator();
		assertEquals("Locator", strLocator, strReturnedLocator);
	}



	private static class TestDataSource
	extends DataSource
	{
		public TestDataSource(String strLocator)
		{
			super(strLocator);
		}


		public Control[] getControls()
		{
			return null;
		}


		public Control getControl(String strControlType)
		{
			return null;
		}


		public String getContentType()
		{
			return null;
		}



		public void connect()
			throws IOException
		{
		}



		public void disconnect()
		{
		}



		public void start()
			throws IOException
		{
		}



		public void stop()
			throws IOException
		{
		}



		public SourceStream[] getStreams()
		{
			return null;
		}
	}
}



/*** DataSourceTestCase.java ***/
