/*
 *	Convert.java
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


import	java.io.FileInputStream;
import	java.io.IOException;
import	java.io.FileNotFoundException;
import	java.io.File;

import	javax.sound.sampled.AudioFileFormat;
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


public class Convert
{

	public static void main(String[] args)
	{
		if (args.length != 1)
		{
			System.out.println("Convert: usage:");
			System.out.println("\tjava Convert <soundfile>");
		}
		else
		{
			String	sFilename = args[0];
			File	file = new File(sFilename);
			AudioInputStream	ais = null;
			try
			{
				ais = AudioSystem.getAudioInputStream(file);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			if (ais == null)
			{
				System.out.println("cannot open audio file");
			}
			else
			{
				AudioFileFormat.Type	fileType = AudioFileFormat.Type.AU;
				// AudioFileFormat	outFileFormat = new AudioFileFormat(type, ais.getFormat(), (int) ais.getLength());
				int	nWrittenFrames = 0;
				try
				{
					nWrittenFrames = AudioSystem.write(ais, fileType, new File("outfile.au"));
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				System.out.println("Written frames: " + nWrittenFrames);
			}
		}
	}

}



/*** Convert.java ***/

