/*
 *	ClipPlayer.java
 */

/*
 *  Copyright (c) 1999 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
 *
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
 *
 */


import	java.io.File;
import	java.io.IOException;

import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioInputStream;
import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.Clip;
import	javax.sound.sampled.DataLine;
import	javax.sound.sampled.LineEvent;
import	javax.sound.sampled.LineListener;
import	javax.sound.sampled.LineUnavailableException;



public class ClipPlayer
	implements	LineListener
{
	private AudioInputStream	m_audioInputStream;
	private AudioFormat		m_format;
	private Clip		m_clip;



	public ClipPlayer(File clipFile, int nLoopCount)
	{
		try
		{
			m_audioInputStream = AudioSystem.getAudioInputStream(clipFile);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (m_audioInputStream != null)
		{
			m_format = m_audioInputStream.getFormat();
			DataLine.Info	info = new DataLine.Info(Clip.class, m_format, AudioSystem.NOT_SPECIFIED);
			try
			{
				m_clip = (Clip) AudioSystem.getLine(info);
				m_clip.addLineListener(this);
				m_clip.open(m_audioInputStream);
			}
			catch (LineUnavailableException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			m_clip.loop(nLoopCount);
		}
		else
		{
			System.out.println("ClipPlayer.<init>(): can't get data from file " + clipFile.getName());
		}
	}



	public void update(LineEvent event)
	{
		System.out.println("ClipPlayer.update(): received event: " + event);
		if (event.getType().equals(LineEvent.Type.STOP))
		{
			m_clip.close();
		}
		else if (event.getType().equals(LineEvent.Type.CLOSE))
		{
			System.out.println("There is a bug in JavaSound 0.90 (jdk1.3beta).");
			System.out.println("You have to terminate the program manually.");
			System.out.println("Press Control-C.");
		}

	}



	public static void main(String[] args)
	{
		if (args.length != 2)
		{
			System.out.println("ClipPlayer: usage:");
			System.out.println("\tjava ClipPlayer <soundfile> <#loops>");
		}
		else
		{
			File	clipFile = new File(args[0]);
			int		nLoopCount = Integer.parseInt(args[1]);
			ClipPlayer	clipPlayer = new ClipPlayer(clipFile, nLoopCount);
			while (true)
			{
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
				}
			}
		}
	}
}



/*** ClipPlayer.java ***/
