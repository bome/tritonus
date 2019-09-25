/*
 *	VorbisSilenceTestCase.java
 */

/*
 *  Copyright (c) 2003 by Dan Rollo
 *
 *  [license not updated to Apache 2.0]
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

package org.tritonus.test;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tritonus.share.sampled.AudioSystemShadow;
import org.tritonus.share.sampled.file.AudioOutputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


/** Tests conversion of Ogg files with long silence at the start of the file.
 * NOTE: These tests create a large (26mb) temporary audio file in the sounds dir
 * which is deleted after the test completes, but be sure you have sufficient disk space.
 */
public class VorbisSilenceTestCase
{
	private File _sourceFileOgg = new File("src/test/resources/" + "sounds/testsilence.ogg");
	private File _destFileWav = new File("src/test/resources/" + "sounds/testsilenceout.wav");

	@BeforeEach
	protected void setUp()
	{
		assertTrue(_sourceFileOgg.exists(),
			   "Missing required test file: " + _sourceFileOgg.getAbsolutePath());

		_destFileWav.deleteOnExit();
	}

    @AfterEach
	protected void tearDown()
	{
		assertTrue(_sourceFileOgg.exists(),
			   "Deleted required test file: " + _sourceFileOgg.getAbsolutePath());

		if (_destFileWav.exists())
		{
			// remove converted file
			assertTrue(_destFileWav.delete(),
				   "Couldn't delete file: " + _destFileWav.getAbsolutePath()
                	   + "; size: " + _destFileWav.length()
                	   + ". (0 size may mean file is locked by buffer loop.)");
		}
	}


    @Test
	public void testConvertSilentOggWithAudioOutStream() throws Exception
	{
		final AudioInputStream inAIStreamOgg = AudioSystem.getAudioInputStream(_sourceFileOgg);

		final AudioFormat destAudioFormatPCM = new AudioFormat(44100.0F, 16, 1, true, false);
		final AudioInputStream inAIStreamPCM = AudioSystem.getAudioInputStream(destAudioFormatPCM, inAIStreamOgg);

		final AudioOutputStream outAOStreamWavPCM = AudioSystemShadow.getAudioOutputStream(
			AudioFileFormat.Type.WAVE,
			destAudioFormatPCM,
			AudioSystem.NOT_SPECIFIED,
			_destFileWav);

		class StreamPump extends Thread {
			boolean isRunFinished;

			StreamPump() {
				super("SilenceTest-StreamPump");
			}


			public void run()
			{
				// pump the streams
				int readCnt;
				final byte[] buf = new byte[65536];

				int cnt = 0;
				try {
					while ((readCnt = inAIStreamPCM.read(buf, 0, buf.length)) != -1) {
						outAOStreamWavPCM.write(buf, 0, readCnt);
						cnt++;
						//System.out.println("readCnt: " + readCnt + " after write, loop cnt: " + cnt);
					}
				} catch (IOException e) {
					e.printStackTrace();
					fail("Exception pumping streams: " + e.getMessage());
				}
				isRunFinished = true;
			}
		};
		StreamPump t = new StreamPump();
		t.start();
		// wait for conversion to finish, within reasonable time
		final int maxWait = 60;
		int waitCnt = 0;
		while (t.isAlive() && waitCnt++ < maxWait)
		{
			Thread.sleep(1000);
		}

		outAOStreamWavPCM.close();
		inAIStreamPCM.close();
		inAIStreamOgg.close();

		assertTrue(_destFileWav.length() > 0, "Converted wav file is empty: " + _destFileWav.getAbsolutePath());
		assertTrue(t.isRunFinished, "Conversion never finished run() method");
		assertTrue(waitCnt < maxWait, "Conversion timeout expired");

		// attempt to read the resulting wav file
		final AudioInputStream aisDest = AudioSystem.getAudioInputStream(_destFileWav);
		// attempt to play the resulting wav file - 5 minutes of silence
		playStream(aisDest);
	}


    @Test
	public void testConvertSilentOggWithAudioSystem() throws Exception
	{
		final AudioInputStream inAIStreamOgg = AudioSystem.getAudioInputStream(_sourceFileOgg);

		final AudioFormat destAudioFormatPCM = new AudioFormat(44100.0F, 16, 1, true, false);
		final AudioInputStream inAIStreamPCM = AudioSystem.getAudioInputStream(destAudioFormatPCM, inAIStreamOgg);

		AudioSystem.write(inAIStreamPCM, AudioFileFormat.Type.WAVE, _destFileWav);

		inAIStreamPCM.close();
		inAIStreamOgg.close();

		assertTrue(_destFileWav.length() > 0, "Converted wav file is empty: " + _destFileWav.getAbsolutePath());

		// attempt to read the resulting wav file
		final AudioInputStream aisDest = AudioSystem.getAudioInputStream(_destFileWav);
		// attempt to play the resulting wav file - 5 minutes of silence
		playStream(aisDest);
	}


	/** Play the given audio stream. Closes the stream when finised.
	 * @param streamToPlay the audio stream to play
	 * @throws LineUnavailableException if can't get line for stream's format
	 * @throws IOException if problem occurs reading the stream
	 */
	private static void playStream(final AudioInputStream streamToPlay)
		throws LineUnavailableException, IOException {

		// @todo Comment this out to really play the file
		if (1==1) {
			streamToPlay.close();
			// @todo Why is GC required to get AudioSytem to release these files???
			// To see the error (under Win2K, jdk 1.3.1_02-b02), just comment out the gc below.
			System.gc();
			return;
		}

		final SourceDataLine line = (SourceDataLine)AudioSystem.getLine(
			new DataLine.Info(SourceDataLine.class, streamToPlay.getFormat()));

		line.open();
		line.start();
		try {
			byte[] buf = new byte[1024];
			int readCnt;
			while ((readCnt = streamToPlay.read(buf, 0, buf.length)) != -1) {
				line.write(buf, 0, readCnt);
			}
		} finally {
			// kludge to get last bit played
			try { Thread.sleep(1000); } catch(InterruptedException e) {}
			streamToPlay.close();
			line.stop();
			line.close();
		}
	}
}



/*** VorbisSilenceTestCase.java ***/
