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
import	javax.microedition.media.PlayerListener;


/**	TestCase for javax.microedition.media.Player.
*/
public class PlayerTestCase
extends TestCase
{
	/** Time to sleep() between return from a transition method and
	    the test if the event has been delivered.
	*/
	private static final int	SLEEP = 200;


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
		callPlayerMethod(player, prefetchMethod, "prefetch()", "CLOSED");
		callPlayerMethod(player, startMethod, "start()", "CLOSED");
		callPlayerMethod(player, stopMethod, "stop()", "CLOSED");
		callPlayerMethod(player, deallocateMethod, "deallocate()", "CLOSED");
		callPlayerMethod(player, setTimeBaseMethod, "setTimeBase()", "CLOSED");
		callPlayerMethod(player, getTimeBaseMethod, "getTimeBase()", "CLOSED");
		callPlayerMethod(player, setMediaTimeMethod, "setMediaTime()", "CLOSED");
		callPlayerMethod(player, getMediaTimeMethod, "getMediaTime()", "CLOSED");
		callPlayerMethod(player, getDurationMethod, "getDuration()", "CLOSED");
		callPlayerMethod(player, getContentTypeMethod, "getContentType()", "CLOSED");
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


	/**	Basic PlayerListener tests.
		Here, the following is tested:
		- the management of the list of player listeners (add/remove)
		- Basic callback
	*/
	public void testPlayerListenerBasic()
		throws Exception
	{
		String	strLocator = "file:/home/matthias/java/tritonus/test/suite/sounds/test.wav";
		Player	player = Manager.createPlayer(strLocator);
		TestPlayerListener	listener = new TestPlayerListener();
		player.prefetch();
		player.addPlayerListener(listener);
		player.start();
		sleep();
		assertTrue("adding of PlayerListener", listener.isCalled());
		player.stop();
		player.removePlayerListener(listener);
		listener.reset();
		player.start();
		sleep();
		assertTrue("removing of PlayerListener", ! listener.isCalled());
	}



	/**	Event PlayerListener tests.
		Here, specific events are tested.

		- correct player argument in callback
	*/
	public void testPlayerListenerEvent()
		throws Exception
	{
		String	strLocator = "file:/home/matthias/java/tritonus/test/suite/sounds/test.wav";
		Player	player = Manager.createPlayer(strLocator);
		TestPlayerListener	listener = new TestPlayerListener();
		Long		mediaTime;
		player.addPlayerListener(listener);
		player.prefetch();

		// STARTED
		player.start();
		sleep();
		assertTrue("STARTED event", listener.isCalled());
		assertEquals("player callback parameter", player, listener.getPlayer());
		checkMediaTimePositive(listener);

		// STOPPED
		player.stop();
		player.removePlayerListener(listener);
		listener.reset();
		player.start();
		sleep();
		assertTrue("STOPPED event", listener.isCalled());
		assertEquals("player callback parameter", player, listener.getPlayer());
		checkMediaTimePositive(listener);

		// CLOSED
		player.stop();
		player.removePlayerListener(listener);
		listener.reset();
		player.start();
		sleep();
		assertTrue("CLOSED event", listener.isCalled());
		assertEquals("player callback parameter", player, listener.getPlayer());
		assertEquals("null event data", null, listener.getEventData());

		// STOPPED AT TIME
		// END OF MEDIA

		// VOLUME CHANGED
		// SIZE CHANGED

		// RECORD STARTED
		// RECORD STOPPED
	}

	private static void checkMediaTimePositive(TestPlayerListener listener)
	{
		Long	mediaTime = (Long) listener.getEventData();
		long	lMediaTime = mediaTime.longValue();
		assertTrue("media time positive", lMediaTime >= 0);
	}


	////////////////////////////////////////////////////////////////////
	//	helper methods


	private static void sleep()
	{
		sleep(SLEEP);
	}


	private static void sleep(int nMilliseconds)
	{
		try
		{
			Thread.sleep(nMilliseconds);
		}
		catch (InterruptedException e)
		{
			// IGNORED
		}
	}


	////////////////////////////////////////////////////////////////////
	//	inner classes



	private static interface TestMethod
	{
		public void callMethod(Player player)
			throws Exception;
	}



	private static class TestPlayerListener
	implements PlayerListener
	{
		private int		m_nCalls;
		private Player		m_player;
		private String		m_strEvent;
		private Object		m_eventData;



		public TestPlayerListener()
		{
			reset();
		}


		public void playerUpdate(Player player,
					 String strEvent,
					 Object eventData)
		{
			m_nCalls++;
			m_player = player;
			m_strEvent = strEvent;
			m_eventData = eventData;
		}



		public boolean isCalled()
		{
			return getCalls() > 0;
		}



		public int getCalls()
		{
			return m_nCalls;
		}



		public Player getPlayer()
		{
			return m_player;
		}


		public String getEvent()
		{
			return m_strEvent;
		}


		public Object getEventData()
		{
			return m_eventData;
		}



		public void reset()
		{
			m_nCalls = 0;
		}
	}
}



/*** PlayerTestCase.java ***/
