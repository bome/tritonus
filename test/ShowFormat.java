/*
 *	ShowFormat.java
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

import	java.net.URL;

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



public class ShowFormat
{

	public static void main(String[] args)
	{
		// org.gnu.tritonus.TDebug.TraceAudioParser = true;
		int	nLoadMethod = 2;	// default: file
		boolean	bCheckAudioInputStream = false;
		int	nCurrentArg = 0;
		while (nCurrentArg < args.length - 1)
		{
			if (args[nCurrentArg].equals("-s"))
			{
				nLoadMethod = 1;
			}
			else if (args[nCurrentArg].equals("-f"))
			{
				nLoadMethod = 2;
			}
			else if (args[nCurrentArg].equals("-u"))
			{
				nLoadMethod = 3;
			}
			else if (args[nCurrentArg].equals("-i"))
			{
				bCheckAudioInputStream = true;
			}

			nCurrentArg++;
		}
		String	sFilename = args[nCurrentArg];
		/*
		  System.out.println("ShowFormat: usage:");
		  System.out.println("\tjava ShowFormat <soundfile>");
		*/
		AudioFileFormat	aff = null;
		AudioInputStream ais = null;
		try
		{
			switch (nLoadMethod)
			{
			case 1:
				FileInputStream	fis = new FileInputStream(sFilename);
				aff = AudioSystem.getAudioFileFormat(fis);
				sFilename = new File(sFilename).getCanonicalPath();
				if (bCheckAudioInputStream)
				{
					ais = AudioSystem.getAudioInputStream(fis);
				}
				break;

			case 2:
				File	file = new File(sFilename);
				aff = AudioSystem.getAudioFileFormat(file);
				sFilename = file.getCanonicalPath();
				if (bCheckAudioInputStream)
				{
					ais = AudioSystem.getAudioInputStream(file);
				}
				break;

			case 3:
				URL	url = new URL("file:///" + sFilename);
				aff = AudioSystem.getAudioFileFormat(url);
				sFilename = url.toString();
				if (bCheckAudioInputStream)
				{
					ais = AudioSystem.getAudioInputStream(url);
				}
				break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (aff == null)
		{
			System.out.println("Cannot determine format");
		}
		else
		{
			System.out.println("---------------------------------------------------------------------------");
			System.out.println("File: " + sFilename);
			System.out.println("---------------------------------------------------------------------------");
			System.out.println("Type: " + aff.getType());
			System.out.println("---------------------------------------------------------------------------");
			AudioFormat format = aff.getFormat();
			System.out.println("AudioFormat: " + format);
			System.out.println("---------------------------------------------------------------------------");
			String	strAudioLength = null;
			if (aff.getFrameLength() != AudioSystem.NOT_SPECIFIED)
			{
				strAudioLength = "" + aff.getFrameLength() + " frames (= " + aff.getFrameLength() * format.getFrameSize() + " bytes)";
			}
			else
			{
				strAudioLength = "unknown";
			}
			System.out.println("Length of audio data: " + strAudioLength);
			String	strFileLength = null;
			if (aff.getByteLength() != AudioSystem.NOT_SPECIFIED)
			{
				strFileLength = "" + aff.getByteLength() + " bytes)";
			}
			else
			{
				strFileLength = "unknown";
			}
			System.out.println("Total length of file (including headers): " + strFileLength);
			if (bCheckAudioInputStream)
			{
				System.out.println("[AudioInputStream says:] Length of audio data: " + ais.getFrameLength() + " frames (= " + ais.getFrameLength() * ais.getFormat().getFrameSize() + " bytes)");
			}
			System.out.println("---------------------------------------------------------------------------");
		}
	}

}



/*** ShowFormat.java ***/
