/*
 *	Player.java
 */

package	javax.microedition.media;



public interface Player
extends Controllable
{
	public static final int	UNREALIZED = 100;
	public static final int	REALIZED = 200;
	public static final int	PREFETCHED = 300;
	public static final int	STARTED = 400;
	public static final int	TIME_UNKNOWN = -1;


	public void realize()
		throws MediaException;
	public void prefetch()
		throws MediaException;
	public void start()
		throws MediaException;
	public void stop();
	public void deallocate();
	public void close();
	public void setTimeBase(TimeBase masterTimeBase)
		throws MediaException;
	public TimeBase getTimeBase();
	public long setMediaTime(long lNow);
	public long getMediaTime();
	public int getState();
	public long getDuration();
	public void setLoopCount(int nCount)
		throws MediaException;
	public void addPlayerListener(PlayerListener playerListener);
	public void removePlayerListener(PlayerListener playerListener);
}



/*** Player.java ***/
