/*
 *	PlayerListener.java
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
	public static final String	RECORD_ERROR = "recordError";


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
