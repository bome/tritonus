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
	public static final String	STARTED = "started";


	/**	TODO:
	*/
	public static final String	STOPPED = "stopped";


	/**	TODO:
	*/
	public static final String	STOPPED_AT_TIME = "stoppedAtTime";


	/**	TODO:
	*/
	public static final String	END_OF_MEDIA = "endOfMedia";


	/**	TODO:
	*/
	public static final String	DURATION_UPDATED = "durationUpdated";


	/**	TODO:
	*/
	public static final String	DEVICE_UNAVAILABLE = "deviceUnavailable";


	/**	TODO:
	*/
	public static final String	DEVICE_AVAILABLE = "deviceAvailable";


	/**	TODO:
	*/
	public static final String	VOLUME_CHANGED = "volumeChanged";


	/**	TODO:
	*/
	public static final String	SIZE_CHANGED = "sizeChanged";


	/**	TODO:
	*/
	public static final String	ERROR = "error";


	/**	TODO:
	*/
	public static final String	CLOSED = "closed";


	/**	TODO:
	*/
	public static final String	RECORD_STARTED = "recordStarted";


	/**	TODO:
	*/
	public static final String	RECORD_STOPPED = "recordStopped";


	/**	TODO:
	*/
	public static final String	RECORD_ERROR = "ercordError";


	/**	TODO:
	*/
	public static final String	BUFFERING_STARTED = "bufferingStarted";


	/**	TODO:
	*/
	public static final String	BUFFERING_STOPPED = "bufferingStopped";




	/**	TODO:
	*/
	public void playerUpdate(Player player, String event, Object eventData);
}



/*** PlayerListener.java ***/
