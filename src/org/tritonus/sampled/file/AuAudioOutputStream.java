/*
 *	AuAudioOutputStream.java
 */

/*
 *  Copyright (c) 2000 by Florian Bomers <florian@bome.com>
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

import	java.io.IOException;
import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioFileFormat;
import	javax.sound.sampled.AudioSystem;
import	org.tritonus.TDebug;



/**	
 * AudioOutputStream for AU files.
 *
 * @author Florian Bomers
 * @author Matthias Pfisterer
 */

public class AuAudioOutputStream extends TAudioOutputStream {

	// this constant is used for chunk lengths when the length is not known yet
	private static final int LENGTH_NOT_KNOWN=-1;
	
	public AuAudioOutputStream(AudioFormat audioFormat,
				   long lLength,
				   TDataOutputStream dataOutputStream)
	{
		// au cannot store more than 2GB
		// when this check is here, we will truncate the output file
		// it could be placed in write(), then the
		// header would say 2GB, but the file would be larger
		super(audioFormat,
		      lLength,
		      dataOutputStream,
		      lLength == AudioSystem.NOT_SPECIFIED && dataOutputStream.supportsSeek());
		if (lLength != AudioSystem.NOT_SPECIFIED && lLength>0x7FFFFFFFl) {
			throw new IllegalArgumentException("AU files cannot be larger than 2GB.");
		}
	}

	protected void writeHeader()
		throws	IOException
	{
		if (TDebug.TraceAudioOutputStream)
		{
			TDebug.out("AuAudioOutputStream.writeHeader(): called.");
		}
		AudioFormat		format = getFormat();
		long			lLength = getLength();
		// if patching the header, and the length has not been known at first
		// writing of the header, just truncate the size fields, don't throw an exception
		if (lLength != AudioSystem.NOT_SPECIFIED && lLength>0x7FFFFFFFl) {
			lLength=0x7FFFFFFFl;
		}
		TDataOutputStream	dos = getDataOutputStream();
		dos.writeInt(AuTool.AU_HEADER_MAGIC);
		dos.writeInt(AuTool.DATA_OFFSET);
		dos.writeInt((lLength!=AudioSystem.NOT_SPECIFIED)?((int) lLength):LENGTH_NOT_KNOWN);
		dos.writeInt(AuTool.getFormatCode(format));
		dos.writeInt((int) format.getSampleRate());
		dos.writeInt(format.getChannels());
	}

	protected void patchHeader()
		throws	IOException
	{
		TDataOutputStream	tdos = getDataOutputStream();
		tdos.seek(0);
		setLengthFromCalculatedLength();
		writeHeader();
	}
}

/*** AuAudioOutputStream.java ***/
