/*
 *	ToneControl.java
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

package	javax.microedition.media.control;

import javax.microedition.media.Control;


/**	TODO:
*/
public interface ToneControl
extends Control
{
	/**	TODO:
	*/
	public static final byte	VERSION = -2;


	/**	TODO:
	*/
	public static final byte	TEMPO = -3;


	/**	TODO:
	*/
	public static final byte	RESOLUTION = -4;


	/**	TODO:
	*/
	public static final byte	BLOCK_START = -5;


	/**	TODO:
	*/
	public static final byte	BLOCK_END = -6;


	/**	TODO:
	*/
	public static final byte	PLAY_BLOCK = -7;


	/**	TODO:
	*/
	public static final byte	SET_VOLUME = -8;


	/**	TODO:
	*/
	public static final byte	REPEAT = -9;


	/**	TODO:
	*/
	public static final byte	C4 = 60;


	/**	TODO:
	*/
	public static final byte	SILENCE = -1;


	/**	TODO:
	*/
	public void setSequence(byte[] abSequence);
}



/*** ToneControl.java ***/
