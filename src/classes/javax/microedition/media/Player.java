/*
 *	Player.java
 */

package	javax.microedition.media;


/**	TODO:
*/
public interface Player
extends Controllable
{
	/**	TODO:
	*/
	public static final int	UNREALIZED = 100;


	/**	TODO:
	*/
	public static final int	REALIZED = 200;


	/**	TODO:
	*/
	public static final int	PREFETCHED = 300;


	/**	TODO:
	*/
	public static final int	STARTED = 400;


	/**	TODO:
	*/
	public static final int	CLOSED = 0;


	/**	TODO:
	*/
	public static final int	TIME_UNKNOWN = -1;




	/**	TODO:
	*/
	public void realize()
		throws MediaException;


	/**	TODO:
	*/
	public void prefetch()
		throws MediaException;


	/**	TODO:
	*/
	public void start()
		throws MediaException;


	/**	TODO:
	*/
	public void stop()
		throws MediaException;


	/**	TODO:
	*/
	public void deallocate();


	/**	TODO:
	*/
	public void close();


	/**	TODO:
	*/
	public void setTimeBase(TimeBase masterTimeBase)
		throws MediaException;


	/**	TODO:
	*/
	public TimeBase getTimeBase();


	/**	TODO:
	*/
	public long setMediaTime(long lNow)
		throws MediaException;


	/**	TODO:
	*/
	public long getMediaTime();


	/**	TODO:
	*/
	public int getState();


	/**	TODO:
	*/
	public long getDuration();


	/**	TODO:
	*/
	public String getContentType();


	/**	TODO:
	*/
	public void setLoopCount(int nCount);


	/**	TODO:
	*/
	public void addPlayerListener(PlayerListener playerListener);


	/**	TODO:
	*/
	public void removePlayerListener(PlayerListener playerListener);
}



/*** Player.java ***/
