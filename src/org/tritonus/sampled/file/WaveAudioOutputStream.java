/*
 *	WaveAudioOutputStream.java
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

import	java.io.IOException;
import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioFileFormat;
import	javax.sound.sampled.AudioSystem;
import	org.tritonus.TDebug;

/**	
 * AudioOutputStream for Wave files.
 *
 * @author Florian Bomers
 */

public class WaveAudioOutputStream extends TAudioOutputStream {

	// this constant is used for chunk lengths when the length is not known yet
	private static final int LENGTH_NOT_KNOWN=-1;

	public WaveAudioOutputStream(AudioFormat audioFormat,
				   long lLength,
				   TDataOutputStream dataOutputStream)
	{
		super(audioFormat,
			lLength, 
			dataOutputStream,
			lLength == AudioSystem.NOT_SPECIFIED && dataOutputStream.supportsSeek());
		// wave cannot store more than 4GB
		if (lLength != AudioSystem.NOT_SPECIFIED && (lLength+WaveTool.DATA_OFFSET)>0xFFFFFFFFl) {
			if (TDebug.TraceAudioOutputStream)
			{
				TDebug.out("WaveAudioOutputStream: Length exceeds 4GB: "+lLength+"=0x"+Long.toHexString(lLength)+" with header="+(lLength+WaveTool.DATA_OFFSET)+"=0x"+Long.toHexString(lLength+WaveTool.DATA_OFFSET));
			}
			throw new IllegalArgumentException("Wave files cannot be larger than 4GB.");
		}
	}

	protected void writeHeader()
		throws	IOException
	{
		if (TDebug.TraceAudioOutputStream)
		{
			TDebug.out("WaveAudioOutputStream.writeHeader(): called.");
		}
		AudioFormat		format = getFormat();
		long			lLength = getLength();
		// if patching the header, and the length has not been known at first
		// writing of the header, just truncate the size fields, don't throw an exception
		if (lLength != AudioSystem.NOT_SPECIFIED && lLength+WaveTool.DATA_OFFSET>0xFFFFFFFFl) {
			lLength=0xFFFFFFFFl-WaveTool.DATA_OFFSET;
		}
		
		// chunks must be on word-boundaries
		long 			lDataChunkSize=lLength+(lLength%2);
		TDataOutputStream	dos = getDataOutputStream();
		
		// write RIFF container chunk
		dos.writeInt(WaveTool.WAVE_RIFF_MAGIC);
		dos.writeLittleEndian32((int) (lDataChunkSize+WaveTool.DATA_OFFSET-WaveTool.CHUNK_HEADER_SIZE));
		dos.writeInt(WaveTool.WAVE_WAVE_MAGIC);
		
		// write fmt_ chunk
		dos.writeInt(WaveTool.WAVE_FMT_MAGIC);
		dos.writeLittleEndian32(WaveTool.FMT_CHUNK_SIZE);
		dos.writeLittleEndian16(WaveTool.getFormatCode(format));
		dos.writeLittleEndian16((short) format.getChannels());
		dos.writeLittleEndian32((int) format.getSampleRate());
		dos.writeLittleEndian32((int) (format.getFrameRate()*format.getFrameSize()));
		dos.writeLittleEndian16((short) format.getFrameSize());
		dos.writeLittleEndian16((short) format.getSampleSizeInBits());
		
		// write header of data chunk
		dos.writeInt(WaveTool.WAVE_DATA_MAGIC);
		dos.writeLittleEndian32((lLength!=AudioSystem.NOT_SPECIFIED)?((int) lLength):LENGTH_NOT_KNOWN);
	}

	protected void patchHeader()
		throws	IOException
	{
		TDataOutputStream	tdos = getDataOutputStream();
		tdos.seek(0);
		setLengthFromCalculatedLength();
		writeHeader();
	}

	public void close() throws IOException {
		long nBytesWritten=getCalculatedLength();
		
		if ((nBytesWritten % 2)==1) {
			if (TDebug.TraceAudioOutputStream) {
				TDebug.out("WaveOutputStream.close(): adding padding byte");
			}
			// extra byte for to align on word boundaries
			TDataOutputStream tdos = getDataOutputStream();
			tdos.writeByte(0);
			// DON'T adjust calculated length !
		}
		super.close();
	}

}

/*** WaveAudioOutputStream.java ***/
