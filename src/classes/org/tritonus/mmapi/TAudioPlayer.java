/*
 *	TAudioPlayer.java
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

package	org.tritonus.mmapi;


import	javax.microedition.media.Control;
import	javax.microedition.media.MediaException;
import	javax.microedition.media.Player;
import	javax.microedition.media.PlayerListener;
import	javax.microedition.media.TimeBase;
import javax.microedition.media.protocol.DataSource;



public class TAudioPlayer
extends TPlayer
{
	public TAudioPlayer(DataSource source)
	{
		super(source);
	}



	protected void doRealize()
		throws Exception
	{
	}



	// TODO:
	protected void doPrefetch()
		throws Exception
	{
	}



	protected void doStart()
		throws Exception
	{
	}



	protected void doStop()
		throws Exception
	{
	}



	protected void doDeallocate()
		throws Exception
	{
	}



	protected void doClose()
		throws Exception
	{
	}
}



/*** TAudioPlayer.java ***/
