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

import java.util.Arrays;
import java.util.Collection;

import	javax.microedition.media.Manager;
import	javax.microedition.media.TimeBase;
import	javax.microedition.media.protocol.DataSource;



/**	TestCase for javax.microedition.media.Manager.
*/
public class ManagerTestCase
extends TestCase
{
	private static final String[]	LEGAL_MIME_PREFIXES =
	{
		"application",
		"audio",
		"image",
		"message",
		"model",
		"multipart",
		"text",
		"video",
	};



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



	public void testGetSupportedContentTypes()
		throws Exception
	{
		String[]	astrTypes;

		// array must not be empty
		astrTypes = Manager.getSupportedContentTypes(null);
		checkContentTypeArray(astrTypes, 1);

		// array may be empty
		astrTypes = Manager.getSupportedContentTypes("http");
		checkContentTypeArray(astrTypes, 0);
		astrTypes = Manager.getSupportedContentTypes("ftp");
		checkContentTypeArray(astrTypes, 0);
		astrTypes = Manager.getSupportedContentTypes("file");
		checkContentTypeArray(astrTypes, 0);
		astrTypes = Manager.getSupportedContentTypes("rtp");
		checkContentTypeArray(astrTypes, 0);
		astrTypes = Manager.getSupportedContentTypes("capture");
		checkContentTypeArray(astrTypes, 0);
		astrTypes = Manager.getSupportedContentTypes("device");
		checkContentTypeArray(astrTypes, 0);

		// array must be empty
		astrTypes = Manager.getSupportedContentTypes("");
		checkContentTypeArray(astrTypes, -1);
		astrTypes = Manager.getSupportedContentTypes("xxx");
		checkContentTypeArray(astrTypes, -1);
	}



	// negative values for nMinimumElements mean check array for emptyness
	// TODO: check content type for well-formedness
	private void checkContentTypeArray(String[] astrTypes, int nMinimumElements)
		throws Exception
	{
		Collection	legalMimePrefixes = Arrays.asList(LEGAL_MIME_PREFIXES);
		assertTrue("ContentType array is not null", astrTypes != null);
		if (nMinimumElements >= 0)
		{
			assertTrue("ContentType array has required number of elements", astrTypes.length >= nMinimumElements);
			for (int i = 0; i < astrTypes.length; i++)
			{
				assertTrue("ContentType is not null", astrTypes[i] != null);
				assertTrue("ContentType is not empty", astrTypes[i].length() > 0);
				int	nSlashPosition = astrTypes[i].indexOf('/');
				assertTrue("ContentType contains a slash", nSlashPosition != -1);
				String	strFirstPart = astrTypes[i].substring(0, nSlashPosition);
				String	strLastPart = astrTypes[i].substring(nSlashPosition + 1);
				assertTrue("ContentType first part legal", legalMimePrefixes.contains(strFirstPart));
				assertTrue("ContentType last part is not empty", strLastPart.length() > 0);
			}
		}
		else
		{
			assertEquals("ContentType array is empty", 0, astrTypes.length);
		}
	}



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



// 	public void testCreatePlayer()
// 		throws Exception
// 	{
// 		Player	player = null;
// 		String[]	astrProtocols = Manager.getSupportedProtocols();
// 		String		strLocator = null;
// 		if (Util.arrayContains(astrProtocols, "file"))
// 		{
// 			strLocator = "file:///vmlinux";
// 			player = Manager.createPlayer(strLocator);
// 		}
// 		if (Util.arrayContains(astrProtocols, "http"))
// 		{
// 			strLocator = "http://www.tritonus.org/index.html";
// 			player = Manager.createPlayer(strLocator);
// 		}
// 		if (Util.arrayContains(astrProtocols, "ftp"))
// 		{
// 			strLocator = "ftp://ftp.debian.org/";
// 			player = Manager.createPlayer(strLocator);
// 		}
// 	}



	public void testPlayTone()
		throws Exception
	{
		boolean	bExceptionThrown;

		// note parameter tests
		Manager.playTone(0, 100, 100);
		Manager.playTone(127, 100, 100);

		// check IllegalArgumentException

		bExceptionThrown = false;
		try
		{
			Manager.playTone(-1, 100, 100);
		}
		catch (IllegalArgumentException e)
		{
			bExceptionThrown = true;
		}
		if (! bExceptionThrown)
		{
			fail("IllegalArgumentException on Manager.playTone(-1, .., ..)");
		}

		bExceptionThrown = false;
		try
		{
			Manager.playTone(128, 100, 100);
		}
		catch (IllegalArgumentException e)
		{
			bExceptionThrown = true;
		}
		if (! bExceptionThrown)
		{
			fail("IllegalArgumentException on Manager.playTone(128, .., ..)");
		}

		// duration parameter tests
		Manager.playTone(60, 0, 100);

		bExceptionThrown = false;
		try
		{
			Manager.playTone(60, -1, 100);
		}
		catch (IllegalArgumentException e)
		{
			bExceptionThrown = true;
		}
		if (! bExceptionThrown)
		{
			fail("IllegalArgumentException on Manager.playTone(.., -1, ..)");
		}


		// check if volume values out of range can be handled
		Manager.playTone(50, 100, Integer.MAX_VALUE);
		Manager.playTone(40, 100, Integer.MIN_VALUE);
	}



	public void testGetSystemTimeBase()
		throws Exception
	{
		TimeBase	base = Manager.getSystemTimeBase();
		assertTrue(base != null);
		assertTrue(base instanceof TimeBase);
	}
}



/*** ManagerTestCase.java ***/
