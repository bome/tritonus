/*
 *	PlayerListener.java
 */

package	javax.microedition.media;


/**	TODO:
*/
public interface PlayerListener
{
	/**	TODO:
	*/
	public static final int	STARTED = 0;


	/**	TODO:
	*/
	public static final int	STOPPED = 1;


	/**	TODO:
	*/
	public static final int	STOPPED_AT_TIME = 2;


	/**	TODO:
	*/
	public static final int	END_OF_MEDIA = 3;


	/**	TODO:
	*/
	public static final int	DURATION_UPDATED = 4;


	/**	TODO:
	*/
	public static final int	DEVICE_UNAVAILABLE = 5;


	/**	TODO:
	*/
	public static final int	DEVICE_AVAILABLE = 6;


	/**	TODO:
	*/
	public static final int	VOLUME_CHANGED = 7;


	/**	TODO:
	*/
	public static final int	SIZE_CHANGED = 8;


	/**	TODO:
	*/
	public static final int	ERROR = 9;


	/**	TODO:
	*/
	public static final int	CLOSED = 10;


	/**	TODO:
	*/
	public static final int	RECORD_STARTED = 11;


	/**	TODO:
	*/
	public static final int	RECORD_STOPPED = 12;


	/**	TODO:
	*/
	public static final int	RECORD_ERROR = 13;


	/**	TODO:
	*/
	public static final int	BUFFERING_STARTED = 14;


	/**	TODO:
	*/
	public static final int	BUFFERING_STOPPED = 15;




	/**	TODO:
	*/
	public void playerUpdate(Player player, int nEvent, Object eventData);
}



/*** PlayerListener.java ***/
