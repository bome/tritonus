/*
 *	TAudioFileWriter.java
 */

/*
 *  Copyright (c) 1999, 2000 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
 *  Copyright (c) 1999, 2000 by Florian Bomers <florian@bome.com>
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
import	java.io.FileOutputStream;
import	java.io.InputStream;
import	java.io.IOException;
import	java.io.OutputStream;
import	java.util.Collection;
import	java.util.Iterator;

import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioFileFormat;
import	javax.sound.sampled.AudioInputStream;
import	javax.sound.sampled.spi.AudioFileWriter;

import	org.tritonus.TDebug;
import	org.tritonus.sampled.AudioUtils;
import	org.tritonus.sampled.TConversionTool;
import	org.tritonus.sampled.Encodings;

/**
 * Common base class for implementing classes of AudioFileWriter.
 * <p>It provides often-used functionality and the new architecture using
 * an AudioOutputStream.
 * <p>There should be only one set of audio formats supported by any given
 * class of TAudioFileWriter. This class assumes implicitely that all
 * supported file types have a common set of audio formats they can handle.
 *
 * @author Matthias Pfisterer
 * @author Florian Bomers
 */

public abstract class TAudioFileWriter
	extends		AudioFileWriter
{

	public static AudioFormat.Encoding PCM_SIGNED=Encodings.getEncoding("PCM_SIGNED");
	public static AudioFormat.Encoding PCM_UNSIGNED=Encodings.getEncoding("PCM_UNSIGNED");

	/**	Buffer length for the loop in the write() method.
	 *	This is in bytes. Perhaps it should be in frames to give an
	 *	equal amount of latency.
	 */
	private static final int	BUFFER_LENGTH = 16384;

	// only needed for Collection.toArray()
	protected static final AudioFileFormat.Type[]	NULL_TYPE_ARRAY = new AudioFileFormat.Type[0];


	/**	The audio file types (AudioFileFormat.Type) that can be
	 *	handled by the AudioFileWriter.
	 */
	private Collection		m_audioFileTypes;



	/**	The AudioFormats that can be handled by the
	 *	AudioFileWriter.
	 */
	// IDEA: implement a special collection that uses matches() to test whether an element is already in
	private Collection		m_audioFormats;


	/**
	 * Inheriting classes should call this constructor
	 * in order to make use of the functionality of TAudioFileWriter.
	 */
	protected TAudioFileWriter(Collection fileTypes,
				   Collection audioFormats)
	{
		m_audioFileTypes = fileTypes;
		m_audioFormats = audioFormats;
	}

	// implementing the interface
	public AudioFileFormat.Type[] getAudioFileTypes()
	{
		return (AudioFileFormat.Type[]) m_audioFileTypes.toArray(NULL_TYPE_ARRAY);
	}


	// implementing the interface
	public boolean isFileTypeSupported(AudioFileFormat.Type fileType)
	{
		return m_audioFileTypes.contains(fileType);
/*
  AudioFileFormat.Type[] aFileTypes = getAudioFileTypes();
  return isFileTypeSupported(aFileTypes, fileType);
*/
	}



	// implementing the interface
	public AudioFileFormat.Type[] getAudioFileTypes(
		AudioInputStream audioInputStream)
	{
		AudioFormat	format = audioInputStream.getFormat();
		if (isAudioFormatSupported(format, null))
		{
			return getAudioFileTypes();
		}
		else
		{
			return NULL_TYPE_ARRAY;
		}
	}



	// implementing the interface
	public boolean isFileTypeSupported(AudioFileFormat.Type fileType, AudioInputStream audioInputStream)
	{
		// TODO: use format of the stream
		return isFileTypeSupported(fileType);
	}



	// implementing the interface
	public int write(AudioInputStream audioInputStream,
			 AudioFileFormat.Type fileType,
			 File file)
		throws	IOException
	{
		if (TDebug.TraceAudioFileWriter)
		{
			TDebug.out("TAudioFileWriter.write(.., File): called");
		}
		AudioFormat	inputFormat = audioInputStream.getFormat();
		if (TDebug.TraceAudioFileWriter)
		{
			TDebug.out("TAudioFileWriter.write(): input format: " + inputFormat);
		}
		AudioFormat	outputFormat = null;
		boolean		bNeedsConversion = false;
		if (isAudioFormatSupported(inputFormat, fileType))
		{
			if (TDebug.TraceAudioFileWriter)
			{
				TDebug.out("TAudioFileWriter.write(): input format is supported directely");
			}
			outputFormat = inputFormat;
			bNeedsConversion = false;
		}
		else
		{
			if (TDebug.TraceAudioFileWriter)
			{
				TDebug.out("TAudioFileWriter.write(): input format is not supported directely; trying to find a convertable format");
			}
			outputFormat = findConvertableFormat(inputFormat, fileType);
			if (outputFormat != null)
			{
				bNeedsConversion = true;
			}
			else
			{
				throw new IllegalArgumentException("format not supported and not convertable");
			}
		}
		long	lLengthInBytes = AudioUtils.getLengthInBytes(audioInputStream);
		AudioOutputStream	audioOutputStream =
			getAudioOutputStream(
				outputFormat,
				lLengthInBytes,
				fileType,
				file);
		return writeImpl(audioInputStream,
				 audioOutputStream,
				 bNeedsConversion);
	}



	// implementing the interface
	public int write(AudioInputStream audioInputStream,
			 AudioFileFormat.Type fileType,
			 OutputStream outputStream)
		throws	IOException
	{
		if (TDebug.TraceAudioFileWriter)
		{
			TDebug.out("TAudioFileWriter.write(.., OutputStream): called");
		}
		AudioFormat	inputFormat = audioInputStream.getFormat();
		if (TDebug.TraceAudioFileWriter)
		{
			TDebug.out("TAudioFileWriter.write(): input format: " + inputFormat);
		}
		AudioFormat	outputFormat = null;
		boolean		bNeedsConversion = false;
		if (isAudioFormatSupported(inputFormat, fileType))
		{
			if (TDebug.TraceAudioFileWriter)
			{
				TDebug.out("TAudioFileWriter.write(): input format is supported directely");
			}
			outputFormat = inputFormat;
			bNeedsConversion = false;
		}
		else
		{
			if (TDebug.TraceAudioFileWriter)
			{
				TDebug.out("TAudioFileWriter.write(): input format is not supported directely; trying to find a convertable format");
			}
			outputFormat = findConvertableFormat(inputFormat, fileType);
			if (outputFormat != null)
			{
				bNeedsConversion = true;
			}
			else
			{
				throw new IllegalArgumentException("format not supported and not convertable");
			}
		}
		long	lLengthInBytes = AudioUtils.getLengthInBytes(audioInputStream);
		AudioOutputStream	audioOutputStream =
			getAudioOutputStream(
				outputFormat,
				lLengthInBytes,
				fileType,
				outputStream);
		return writeImpl(audioInputStream,
				 audioOutputStream,
				 bNeedsConversion);
	}



	protected int writeImpl(
		AudioInputStream audioInputStream,
		AudioOutputStream audioOutputStream,
		boolean bNeedsConversion)
		throws	IOException
	{
		if (TDebug.TraceAudioFileWriter)
		{
			TDebug.out("TAudioFileWriter.writeImpl(): called");
		}
		int	nTotalWritten = 0;
		AudioFormat	inputFormat = audioInputStream.getFormat();
		AudioFormat	outputFormat = audioOutputStream.getFormat();

		// boolean	bConvert = ! inputFormat.matches(outputFormat);
		// TODO: handle case when frame size is unknown ?
		int	nBytesPerSample = outputFormat.getFrameSize() / outputFormat.getChannels();
		
		//$$fb 2000-07-18: BUFFER_LENGTH must be a multiple of frame size...
		int nBufferSize=((int)BUFFER_LENGTH/outputFormat.getFrameSize())*outputFormat.getFrameSize();
		byte[]	abBuffer = new byte[nBufferSize];
		while (true)
		{
			if (TDebug.TraceAudioFileWriter)
			{
				TDebug.out("TAudioFileWriter.writeImpl(): trying to read (bytes): " + abBuffer.length);
			}
			int	nBytesRead = audioInputStream.read(abBuffer);
			if (TDebug.TraceAudioFileWriter)
			{

				TDebug.out("TAudioFileWriter.writeImpl(): read (bytes): " + nBytesRead);
			}
			if (nBytesRead == -1)
			{
				break;
			}
			if (bNeedsConversion)
			{
				TConversionTool.changeOrderOrSign(abBuffer, 0,
						  nBytesRead, nBytesPerSample);
			}
			int	nWritten = audioOutputStream.write(abBuffer, 0, nBytesRead);
			nTotalWritten += nWritten;
		}
		if (TDebug.TraceAudioFileWriter)
		{
			TDebug.out("TAudioFileWriter.writeImpl(): after main loop");
		}
		audioOutputStream.close();
		// TODO: get bytes written for header etc. from AudioOutputStrem and add to nTotalWrittenBytes
		return nTotalWritten;
	}


	/**	Returns the AudioFormat that can be handled for the given file type.
	 *	In this simple implementation, all handled AudioFormats are
	 *	returned (i.e. the fileType argument is ignored). If the
	 *	handled AudioFormats depend on the file type, this method
	 *	has to be overwritten by subclasses.
	 */
	protected Iterator getSupportedAudioFormats(AudioFileFormat.Type fileType)
	{
		return m_audioFormats.iterator();
	}



	/**	Checks whether the passed AudioFormat can be handled.
	 *	In this simple implementation, it is only checked if the
	 *	passed AudioFormat matches one of the generally handled
	 *	formats (i.e. the fileType argument is ignored). If the
	 *	handled AudioFormats depend on the file type, this method
	 *	or getSupportedAudioFormats() (on which this method relies)
	 *	has to be  overwritten by subclasses.
	 */
	protected boolean isAudioFormatSupported(
		AudioFormat audioFormat,
		AudioFileFormat.Type fileType)
	{
		if (TDebug.TraceAudioFileWriter)
		{
			TDebug.out("TAudioFileWriter.isAudioFormatSupported(): format to test: " + audioFormat);
		}
		Iterator	audioFormats = getSupportedAudioFormats(fileType);
		while (audioFormats.hasNext())
		{
			AudioFormat	handledFormat = (AudioFormat) audioFormats.next();
			if (TDebug.TraceAudioFileWriter)
			{
				TDebug.out("TAudioFileWriter.isAudioFormatSupported(): matching against format : " + handledFormat);
			}
			if (handledFormat.matches(audioFormat))
			{
				if (TDebug.TraceAudioFileWriter)
				{
					TDebug.out("...succeeded.");
				}
				return true;
			}
			else
			{
				if (TDebug.TraceAudioFileWriter)
				{
					TDebug.out("...failed.");
				}
			}
		}
		return false;
	}



	protected abstract AudioOutputStream getAudioOutputStream(
		AudioFormat audioFormat,
		long lLengthInBytes,
		AudioFileFormat.Type fileType,
		File file)
		throws	IOException;



	protected abstract AudioOutputStream getAudioOutputStream(
		AudioFormat audioFormat,
		long lLengthInBytes,
		AudioFileFormat.Type fileType,
		OutputStream outputStream)
		throws	IOException;



	private AudioFormat findConvertableFormat(
		AudioFormat inputFormat,
		AudioFileFormat.Type fileType)
	{
		if (TDebug.TraceAudioFileWriter)
		{
			TDebug.out("TAudioFileWriter.findConvertableFormat(): input format: " + inputFormat);
		}
		AudioFormat.Encoding	inputEncoding = inputFormat.getEncoding();
		if (inputEncoding.equals(PCM_UNSIGNED) &&
		    inputFormat.getSampleSizeInBits() == 8)
		{
			AudioFormat	outputFormat = new AudioFormat(
				PCM_SIGNED,
				inputFormat.getSampleRate(),
				inputFormat.getSampleSizeInBits(),
				inputFormat.getChannels(),
				inputFormat.getFrameSize(),
				inputFormat.getFrameRate(),
				inputFormat.isBigEndian());
			if (TDebug.TraceAudioFileWriter)
			{
				TDebug.out("TAudioFileWriter.findConvertableFormat(): trying output format: " + outputFormat);
			}
			if (isAudioFormatSupported(outputFormat, fileType))
			{
				return outputFormat;
			}
			else
			{
				return null;
			}
		}
		else if (inputEncoding.equals(PCM_SIGNED) &&
			 inputFormat.getSampleSizeInBits() == 8)
		{
			AudioFormat	outputFormat = new AudioFormat(
				PCM_UNSIGNED,
				inputFormat.getSampleRate(),
				inputFormat.getSampleSizeInBits(),
				inputFormat.getChannels(),
				inputFormat.getFrameSize(),
				inputFormat.getFrameRate(),
				inputFormat.isBigEndian());
			if (TDebug.TraceAudioFileWriter)
			{
				TDebug.out("TAudioFileWriter.findConvertableFormat(): trying output format: " + outputFormat);
			}
			if (isAudioFormatSupported(outputFormat, fileType))
			{
				return outputFormat;
			}
			else
			{
				return null;
			}
		}
		else if (inputEncoding.equals(PCM_SIGNED) &&
			 (inputFormat.getSampleSizeInBits() == 16 ||
			  inputFormat.getSampleSizeInBits() == 24 ||
			  inputFormat.getSampleSizeInBits() == 32) )
		{
			// TODO: possible to allow all sample sized > 8 bit?
			AudioFormat	outputFormat = new AudioFormat(
				PCM_SIGNED,
				inputFormat.getSampleRate(),
				inputFormat.getSampleSizeInBits(),
				inputFormat.getChannels(),
				inputFormat.getFrameSize(),
				inputFormat.getFrameRate(),
				! inputFormat.isBigEndian());
			if (TDebug.TraceAudioFileWriter)
			{
				TDebug.out("TAudioFileWriter.findConvertableFormat(): trying output format: " + outputFormat);
			}
			if (isAudioFormatSupported(outputFormat, fileType))
			{
				return outputFormat;
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}


	// TODO: should use TConversionTool.changeOrderOrSign()
	// $$fb why not ?
	/*private void changeOrderOrSign(byte[] buffer, int nOffset, int nByteLength, int nBytesPerSample)
	{
		switch (nBytesPerSample)
		{
		case 1:
			TConversionTool.convertSign8(buffer, nOffset, nByteLength);
			break;

		case 2:
			TConversionTool.swapOrder16(buffer, nOffset, nByteLength / 2);
			break;

		case 3:
			TConversionTool.swapOrder24(buffer, nOffset, nByteLength / 3);
			break;

		case 4:
			TConversionTool.swapOrder32(buffer, nOffset, nByteLength / 4);
			break;
		}
	}*/

}



/*** TAudioFileWriter.java ***/
