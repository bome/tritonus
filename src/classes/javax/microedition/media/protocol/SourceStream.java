/*
 *	SourceStream.java
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

package	javax.microedition.media.protocol;


import	java.io.IOException;
import	javax.microedition.media.Controllable;


/**	TODO:
*/
public interface SourceStream
extends Controllable
{
	/**	TODO:
	*/
	public static final int	NOT_SEEKABLE = 0;


	/**	TODO:
	*/
	public static final int	SEEKABLE_TO_START = 1;


	/**	TODO:
	*/
	public static final int	RANDOM_ACCESSIBLE = 2;




	/**	TODO:
	*/
	public ContentDescriptor getContentDescriptor();


	/**	TODO:
	*/
	public long getContentLength();


	/**	TODO:
	*/
	public int read(byte[] abData, int nOffset, int nLength)
		throws IOException;


	/**	TODO:
	*/
	public int getTransferSize();


	/**	TODO:
	*/
	public long seek(long lPosition)
		throws IOException;


	/**	TODO:
	*/
	public long tell();


	/**	TODO:
	*/
	public int getSeekType();
}



/*** SourceStream.java ***/
