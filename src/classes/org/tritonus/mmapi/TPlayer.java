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



	public TPlayer()
	{
		this(new Control[0]);
	}


	public TPlayer(Control[] aControls)
	{
		super(aControls);
		m_listeners = new Vector();
		m_nState = UNREALIZED;
	}



	// Player methods

	// TODO:
	public void realize()
		throws MediaException
	{
	}



	// TODO:
	public void prefetch()
		throws MediaException
	{
	}



	// TODO:
	public void start()
		throws MediaException
	{
	}



	// TODO:
	public void stop()
	{
	}



	// TODO:
	public void deallocate()
	{
	}



	// TODO:
	public void close()
	{
	}



	/*
	  As some kind of default behaviour, setting the TimeBase is
	  not allowed. If you want to change this, you need to
	  override this method.
	 */
	public void setTimeBase(TimeBase masterTimeBase)
		throws MediaException
	{
		throw new MediaException("setting TimeBase for this player is not possible");
	}



	public TimeBase getTimeBase()
	{
		return m_timeBase;
	}



	// TODO:
	public long setMediaTime(long lNow)
	{
		return -1;
	}



	// TODO:
	public long getMediaTime()
	{
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


	protected boolean isStateLegalForControls()
	{
		int	nState = getState();
		return nState != UNREALIZED && nState != CLOSED;
	}
}



/*** TPlayer.java ***/
