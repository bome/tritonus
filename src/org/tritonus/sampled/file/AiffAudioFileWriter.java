/*
 *	AiffAudioFileWriter.java
 */

/*
 *  Copyright (c) 2000 by Florian Bomers <florian@bome.com>
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
 * Class for writing AIFF and AIFF-C files.
 *
 * @author Florian Bomers
 */

public class AiffAudioFileWriter extends TAudioFileWriter {

	private static final AudioFileFormat.Type[] FILE_TYPES =
	    {
	        AiffTool.AIFF,
	        AiffTool.AIFC
	    };

	private static final int ALL=AudioSystem.NOT_SPECIFIED;

	// IMPORTANT: this array depends on the AudioFormat.match() algorithm which takes
	//            AudioSystem.NOT_SPECIFIED into account !
	private static final AudioFormat[]	AUDIO_FORMATS =
	    {
	        new AudioFormat(AiffTool.PCM, ALL, 8, ALL, ALL, ALL, true),
	        new AudioFormat(AiffTool.PCM, ALL, 8, ALL, ALL, ALL, false),
	        new AudioFormat(AiffTool.ULAW, ALL, 8, ALL, ALL, ALL, false),
	        new AudioFormat(AiffTool.ULAW, ALL, 8, ALL, ALL, ALL, true),
	        new AudioFormat(AiffTool.PCM, ALL, 16, ALL, ALL, ALL, true),
	        new AudioFormat(AiffTool.PCM, ALL, 24, ALL, ALL, ALL, true),
	        new AudioFormat(AiffTool.PCM, ALL, 32, ALL, ALL, ALL, true),
	    };

	public AiffAudioFileWriter() {
		super(Arrays.asList(FILE_TYPES),
		      Arrays.asList(AUDIO_FORMATS));
	}


	protected boolean isAudioFormatSupportedImpl(AudioFormat format,
	        AudioFileFormat.Type fileType) {
		return AiffTool.getFormatCode(format)!=AiffTool.AIFF_COMM_UNSPECIFIED;
	}


	protected AudioOutputStream getAudioOutputStream(
	    AudioFormat audioFormat,
	    long lLengthInBytes,
	    AudioFileFormat.Type fileType,
	    File file)
	throws	IOException {
		// TODO: (generalized) check if either seek is possible
		//       or length is not required in header
		TDataOutputStream	dataOutputStream = new SeekableTDOS(file);
		return new AiffAudioOutputStream(audioFormat, fileType,
		                                 lLengthInBytes,
		                                 dataOutputStream);
	}

	protected AudioOutputStream getAudioOutputStream(
	    AudioFormat audioFormat,
	    long lLengthInBytes,
	    AudioFileFormat.Type fileType,
	    OutputStream outputStream)
	throws	IOException {
		// it should be thrown an exception if it is tried to write
		// to a stream but lLengthInFrames is AudioSystem.NOT_SPECIFIED
		TDataOutputStream	dataOutputStream = new NonSeekableTDOS(outputStream);
		return new AiffAudioOutputStream(audioFormat, fileType,
		                                 lLengthInBytes,
		                                 dataOutputStream);
	}

}

/*** WaveAudioFileWriter.java ***/
