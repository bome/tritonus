/*
 *	ManagerTestCase.java
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

import	junit.framework.TestCase;

import	javax.microedition.media.Manager;
import	javax.microedition.media.TimeBase;
import	javax.microedition.media.protocol.DataSource;



public class ManagerTestCase
extends TestCase
{
	public ManagerTestCase(String strName)
	{
		super(strName);
	}



	public void testConstants()
		throws Exception
	{
		assertEquals("device://tone", Manager.TONE_DEVICE_LOCATOR);
		assertEquals("device://midi", Manager.MIDI_DEVICE_LOCATOR);
	}



// 	public void testGetSupportedContentTypes()
// 		throws Exception
// 	{
// 		String[]	astrTypes = Manager.getSupportedContentTypes();
// 		assertTrue("returned array is null", astrTypes != null);
// 		assertTrue("returned array has length 0", astrTypes.length > 0);
// 		for (int i = 0; i < astrTypes.length; i++)
// 		{
// 			assertTrue("array element is null", astrTypes[i] != null);
// 			assertTrue("array element (String) has length 0", astrTypes[i].length() > 0);
// 		}
// 	}



// 	public void testGetSupportedProtocols()
// 		throws Exception
// 	{
// 		String[]	astrProtocols = Manager.getSupportedProtocols();
// 		assertTrue("returned array is null", astrProtocols != null);
// 		assertTrue("returned array has length 0", astrProtocols.length > 0);
// 		for (int i = 0; i < astrProtocols.length; i++)
// 		{
// 			assertTrue("array element is null", astrProtocols[i] != null);
// 			assertTrue("array element (String) has length 0", astrProtocols[i].length() > 0);
// 		}
// 		assertTrue("http protocol not supported", Util.arrayContains(astrProtocols, "http"));
// 		assertTrue("ftp protocol not supported", Util.arrayContains(astrProtocols, "ftp"));
// 		assertTrue("file protocol not supported", Util.arrayContains(astrProtocols, "file"));
// 		assertTrue("rtp protocol not supported", Util.arrayContains(astrProtocols, "rtp"));
// 		assertTrue("capture protocol not supported", Util.arrayContains(astrProtocols, "capture"));
// 	}



	public void testGetSystemTimeBase()
		throws Exception
	{
		TimeBase	base = Manager.getSystemTimeBase();
		assertTrue(base != null);
		assertTrue(base instanceof TimeBase);
	}



// 	public void testCreateDataSource()
// 		throws Exception
// 	{
// 		DataSource	dataSource = null;
// 		String[]	astrProtocols = Manager.getSupportedProtocols();
// 		String		strLocator = null;
// 		if (Util.arrayContains(astrProtocols, "file"))
// 		{
// 			strLocator = "file:///vmlinux";
// 			dataSource = Manager.createDataSource(strLocator);
// 		}
// 		if (Util.arrayContains(astrProtocols, "http"))
// 		{
// 			strLocator = "http://www.tritonus.org/index.html";
// 			dataSource = Manager.createDataSource(strLocator);
// 		}
// 		if (Util.arrayContains(astrProtocols, "ftp"))
// 		{
// 			strLocator = "ftp://ftp.debian.org/";
// 			dataSource = Manager.createDataSource(strLocator);
// 		}
// 	}
}



/*** ManagerTestCase.java ***/
