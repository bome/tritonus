/*
 *	TPlayer.java
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


import	java.util.Enumeration;
import	java.util.Vector;

import	javax.microedition.media.Control;
import	javax.microedition.media.Manager;
import	javax.microedition.media.MediaException;
import	javax.microedition.media.Player;
import	javax.microedition.media.PlayerListener;
import	javax.microedition.media.TimeBase;
import	javax.microedition.media.control.StopTimeControl;
import	javax.microedition.media.control.VolumeControl;
import	javax.microedition.media.protocol.DataSource;



/**	Base class for Player implementation.
*/
public abstract class TPlayer
extends TControllable
implements Player
{
	/**	Default playback rate.
		Since in MMAPI, playback rate is specified in
		'milli-percentage', the value 100000 is equal to 100 %.
	*/
	protected static final int	DEFAULT_RATE = 100000;

	private DataSource		m_dataSource;
	private TimeBase		m_timeBase;
	private Vector			m_listeners;
	private int			m_nState;

	/**	Start time of the player in terms of media time.
		Set by {@link setMediaTime(long) setMediaTime} and
		{@link stop() stop}.
	*/
	private long			m_lMediaStartTime;

	/**	Start time of the player in terms of TimeBase time.
		Set by {@link start() start}.
	*/
	private long			m_lTimeBaseStartTime;
	private EventQueue		m_eventQueue;
	private TStopTimeControl	m_stopTimeControl;



	protected TPlayer(DataSource dataSource)
	{
		this(dataSource,
		     new Control[0]);
	}



	protected TPlayer(DataSource dataSource,
			  Control[] aControls)
	{
		super(aControls);
		m_dataSource = dataSource;
		m_listeners = new Vector();
		m_nState = UNREALIZED;
		m_timeBase = getDefaultTimeBase();
		m_eventQueue = new EventQueue();
		m_stopTimeControl = null;
	}



	// Player methods

	// TODO:
	public void realize()
		throws MediaException
	{
		// The following check can throw an IllegalStateException.
		checkStateNotClosed();

		if (getState() == UNREALIZED)
		{
			try
			{
				doRealize();
			}
			catch (Exception e)
			{
				MediaException	me = new MediaException("can't realize Player");
				// NOTE: The following is a 1.4 construct.
				me.initCause(e);
				throw me;
			}
			setState(REALIZED);
		}
	}


	protected abstract void doRealize()
		throws Exception;


	// TODO:
	public void prefetch()
		throws MediaException
	{
		// The following check can throw an IllegalStateException.
		checkStateNotClosed();

		if (getState() == UNREALIZED)
		{
			realize();
		}
		if (getState() == REALIZED)
		{
			try
			{
				doPrefetch();
			}
			catch (Exception e)
			{
				MediaException	me = new MediaException("can't prefetch Player");
				// NOTE: The following is a 1.4 construct.
				me.initCause(e);
				throw me;
			}
			setState(PREFETCHED);
		}
	}


	protected abstract void doPrefetch()
		throws Exception;


	// TODO:
	public void start()
		throws MediaException
	{
		// The following check can throw an IllegalStateException.
		checkStateNotClosed();

		if (getState() == UNREALIZED || getState() == REALIZED)
		{
			prefetch();
		}
		if (getState() == PREFETCHED)
		{
			setState(STARTED);
			try
			{
				m_lTimeBaseStartTime = getTimeBaseTime();
				doStart();
				postStartedEvent();
				if (getStopTimeControl() != null)
				{
					getStopTimeControl().init();
				}
			}
			catch (Exception e)
			{
				MediaException	me = new MediaException("can't start Player");
				// NOTE: The following is a 1.4 construct.
				me.initCause(e);
				throw me;
			}
		}
	}


	protected abstract void doStart()
		throws Exception;


	// TODO:
	public void stop()
	{
		stop(false);
	}


	/**
	   @param bAtTime If true, a STOPPED_AT_TIME event is posted. Otherwise,
	   a STOPPED event is posted.
	*/
	protected void stop(boolean bAtTime)
	{
		// The following check can throw an IllegalStateException.
		checkStateNotClosed();

		if (getState() == STARTED)
		{
			try
			{
				doStop();
			}
			catch (Exception e)
			{
				// IGNORED
			}
			// we need this if we want to start again.
			m_lMediaStartTime = calculateCurrentMediaTime();
			setState(PREFETCHED);
			postStoppedEvent(bAtTime);
		}
	}



	protected abstract void doStop()
		throws Exception;



	// TODO:
	public void deallocate()
	{
		// The following check can throw an IllegalStateException.
		checkStateNotClosed();


		if (getState() == PREFETCHED)
		{
			try
			{
				doDeallocate();
			}
			catch (Exception e)
			{
				// IGNORED
			}
			setState(REALIZED);
		}
		// TODO: implement interrupting of realize()
	}



	protected abstract void doDeallocate()
		throws Exception;



	public void close()
	{
		switch (getState())
		{
		case STARTED:
			try
			{
				doStop();
			}
			catch (Exception e)
			{
				// IGNORED
			}
			// FALL THROUGH

		case PREFETCHED:
			try
			{
				doDeallocate();
			}
			catch (Exception e)
			{
				// IGNORED
			}
			// FALL THROUGH

		case REALIZED:
			// TODO:
			// doDeallocate2();
			// FALL THROUGH

		case UNREALIZED:
			try
			{
				doClose();
			}
			catch (Exception e)
			{
				// IGNORED
			}
			setState(CLOSED);
			postClosedEvent();
		}
	}


	protected abstract void doClose()
		throws Exception;


	/**	Set TimeBase for this player.
		This implementation checks for a legal state to set the TimeBase
		and sets the passed TimeBase. If null is passed, a value
		returned by {@link #getDefaultTimeBase() getDefaultTimeBase()}
		is used to set the TimeBase. No further restrictions are enforced.
		If additional restrictions are needed, subclasses should override
		this method (not really a good solution, but for now).

		@see #getDefaultTimeBase()
	*/
	public synchronized void setTimeBase(TimeBase timeBase)
		throws MediaException
	{
		// The following call can throw an IllegalStateException.
		checkStateRealizedNotStarted();

		if (timeBase == null)
		{
			timeBase = getDefaultTimeBase();
		}
		m_timeBase = timeBase;
	}



	public synchronized TimeBase getTimeBase()
	{
		// The following call can throw an IllegalStateException.
		checkStateRealized();

		return m_timeBase;
	}


	/**	Get the current TimeBase time.
		Just a convenience method.

		@return the time of this player's TimeBase in microseconds.
	*/
	protected long getTimeBaseTime()
	{
		return getTimeBase().getTime();
	}


	/**	TODO:
		From within this method, doSetMediaTime(long) is called.
		@throws IllegalStateException as specified by the contract.
		@throws MediaException in one of two cases: a) during setting
		the media time, the player is restarted and either start()
		or stop() throws a MediaException b) if doSetMediaTime
		throws an Exception.
	*/
	public synchronized long setMediaTime(long lMediaTime)
		throws MediaException
	{
		boolean	bRestarting = false;
		// The following check can throw an IllegalStateException.
		checkStateRealized();

		if (getState() == STARTED && getRestartingOnSetMediaTime())
		{
			stop();
			bRestarting = true;
		}

		lMediaTime = Math.max(0, lMediaTime);
		long	lDuration = getDuration();
		if (lDuration != TIME_UNKNOWN)
		{
			lMediaTime = Math.min(lDuration, lMediaTime);
		}

		m_lMediaStartTime = lMediaTime;

		try
		{
			doSetMediaTime(lMediaTime);
		}
		catch (Exception e)
		{
			MediaException	me = new MediaException("can't start Player");
			// NOTE: The following is a 1.4 construct.
			me.initCause(e);
			throw me;
		}

		if (bRestarting)
		{
			start();
		}
		return lMediaTime;
	}


	/**	Returns if the player should be restated on setMediaTime().
		If this is true, stop() is called from setMediaTime()
		prior to actually setting the time. Afterwards, start() is
		called.

		The default behaviour is to do this, since this class'
		implementation of the method always returns true.
		If a subclass doesn't need this behaviour, it
		can override this method to return false.

		Currently, this behaviour is enforced for all players
		derived from this class (note that this method can't be
		overridden because it is final). This is because the effect
		of not doing so has not been checked.
	*/
	protected final boolean getRestartingOnSetMediaTime()
	{
		return true;
	}



	protected abstract void doSetMediaTime(long lMediaTime)
		throws Exception;



	/*	TODO:
	 */
	public long getMediaTime()
	{
		// The following check can throw an IllegalStateException.
		checkStateNotClosed();

		return getMediaTimeImpl();
	}


	/*	TODO:
	 */
	protected long getMediaTimeImpl()
	{
		long	lMediaTime;

		if (getState() == STARTED)
		{
			lMediaTime = calculateCurrentMediaTime();
		}
		else
		{
			lMediaTime = m_lMediaStartTime;
		}
		return lMediaTime;
	}


	/**	calculate the current media time.
		This method assumes that the player is started.
	*/
	private long calculateCurrentMediaTime()
	{
		long	lTimeBaseNow = getTimeBaseTime();
		long	lMediaTime = (lTimeBaseNow - m_lTimeBaseStartTime);
		lMediaTime = (lMediaTime * getRate()) / DEFAULT_RATE;
		lMediaTime += m_lMediaStartTime;
		return lMediaTime;
	}


	/**	Get the playback rate of the player.
		See RateControl.
		Currently, always returns .
	 */
	private int getRate()
	{
		return DEFAULT_RATE;
	}



	// TODO:
	public int getState()
	{
		return m_nState;
	}


	// TODO:
	public long getDuration()
	{
		checkStateNotClosed();
		return -1;
	}



	/**	TODO:
	 */
	public String getContentType()
	{
		checkStateRealized();
		DataSource	dataSource = getDataSource();
		return dataSource.getContentType();
	}



	public void setLoopCount(int nCount)
	{
		checkStateNotStartedNotClosed();
		// TODO:
	}



	public void addPlayerListener(PlayerListener playerListener)
	{
		synchronized (m_listeners)
		{
			if (! m_listeners.contains(playerListener))
			{
				m_listeners.add(playerListener);
			}
		}
	}



	public void removePlayerListener(PlayerListener playerListener)
	{
		synchronized (m_listeners)
		{
			m_listeners.remove(playerListener);
		}
	}



	protected void postEvent(String strEvent,
				 Object eventData)
	{
 		m_eventQueue.addEvent(strEvent,
				      eventData);
	}



	protected void postMediaTimeEvent(String strEvent)
	{
		long	lMediaTime = getMediaTimeImpl();
		postEvent(strEvent, new Long(lMediaTime));
	}



	protected void postStartedEvent()
	{
		// TODO: reconsider if there is a member startTime
		postMediaTimeEvent(PlayerListener.STARTED);
	}



	protected void postStoppedEvent(boolean bAtTime)
	{
		String	strEvent = bAtTime ? PlayerListener.STOPPED_AT_TIME : PlayerListener.STOPPED;
		postMediaTimeEvent(strEvent);
	}



	protected void postEndOfMediaEvent()
	{
		postMediaTimeEvent(PlayerListener.END_OF_MEDIA);
	}



	protected void postDurationUpdatedEvent()
	{
		long	lDuration = getDuration();
		postEvent(PlayerListener.DURATION_UPDATED, new Long(lDuration));
	}



	protected void postDeviceAvailableEvent(boolean bAvailable,
						String strDevice)
	{
		String	strEvent = bAvailable ? PlayerListener.DEVICE_AVAILABLE : PlayerListener.DEVICE_UNAVAILABLE;
		postEvent(strEvent, strDevice);
	}



	protected void postControlChangedEvent(Control control)
	{
		String	strEvent = (control instanceof VolumeControl) ?
			PlayerListener.VOLUME_CHANGED :
			PlayerListener.SIZE_CHANGED;
		postEvent(strEvent, control);
	}



	protected void postErrorEvent(String strMessage)
	{
		postEvent(PlayerListener.ERROR, strMessage);
	}



	protected void postClosedEvent()
	{
		postEvent(PlayerListener.CLOSED, null);
	}



	protected void postRecordEvent(boolean bStarted)
	{
		String	strEvent = bStarted ?
			PlayerListener.RECORD_STARTED :
			PlayerListener.RECORD_STOPPED;
		postMediaTimeEvent(strEvent);
	}



	protected void postRecordErrorEvent(String strMessage)
	{
		postEvent(PlayerListener.RECORD_ERROR, strMessage);
	}



	protected void postBufferingEvent(boolean bStarted)
	{
		String	strEvent = bStarted ?
			PlayerListener.BUFFERING_STARTED :
			PlayerListener.BUFFERING_STOPPED;
		postMediaTimeEvent(strEvent);
	}



	protected DataSource getDataSource()
	{
		return m_dataSource;
	}



	protected TimeBase getDefaultTimeBase()
	{
		return Manager.getSystemTimeBase();
	}


	protected boolean isStateLegalForControls()
	{
		int	nState = getState();
		return nState != UNREALIZED && nState != CLOSED;
	}



	private void setState(int nState)
	{
		m_nState = nState;
	}


	/**	Checks if the state is REALIZED, PREFETCHED or STARTED.
		If this condition is not met (states UNREALIZED or CLOSED),
		an IllegalStateException is throw.

		@throws IllegalStateException Thrown if the state is UNREALIZED
		or CLOSED.
	*/
	private void checkStateRealized()
	{
		int	nState = getState();
		if (nState == UNREALIZED || nState == CLOSED)
		{
			throw new IllegalStateException("state is UNREALIZED or CLOSED. Required state is REALIZED, PREFETCHED or STARTED.");
		}
	}



	/**	Checks if the state is REALIZED or PREFETCHED.
		If this condition is not met (states UNREALIZED, STARTED or CLOSED),
		an IllegalStateException is throw.

		@throws IllegalStateException Thrown if the state is UNREALIZED,
		STARTED or CLOSED.
	*/
	private void checkStateRealizedNotStarted()
	{
		int	nState = getState();
		if (nState == UNREALIZED || nState == STARTED || nState == CLOSED)
		{
			throw new IllegalStateException("state is UNREALIZED, STARTED or CLOSED. Required state is REALIZED or PREFETCHED STARTED.");
		}
	}



	/**	Checks if the state is UNREALIZED, REALIZED, PREFETCHED or STARTED.
		If this condition is not met (state CLOSED),
		an IllegalStateException is throw.

		@throws IllegalStateException Thrown if the state is CLOSED.
	*/
	private void checkStateNotStartedNotClosed()
	{
		int	nState = getState();
		if (nState == STARTED || nState == CLOSED)
		{
			throw new IllegalStateException("state is STARTED or CLOSED. Required state is UNREALIZED, REALIZED or PREFETCHED.");
		}
	}



	/**	Checks if the state is UNREALIZED, REALIZED, PREFETCHED or STARTED.
		If this condition is not met (state CLOSED),
		an IllegalStateException is throw.

		@throws IllegalStateException Thrown if the state is CLOSED.
	*/
	private void checkStateNotClosed()
	{
		int	nState = getState();
		if (nState == CLOSED)
		{
			throw new IllegalStateException("state is CLOSED. Required state is UNREALIZED, REALIZED, PREFETCHED or STARTED.");
		}
	}



	private TStopTimeControl getStopTimeControl()
	{
		return m_stopTimeControl;
	}



	private void setStopTimeControl(TStopTimeControl stopTimeControl)
	{
		m_stopTimeControl = stopTimeControl;
	}



	///////////////////////////////////////////////////////////////////////
	//
	//	inner classes
	//
	///////////////////////////////////////////////////////////////////////




	private class EventQueue
	extends Thread
	{
		private Vector		m_events;


		public EventQueue()
		{
			m_events = new Vector();
			setDaemon(true);
			start();
		}



		public void addEvent(String strEvent, Object eventData)
		{
			EventRecord	eventRecord = new EventRecord(strEvent, eventData);
			synchronized (this)
			{
				m_events.addElement(eventRecord);
				notify();
			}
		}



		public void run()
		{
			while (true)
			{
				synchronized (this)
				{
					while (m_events.size() == 0)
					{
						try
						{
							wait();
						}
						catch (InterruptedException e)
						{
							// IGNORED
						}
					}
					for (int i = 0; i < m_events.size(); i++)
					{
						EventRecord	eventRecord = (EventRecord) m_events.elementAt(0);
						m_events.removeElementAt(0);
						deliver(eventRecord);
					}
				}
			}
		}



		private void deliver(EventRecord eventRecord)
		{
			Vector	listeners = null;
			synchronized (TPlayer.this.m_listeners)
			{
				listeners = (Vector) TPlayer.this.m_listeners.clone();
			}
			Enumeration	enum = listeners.elements();
			while (enum.hasMoreElements())
			{
				PlayerListener	listener = (PlayerListener) enum.nextElement();
				listener.playerUpdate(TPlayer.this,
						      eventRecord.getEvent(),
						      eventRecord.getEventData());
			}

		}
	}


	/**	Internal storage object for EventQueue.
		Instances of this class form the entries
		in the EventQueue. The objects store the
		(type of) event and the event data.
	*/
	private /*static*/ class EventRecord
	{
		private String		m_strEvent;
		private Object		m_eventData;


		public EventRecord(String strEvent,
				   Object eventData)
		{
			m_strEvent = strEvent;
			m_eventData = eventData;
		}


		public String getEvent()
		{
			return m_strEvent;
		}


		public Object getEventData()
		{
			return m_eventData;
		}
	}


	/**	StopTimeControl for TPlayer.
		The basic idea is to have a thread that sleeps until the desired
		stop time and then calls the normal stop() method.
	*/
	protected class TStopTimeControl
	implements StopTimeControl, Runnable
	{
		private long		m_lStopTime;
		private Thread		m_thread;


		public TStopTimeControl()
		{
			m_lStopTime = RESET;
		}



		/**
		   cases:

		*/
		public void setStopTime(long lStopTime)
		{
			if (getStopTime() != RESET &&
			    TPlayer.this.getState() == STARTED)
			{
				throw new IllegalStateException("already set stop time cannot be changed on a started player");
			}
			// the following is from the specification of Player.setMediaTime(long)
			// TODO: open specification bug, mail to Florian sent
			if (lStopTime < 0)
			{
				lStopTime = 0;
			}

			m_lStopTime = lStopTime;
			if (lStopTime == RESET)
			{
				TPlayer.this.setStopTimeControl(null);
				if (TPlayer.this.getState() == STARTED)
				{
					if (m_thread != null)
					{
						m_thread.interrupt();
						m_thread = null;
					}
				}
			}
			else
			{
				TPlayer.this.setStopTimeControl(this);
				if (TPlayer.this.getState() == STARTED)
				{
					init();
				}
			}

		}



		public long getStopTime()
		{
			return m_lStopTime;
		}



		private void init()
		{
			long	lMediaTime = TPlayer.this.getMediaTime();
			if (lMediaTime >= getStopTime())
			{
				this.stop();
			}
			else
			{
				m_thread = new Thread(this);
				m_thread.start();
			}
		}


		/**
		   NOTE: the current implementation only works correctely if
		   the rate is 1.0. This has to be reworked in the future.
		*/
		public void run()
		{
			/*	If Thread.sleep() throws an InterruptedException,
				the interrupted state of the thread is reset.
				So to get the interrupted condition, we have to
				track it separately.
			*/
			boolean	bInterrupted = false;

			// these values are in microseconds
			long	lMediaTime = TPlayer.this.getMediaTime();
			long	lRemainingMediaTime = getStopTime() - lMediaTime;
			// the following is in milliseconds
			long	lTimeToSleep = lRemainingMediaTime / 1000;
			try
			{
				Thread.sleep(lTimeToSleep);
			}
			catch (InterruptedException e)
			{
				bInterrupted = true;
			}
			if (! m_thread.isInterrupted() || bInterrupted)
			{
				stop();
			}
		}


		private void stop()
		{
			TPlayer.this.stop(true);
			m_lStopTime = RESET;
		}
	}
}



/*** TPlayer.java ***/
