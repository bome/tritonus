/*
 *	TPlayer.java
 */

package	org.tritonus.mmapi;


import	java.util.Vector;

import	javax.microedition.media.Control;
import	javax.microedition.media.MediaException;
import	javax.microedition.media.Player;
import	javax.microedition.media.PlayerListener;
import	javax.microedition.media.TimeBase;



public abstract class TPlayer
extends TControllable
implements Player
{
	private TimeBase	m_timeBase;
	private Vector		m_listeners;
	private int		m_nState;



	protected TPlayer()
	{
		this(new Control[0]);
	}



	protected TPlayer(Control[] aControls)
	{
		super(aControls);
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
	}



	// TODO:
	public void prefetch()
		throws MediaException
	{
		// The following check can throw an IllegalStateException.
		checkStateNotClosed();
	}



	// TODO:
	public void start()
		throws MediaException
	{
		// The following check can throw an IllegalStateException.
		checkStateNotClosed();
	}



	// TODO:
	public void stop()
	{
		// The following check can throw an IllegalStateException.
		checkStateNotClosed();
	}



	// TODO:
	public void deallocate()
	{
		// The following check can throw an IllegalStateException.
		checkStateNotClosed();
	}



	// TODO:
	public void close()
	{
		// The following check can throw an IllegalStateException.
		checkStateRealized();
	}



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
		return -1;
	}



	public void setLoopCount(int nCount)
	{
		// TODO:
	}



	public void addPlayerListener(PlayerListener playerListener)
	{
		if (! m_listeners.contains(playerListener))
		{
			m_listeners.add(playerListener);
		}
	}



	public void removePlayerListener(PlayerListener playerListener)
	{
		m_listeners.remove(playerListener);
	}


	/**	TODO:
	 */
	public String getContentType()
	{
		// TODO:
		return null;
	}



	protected abstract TimeBase getDefaultTimeBase();


	protected boolean isStateLegalForControls()
	{
		int	nState = getState();
		return nState != UNREALIZED && nState != CLOSED;
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
