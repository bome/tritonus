/*
 *	AudioPlayer.java
 */

/*
 *  Copyright (c) 2002 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

import java.io.IOException;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;



public class AudioPlayer
{
	public static void main(String[] args)
	{
		String	strLocator = args[0];
		try
		{
			Player	player = Manager.createPlayer(strLocator);
			System.out.println("player: " + player);
			player.start();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (MediaException e)
		{
			e.printStackTrace();
		}
	}
}



/*** AudioPlayer.java ***/
