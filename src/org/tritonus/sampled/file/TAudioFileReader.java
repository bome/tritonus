/*
 *	TAudioFileReader.java
 */

/*
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


import	java.io.BufferedInputStream;
import	java.io.File;
import	java.io.FileInputStream;
import	java.io.DataInputStream;
import	java.io.InputStream;
import	java.io.IOException;
import	java.io.EOFException;

import	java.net.URL;

import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioInputStream;
import	javax.sound.sampled.AudioFileFormat;
import	javax.sound.sampled.UnsupportedAudioFileException;
import	javax.sound.sampled.spi.AudioFileReader;

/**
 * Base class for audio file readers.
 *
 * @author Matthias Pfisterer
 */

public abstract class TAudioFileReader
	extends	AudioFileReader
{
	public AudioFileFormat getAudioFileFormat(File file)
		throws	UnsupportedAudioFileException, IOException
	{
		InputStream	inputStream = new FileInputStream(file);
		try
		{
			return getAudioFileFormat(inputStream);
		}
		finally
		{
			inputStream.close();
		}
	}



	public AudioFileFormat getAudioFileFormat(URL url)
		throws	UnsupportedAudioFileException, IOException

	{
		InputStream	inputStream = url.openStream();
		try
		{
			return getAudioFileFormat(inputStream);
		}
		finally
		{
			inputStream.close();
		}
	}



	public AudioInputStream getAudioInputStream(File file)
		throws	UnsupportedAudioFileException, IOException
	{
		InputStream	inputStream = new FileInputStream(file);
		try
		{
			return getAudioInputStream(inputStream);
		}
		catch (UnsupportedAudioFileException e)
		{
			inputStream.close();
			throw e;
		}
		catch (IOException e)
		{
			inputStream.close();
			throw e;
		}
	}



	public AudioInputStream getAudioInputStream(URL url)
		throws	UnsupportedAudioFileException, IOException
	{
		InputStream	inputStream = url.openStream();
		try
		{
			return getAudioInputStream(inputStream);
		}
		catch (UnsupportedAudioFileException e)
		{
			inputStream.close();
			throw e;
		}
		catch (IOException e)
		{
			inputStream.close();
			throw e;
		}
	}



	public AudioInputStream getAudioInputStream(InputStream inputStream)
		throws	UnsupportedAudioFileException, IOException
	{
		AudioFileFormat	audioFileFormat = getAudioFileFormat(inputStream);
		return new AudioInputStream(inputStream, audioFileFormat.getFormat(), audioFileFormat.getFrameLength());
	}



	public static int readLittleEndianInt(InputStream is)
		throws	IOException
	{
		int	b0 = is.read();
		int	b1 = is.read();
		int	b2 = is.read();
		int	b3 = is.read();
		if ((b0 | b1 | b2 | b3) < 0)
		{
			throw new EOFException();
		}
		return (b3 << 24) + (b2 << 16) + (b1 << 8) + (b0 << 0);
	}



	public static short readLittleEndianShort(InputStream is)
		throws	IOException
	{
		int	b0 = is.read();
		int	b1 = is.read();
		if ((b0 | b1) < 0)
		{
			throw new EOFException();
		}
		return (short) ((b1 << 8) + (b0 << 0));
	}
	
/*
 * C O N V E R T   F R O M   I E E E   E X T E N D E D  
 */
	
/* 
 * Copyright (C) 1988-1991 Apple Computer, Inc.
 * All rights reserved.
 *
 * Machine-independent I/O routines for IEEE floating-point numbers.
 *
 * NaN's and infinities are converted to HUGE_VAL or HUGE, which
 * happens to be infinity on IEEE machines.  Unfortunately, it is
 * impossible to preserve NaN's in a machine-independent way.
 * Infinities are, however, preserved on IEEE machines.
 *
 * These routines have been tested on the following machines:
 *    Apple Macintosh, MPW 3.1 C compiler
 *    Apple Macintosh, THINK C compiler
 *    Silicon Graphics IRIS, MIPS compiler
 *    Cray X/MP and Y/MP
 *    Digital Equipment VAX
 *
 *
 * Implemented by Malcolm Slaney and Ken Turkowski.
 *
 * Malcolm Slaney contributions during 1988-1990 include big- and little-
 * endian file I/O, conversion to and from Motorola's extended 80-bit
 * floating-point format, and conversions to and from IEEE single-
 * precision floating-point format.
 *
 * In 1991, Ken Turkowski implemented the conversions to and from
 * IEEE double-precision format, added more precision to the extended
 * conversions, and accommodated conversions involving +/- infinity,
 * NaN's, and denormalized numbers.
 */

	public static double readIeeeExtended(DataInputStream dis)
		throws IOException
	{
		double f = 0.0D;
		int expon = 0;
		long hiMant = 0L;
		long loMant = 0L;
		double HUGE = 3.4028234663852886E+038D;
		expon = dis.readUnsignedShort();
		long t1 = dis.readUnsignedShort();
		long t2 = dis.readUnsignedShort();
		hiMant = t1 << 16 | t2;
		t1 = dis.readUnsignedShort();
		t2 = dis.readUnsignedShort();
		loMant = t1 << 16 | t2;
		if(expon == 0 && hiMant == 0L && loMant == 0L)
		{
			f = 0.0D;
		}
		else
		{
			if(expon == 32767)
			{
				f = HUGE;
			}
			else
			{
				expon -= 16383;
				expon -= 31;
				f = hiMant * Math.pow(2D, expon);
				expon -= 32;
				f += loMant * Math.pow(2D, expon);
			}
		}
		return f;
	}
	
	
}



/*** TAudioFileReader.java ***/

