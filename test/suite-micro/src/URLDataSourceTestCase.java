/*
 *	URLDataSourceTestCase.java
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
import java.io.File;
import java.net.URL;

import javax.microedition.media.Control;
import javax.microedition.media.protocol.DataSource;
import javax.microedition.media.protocol.SourceStream;

import org.tritonus.mmapi.URLDataSource;


public class URLDataSourceTestCase
extends BaseDataSourceTestCase
{
	public URLDataSourceTestCase(String strName)
	{
		super(strName);
	}



	protected DataSource createDataSource()
		throws Exception
	{
		String		strLocator = "file:../suite/sounds/test.au";
		DataSource	dataSource = new URLDataSource(strLocator);
		return dataSource;
	}



	public void testDataSourceContentTypes()
		throws Exception
	{
		String	strBaseLocator = "file:../suite/sounds";
		checkDataSourceContentType(strBaseLocator + "/test.au", "audio/basic");
		checkDataSourceContentType(strBaseLocator + "/test.aiff", "audio/x-aiff");
		checkDataSourceContentType(strBaseLocator + "/test.wav", "audio/x-wav");
	}



	private void checkDataSourceContentType(String strLocator,
						String strContentType)
		throws Exception
	{
		DataSource	dataSource = new URLDataSource(strLocator);
		dataSource.connect();
		String		strReturnedContentType = dataSource.getContentType();
		assertEquals("DataSource content type", strContentType, strReturnedContentType);
	}



	public void testSourceStreamContentTypes()
		throws Exception
	{
		String	strBaseLocator = "file:../suite/sounds";
		checkSourceStreamContentType(strBaseLocator + "/test.au", "audio/basic");
		checkSourceStreamContentType(strBaseLocator + "/test.aiff", "audio/x-aiff");
		checkSourceStreamContentType(strBaseLocator + "/test.wav", "audio/x-wav");
	}



	private void checkSourceStreamContentType(String strLocator,
						String strContentType)
		throws Exception
	{
		DataSource	dataSource = new URLDataSource(strLocator);
		dataSource.connect();
		SourceStream[]	aSourceStreams = dataSource.getStreams();
		for (int i = 0 ; i < aSourceStreams.length; i++)
		{
			String		strReturnedContentType = aSourceStreams[i].getContentDescriptor().getContentType();
			assertEquals("SourceStream content type", strContentType, strReturnedContentType);
		}
	}


	/**	Tests SourceStream.getContentLength()
	 */
	public void testGetContentLength()
		throws Exception
	{
		String	strBaseFilename = "../suite/sounds";
		checkGetContentLength(strBaseFilename + "/test.au");
		checkGetContentLength(strBaseFilename + "/test.aiff");
		checkGetContentLength(strBaseFilename + "/test.wav");
	}



	/*
	  Here, we are assuming that getStreams() returns an array with
	  one element, i. e. one stream.
	 */
	private void checkGetContentLength(String strFilename)
		throws Exception
	{
		File		file = new File(strFilename);
		long		lFileLength = file.length();
		String		strLocator = "file:" + strFilename;
		DataSource	dataSource = new URLDataSource(strLocator);
		dataSource.connect();
		SourceStream[]	aSourceStreams = dataSource.getStreams();
		long		lContentLength = aSourceStreams[0].getContentLength();
		assertEquals("SourceStream content length", lFileLength, lContentLength);
	}



	/**	Tests content of source streams.
	 */
	public void testStreamContent()
		throws Exception
	{
		String	strBaseFilename = "../suite/sounds";
		checkStreamContent(strBaseFilename + "/test.au");
		checkStreamContent(strBaseFilename + "/test.aiff");
		checkStreamContent(strBaseFilename + "/test.wav");
	}



	/**	Check content of source streams.
	 */
	private void checkStreamContent(String strFilename)
		throws Exception
	{
		File		file = new File(strFilename);
		byte[]		abExpectedContent = Util.getByteArrayFromFile(file);
		String		strLocator = "file:" + strFilename;
		DataSource	dataSource = new URLDataSource(strLocator);
		dataSource.connect();
		SourceStream	sourceStream = dataSource.getStreams()[0];
		byte[]		abContent = Util.getByteArrayFromSourceStream(sourceStream);
		int		nContentLength = (int) sourceStream.getContentLength();
		assertTrue("SourceStream content", Util.compareByteArrays(abExpectedContent, 0, abContent, 0, nContentLength)
);
//	IDEA: instread, use:
// 			for (int i = 0; i < nExpectedDataLength; i++)
// 			{
// 				assertEquals("data content", 0, abRetrievedData[i]);
// 			}
	}
}



/*** URLDataSourceTestCase.java ***/
