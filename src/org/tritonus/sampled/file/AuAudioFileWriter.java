/*
 *	AuAudioFileWriter.java
 */

/*
 *  Copyright (c) 1999,2000 by Florian Bomers <florian@bome.com>
 *  Copyright (c) 1999 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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


package	org.tritonus.sampled.file;


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

/**
 * AudioFileWriter for Sun/Next AU files.
 *
 * @author Florian Bomers
 * @author Matthias Pfisterer
 */
public class AuAudioFileWriter extends TAudioFileWriter {

	private static final AudioFileFormat.Type[]	FILE_TYPES =
	{
		AudioFileFormat.Type.AU
	};
	
	// IMPORTANT: this array depends on the AudioFormat.match() algorithm which takes
	//            AudioSystem.NOT_SPECIFIED into account !
	private static final AudioFormat[]	AUDIO_FORMATS =
	{
		// IDEA: allow other number of channels that 1 and 2 ?
		new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, 8, 1, 1, AudioSystem.NOT_SPECIFIED, true),
		new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, 8, 2, 2, AudioSystem.NOT_SPECIFIED, true),
		/*	Because there is only ony byte per sample,
		 *	byte order doesn't matter. So we allow little-endian,
		 *	too.
		 */
		new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, 8, 1, 1, AudioSystem.NOT_SPECIFIED, false),
		new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, 8, 2, 2, AudioSystem.NOT_SPECIFIED, false),

		new AudioFormat(AudioFormat.Encoding.ULAW, AudioSystem.NOT_SPECIFIED, 8, 1, 1, AudioSystem.NOT_SPECIFIED, false),
		new AudioFormat(AudioFormat.Encoding.ULAW, AudioSystem.NOT_SPECIFIED, 8, 2, 2, AudioSystem.NOT_SPECIFIED, false),
		new AudioFormat(AudioFormat.Encoding.ULAW, AudioSystem.NOT_SPECIFIED, 8, 1, 1, AudioSystem.NOT_SPECIFIED, true),
		new AudioFormat(AudioFormat.Encoding.ULAW, AudioSystem.NOT_SPECIFIED, 8, 2, 2, AudioSystem.NOT_SPECIFIED, true),

		new AudioFormat(AudioFormat.Encoding.ALAW, AudioSystem.NOT_SPECIFIED, 8, 1, 1, AudioSystem.NOT_SPECIFIED, false),
		new AudioFormat(AudioFormat.Encoding.ALAW, AudioSystem.NOT_SPECIFIED, 8, 2, 2, AudioSystem.NOT_SPECIFIED, false),
		new AudioFormat(AudioFormat.Encoding.ALAW, AudioSystem.NOT_SPECIFIED, 8, 1, 1, AudioSystem.NOT_SPECIFIED, true),
		new AudioFormat(AudioFormat.Encoding.ALAW, AudioSystem.NOT_SPECIFIED, 8, 2, 2, AudioSystem.NOT_SPECIFIED, true),

		new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, 16, 1, 2, AudioSystem.NOT_SPECIFIED, true),
		new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, 16, 2, 4, AudioSystem.NOT_SPECIFIED, true),

		new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, 24, 1, 3, AudioSystem.NOT_SPECIFIED, true),
		new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, 24, 2, 6, AudioSystem.NOT_SPECIFIED, true),

		new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, 32, 1, 4, AudioSystem.NOT_SPECIFIED, true),
		new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, 32, 2, 8, AudioSystem.NOT_SPECIFIED, true)
	};
	
	public AuAudioFileWriter()
	{
		super(Arrays.asList(FILE_TYPES),
		      Arrays.asList(AUDIO_FORMATS));
	}


	protected boolean isAudioFormatSupported(AudioFormat format)
	{
		return AuTool.getFormatCode(format)!=AuTool.SND_FORMAT_UNSPECIFIED;
	}

	protected AudioOutputStream getAudioOutputStream(
		AudioFormat audioFormat,
		long lLengthInBytes,
		AudioFileFormat.Type fileType,
		File file)
		throws	IOException
	{
		// TODO: (generalized) check if either seek is possible 
		//       or length is not required in header
		TDataOutputStream	dataOutputStream = new SeekableTDOS(file);
		return new AuAudioOutputStream(audioFormat,
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
		// it should be thrown an exception if it is tried to write
		// to a stream but lLengthInFrames is AudioSystem.NOT_SPECIFIED
		TDataOutputStream	dataOutputStream = new NonSeekableTDOS(outputStream);
		return new AuAudioOutputStream(audioFormat,
					       lLengthInBytes,
					       dataOutputStream);
	}

}

/*** AuAudioFileWriter.java ***/
