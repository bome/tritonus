/*
 *	WaveTool.java
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


import	javax.sound.sampled.AudioFormat;

/**	
 * Common constants and methods for handling wave files.
 *
 * @author Florian Bomers
 */

public class WaveTool {

	public static final int	WAVE_RIFF_MAGIC = 0x52494646;
	public static final int	WAVE_WAVE_MAGIC = 0x057415645;
	public static final int	WAVE_FMT_MAGIC = 0x0666D7420;
	public static final int	WAVE_DATA_MAGIC = 0x064617461;

	public static final short	WAVE_FORMAT_UNSPECIFIED = 0;
	public static final short	WAVE_FORMAT_PCM = 0x0001;
	public static final short	WAVE_FORMAT_MS_ADPCM = 0x0002;
	public static final short	WAVE_FORMAT_ALAW = 0x0006;
	public static final short	WAVE_FORMAT_ULAW = 0x0007;
	public static final short	WAVE_FORMAT_IMA_ADPCM = 17;
	public static final short	WAVE_FORMAT_G723_ADPCM = 20;
	public static final short	WAVE_FORMAT_GSM_610 = 49;
	public static final short	WAVE_FORMAT_G721_ADPCM = 64;
	public static final short	WAVE_FORMAT_MPEG = 80;

	public static final int MIN_FMT_CHUNK_LENGTH=14;
	public static final int MIN_DATA_OFFSET=12+8+MIN_FMT_CHUNK_LENGTH+8;

	// we always write the sample size in bits.
	// There are programs (CoolEdit) that rely on the fact
	// that there is the entry for sample size in bits.
	public static final int FMT_CHUNK_SIZE=16;
	public static final int RIFF_CONTAINER_CHUNK_SIZE=12;
	public static final int CHUNK_HEADER_SIZE=8;
	public static final int DATA_OFFSET=RIFF_CONTAINER_CHUNK_SIZE
			+CHUNK_HEADER_SIZE+FMT_CHUNK_SIZE+CHUNK_HEADER_SIZE;

	public static short getFormatCode(AudioFormat format)
	{
		AudioFormat.Encoding	encoding = format.getEncoding();
		int		nSampleSize = format.getSampleSizeInBits();
		boolean smallEndian = !format.isBigEndian();
		
		if ((encoding.equals(AudioFormat.Encoding.PCM_SIGNED) && smallEndian)
				|| (encoding.equals(AudioFormat.Encoding.PCM_UNSIGNED) && nSampleSize==8))
		{
			return WAVE_FORMAT_PCM;
		}
		else 
		if (encoding.equals(AudioFormat.Encoding.ULAW) && nSampleSize == 8)
		{
			return WAVE_FORMAT_ULAW;
		}
		else 
		if (encoding.equals(AudioFormat.Encoding.ALAW) && nSampleSize == 8)
		{
			return WAVE_FORMAT_ALAW;
		}
		else
		{
			return WAVE_FORMAT_UNSPECIFIED;
		}
	}

}

/*** WaveTool.java ***/
