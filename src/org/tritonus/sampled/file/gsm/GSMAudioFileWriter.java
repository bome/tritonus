/*
 *	GSMAudioFileWriter.java
 */

/*
 *  Copyright (c) 2000 by Florian Bomers <florian@bome.com>
 *  Copyright (c) 2000 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
 *
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
 *
 */

package	org.tritonus.sampled.file.gsm;


import	java.io.File;
import	java.io.InputStream;
import	java.io.IOException;
import	java.io.OutputStream;
import	java.io.DataOutputStream;
import	java.util.Arrays;

import	javax.sound.sampled.AudioFileFormat;
import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioInputStream;
import	javax.sound.sampled.AudioSystem;

import	org.tritonus.TDebug;
import	org.tritonus.sampled.file.AudioOutputStream;
import	org.tritonus.sampled.file.TAudioFileWriter;
import	org.tritonus.sampled.file.TDataOutputStream;
import	org.tritonus.sampled.file.HeaderLessAudioOutputStream;
import	org.tritonus.sampled.file.SeekableTDOS;
import	org.tritonus.sampled.file.NonSeekableTDOS;
import	org.tritonus.sampled.Encodings;
import	org.tritonus.sampled.AudioFileTypes;



/**	Class for writing GSM streams
 *
 * @author Florian Bomers
 * @author Matthias Pfisterer
 */
public class GSMAudioFileWriter
	extends TAudioFileWriter
{
	private static final AudioFileFormat.Type[]	FILE_TYPES =
	{
		AudioFileTypes.getType("GSM", "gsm")
	};

	private static final AudioFormat[]	AUDIO_FORMATS =
	{
		new AudioFormat(Encodings.getEncoding("GSM0610"), 8000.0F, AudioSystem.NOT_SPECIFIED, 1, 33, 50.0F, false),
		new AudioFormat(Encodings.getEncoding("GSM0610"), 8000.0F, AudioSystem.NOT_SPECIFIED, 1, 33, 50.0F, true),
	};



	public GSMAudioFileWriter()
	{
		super(Arrays.asList(FILE_TYPES),
		      Arrays.asList(AUDIO_FORMATS));
	}



	protected AudioOutputStream getAudioOutputStream(
		AudioFormat audioFormat,
		long lLengthInBytes,
		AudioFileFormat.Type fileType,
		File file)
		throws	IOException
	{
		TDataOutputStream	dataOutputStream = new SeekableTDOS(file);
		return new HeaderLessAudioOutputStream(audioFormat,
					       lLengthInBytes,
					       dataOutputStream);
	}

	protected AudioOutputStream getAudioOutputStream(
		AudioFormat audioFormat,
		long lLengthInBytes,
		AudioFileFormat.Type fileType,
		OutputStream outputStream)
		throws	IOException
	{
		TDataOutputStream	dataOutputStream = new NonSeekableTDOS(outputStream);
		return new HeaderLessAudioOutputStream(audioFormat,
					       lLengthInBytes,
					       dataOutputStream);
	}
	
}



/*** GSMAudioFileWriter.java ***/
