/*
 *	Player.java
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
import	java.io.FileInputStream;
import	java.io.IOException;
import	java.io.FileNotFoundException;

import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioInputStream;
import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.Mixer;
import	javax.sound.sampled.LineUnavailableException;
import	javax.sound.sampled.SourceDataLine;
import	javax.sound.sampled.DataLine;
import	javax.sound.sampled.Line;
import	javax.sound.sampled.LineListener;
import	javax.sound.sampled.LineEvent;



public class Player
	extends		Thread
	implements	LineListener
{
	private static final int	EXTERNAL_BUFFER_SIZE = 44000 * 4;
	private AudioInputStream	m_audioStream;
	private byte[]			m_data;
	private AudioFormat		m_format;



	public Player(String sFilename)
	{
		super("player thread");
		// dumpThreads();
		// org.gnu.tritonus.TDebug.TraceMixer = true;
		// org.gnu.tritonus.TDebug.TraceOutputChannel = true;
		m_data = new byte[EXTERNAL_BUFFER_SIZE];
		try
		{
			m_audioStream = AudioSystem.getAudioInputStream(new File (sFilename));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (m_audioStream == null)
		{
			System.out.println("###  m_audioStream == null");
		}
		m_format = m_audioStream.getFormat();
		System.out.println("Player: end of contructor");
	}



	public void run()
	{
		System.out.println("Player.run(): called");
		// dumpThreads();
		// org.gnu.tritonus.TDebug.TraceOutputChannel = true;
		Mixer	mixer = AudioSystem.getMixer(null);
		// System.out.println("Player.run(): after getMixer()");
		// dumpThreads();
		System.out.println("Player.run(): got Mixer: " + mixer);
		SourceDataLine	line = null;
		DataLine.Info	info = new DataLine.Info(SourceDataLine.class, m_format, AudioSystem.NOT_SPECIFIED);
		try
		{
			line = (SourceDataLine) mixer.getLine(info);
		}
		catch (LineUnavailableException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		line.addLineListener(this);
		try
		{
			line.open(m_format, line.getBufferSize());
		}
		catch (LineUnavailableException e)
		{
			e.printStackTrace();
		}
		line.start();
		int	nRead = 0;
		while (nRead != -1)
		{
			try
			{
				nRead = m_audioStream.read(m_data, 0, m_data.length);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			// int	nFramesToWrite = nRead;
			if (nRead >= 0)
			{
				System.out.println("Player.run(): in loop, trying to write: " + nRead);
				int	nWritten = line.write(m_data, 0, nRead);
				System.out.println("Player.run(): written: " + nWritten);
			}
		}
		// dumpThreads();
		System.out.println("after write loop");
		line.close();
		/*
		  dumpThreads();
		*/
	}



	public void update(LineEvent event)
	{
		System.out.println("%% Received: " + event);
	}



	public static void main(String[] args)
	{
		if (args.length != 1)
		{
			System.out.println("Player: usage:");
			System.out.println("\tjava Player <soundfile>");
		}
		else
		{
			// dumpThreads();
			Thread	thread = new Player(args[0]);
			// dumpThreads();
			thread.start();
		}
	}



	public static void dumpThreads()
	{
		System.out.println("--------------------------");
		ThreadGroup	group = Thread.currentThread().getThreadGroup();
		while (group.getParent() != null)
		{
			group = group.getParent();
		}
		Thread[]	threads = new Thread[20];
		int		nThreads = group.enumerate(threads);
		for (int i = 0; i < nThreads; i++)
		{
			System.out.println("Thread: " + threads[i] + " daemon: " + threads[i].isDaemon());
		}
		System.out.println("--------------------------");
	}
}



/*** Player.java ***/
