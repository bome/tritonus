/*
 *	BaseDataSourceTestCase.java
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

import javax.microedition.media.Control;
import javax.microedition.media.Controllable;
import javax.microedition.media.protocol.DataSource;
import javax.microedition.media.protocol.SourceStream;



/**	Base TestCases for javax.microedition.media.protocol.DataSource.
*/
public abstract class BaseDataSourceTestCase
extends BaseControllableTestCase
{
	public BaseDataSourceTestCase(String strName)
	{
		super(strName);
	}


	/**	Create Controllable for simple tests.
	*/
	protected Controllable createControllable()
		throws Exception
	{
		DataSource	dataSource = createDataSource();
		dataSource.connect();
		return dataSource;
	}


	/**	Create DataSource for state tests.
		Subclasses must implement this method in a way
		that a DataSource is returned that can be fully used.
	*/
	protected abstract DataSource createDataSource()
		throws Exception;


	/**	Test stepping through the life cycle of a DataSource.
	 */
	public void testStates()
		throws Exception
	{
		DataSource	dataSource = createDataSource();
		dataSource.connect();
		dataSource.start();
		dataSource.stop();
		dataSource.disconnect();
	}



	public void testIllegalStates()
		throws Exception
	{
		DataSource	dataSource = createDataSource();
		TestMethod	getContentTypeMethod = new TestMethod()
			{
				public void callMethod(DataSource dataSource)
					throws Exception
				{
					dataSource.getContentType();
				}
			};
		TestMethod	startMethod = new TestMethod()
			{
				public void callMethod(DataSource dataSource)
					throws Exception
				{
					dataSource.start();
				}
			};
		TestMethod	getStreamsMethod = new TestMethod()
			{
				public void callMethod(DataSource dataSource)
					throws Exception
				{
					dataSource.getStreams();
				}
			};

		callDataSourceMethod(dataSource, getContentTypeMethod, "getContentType()");
		callDataSourceMethod(dataSource, startMethod, "start()");
		callDataSourceMethod(dataSource, getStreamsMethod, "getStreams()");
		dataSource.connect();
		dataSource.disconnect();
		callDataSourceMethod(dataSource, getContentTypeMethod, "getContentType()");
		callDataSourceMethod(dataSource, startMethod, "start()");
		callDataSourceMethod(dataSource, getStreamsMethod, "getStreams()");
	}



	/**	Calls TestMethod's method to see if an exception occurs.
	 */
	private void callDataSourceMethod(DataSource dataSource,
				      TestMethod method,
				      String strMethodName)
		throws Exception
	{
		boolean	bExceptionThrown = false;
		try
		{
			method.callMethod(dataSource);
		}
		catch (IllegalStateException e)
		{
			bExceptionThrown = true;
		}
		if (! bExceptionThrown)
		{
			fail("IllegalStateException on " + strMethodName + " while not connected.");
		}
	}


	public void testGetStreams()
		throws Exception
	{
		DataSource	dataSource = createDataSource();
		dataSource.connect();
		SourceStream[]	aSourceStreams = dataSource.getStreams();
		assertTrue("SourceStream[] length", aSourceStreams.length >= 1);
	}



	public void testSourceStreamGetSeekType()
		throws Exception
	{
		final int[]	anLegalSeekTypes = new int[]
			{
				SourceStream.NOT_SEEKABLE,
				SourceStream.SEEKABLE_TO_START,
				SourceStream.RANDOM_ACCESSIBLE,
			};
		DataSource	dataSource = createDataSource();
		dataSource.connect();
		SourceStream[]	aSourceStreams = dataSource.getStreams();
		for (int i = 0 ; i < aSourceStreams.length; i++)
		{
			int	nSeekType = aSourceStreams[i].getSeekType();
			// check if in array
			assertTrue("SourceStream search type", Util.arrayContains(anLegalSeekTypes, nSeekType));
		}
	}

	/** TODO:
	 */
	public void testControls()
		throws Exception
	{
		DataSource	dataSource = createDataSource();
		dataSource.connect();
		Control[]	aControls = dataSource.getControls();
		assertEquals("Control[] length", 0, aControls.length);
	}


	// TODO:
	public void testSourceStreamControls()
		throws Exception
	{
		DataSource	dataSource = createDataSource();
		dataSource.connect();
		SourceStream[]	aSourceStreams = dataSource.getStreams();
		for (int i = 0; i < aSourceStreams.length; i++)
		{
			Control[]	aControls = aSourceStreams[i].getControls();
			assertEquals("SourceStream's Control[] length", 0, aControls.length);
		}
	}






	private static interface TestMethod
	{
		public void callMethod(DataSource dataSource)
			throws Exception;
	}
}



/*** BaseDataSourceTestCase.java ***/
