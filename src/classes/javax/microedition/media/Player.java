/*
 *	Player.java
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
