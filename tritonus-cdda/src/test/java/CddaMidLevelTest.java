/*
 *	CddaMidLevelTest.java
 */

/*
 *  Copyright (c) 2001 - 2002 by Matthias Pfisterer
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

/*
  programs related to cdda:
  command-line extractor
  cd player
  extractor/mp3 encoder
 */

import	java.io.InputStream;
import	java.io.IOException;

import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioInputStream;
import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.SourceDataLine;
import	javax.sound.sampled.DataLine;
import	javax.sound.sampled.Line;
import	javax.sound.sampled.LineUnavailableException;

import	org.tritonus.lowlevel.cdda.CddaMidLevel;
import	org.tritonus.lowlevel.cdda.CddaUtils;


public class CddaMidLevelTest
{
	public static void main(String[] args)
	{
		boolean		bTocOnly = true;
		int		nTrack = 0;
		if (args.length < 1)
		{
			bTocOnly = true;
		}
		else if (args.length == 1)
		{
			nTrack = Integer.parseInt(args[0]);
			bTocOnly = false;
		}
		CddaMidLevel	cddaMidLevel = CddaUtils.getCddaMidLevel();
		InputStream	tocInputStream = null;
		try
		{
			tocInputStream = cddaMidLevel.getTocAsXml("TODO:");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		byte[]	abData = new byte[4096];
		int	nRead;
		try
		{
			while ((tocInputStream.read(abData)) >= 0)
			{
				System.out.print(new String(abData));
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.print("\n");
		if (! bTocOnly)
		{
			AudioInputStream	track = null;
			SourceDataLine		line = null;
			AudioFormat	audioFormat = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED,
				44100.0F, 16, 2, 4, 44100.0F, false);
			Line.Info	info = new DataLine.Info(SourceDataLine.class, audioFormat);
			abData = new byte[2352 * 8];
			try
			{
				track = cddaMidLevel.getTrack("TODO:", nTrack);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			try
			{
				line = (SourceDataLine) AudioSystem.getLine(info);
				line.open();
				line.start();
			}
			catch (LineUnavailableException e)
			{
				e.printStackTrace();
			}
			try
			{
			while ((nRead = track.read(abData)) >= 0)
			{
				line.write(abData, 0, nRead);
			}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		// cdda.close();
	}
}


/*** CddaMidLevelTest.java ****/
