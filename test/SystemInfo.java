/*
 *	SystemInfo.java
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



public class SystemInfo
{

	public static void main(String[] args)
	{
		int	nLoadMethod = 2;	// default: file
		boolean	bCheckAudioInputStream = false;
		int	nCurrentArg = 0;
		/*
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
		*/
		/*
		  System.out.println("SystemInfo: usage:");
		  System.out.println("\tjava SystemInfo <soundfile>");
		*/
		out("---------------------------------------------------------------------------");
		outputMixers();
		out("---------------------------------------------------------------------------");
	}


	private static void outputMixers()
	{
		Mixer.Info[]	aMixerInfos = AudioSystem.getMixerInfo();
		if (aMixerInfos.length > 0)
		{
			boolean		bFirstMixer = true;
			for (int i = 0; i < aMixerInfos.length; i++)
			{
				if (bFirstMixer)
				{
					bFirstMixer = false;
				}
				else
				{
					out("---------------------------------------------------------------------------");
				}
				outputMixer(aMixerInfos[i]);
			}
		}
		else
		{
			out("No mixers present in the system.");
		}
	}



	private static void outputMixer(Mixer.Info mixerInfo)
	{
		// base info about the mixer
		out("Mixer");
		out("Name: " + mixerInfo.getName());
		out("Vendor: " + mixerInfo.getVendor());
		out("Description: " + mixerInfo.getDescription());
		out("Version: " + mixerInfo.getVersion());
		if (true)	// additional info
		{
			out("------");
			Mixer	mixer = AudioSystem.getMixer(mixerInfo);
			if (mixer != null)
			{
				out("Mixer: " + mixer);
				// source lines info
				Line.Info[]	aSourceLineInfos = mixer.getSourceLineInfo();
				if (aSourceLineInfos != null && aSourceLineInfos.length > 0)
				{
					for (int i = 0; i < aSourceLineInfos.length; i++)
					{
						out(aSourceLineInfos.toString());
					}
				}
				else
				{
					out("no source lines");
				}

				// target lines info
				Line.Info[]	aTargetLineInfos = mixer.getTargetLineInfo();
				if (aTargetLineInfos != null && aTargetLineInfos.length > 0)
				{
					for (int i = 0; i < aTargetLineInfos.length; i++)
					{
						out(aTargetLineInfos.toString());
					}
				}
				else
				{
					out("no target lines");
				}
			}
			else
			{
				out("mixer seems not to be available");
			}

		}
		else
		{
			System.out.println("No mixers present in the system.");
		}
	}


	private static void out(String str)
	{
		System.out.println(str);
	}
}



/*** SystemInfo.java ***/
