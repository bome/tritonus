/*
 *	PlayerListener.java
 */

package	javax.microedition.media;



public interface PlayerListener
{
	public static final int	STARTED = 0;
	public static final int	STOPPED = 1;
	public static final int	STOPPED_AT_TIME = 2;
	public static final int	END_OF_MEDIA = 3;
	public static final int	DURATION_UPDATED = 4;
	public static final int	AUDIO_DEVICE_UNAVAILABLE = 5;
	public static final int	VOLUME_CHANGED = 6;
	public static final int	SIZE_CHANGED = 7;
	public static final int	ERROR = 8;
	public static final int	CLOSED = 9;
	public static final int	RECORD_STARTED = 10;
	public static final int	RECORD_STOPPED = 11;
	public static final int	PRIVATE_DATA_AVAILABLE = 12;
	public static final int	PRIVATE_DATA_OVERWRITTEN = 13;
	public static final int	BUFFERING_STARTED = 14;
	public static final int	BUFFERING_STOPPED = 15;

	public void playerUpdate(Player player, int nEvent, Object eventData);
}



/*** PlayerListener.java ***/
