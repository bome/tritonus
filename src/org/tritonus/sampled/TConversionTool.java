/*
 *	TConversionTool.java
 */

/*
 *  Copyright (c) 1999,2000 by Florian Bomers <florian@bome.com>
 *  Copyright (c) 2000 by Matthias Pfisterer <matthias.pfisterer@gmx.de>
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

package	org.tritonus.sampled;


/**
 * Useful methods for converting audio data.
 *
 * @author Florian Bomers
 * @author Matthias Pfisterer
 */

// TODO: clean-up the ulaw functions
// TODO: the buffer-based alaw2linear, ulaw2alaw and alaw2ulaw direct access to the array

 /*
 For convenience, a list of available methods is maintained here.
 Some hints:
 - buffers: always byte arrays
 - offsets: always in bytes
 - sampleCount: number of SAMPLES to read/write, as opposed to FRAMES !
 - when in buffer and out buffer are given, the data is copied,
   otherwise it is replaced in the same buffer (buffer size is not checked!)
 - a number (except "2") gives the number of bits in which format
   the samples have to be.
 - >8 bits per sample is always treated signed.
 - all functions are tried to be optimized
 
 void 	convertSign8(buffer, byteOffset, sampleCount)
 void 	swapOrder16(buffer, byteOffset, sampleCount)
 void 	swapOrder24(buffer, byteOffset, sampleCount)
 void 	swapOrder32(buffer, byteOffset, sampleCount)
 void 	convertSign8(inBuffer, inByteOffset, outBuffer, outByteOffset, sampleCount)
 void 	swapOrder16(inBuffer, inByteOffset, outBuffer, outByteOffset, sampleCount)
 void 	swapOrder24(inBuffer, inByteOffset, outBuffer, outByteOffset, sampleCount)
 void 	swapOrder32(inBuffer, inByteOffset, outBuffer, outByteOffset, sampleCount)
 
 short 	convertBytesToShort(highByte, lowByte)
 void 	convertShortToLittleEndianBytes(sample, buffer, byteOffset)
 void 	convertShortToBigEndianBytes(sample, buffer, byteOffset)
 
 byte 	linear2ulaw(int sample)
 short 	ulaw2linear(int ulawbyte)
 byte 	alaw2ulaw(byte sample)
 byte 	ulaw2alaw(byte sample)
 void 	pcm16BigEndian2ulaw(buffer, byteOffset, sampleCount)
 void 	pcm16LittleEndian2ulaw(buffer, byteOffset, sampleCount)
 void 	pcm16BigEndian2ulaw(inBuffer, inByteOffset, outBuffer, outByteOffset, sampleCount)
 void 	pcm16LittleEndian2ulaw(inBuffer, inByteOffset, outBuffer, outByteOffset, sampleCount)
 void 	pcm8Signed2ulaw(buffer, byteOffset, sampleCount)
 void 	pcm8Unsigned2ulaw(buffer, byteOffset, sampleCount)
 void 	pcm8Signed2ulaw(inBuffer, inByteOffset, outBuffer, outByteOffset, sampleCount)
 void 	pcm8Unsigned2ulaw(inBuffer, inByteOffset, outBuffer, outByteOffset, sampleCount)
 void 	ulaw2pcm16BigEndian(inBuffer, inByteOffset, outBuffer, outByteOffset, sampleCount)
 void 	ulaw2pcm16LittleEndian(inBuffer, inByteOffset, outBuffer, outByteOffset, sampleCount)
 void 	ulaw2pcm8Signed(buffer, byteOffset, sampleCount)
 void 	ulaw2pcm8Signed(inBuffer, inByteOffset, outBuffer, outByteOffset, sampleCount)
 void 	ulaw2pcm8Unsigned(inBuffer, inByteOffset, outBuffer, outByteOffset, sampleCount)
 void 	alaw2ulaw(buffer, byteOffset, sampleCount)
 void 	alaw2ulaw(inBuffer, inByteOffset, outBuffer, outByteOffset, sampleCount)
 void 	ulaw2alaw(buffer, byteOffset, sampleCount)
 void 	ulaw2alaw(inBuffer, inByteOffset, outBuffer, outByteOffset, sampleCount)

 byte linear2alaw(short pcm_val)
 short alaw2linear(byte ulawbyte)
 void pcm162alaw(inBuffer, inByteOffset, outBuffer, outByteOffset, sampleCount, bigEndian)
 void pcm162alaw(buffer, byteOffset, sampleCount, bigEndian)
 void pcm82alaw(buffer, byteOffset, sampleCount, signed)
 void pcm82alaw(inBuffer, inByteOffset, outBuffer, outByteOffset, sampleCount, signed)
 void alaw2pcm8(buffer, byteOffset, sampleCount, signed)
 void alaw2pcm8(inBuffer, inByteOffset, outBuffer, outByteOffset, sampleCount, signed)
 void alaw2pcm16(inBuffer, inByteOffset, outBuffer, outByteOffset, sampleCount, bigEndian)

*/

public class TConversionTool
{
	
	//TODO: 24bit and 32bit aren't tested !
	
	/////////// sign/byte-order conversion /////////////////////////////////////////////
	public static void convertSign8(byte[] buffer, int byteOffset, int sampleCount) {
		sampleCount+=byteOffset;
		for (int i=byteOffset; i<sampleCount; i++) {
			buffer[i]+=128; // todo: check that this conversion is correct
		}
	}
	
	public static void swapOrder16(byte[] buffer, int byteOffset, int sampleCount) {
		int byteMax=sampleCount*2+byteOffset-1;
		int i=byteOffset;
		while (i<byteMax) {
			byte h=buffer[i];
			buffer[i]=buffer[++i];
			buffer[i++]=h;
		}
	}
			
	public static void swapOrder24(byte[] buffer, int byteOffset, int sampleCount) {
		int byteMax=sampleCount*3+byteOffset-2;
		int i=byteOffset;
		while (i<byteMax) {
			byte h=buffer[i];
			buffer[i]=buffer[++i+1];
			buffer[++i]=h;
			i++;
		}
	}
	
	public static void swapOrder32(byte[] buffer, int byteOffset, int sampleCount) {
		int byteMax=sampleCount*4+byteOffset-3;
		int i=byteOffset;
		while (i<byteMax) {
			byte h=buffer[i];
			buffer[i]=buffer[i+3];
			buffer[i+3]=h;
			i++;
			h=buffer[i];
			buffer[i]=buffer[++i];
			buffer[i++]=h;
			i++;
		}
	}

	public static void convertSign8(byte[] inBuffer, int inByteOffset, 
			byte[] outBuffer, int outByteOffset, int sampleCount) {
		while (sampleCount>0) {
			outBuffer[outByteOffset++]=(byte)(inBuffer[inByteOffset++]+128);
			sampleCount--;
		}
	}
	
	public static void swapOrder16(byte[] inBuffer, int inByteOffset, 
			byte[] outBuffer, int outByteOffset, int sampleCount) {
		while (sampleCount>0) {
			outBuffer[outByteOffset++]=inBuffer[inByteOffset+1];
			outBuffer[outByteOffset++]=inBuffer[inByteOffset++];
			inByteOffset++;
			sampleCount--;
		}
	}
			
	public static void swapOrder24(byte[] inBuffer, int inByteOffset, 
			byte[] outBuffer, int outByteOffset, int sampleCount) {
		while (sampleCount>0) {
			outBuffer[outByteOffset++]=inBuffer[inByteOffset+2];
			outByteOffset++;
			outBuffer[outByteOffset++]=inBuffer[inByteOffset++];
			inByteOffset++; inByteOffset++;
			sampleCount--;
		}
	}
	
	public static void swapOrder32(byte[] inBuffer, int inByteOffset, 
			byte[] outBuffer, int outByteOffset, int sampleCount) {
		while (sampleCount>0) {
			outBuffer[outByteOffset++]=inBuffer[inByteOffset+3];
			outBuffer[outByteOffset++]=inBuffer[inByteOffset+2];
			outBuffer[outByteOffset++]=inBuffer[inByteOffset+1];
			outBuffer[outByteOffset++]=inBuffer[inByteOffset++];
			inByteOffset++; inByteOffset++; inByteOffset++;
			sampleCount--;
		}
	}
	
	public static short convertBytesToShort(byte highByte, byte lowByte) {
		return (short) ((highByte<<8) | (lowByte & 0xFF));
	}

	/**
	 * Fills buffer[byteOffset] with low byte of sample.
	 * Fills buffer[byteOffset+1] with high byte of sample.
	 */
	public static void convertShortToLittleEndianBytes(short sample, byte[] buffer, int byteOffset) {
		buffer[byteOffset++]=(byte) (sample & 0xFF);
		buffer[byteOffset]=(byte) ((sample & 0xFF00) >> 8);
	}
	
	/**
	 * Fills buffer[byteOffset] with high byte of sample.
	 * Fills buffer[byteOffset+1] with low byte of sample.
	 */
	public static void convertShortToBigEndianBytes(short sample, byte[] buffer, int byteOffset) {
		buffer[byteOffset++]=(byte) ((sample & 0xFF00) >> 8);
		buffer[byteOffset]=(byte) (sample & 0xFF);
	}

/////////////////////// ULAW ///////////////////////////////////////////

  private static final boolean ZEROTRAP=true;  
  private static final short BIAS=0x84;
  private static final int CLIP=32635;
  private static final int exp_lut1[] ={
      0,0,1,1,2,2,2,2,3,3,3,3,3,3,3,3,
      4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,
      5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,
      5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,
      6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,
      6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,
      6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,
      6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,
      7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,
      7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,
      7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,
      7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,
      7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,
      7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,
      7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,
      7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7
  };
  
  private static final int exp_lut2[] = {
    0,132,396,924,1980,4092,8316,16764
  };
  

  /** 
  * Converts a linear signed 16bit sample to a uLaw byte.
  * Ported to Java by fb.
  * <BR>Originally by:<BR>
  * Craig Reese: IDA/Supercomputing Research Center <BR>
  * Joe Campbell: Department of Defense <BR>
  * 29 September 1989 <BR>
  */
  public static byte linear2ulaw(int sample) {
    int sign, exponent, mantissa, ulawbyte;
    
    if (sample>32767) sample=32767;
    else if (sample<-32768) sample=-32768;
    /* Get the sample into sign-magnitude. */
    sign = (sample >> 8) & 0x80;    /* set aside the sign */
    if (sign != 0) sample = -sample;    /* get magnitude */
    if (sample > CLIP) sample = CLIP;    /* clip the magnitude */
    
    /* Convert from 16 bit linear to ulaw. */
    sample = sample + BIAS;
    exponent = exp_lut1[(sample >> 7) & 0xFF];
    mantissa = (sample >> (exponent + 3)) & 0x0F;
    ulawbyte = ~(sign | (exponent << 4) | mantissa);
    if (ZEROTRAP)
      if (ulawbyte == 0) ulawbyte = 0x02;  /* optional CCITT trap */
    
    return((byte) ulawbyte);
  }
  
	private static short[] _u2l = {			/* u-law to linear conversion table */
		-32124, -31100, -30076, -29052, -28028, -27004, -25980, -24956, 
		-23932, -22908, -21884, -20860, -19836, -18812, -17788, -16764, 
		-15996, -15484, -14972, -14460, -13948, -13436, -12924, -12412, 
		-11900, -11388, -10876, -10364, -9852, -9340, -8828, -8316, 
		-7932, -7676, -7420, -7164, -6908, -6652, -6396, -6140, 
		-5884, -5628, -5372, -5116, -4860, -4604, -4348, -4092, 
		-3900, -3772, -3644, -3516, -3388, -3260, -3132, -3004, 
		-2876, -2748, -2620, -2492, -2364, -2236, -2108, -1980, 
		-1884, -1820, -1756, -1692, -1628, -1564, -1500, -1436, 
		-1372, -1308, -1244, -1180, -1116, -1052, -988, -924, 
		-876, -844, -812, -780, -748, -716, -684, -652, 
		-620, -588, -556, -524, -492, -460, -428, -396, 
		-372, -356, -340, -324, -308, -292, -276, -260, 
		-244, -228, -212, -196, -180, -164, -148, -132, 
		-120, -112, -104, -96, -88, -80, -72, -64, 
		-56, -48, -40, -32, -24, -16, -8, 0, 
		32124, 31100, 30076, 29052, 28028, 27004, 25980, 24956, 
		23932, 22908, 21884, 20860, 19836, 18812, 17788, 16764, 
		15996, 15484, 14972, 14460, 13948, 13436, 12924, 12412, 
		11900, 11388, 10876, 10364, 9852, 9340, 8828, 8316, 
		7932, 7676, 7420, 7164, 6908, 6652, 6396, 6140, 
		5884, 5628, 5372, 5116, 4860, 4604, 4348, 4092, 
		3900, 3772, 3644, 3516, 3388, 3260, 3132, 3004, 
		2876, 2748, 2620, 2492, 2364, 2236, 2108, 1980, 
		1884, 1820, 1756, 1692, 1628, 1564, 1500, 1436, 
		1372, 1308, 1244, 1180, 1116, 1052, 988, 924, 
		876, 844, 812, 780, 748, 716, 684, 652, 
		620, 588, 556, 524, 492, 460, 428, 396, 
		372, 356, 340, 324, 308, 292, 276, 260, 
		244, 228, 212, 196, 180, 164, 148, 132, 
		120, 112, 104, 96, 88, 80, 72, 64, 
		56, 48, 40, 32, 24, 16, 8, 0
	};
	public static short ulaw2linear(byte ulawbyte) {
		return _u2l[ulawbyte & 0xFF];
	}
      
  
  /** 
  * Converts a uLaw byte to a linear signed 16bit sample.
  * Ported to Java by fb.
  * <BR>Originally by:<BR>
  *
  * Craig Reese: IDA/Supercomputing Research Center <BR>
  * 29 September 1989 <BR>
  *
  * References: <BR>
  * <OL>
  * <LI>CCITT Recommendation G.711  (very difficult to follow)</LI>
  * <LI>MIL-STD-188-113,"Interoperability and Performance Standards
  *     for Analog-to_Digital Conversion Techniques,"
  *     17 February 1987</LI>
  * </OL>
  */
  /*public static short ulaw2linear(int ulawbyte) {
    int sign, exponent, mantissa, sample;
    
    ulawbyte = ~ulawbyte;
    sign = (ulawbyte & 0x80);
    exponent = (ulawbyte >> 4) & 0x07;
    mantissa = ulawbyte & 0x0F;
    sample = exp_lut2[exponent] + (mantissa << (exponent + 3));
    if (sign != 0) sample = -sample;
    return((short) sample);
  }*/
  
	/** 
	 * alaw2ulaw converts one A-Law sample to u-Law
	 * (c) Florian Bomers :)
	 */
	/*public static byte alaw2ulaw(byte sample) {
		sample ^= 0x55;
		byte seg = (byte) (sample & 0x70);
		if (seg==0) {
			sample &= 0x80;		//preserve only sign bit ->minimum value in u-Law
		}
  		return (byte) (sample ^ 0x7F);
	}*/

	/**
	 * ulaw2alaw converts one u-Law sample to A-Law
	 */
	// TODO: this doesn't work well (the exponent seems one too much ?)
	/*public static byte ulaw2alaw(byte sample) {
		sample ^= 0x7F;
		byte seg = (byte) (sample & 0x70);
		if ((seg & 0xFF)==0x70)
			sample  |= 0x7F;    //maximum value in A-Law encoding
		//else
		//	sample=(byte) ((sample & 0xFF)+16);
		return (byte) (sample ^ 0x55);
	}*/
  
/* copy from CCITT G.711 specifications */
	private static byte[] _u2a = {			/* u- to A-law conversions */
	1,	1,	2,	2,	3,	3,	4,	4,
	5,	5,	6,	6,	7,	7,	8,	8,
	9,	10,	11,	12,	13,	14,	15,	16,
	17,	18,	19,	20,	21,	22,	23,	24,
	25,	27,	29,	31,	33,	34,	35,	36,
	37,	38,	39,	40,	41,	42,	43,	44,
	46,	48,	49,	50,	51,	52,	53,	54,
	55,	56,	57,	58,	59,	60,	61,	62,
	64,	65,	66,	67,	68,	69,	70,	71,
	72,	73,	74,	75,	76,	77,	78,	79,
	81,	82,	83,	84,	85,	86,	87,	88,
	89,	90,	91,	92,	93,	94,	95,	96,
	97,	98,	99,	100,	101,	102,	103,	104,
	105,	106,	107,	108,	109,	110,	111,	112,
	113,	114,	115,	116,	117,	118,	119,	120,
	121,	122,	123,	124,	125,	126,	127,	(byte) 128};

	private static byte[] _a2u = {			/* A- to u-law conversions */
		1,	3,	5,	7,	9,	11,	13,	15,
		16,	17,	18,	19,	20,	21,	22,	23,
		24,	25,	26,	27,	28,	29,	30,	31,
		32,	32,	33,	33,	34,	34,	35,	35,
		36,	37,	38,	39,	40,	41,	42,	43,
		44,	45,	46,	47,	48,	48,	49,	49,
		50,	51,	52,	53,	54,	55,	56,	57,
		58,	59,	60,	61,	62,	63,	64,	64,
		65,	66,	67,	68,	69,	70,	71,	72,
		73,	74,	75,	76,	77,	78,	79,	79,
		80,	81,	82,	83,	84,	85,	86,	87,
		88,	89,	90,	91,	92,	93,	94,	95,
		96,	97,	98,	99,	100,	101,	102,	103,
		104,	105,	106,	107,	108,	109,	110,	111,
		112,	113,	114,	115,	116,	117,	118,	119,
		120,	121,	122,	123,	124,	125,	126,	127};

	/*
	 * This source code is a product of Sun Microsystems, Inc. and is provided
	 * for unrestricted use.  Users may copy or modify this source code without
	 * charge.
	 */
	/** A-law to u-law conversion */
	// TODO: complete table ?
	public static byte alaw2ulaw(byte sample) {
		sample &= 0xff;
		return (byte) (((sample & 0x80)!=0) ? (0xFF ^ _a2u[(sample ^ 0xD5) & 0x7F]) :
		    (0x7F ^ _a2u[(sample ^ 0x55) & 0x7F]));
	}
	
	/*
	 * This source code is a product of Sun Microsystems, Inc. and is provided
	 * for unrestricted use.  Users may copy or modify this source code without
	 * charge.
	 */
	/** u-law to A-law conversion */
	// TODO: complete table ?
	public static byte ulaw2alaw(byte sample) {
		sample &= 0xff;
		return (byte) (((sample & 0x80)!=0) ? (0xD5 ^ (_u2a[(0x7F ^ sample) & 0x7F] - 1)) :
		    (0x55 ^ (_u2a[(0x7F ^ sample) & 0x7F] - 1)));
	}
  
  
	// there are all these specialized methods for optimization.
	
	/**
	 * Converts a buffer of signed 16bit big endian samples to uLaw.
	 * The uLaw bytes overwrite the original 16 bit values.
	 * The first byte-offset of the uLaw bytes is byteOffset.
	 * It will be written sampleCount/2 bytes.
	 */
	// **NOT TESTED**
	public static void pcm16BigEndian2ulaw(byte[] buffer, int byteOffset, int sampleCount) {
		int shortIndex=byteOffset;
		int ulawIndex=shortIndex;
		while (sampleCount>0) {
			buffer[ulawIndex++]=linear2ulaw(convertBytesToShort(buffer[shortIndex], buffer[shortIndex+1]));
			shortIndex++; shortIndex++;
			sampleCount--;
		}
	}
	
	// **NOT TESTED**
	public static void pcm16LittleEndian2ulaw(byte[] buffer, int byteOffset, int sampleCount) {
		int shortIndex=byteOffset;
		int ulawIndex=shortIndex;
		while (sampleCount>0) {
			buffer[ulawIndex++]=linear2ulaw(convertBytesToShort(buffer[shortIndex+1], buffer[shortIndex]));
			shortIndex++; shortIndex++;
			sampleCount--;
		}
	}
	
	/**
	 * Fills outBuffer with ulaw samples.
	 * reading starts from inBuffer[inByteOffset].
	 * writing starts at outBuffer[outByteOffset].
	 * There will be sampleCount*2 bytes read from inBuffer;
	 * There will be sampleCount <B>bytes</B> written to outBuffer.
	 */
	public static void pcm16BigEndian2ulaw(byte[] inBuffer, int inByteOffset, 
			byte[] outBuffer, int outByteOffset, int sampleCount) {
		int shortIndex=inByteOffset;
		int ulawIndex=outByteOffset;
		while (sampleCount>0) {
			outBuffer[ulawIndex++]=linear2ulaw(convertBytesToShort(inBuffer[shortIndex], inBuffer[shortIndex+1]));
			shortIndex++; shortIndex++;
			sampleCount--;
		}
	}
	
	public static void pcm16LittleEndian2ulaw(byte[] inBuffer, int inByteOffset, 
			byte[] outBuffer, int outByteOffset, int sampleCount) {
		int shortIndex=inByteOffset;
		int ulawIndex=outByteOffset;
		while (sampleCount>0) {
			outBuffer[ulawIndex++]=linear2ulaw(convertBytesToShort(inBuffer[shortIndex+1], inBuffer[shortIndex]));
			shortIndex++; shortIndex++;
			sampleCount--;
		}
	}

	// TODO: either direct 8bit pcm to ulaw, or better conversion from 8bit to 16bit
	/**
	 * Converts a buffer of signed 8bit samples to uLaw.
	 * The uLaw bytes overwrite the original 8 bit values.
	 * The first byte-offset of the uLaw bytes is byteOffset.
	 * It will be written sampleCount bytes.
	 */
	public static void pcm8Signed2ulaw(byte[] buffer, int byteOffset, int sampleCount) {
		sampleCount+=byteOffset;
		for (int i=byteOffset; i<sampleCount; i++) {
			buffer[i]=linear2ulaw(buffer[i] << 8);
		}
	}
	
	public static void pcm8Unsigned2ulaw(byte[] buffer, int byteOffset, int sampleCount) {
		sampleCount+=byteOffset;
		for (int i=byteOffset; i<sampleCount; i++) {
			buffer[i]=linear2ulaw(((byte) (buffer[i]+128)) << 8);
		}
	}
	
	/**
	 * Fills outBuffer with ulaw samples.
	 * reading starts from inBuffer[inByteOffset].
	 * writing starts at outBuffer[outByteOffset].
	 * There will be sampleCount <B>bytes</B> written to outBuffer.
	 */
	public static void pcm8Signed2ulaw(byte[] inBuffer, int inByteOffset, 
			byte[] outBuffer, int outByteOffset, int sampleCount) {
		int ulawIndex=outByteOffset;
		int pcmIndex=inByteOffset;
		while (sampleCount>0) {
			outBuffer[ulawIndex++]=linear2ulaw(inBuffer[pcmIndex++] << 8);
			sampleCount--;
		}
	}
	
	public static void pcm8Unsigned2ulaw(byte[] inBuffer, int inByteOffset, 
			byte[] outBuffer, int outByteOffset, int sampleCount) {
		int ulawIndex=outByteOffset;
		int pcmIndex=inByteOffset;
		while (sampleCount>0) {
			outBuffer[ulawIndex++]=linear2ulaw(((byte) (inBuffer[pcmIndex++]+128)) << 8);
			sampleCount--;
		}
	}

	/**
	 * Fills outBuffer with pcm signed 16 bit samples.
	 * reading starts from inBuffer[inByteOffset].
	 * writing starts at outBuffer[outByteOffset].
	 * There will be sampleCount bytes read from inBuffer;
	 * There will be sampleCount*2 bytes written to outBuffer.
	 */
	public static void ulaw2pcm16BigEndian(byte[] inBuffer, int inByteOffset, 
			byte[] outBuffer, int outByteOffset, int sampleCount) {
		int shortIndex=outByteOffset;
		int ulawIndex=inByteOffset;
		while (sampleCount>0) {
			//convertShortToBigEndianBytes(ulaw2linear(inBuffer[ulawIndex++]), outBuffer, shortIndex++);
			convertShortToBigEndianBytes(_u2l[inBuffer[ulawIndex++] & 0xFF], outBuffer, shortIndex++);
			shortIndex++;
			sampleCount--;
		}
	}
	
	public static void ulaw2pcm16LittleEndian(byte[] inBuffer, int inByteOffset, 
			byte[] outBuffer, int outByteOffset, int sampleCount) {
		int shortIndex=outByteOffset;
		int ulawIndex=inByteOffset;
		while (sampleCount>0) {
			//convertShortToLittleEndianBytes(ulaw2linear(inBuffer[ulawIndex++]), outBuffer, shortIndex++);
			convertShortToLittleEndianBytes(_u2l[inBuffer[ulawIndex++] & 0xFF], outBuffer, shortIndex++);
			shortIndex++;
			sampleCount--;
		}
	}

	// TODO: either direct 8bit pcm to ulaw, or better conversion from 8bit to 16bit
	/**
	 * Converts a buffer ulaw to signed 8bit samples
	 * The 8bit bytes overwrite the original ulaw values.
	 * The first byte-offset of the uLaw bytes is byteOffset.
	 * It will be written sampleCount bytes.
	 */
	public static void ulaw2pcm8Signed(byte[] buffer, int byteOffset, int sampleCount) {
		sampleCount+=byteOffset;
		for (int i=byteOffset; i<sampleCount; i++) {
			//buffer[i]=(byte) ((ulaw2linear(buffer[i]) >> 8) & 0xFF);
			buffer[i]=(byte) ((_u2l[buffer[i] & 0xFF] >> 8) & 0xFF);
		}
	}
	
	public static void ulaw2pcm8Unsigned(byte[] buffer, int byteOffset, int sampleCount) {
		sampleCount+=byteOffset;
		for (int i=byteOffset; i<sampleCount; i++) {
			//buffer[i]=(byte) ((ulaw2linear(buffer[i])>>8)+128);
			buffer[i]=(byte) ((_u2l[buffer[i] & 0xFF]>>8)+128);
		}
	}
	
	/**
	 * Fills outBuffer with ulaw samples.
	 * reading starts from inBuffer[inByteOffset].
	 * writing starts at outBuffer[outByteOffset].
	 * There will be sampleCount <B>bytes</B> written to outBuffer.
	 */
	public static void ulaw2pcm8Signed(byte[] inBuffer, int inByteOffset, 
			byte[] outBuffer, int outByteOffset, int sampleCount) {
		int ulawIndex=inByteOffset;
		int pcmIndex=outByteOffset;
		while (sampleCount>0) {
			//outBuffer[pcmIndex++]=(byte) ((ulaw2linear(inBuffer[ulawIndex++]) >> 8) & 0xFF);
			outBuffer[pcmIndex++]=(byte) ((_u2l[inBuffer[ulawIndex++] & 0xFF] >> 8) & 0xFF);
			sampleCount--;
		}
	}
	
	public static void ulaw2pcm8Unsigned(byte[] inBuffer, int inByteOffset, 
			byte[] outBuffer, int outByteOffset, int sampleCount) {
		int ulawIndex=inByteOffset;
		int pcmIndex=outByteOffset;
		while (sampleCount>0) {
			//outBuffer[pcmIndex++]=(byte) ((ulaw2linear(inBuffer[ulawIndex++])>>8)+128);
			outBuffer[pcmIndex++]=(byte) ((_u2l[inBuffer[ulawIndex++] & 0xFF]>>8)+128);
			sampleCount--;
		}
	}


	/**
	 * Converts a buffer of aLaw samples to uLaw.
	 * The uLaw bytes overwrite the original aLaw values.
	 * The first byte-offset of the uLaw bytes is byteOffset.
	 * It will be written sampleCount bytes.
	 */
	public static void alaw2ulaw(byte[] buffer, int byteOffset, int sampleCount) {
		sampleCount+=byteOffset;
		for (int i=byteOffset; i<sampleCount; i++) {
			buffer[i]=alaw2ulaw(buffer[i]);
		}
	}
	
	/**
	 * Fills outBuffer with ulaw samples.
	 * reading starts from inBuffer[inByteOffset].
	 * writing starts at outBuffer[outByteOffset].
	 * There will be sampleCount <B>bytes</B> written to outBuffer.
	 */
	public static void alaw2ulaw(byte[] inBuffer, int inByteOffset, 
			byte[] outBuffer, int outByteOffset, int sampleCount) {
		int ulawIndex=outByteOffset;
		int alawIndex=inByteOffset;
		while (sampleCount>0) {
			outBuffer[ulawIndex++]=alaw2ulaw(inBuffer[alawIndex++]);
			sampleCount--;
		}
	}
	
	/**
	 * Converts a buffer of uLaw samples to aLaw.
	 */
	public static void ulaw2alaw(byte[] buffer, int byteOffset, int sampleCount) {
		sampleCount+=byteOffset;
		for (int i=byteOffset; i<sampleCount; i++) {
			buffer[i]=ulaw2alaw(buffer[i]);
		}
	}
	
	/**
	 * Fills outBuffer with alaw samples.
	 */
	public static void ulaw2alaw(byte[] inBuffer, int inByteOffset, 
			byte[] outBuffer, int outByteOffset, int sampleCount) {
		int ulawIndex=outByteOffset;
		int alawIndex=inByteOffset;
		while (sampleCount>0) {
			outBuffer[alawIndex++]=ulaw2alaw(inBuffer[ulawIndex++]);
			sampleCount--;
		}
	}

//////////////////// ALAW ////////////////////////////


	/*
	 * This source code is a product of Sun Microsystems, Inc. and is provided
	 * for unrestricted use.  Users may copy or modify this source code without
	 * charge.
	 *
	 * linear2alaw() - Convert a 16-bit linear PCM value to 8-bit A-law
	 *
	 * linear2alaw() accepts an 16-bit integer and encodes it as A-law data.
	 *
	 *		Linear Input Code	Compressed Code
	 *	------------------------	---------------
	 *	0000000wxyza			000wxyz
	 *	0000001wxyza			001wxyz
	 *	000001wxyzab			010wxyz
	 *	00001wxyzabc			011wxyz
	 *	0001wxyzabcd			100wxyz
	 *	001wxyzabcde			101wxyz
	 *	01wxyzabcdef			110wxyz
	 *	1wxyzabcdefg			111wxyz
	 *
	 * For further information see John C. Bellamy's Digital Telephony, 1982,
	 * John Wiley & Sons, pps 98-111 and 472-476.
	 */
	private static final byte QUANT_MASK = 0xf;		/* Quantization field mask. */
	private static final byte SEG_SHIFT = 4;		/* Left shift for segment number. */
	private static final short[] seg_end = {
		0xFF, 0x1FF, 0x3FF, 0x7FF, 0xFFF, 0x1FFF, 0x3FFF, 0x7FFF
	};
	 
	public static byte linear2alaw(short pcm_val)	/* 2's complement (16-bit range) */
	{
		byte	mask;
		byte	seg=8;
		byte	aval;
	
		if (pcm_val >= 0) {
			mask = (byte) 0xD5;		/* sign (7th) bit = 1 */
		} else {
			mask = 0x55;		/* sign bit = 0 */
			pcm_val = (short) (-pcm_val - 8);
		}
	
		/* Convert the scaled magnitude to segment number. */
		for (int i = 0; i < 8; i++) {
			if (pcm_val <= seg_end[i]) {
				seg=(byte) i;
				break;
			}
		}
	
		/* Combine the sign, segment, and quantization bits. */
		if (seg >= 8)		/* out of range, return maximum value. */
			return (byte) ((0x7F ^ mask) & 0xFF);
		else {
			aval = (byte) (seg << SEG_SHIFT);
			if (seg < 2)
				aval |= (pcm_val >> 4) & QUANT_MASK;
			else
				aval |= (pcm_val >> (seg + 3)) & QUANT_MASK;
			return (byte) ((aval ^ mask) & 0xFF);
		}
	}

	private static short[] _a2l = {
		-5504, -5248, -6016, -5760, -4480, -4224, -4992, -4736, 
		-7552, -7296, -8064, -7808, -6528, -6272, -7040, -6784, 
		-2752, -2624, -3008, -2880, -2240, -2112, -2496, -2368, 
		-3776, -3648, -4032, -3904, -3264, -3136, -3520, -3392, 
		-22016, -20992, -24064, -23040, -17920, -16896, -19968, -18944, 
		-30208, -29184, -32256, -31232, -26112, -25088, -28160, -27136, 
		-11008, -10496, -12032, -11520, -8960, -8448, -9984, -9472, 
		-15104, -14592, -16128, -15616, -13056, -12544, -14080, -13568, 
		-344, -328, -376, -360, -280, -264, -312, -296, 
		-472, -456, -504, -488, -408, -392, -440, -424, 
		-88, -72, -120, -104, -24, -8, -56, -40, 
		-216, -200, -248, -232, -152, -136, -184, -168, 
		-1376, -1312, -1504, -1440, -1120, -1056, -1248, -1184, 
		-1888, -1824, -2016, -1952, -1632, -1568, -1760, -1696, 
		-688, -656, -752, -720, -560, -528, -624, -592, 
		-944, -912, -1008, -976, -816, -784, -880, -848, 
		5504, 5248, 6016, 5760, 4480, 4224, 4992, 4736, 
		7552, 7296, 8064, 7808, 6528, 6272, 7040, 6784, 
		2752, 2624, 3008, 2880, 2240, 2112, 2496, 2368, 
		3776, 3648, 4032, 3904, 3264, 3136, 3520, 3392, 
		22016, 20992, 24064, 23040, 17920, 16896, 19968, 18944, 
		30208, 29184, 32256, 31232, 26112, 25088, 28160, 27136, 
		11008, 10496, 12032, 11520, 8960, 8448, 9984, 9472, 
		15104, 14592, 16128, 15616, 13056, 12544, 14080, 13568, 
		344, 328, 376, 360, 280, 264, 312, 296, 
		472, 456, 504, 488, 408, 392, 440, 424, 
		88, 72, 120, 104, 24, 8, 56, 40, 
		216, 200, 248, 232, 152, 136, 184, 168, 
		1376, 1312, 1504, 1440, 1120, 1056, 1248, 1184, 
		1888, 1824, 2016, 1952, 1632, 1568, 1760, 1696, 
		688, 656, 752, 720, 560, 528, 624, 592, 
		944, 912, 1008, 976, 816, 784, 880, 848
	};

	public static short alaw2linear(byte ulawbyte) {
		return _a2l[ulawbyte & 0xFF];
	}

	/**
	 * Converts a buffer of signed 16bit big endian samples to uLaw.
	 * The uLaw bytes overwrite the original 16 bit values.
	 * The first byte-offset of the uLaw bytes is byteOffset.
	 * It will be written sampleCount/2 bytes.
	 */
	public static void pcm162alaw(byte[] buffer, int byteOffset, int sampleCount, boolean bigEndian) {
		int shortIndex=byteOffset;
		int alawIndex=shortIndex;
		if (bigEndian) {
			while (sampleCount>0) {
				buffer[alawIndex++]=linear2alaw(convertBytesToShort(buffer[shortIndex], buffer[shortIndex+1]));
				shortIndex++; shortIndex++;
				sampleCount--;
			}
		} else {
			while (sampleCount>0) {
				buffer[alawIndex++]=linear2alaw(convertBytesToShort(buffer[shortIndex+1], buffer[shortIndex]));
				shortIndex++; shortIndex++;
				sampleCount--;
			}
		}
	}
	
	/**
	 * Fills outBuffer with alaw samples.
	 * reading starts from inBuffer[inByteOffset].
	 * writing starts at outBuffer[outByteOffset].
	 * There will be sampleCount*2 bytes read from inBuffer;
	 * There will be sampleCount <B>bytes</B> written to outBuffer.
	 */
	public static void pcm162alaw(byte[] inBuffer, int inByteOffset, 
			byte[] outBuffer, int outByteOffset, int sampleCount, boolean bigEndian) {
		int shortIndex=inByteOffset;
		int alawIndex=outByteOffset;
		if (bigEndian) {
			while (sampleCount>0) {
				outBuffer[alawIndex++]=linear2alaw(convertBytesToShort(inBuffer[shortIndex], inBuffer[shortIndex+1]));
				shortIndex++; shortIndex++;
				sampleCount--;
			}
		} else {
			while (sampleCount>0) {
				outBuffer[alawIndex++]=linear2alaw(convertBytesToShort(inBuffer[shortIndex+1], inBuffer[shortIndex]));
				shortIndex++; shortIndex++;
				sampleCount--;
			}
		}
	}
	
	/**
	 * Converts a buffer of 8bit samples to alaw.
	 * The alaw bytes overwrite the original 8 bit values.
	 * The first byte-offset of the aLaw bytes is byteOffset.
	 * It will be written sampleCount bytes.
	 */
	public static void pcm82alaw(byte[] buffer, int byteOffset, int sampleCount, boolean signed) {
		sampleCount+=byteOffset;
		if (signed) {
			for (int i=byteOffset; i<sampleCount; i++) {
				buffer[i]=linear2alaw((short) (buffer[i] << 8));
			}
		} else {
			for (int i=byteOffset; i<sampleCount; i++) {
				buffer[i]=linear2alaw((short) (((byte) (buffer[i]+128)) << 8));
			}
		}
	}
	
	/**
	 * Fills outBuffer with alaw samples.
	 * reading starts from inBuffer[inByteOffset].
	 * writing starts at outBuffer[outByteOffset].
	 * There will be sampleCount <B>bytes</B> written to outBuffer.
	 */
	public static void pcm82alaw(byte[] inBuffer, int inByteOffset, 
			byte[] outBuffer, int outByteOffset, int sampleCount, boolean signed) {
		int alawIndex=outByteOffset;
		int pcmIndex=inByteOffset;
		if (signed) {
			while (sampleCount>0) {
				outBuffer[alawIndex++]=linear2alaw((short) (inBuffer[pcmIndex++] << 8));
				sampleCount--;
			}
		} else {
			while (sampleCount>0) {
				outBuffer[alawIndex++]=linear2alaw((short) (((byte) (inBuffer[pcmIndex++]+128)) << 8));
				sampleCount--;
			}
		}
	}
	


	/**
	 * Converts an alaw buffer to 8bit pcm samples
	 * The 8bit bytes overwrite the original alaw values.
	 * The first byte-offset of the aLaw bytes is byteOffset.
	 * It will be written sampleCount bytes.
	 */
	public static void alaw2pcm8(byte[] buffer, int byteOffset, int sampleCount, boolean signed) {
		sampleCount+=byteOffset;
		if (signed) {		
			for (int i=byteOffset; i<sampleCount; i++) {
				buffer[i]=(byte) ((_a2l[buffer[i] & 0xFF] >> 8) & 0xFF);
			}
		} else {
			for (int i=byteOffset; i<sampleCount; i++) {
				buffer[i]=(byte) ((_a2l[buffer[i] & 0xFF]>>8)+128);
			}
		}
	}

	/**
	 * Fills outBuffer with alaw samples.
	 * reading starts from inBuffer[inByteOffset].
	 * writing starts at outBuffer[outByteOffset].
	 * There will be sampleCount <B>bytes</B> written to outBuffer.
	 */
	public static void alaw2pcm8(byte[] inBuffer, int inByteOffset, 
			byte[] outBuffer, int outByteOffset, int sampleCount, boolean signed) {
		int alawIndex=inByteOffset;
		int pcmIndex=outByteOffset;
		if (signed) {
			while (sampleCount>0) {
				outBuffer[pcmIndex++]=(byte) ((_a2l[inBuffer[alawIndex++] & 0xFF] >> 8) & 0xFF);
				sampleCount--;
			}
		} else {
			while (sampleCount>0) {
				outBuffer[pcmIndex++]=(byte) ((_a2l[inBuffer[alawIndex++] & 0xFF]>>8)+128);
				sampleCount--;
			}
		}
	}
	
	/**
	 * Fills outBuffer with pcm signed 16 bit samples.
	 * reading starts from inBuffer[inByteOffset].
	 * writing starts at outBuffer[outByteOffset].
	 * There will be sampleCount bytes read from inBuffer;
	 * There will be sampleCount*2 bytes written to outBuffer.
	 */
	public static void alaw2pcm16(byte[] inBuffer, int inByteOffset, 
			byte[] outBuffer, int outByteOffset, int sampleCount, boolean bigEndian) {
		int shortIndex=outByteOffset;
		int alawIndex=inByteOffset;
		if (bigEndian) {
			while (sampleCount>0) {
				convertShortToBigEndianBytes(_a2l[inBuffer[alawIndex++] & 0xFF], outBuffer, shortIndex++);
				shortIndex++;
				sampleCount--;
			}
		} else {
			while (sampleCount>0) {
				convertShortToLittleEndianBytes(_a2l[inBuffer[alawIndex++] & 0xFF], outBuffer, shortIndex++);
				shortIndex++;
				sampleCount--;
			}
		}	
	}
	





	/*
	 *	!! Here, unlike other functions in this class, the length is
	 *	in bytes rather than samples !!
	 */
	public static void changeOrderOrSign(byte[] buffer, int nOffset, int nByteLength, int nBytesPerSample)
	{
		switch (nBytesPerSample)
		{
		case 1:
			convertSign8(buffer, nOffset, nByteLength);
			break;

		case 2:
			swapOrder16(buffer, nOffset, nByteLength / 2);
			break;

		case 3:
			swapOrder24(buffer, nOffset, nByteLength / 3);
			break;

		case 4:
			swapOrder32(buffer, nOffset, nByteLength / 4);
			break;
		}
	}



	/*
	 *	!! Here, unlike other functions in this class, the length is
	 *	in bytes rather than samples !!
	 */
	public static void changeOrderOrSign(
		byte[] inBuffer, int nInOffset,
		byte[] outBuffer, int nOutOffset,
		int nByteLength, int nBytesPerSample)
	{
		switch (nBytesPerSample)
		{
		case 1:
			convertSign8(
				inBuffer, nInOffset,
				outBuffer, nOutOffset,
				nByteLength);
			break;

		case 2:
			swapOrder16(
				inBuffer, nInOffset,
				outBuffer, nOutOffset,
				nByteLength / 2);
			break;

		case 3:
			swapOrder24(
				inBuffer, nInOffset,
				outBuffer, nOutOffset,
				nByteLength / 3);
			break;

		case 4:
			swapOrder32(
				inBuffer, nInOffset,
				outBuffer, nOutOffset,
				nByteLength / 4);
			break;
		}
	}



}


/*** TConversionTool.java ***/
