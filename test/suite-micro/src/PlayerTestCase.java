/*
 *	PlayerTestCase.java
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

import	javax.microedition.media.Controllable;
import	javax.microedition.media.Manager;
import	javax.microedition.media.Player;



public class PlayerTestCase
extends TestCase
{
	public PlayerTestCase(String strName)
	{
		super(strName);
	}



	public void testConstants()
		throws Exception
	{
		assertEquals(100, Player.UNREALIZED);
		assertEquals(200, Player.REALIZED);
		assertEquals(300, Player.PREFETCHED);
		assertEquals(400, Player.STARTED);
		assertEquals(0, Player.CLOSED);
		assertEquals(-1, Player.TIME_UNKNOWN);
	}



	public void testStates()
		throws Exception
	{
		String	strLocator = "file:/home/matthias/java/tritonus/test/suite/sounds/test.wav";
		Player	player = null;

		// UNREALIZED -> CLOSED
		player = Manager.createPlayer(strLocator);

		assertEquals("UNREALIZED state", Player.UNREALIZED, player.getState());
		player.close();
		assertEquals("CLOSED state", Player.CLOSED, player.getState());

		// UNREALIZED -> REALIZED -> CLOSED
		player = Manager.createPlayer(strLocator);

		assertEquals("UNREALIZED state", Player.UNREALIZED, player.getState());
		player.realize();
		assertEquals("REALIZED state", Player.REALIZED, player.getState());
		player.close();
		assertEquals("CLOSED state", Player.CLOSED, player.getState());

		// UNREALIZED -> REALIZED -> PREFETCHED -> CLOSED
		player = Manager.createPlayer(strLocator);

		assertEquals("UNREALIZED state", Player.UNREALIZED, player.getState());
		player.realize();
		assertEquals("REALIZED state", Player.REALIZED, player.getState());
		player.prefetch();
		assertEquals("PREFETCHED state", Player.PREFETCHED, player.getState());
		player.close();
		assertEquals("CLOSED state", Player.CLOSED, player.getState());

		// UNREALIZED -> PREFETCHED -> REALIZED
		player = Manager.createPlayer(strLocator);

		assertEquals("UNREALIZED state", Player.UNREALIZED, player.getState());
		player.prefetch();
		assertEquals("PREFETCHED state", Player.PREFETCHED, player.getState());
		player.deallocate();
		assertEquals("REALIZED state", Player.REALIZED, player.getState());

		// UNREALIZED -> PREFETCHED -> STARTED -> CLOSED
		player = Manager.createPlayer(strLocator);

		assertEquals("UNREALIZED state", Player.UNREALIZED, player.getState());
		player.prefetch();
		assertEquals("PREFETCHED state", Player.PREFETCHED, player.getState());
		player.start();
		assertEquals("STARTED state", Player.STARTED, player.getState());
		player.close();
		assertEquals("CLOSED state", Player.CLOSED, player.getState());

		// UNREALIZED -> REALIZED -> STARTED -> PREFETCHED
		player = Manager.createPlayer(strLocator);

		assertEquals("UNREALIZED state", Player.UNREALIZED, player.getState());
		player.realize();
		assertEquals("REALIZED state", Player.REALIZED, player.getState());
		player.start();
		assertEquals("STARTED state", Player.STARTED, player.getState());
		player.stop();
		assertEquals("PREFETCHED state", Player.PREFETCHED, player.getState());

		// UNREALIZED -> STARTED
		player = Manager.createPlayer(strLocator);

		assertEquals("UNREALIZED state", Player.UNREALIZED, player.getState());
		player.start();
		assertEquals("STARTED state", Player.STARTED, player.getState());
	}



	public void testIllegalStatesControllable()
		throws Exception
	{
		String	strLocator = "file:/home/matthias/java/tritonus/test/suite/sounds/test.wav";
		Player	player = Manager.createPlayer(strLocator);

		// UNREALIZED
		callControllableMethods(player, "UNREALIZED");

		// CLOSED
		player.close();
		callControllableMethods(player, "CLOSED");
	}


	/**	Calls both Controllable methods to see if an exception occurs.
	 */
	private void callControllableMethods(Controllable controllable,
					     String strState)
		throws Exception
	{
		boolean	bExceptionThrown;
		String	strControlType = "GUIControl";

		bExceptionThrown = false;
		try
		{
			controllable.getControls();
		}
		catch (IllegalStateException e)
		{
			bExceptionThrown = true;
		}
		if (! bExceptionThrown)
		{
			fail("IllegalStateException on getControls() in " + strState + " state");
		}

		bExceptionThrown = false;
		try
		{
			controllable.getControl(strControlType);
		}
		catch (IllegalStateException e)
		{
			bExceptionThrown = true;
		}
		if (! bExceptionThrown)
		{
			fail("IllegalStateException on getControl(String) in " + strState + " state");
		}
	}



	public void testIllegalStatesPlayer()
		throws Exception
	{
		String	strLocator = "file:/home/matthias/java/tritonus/test/suite/sounds/test.wav";
		Player	player = Manager.createPlayer(strLocator);
		boolean	bExceptionThrown;

		TestMethod	realizeMethod = new TestMethod()
			{
				public void callMethod(Player player)
					throws Exception
				{
					player.realize();
				}
			};
		TestMethod	prefetchMethod = new TestMethod()
			{
				public void callMethod(Player player)
					throws Exception
				{
					player.prefetch();
				}
			};
		TestMethod	startMethod = new TestMethod()
			{
				public void callMethod(Player player)
					throws Exception
				{
					player.start();
				}
			};
		TestMethod	stopMethod = new TestMethod()
			{
				public void callMethod(Player player)
					throws Exception
				{
					player.stop();
				}
			};
		TestMethod	deallocateMethod = new TestMethod()
			{
				public void callMethod(Player player)
					throws Exception
				{
					player.deallocate();
				}
			};
		TestMethod	setTimeBaseMethod = new TestMethod()
			{
				public void callMethod(Player player)
					throws Exception
				{
					player.setTimeBase(Manager.getSystemTimeBase());
				}
			};
		TestMethod	getTimeBaseMethod = new TestMethod()
			{
				public void callMethod(Player player)
					throws Exception
				{
					player.getTimeBase();
				}
			};
		TestMethod	setMediaTimeMethod = new TestMethod()
			{
				public void callMethod(Player player)
					throws Exception
				{
					player.setMediaTime(0L);
				}
			};
		TestMethod	getMediaTimeMethod = new TestMethod()
			{
				public void callMethod(Player player)
					throws Exception
				{
					player.getMediaTime();
				}
			};
		TestMethod	getDurationMethod = new TestMethod()
			{
				public void callMethod(Player player)
					throws Exception
				{
					player.getDuration();
				}
			};
		TestMethod	getContentTypeMethod = new TestMethod()
			{
				public void callMethod(Player player)
					throws Exception
				{
					player.getContentType();
				}
			};
		TestMethod	setLoopCountMethod = new TestMethod()
			{
				public void callMethod(Player player)
					throws Exception
				{
					player.setLoopCount(1);
				}
			};

		// UNREALIZED state
		callPlayerMethod(player, setTimeBaseMethod, "setTimeBase()", "UNREALIZED");
		callPlayerMethod(player, getTimeBaseMethod, "getTimeBase()", "UNREALIZED");
		callPlayerMethod(player, setMediaTimeMethod, "setMediaTime()", "UNREALIZED");
		callPlayerMethod(player, getContentTypeMethod, "getContentType()", "UNREALIZED");

		// STARTED
		player.start();
		callPlayerMethod(player, setTimeBaseMethod, "setTimeBase()", "STARTED");
		callPlayerMethod(player, setLoopCountMethod, "setLoopCount()", "STARTED");

		// CLOSED
		player.close();
		callPlayerMethod(player, realizeMethod, "realize()", "CLOSED");
		// prefetch()
		callPlayerMethod(player, prefetchMethod, "prefetch()", "CLOSED");
		// start()
		callPlayerMethod(player, startMethod, "start()", "CLOSED");
		// stop()
		callPlayerMethod(player, stopMethod, "stop()", "CLOSED");
		// deallocate()
		callPlayerMethod(player, deallocateMethod, "deallocate()", "CLOSED");
		// setTimeBase()
		callPlayerMethod(player, setTimeBaseMethod, "setTimeBase()", "CLOSED");
		// getTimeBase()
		callPlayerMethod(player, getTimeBaseMethod, "getTimeBase()", "CLOSED");
		// setMediaTime()
		callPlayerMethod(player, setMediaTimeMethod, "setMediaTime()", "CLOSED");
		// getMediaTime()
		callPlayerMethod(player, getMediaTimeMethod, "getMediaTime()", "CLOSED");
		// getDuration()
		callPlayerMethod(player, getDurationMethod, "getDuration()", "CLOSED");
		// getContentType()
		callPlayerMethod(player, getContentTypeMethod, "getContentType()", "CLOSED");
		// setLoopCount()
		callPlayerMethod(player, setLoopCountMethod, "setLoopCount()", "CLOSED");
	}



	/**	Calls both Controllable methods to see if an exception occurs.
	 */
	private void callPlayerMethod(Player player,
				      TestMethod method,
				      String strMethodName,
				      String strState)
		throws Exception
	{
		boolean	bExceptionThrown = false;
		try
		{
			method.callMethod(player);
		}
		catch (IllegalStateException e)
		{
			bExceptionThrown = true;
		}
		if (! bExceptionThrown)
		{
			fail("IllegalStateException on " + strMethodName + " in " + strState + " state");
		}
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


	private static interface TestMethod
	{
		public void callMethod(Player player)
			throws Exception;
	}
}



/*** PlayerTestCase.java ***/
