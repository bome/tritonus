/*
 *	JavaSoundAudioPlayer.java
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

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import javax.microedition.media.MediaException;
import javax.microedition.media.protocol.DataSource;
import javax.microedition.media.protocol.SourceStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.tritonus.mmapi.URLDataSource.URLSourceStream;

/*
  TODO (general):
  - this player
  - event delivery
  - event generation
  - StopTime, VolumeControl
  - realize()/deallocate()
  - properties
  - rework playTone()
  - test InputStreamDataSurce
  - more player tests
 */

/**
*/
public class JavaSoundAudioPlayer
extends TAudioPlayer
implements Runnable
{
	private static final int	BUFFER_SIZE = 128000;

	private AudioInputStream	m_audioInputStream;
	private SourceDataLine		m_sourceDataLine;
	private Thread			m_thread;



	public JavaSoundAudioPlayer(DataSource dataSource)
	{
		super(dataSource);
	}



	protected void doRealize()
		throws Exception
	{
		DataSource	dataSource = getDataSource();
		dataSource.connect();
		dataSource.start();
		SourceStream[]	aSourceStreams = dataSource.getStreams();
		SourceStream	sourceStream = aSourceStreams[0];
		if (sourceStream instanceof URLSourceStream)
		{
			URL	url = ((URLSourceStream) sourceStream).getURL();
			m_audioInputStream = AudioSystem.getAudioInputStream(url);
		}
		else
		{
			InputStream	inputStream = new SourceInputStream(aSourceStreams[0]);
			m_audioInputStream = AudioSystem.getAudioInputStream(inputStream);
		}
		Line.Info	info = new DataLine.Info(SourceDataLine.class, getAudioFormat());
		m_sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
	}



	protected void doPrefetch()
		throws Exception
	{
		getSourceDataLine().open(getAudioFormat());
		m_thread = new Thread(this);
		m_thread.start();
	}



	protected void doStart()
		throws Exception
	{
		getSourceDataLine().start();
	}



	protected void doStop()
		throws Exception
	{
		getSourceDataLine().stop();
	}



	protected void doDeallocate()
		throws Exception
	{
		// TODO:
	}



	protected void doClose()
		throws Exception
	{
		// TODO:
	}



	public void run()
	{
		byte[]	abBuffer = new byte[BUFFER_SIZE];
		while (true)
		{
		}
	}



	private AudioInputStream getAudioInputStream()
	{
		return m_audioInputStream;
	}



	private AudioFormat getAudioFormat()
	{
		return m_audioInputStream.getFormat();
	}



	private SourceDataLine getSourceDataLine()
	{
		return m_sourceDataLine;
	}


	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////


	private class SourceInputStream
	extends InputStream
	{
		private SourceStream	m_sourceStream;
		private byte[]		m_abOneByteBuffer;


		public SourceInputStream(SourceStream sourceStream)
		{
			m_sourceStream = sourceStream;
		}



		public int read()
			throws IOException
		{
			if (m_abOneByteBuffer == null)
			{
				m_abOneByteBuffer = new byte[1];
			}
			read(m_abOneByteBuffer);
			return m_abOneByteBuffer[0];
		}



		public int read(byte[] abData,
				int nOffset,
				int nLength)
			throws IOException
		{
			int	nReturn = m_sourceStream.read(abData,
							      nOffset,
							      nLength);
			return nReturn;
		}
	}
}



/*** JavaSoundAudioPlayer.java ***/
