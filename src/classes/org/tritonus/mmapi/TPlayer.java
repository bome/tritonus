/*
 *	TPlayer.java
 */

package	org.tritonus.mmapi;


import	java.util.Vector;

import	javax.microedition.media.Control;
import	javax.microedition.media.Manager;
import	javax.microedition.media.MediaException;
import	javax.microedition.media.Player;
import	javax.microedition.media.PlayerListener;
import	javax.microedition.media.TimeBase;
import	javax.microedition.media.protocol.DataSource;



public abstract class TPlayer
extends TControllable
implements Player
{
	private DataSource	m_dataSource;
	private TimeBase	m_timeBase;
	private Vector		m_listeners;
	private int		m_nState;



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
			doRealize();
			setState(REALIZED);
		}
	}


	protected abstract void doRealize();


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
			doPrefetch();
			setState(PREFETCHED);
		}
	}


	protected abstract void doPrefetch();


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
			doStart();
		}
	}


	protected abstract void doStart();


	// TODO:
	public void stop()
	{
		// The following check can throw an IllegalStateException.
		checkStateNotClosed();

		if (getState() == STARTED)
		{
			doStop();
			setState(PREFETCHED);
		}
	}


	protected abstract void doStop();


	// TODO:
	public void deallocate()
	{
		// The following check can throw an IllegalStateException.
		checkStateNotClosed();


		if (getState() == PREFETCHED)
		{
			doDeallocate();
			setState(REALIZED);
		}
		// TODO: implement interrupting of realize()
	}


	protected abstract void doDeallocate();


	public void close()
	{
		switch (getState())
		{
		case STARTED:
			doStop();
			// FALL THROUGH

		case PREFETCHED:
			doDeallocate();
			// FALL THROUGH

		case REALIZED:
			// TODO:
			// doDeallocate2();
			// FALL THROUGH

		case UNREALIZED:
			doClose();
			setState(CLOSED);
		}
	}


	protected abstract void doClose();


	/**	Set TimeBase for this player.
		This implementation checks for a legal state to set the TimeBase
		and sets the passed TimeBase. If null is passed, a value
		returned by {@link #getDefaultTimeBase() getDefaultTimeBase()}
		is used to set the TimeBase. No further restrictions are enforced.
		If additional restrictions are needed, subclasses should override
		this method (not really a good solution, but for now).

		@see #getDefaultTimeBase()
	*/
	public void setTimeBase(TimeBase timeBase)
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



	public TimeBase getTimeBase()
	{
		// The following call can throw an IllegalStateException.
		checkStateRealized();

		return m_timeBase;
	}



	// TODO:
	public long setMediaTime(long lNow)
	{
		// The following check can throw an IllegalStateException.
		checkStateRealized();
		return -1;
	}



	// TODO:
	public long getMediaTime()
	{
		// The following check can throw an IllegalStateException.
		checkStateNotClosed();
		return -1;
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
		// TODO: sent event
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
}



/*** TPlayer.java ***/
