/*
 *	LineTest.java
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



public class LineTest
{
	public static void main(String[] args)
	{
		Mixer.Info[]	aMixerInfos = AudioSystem.getMixerInfo();
		Mixer		mixer = AudioSystem.getMixer(aMixerInfos[0]);
		System.out.println("Mixer: " + mixer);

		AudioFormat	format = new AudioFormat(
			AudioFormat.Encoding.PCM_SIGNED,
			44100.0F,
			16,
			2,
			4,
			44100.0F,
			false);
		DataLine.Info	lineInfo = new DataLine.Info(SourceDataLine.class, format);
		boolean	bSupported = mixer.isLineSupported(lineInfo);
		System.out.println("Supported: " + bSupported);
	}
}



/*** LineTest.java ***/
