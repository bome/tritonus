/*
 *	AlsaPcm.java
 */

/*
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


package	org.tritonus.lowlevel.alsa;


import	org.tritonus.TDebug;


public class AlsaPcm
{
	public static final int	SND_PCM_OPEN_PLAYBACK =	0x0001;
	public static final int	SND_PCM_OPEN_CAPTURE =	0x0002;
	public static final int	SND_PCM_OPEN_DUPLEX =	0x0003;
	public static final int	SND_PCM_OPEN_NONBLOCK =	0x1000;

	public static final int	SND_PCM_CHANNEL_PLAYBACK =	0;
	public static final int	SND_PCM_CHANNEL_CAPTURE =	1;

	public static final int	SND_PCM_MODE_UNKNOWN =		(-1);
	public static final int	SND_PCM_MODE_STREAM =		0;
	public static final int	SND_PCM_MODE_BLOCK =		1;

	public static final int	SND_PCM_SFMT_S8 =			0;
	public static final int	SND_PCM_SFMT_U8 =			1;
	public static final int	SND_PCM_SFMT_S16_LE =		2;
	public static final int	SND_PCM_SFMT_S16_BE =		3;
	public static final int	SND_PCM_SFMT_U16_LE =		4;
	public static final int	SND_PCM_SFMT_U16_BE =		5;
	public static final int	SND_PCM_SFMT_S24_LE =		6;	/* low three bytes */
	public static final int	SND_PCM_SFMT_S24_BE =		7;	/* low three bytes */
	public static final int	SND_PCM_SFMT_U24_LE =		8;	/* low three bytes */
	public static final int	SND_PCM_SFMT_U24_BE =		9;	/* low three bytes */
	public static final int	SND_PCM_SFMT_S32_LE =		10;
	public static final int	SND_PCM_SFMT_S32_BE =		11;
	public static final int	SND_PCM_SFMT_U32_LE =		12;
	public static final int	SND_PCM_SFMT_U32_BE =		13;
	public static final int	SND_PCM_SFMT_FLOAT_LE =		14;	/* 4-byte float, IEEE-754 32-bit */
	public static final int	SND_PCM_SFMT_FLOAT_BE =		15;	/* 4-byte float, IEEE-754 32-bit */
	public static final int	SND_PCM_SFMT_FLOAT64_LE =		16;	/* 8-byte float, IEEE-754 64-bit */
	public static final int	SND_PCM_SFMT_FLOAT64_BE =		17;	/* 8-byte float, IEEE-754 64-bit */
	public static final int	SND_PCM_SFMT_IEC958_SUBFRAME_LE =	18;	/* IEC-958 subframe, Little Endian */
	public static final int	SND_PCM_SFMT_IEC958_SUBFRAME_BE =	19;	/* IEC-958 subframe, Big Endian */
	public static final int	SND_PCM_SFMT_MU_LAW =		20;
	public static final int	SND_PCM_SFMT_A_LAW =		21;
	public static final int	SND_PCM_SFMT_IMA_ADPCM =		22;
	public static final int	SND_PCM_SFMT_MPEG =		23;
	public static final int	SND_PCM_SFMT_GSM =		24;
	public static final int	SND_PCM_SFMT_SPECIAL =		31;
/*
#ifdef SND_LITTLE_ENDIAN
	public static final int	SND_PCM_SFMT_S16 =		SND_PCM_SFMT_S16_LE
	public static final int	SND_PCM_SFMT_U16		SND_PCM_SFMT_U16_LE
	public static final int	SND_PCM_SFMT_S24		SND_PCM_SFMT_S24_LE
	public static final int	SND_PCM_SFMT_U24		SND_PCM_SFMT_U24_LE
	public static final int	SND_PCM_SFMT_S32		SND_PCM_SFMT_S32_LE
	public static final int	SND_PCM_SFMT_U32		SND_PCM_SFMT_U32_LE
	public static final int	SND_PCM_SFMT_FLOAT		SND_PCM_SFMT_FLOAT_LE
	public static final int	SND_PCM_SFMT_FLOAT64		SND_PCM_SFMT_FLOAT64_LE
	public static final int	SND_PCM_SFMT_IEC958_SUBFRAME	SND_PCM_SFMT_IEC958_SUBFRAME_LE
#endif
#ifdef SND_BIG_ENDIAN
	public static final int	SND_PCM_SFMT_S16		SND_PCM_SFMT_S16_BE
	public static final int	SND_PCM_SFMT_U16		SND_PCM_SFMT_U16_BE
	public static final int	SND_PCM_SFMT_S24		SND_PCM_SFMT_S24_BE
	public static final int	SND_PCM_SFMT_U24		SND_PCM_SFMT_U24_BE
	public static final int	SND_PCM_SFMT_S32		SND_PCM_SFMT_S32_BE
	public static final int	SND_PCM_SFMT_U32		SND_PCM_SFMT_U32_BE
	public static final int	SND_PCM_SFMT_FLOAT		SND_PCM_SFMT_FLOAT_BE
	public static final int	SND_PCM_SFMT_FLOAT64		SND_PCM_SFMT_FLOAT64_BE
	public static final int	SND_PCM_SFMT_IEC958_SUBFRAME	SND_PCM_SFMT_IEC958_SUBFRAME_BE
#endif
*/
	public static final int	SND_PCM_FMT_S8 =			(1 << SND_PCM_SFMT_S8);
	public static final int	SND_PCM_FMT_U8 =			(1 << SND_PCM_SFMT_U8);
	public static final int	SND_PCM_FMT_S16_LE =		(1 << SND_PCM_SFMT_S16_LE);
	public static final int	SND_PCM_FMT_S16_BE =		(1 << SND_PCM_SFMT_S16_BE);
	public static final int	SND_PCM_FMT_U16_LE =		(1 << SND_PCM_SFMT_U16_LE);
	public static final int	SND_PCM_FMT_U16_BE =		(1 << SND_PCM_SFMT_U16_BE);
	public static final int	SND_PCM_FMT_S24_LE =		(1 << SND_PCM_SFMT_S24_LE);
	public static final int	SND_PCM_FMT_S24_BE =		(1 << SND_PCM_SFMT_S24_BE);
	public static final int	SND_PCM_FMT_U24_LE =		(1 << SND_PCM_SFMT_U24_LE);
	public static final int	SND_PCM_FMT_U24_BE =		(1 << SND_PCM_SFMT_U24_BE);
	public static final int	SND_PCM_FMT_S32_LE =		(1 << SND_PCM_SFMT_S32_LE);
	public static final int	SND_PCM_FMT_S32_BE =		(1 << SND_PCM_SFMT_S32_BE);
	public static final int	SND_PCM_FMT_U32_LE =		(1 << SND_PCM_SFMT_U32_LE);
	public static final int	SND_PCM_FMT_U32_BE =		(1 << SND_PCM_SFMT_U32_BE);
	public static final int	SND_PCM_FMT_FLOAT_LE =		(1 << SND_PCM_SFMT_FLOAT_LE);
	public static final int	SND_PCM_FMT_FLOAT_BE =		(1 << SND_PCM_SFMT_FLOAT_BE);
	public static final int	SND_PCM_FMT_FLOAT64_LE =		(1 << SND_PCM_SFMT_FLOAT64_LE);
	public static final int	SND_PCM_FMT_FLOAT64_BE =		(1 << SND_PCM_SFMT_FLOAT64_BE);
	public static final int	SND_PCM_FMT_IEC958_SUBFRAME_LE =	(1 << SND_PCM_SFMT_IEC958_SUBFRAME_LE);
	public static final int	SND_PCM_FMT_IEC958_SUBFRAME_BE =	(1 << SND_PCM_SFMT_IEC958_SUBFRAME_BE);
	public static final int	SND_PCM_FMT_MU_LAW =		(1 << SND_PCM_SFMT_MU_LAW);
	public static final int	SND_PCM_FMT_A_LAW =		(1 << SND_PCM_SFMT_A_LAW);
	public static final int	SND_PCM_FMT_IMA_ADPCM =		(1 << SND_PCM_SFMT_IMA_ADPCM);
	public static final int	SND_PCM_FMT_MPEG =		(1 << SND_PCM_SFMT_MPEG);
	public static final int	SND_PCM_FMT_GSM	 =		(1 << SND_PCM_SFMT_GSM);
	public static final int	SND_PCM_FMT_SPECIAL =		(1 << SND_PCM_SFMT_SPECIAL);

/*
#ifdef SND_LITTLE_ENDIAN
	public static final int	SND_PCM_FMT_S16 =			SND_PCM_FMT_S16_LE
	public static final int	SND_PCM_FMT_U16			SND_PCM_FMT_U16_LE
	public static final int	SND_PCM_FMT_S24			SND_PCM_FMT_S24_LE
	public static final int	SND_PCM_FMT_U24			SND_PCM_FMT_U24_LE
	public static final int	SND_PCM_FMT_S32			SND_PCM_FMT_S32_LE
	public static final int	SND_PCM_FMT_U32			SND_PCM_FMT_U32_LE
	public static final int	SND_PCM_FMT_FLOAT		SND_PCM_FMT_FLOAT_LE
	public static final int	SND_PCM_FMT_FLOAT64		SND_PCM_FMT_FLOAT64_LE
	public static final int	SND_PCM_FMT_IEC958_SUBFRAME	SND_PCM_FMT_IEC958_SUBFRAME_LE
#endif
#ifdef SND_BIG_ENDIAN
	public static final int	SND_PCM_FMT_S16			SND_PCM_FMT_S16_BE
	public static final int	SND_PCM_FMT_U16			SND_PCM_FMT_U16_BE
	public static final int	SND_PCM_FMT_S24			SND_PCM_FMT_S24_BE
	public static final int	SND_PCM_FMT_U24			SND_PCM_FMT_U24_BE
	public static final int	SND_PCM_FMT_S32			SND_PCM_FMT_S32_BE
	public static final int	SND_PCM_FMT_U32			SND_PCM_FMT_U32_BE
	public static final int	SND_PCM_FMT_FLOAT		SND_PCM_FMT_FLOAT_BE
	public static final int	SND_PCM_FMT_FLOAT64		SND_PCM_FMT_FLOAT64_BE
	public static final int	SND_PCM_FMT_IEC958_SUBFRAME	SND_PCM_FMT_IEC958_SUBFRAME_BE
#endif
*/

	public static final int	SND_PCM_RATE_CONTINUOUS =		(1<<0);		/* continuous range */
	public static final int	SND_PCM_RATE_KNOT =		(1<<1);		/* supports more non-continuos rates */
	public static final int	SND_PCM_RATE_8000 =		(1<<2);		/* 8000Hz */
	public static final int	SND_PCM_RATE_11025 =		(1<<3);		/* 11025Hz */
	public static final int	SND_PCM_RATE_16000 =		(1<<4);		/* 16000Hz */
	public static final int	SND_PCM_RATE_22050 =		(1<<5);		/* 22050Hz */
	public static final int	SND_PCM_RATE_32000 =		(1<<6);		/* 32000Hz */
	public static final int	SND_PCM_RATE_44100 =		(1<<7);		/* 44100Hz */
	public static final int	SND_PCM_RATE_48000 =		(1<<8);		/* 48000Hz */
	public static final int	SND_PCM_RATE_88200 =		(1<<9);		/* 88200Hz */
	public static final int	SND_PCM_RATE_96000 =		(1<<10);		/* 96000Hz */
	public static final int	SND_PCM_RATE_176400 =		(1<<11);		/* 176400Hz */
	public static final int	SND_PCM_RATE_192000 =		(1<<12);		/* 192000Hz */

	public static final int	SND_PCM_RATE_8000_44100 =		(SND_PCM_RATE_8000|SND_PCM_RATE_11025|
					 SND_PCM_RATE_16000|SND_PCM_RATE_22050|
					 SND_PCM_RATE_32000|SND_PCM_RATE_44100);
	public static final int	SND_PCM_RATE_8000_48000	 =	(SND_PCM_RATE_8000_44100|SND_PCM_RATE_48000);
	public static final int	SND_PCM_RATE_8000_96000	 =	(SND_PCM_RATE_8000_48000|SND_PCM_RATE_88200|
					 SND_PCM_RATE_96000);

	public static final int	SND_PCM_INFO_PLAYBACK =		0x00000001;
	public static final int	SND_PCM_INFO_CAPTURE =		0x00000002;
	public static final int	SND_PCM_INFO_DUPLEX =		0x00000100;
	public static final int	SND_PCM_INFO_DUPLEX_RATE =	0x00000200;	/* rate for playback & capture channels must be same!!! */
	public static final int	SND_PCM_INFO_DUPLEX_MONO =	0x00000400;	/* in duplex mode - only mono (one channel) is supported */

	public static final int	SND_PCM_CHNINFO_MMAP =		0x00000001;	/* hardware supports mmap */
	public static final int	SND_PCM_CHNINFO_STREAM =		0x00000002;	/* hardware supports streaming */
	public static final int	SND_PCM_CHNINFO_BLOCK =		0x00000004;	/* hardware supports block mode */
	public static final int	SND_PCM_CHNINFO_BATCH =		0x00000010;	/* double buffering */
	public static final int	SND_PCM_CHNINFO_INTERLEAVE =	0x00000100;	/* voices are interleaved */
	public static final int	SND_PCM_CHNINFO_NONINTERLEAVE =	0x00000200;	/* voices are not interleaved */
	public static final int	SND_PCM_CHNINFO_BLOCK_TRANSFER =	0x00010000;	/* hardware transfer block of samples */
	public static final int	SND_PCM_CHNINFO_OVERRANGE =	0x00020000;	/* hardware supports ADC (capture) overrange detection */
	public static final int	SND_PCM_CHNINFO_MMAP_VALID =	0x00040000;	/* fragment data are valid during transfer */
	public static final int	SND_PCM_CHNINFO_PAUSE =		0x00080000;	/* pause ioctl is supported */
	public static final int	SND_PCM_CHNINFO_GLOBAL_PARAMS =	0x00100000;	/* parameters can be set via switches only */

	public static final int	SND_PCM_START_DATA =		0;	/* start when some data are written (playback) or requested (capture) */
	public static final int	SND_PCM_START_FULL =		1;	/* start when whole queue is filled (playback) */
	public static final int	SND_PCM_START_GO =		2;	/* start on the go command */

	public static final int	SND_PCM_STOP_STOP =		0;	/* stop when underrun/overrun */
	public static final int	SND_PCM_STOP_STOP_ERASE =		1;	/* stop & erase when overrun (capture) */
	public static final int	SND_PCM_STOP_ROLLOVER =		2;	/* rollover when overrun/underrun */

	public static final int	SND_PCM_FILL_NONE =		0;	/* don't fill the buffer with silent samples */
	public static final int	SND_PCM_FILL_SILENCE_WHOLE =	1;	/* fill the whole buffer with silence */
	public static final int	SND_PCM_FILL_SILENCE =		2;	/* fill the partial buffer with silence */

	public static final int	SND_PCM_STATUS_NOTREADY =		0;	/* channel is not ready */
	public static final int	SND_PCM_STATUS_READY =		1;	/* channel is ready for prepare call */
	public static final int	SND_PCM_STATUS_PREPARED =		2;	/* channel is ready to go */
	public static final int	SND_PCM_STATUS_RUNNING =		3;	/* channel is running */
	public static final int	SND_PCM_STATUS_UNDERRUN =		4;	/* channel reached an underrun and it is not ready */
	public static final int	SND_PCM_STATUS_OVERRUN =		5;	/* channel reached an overrun and it is not ready */
	public static final int	SND_PCM_STATUS_PAUSED =		6;	/* channel is paused */

	public static final int	SND_PCM_BOUNDARY =		0xf0000000;

	public static final int	SND_PCM_MMAP_OFFSET_CONTROL =	0x00000000;
	public static final int	SND_PCM_MMAP_OFFSET_DATA =	0x80000000;

	public static final String	SND_PCM_SW_RATE =		"Sample Rate";
	public static final String	SND_PCM_SW_FORMAT =		"Format";
	public static final String	SND_PCM_SW_VOICES =		"Voices";
	public static final String	SND_PCM_SW_FRAGMENT_SIZE =	"Fragment Size";

	public static final int	SND_PCM_DIG0_PROFESSIONAL =	(1<<0);	/* 0 = consumer, 1 = professional */
	public static final int	SND_PCM_DIG0_NONAUDIO =		(1<<1);	/* 0 = audio, 1 = non-audio */
	public static final int	SND_PCM_DIG0_PRO_EMPHASIS =	(7<<2);	/* mask - emphasis */
	public static final int	SND_PCM_DIG0_PRO_EMPHASIS_NOTID =	(0<<2);	/* emphasis not indicated */
	public static final int	SND_PCM_DIG0_PRO_EMPHASIS_NONE =	(1<<2);	/* none emphasis */
	public static final int	SND_PCM_DIG0_PRO_EMPHASIS_5015 =	(3<<2);	/* 50/15us emphasis */
	public static final int	SND_PCM_DIG0_PRO_EMPHASIS_CCITT =	(7<<2);	/* CCITT J.17 emphasis */
	public static final int	SND_PCM_DIG0_PRO_FREQ_UNLOCKED =	(1<<5);	/* source sample frequency: 0 = locked, 1 = unlocked */
	public static final int	SND_PCM_DIG0_PRO_FS =		(3<<6);	/* mask - sample frequency */
	public static final int	SND_PCM_DIG0_PRO_FS_NOTID =	(0<<6);	/* fs not indicated */
	public static final int	SND_PCM_DIG0_PRO_FS_44100 =	(1<<6);	/* 44.1kHz */
	public static final int	SND_PCM_DIG0_PRO_FS_48000 =	(2<<6);	/* 48kHz */
	public static final int	SND_PCM_DIG0_PRO_FS_32000 =	(3<<6);	/* 32kHz */
	public static final int	SND_PCM_DIG0_CON_NOT_COPYRIGHT =	(1<<2);	/* 0 = copyright, 1 = not copyright */
	public static final int	SND_PCM_DIG0_CON_EMPHASIS =	(7<<3);	/* mask - emphasis */
	public static final int	SND_PCM_DIG0_CON_EMPHASIS_NONE =	(0<<3);	/* none emphasis */
	public static final int	SND_PCM_DIG0_CON_EMPHASIS_5015 =	(1<<3);	/* 50/15us emphasis */
	public static final int	SND_PCM_DIG0_CON_MODE =		(3<<6);	/* mask - mode */
	public static final int	SND_PCM_DIG1_PRO_MODE =		(15<<0);	/* mask - channel mode */
	public static final int	SND_PCM_DIG1_PRO_MODE_NOTID =	(0<<0);	/* not indicated */
	public static final int	SND_PCM_DIG1_PRO_MODE_STEREOPHONIC = (2<<0); /* stereophonic - ch A is left */
	public static final int	SND_PCM_DIG1_PRO_MODE_SINGLE =	(4<<0);	/* single channel */
	public static final int	SND_PCM_DIG1_PRO_MODE_TWO =	(8<<0);	/* two channels */
	public static final int	SND_PCM_DIG1_PRO_MODE_PRIMARY =	(12<<0);	/* primary/secondary */
	public static final int	SND_PCM_DIG1_PRO_MODE_BYTE3 =	(15<<0);	/* vector to byte 3 */
	public static final int	SND_PCM_DIG1_PRO_USERBITS =	(15<<4);	/* mask - user bits */
	public static final int	SND_PCM_DIG1_PRO_USERBITS_NOTID =	(0<<4);	/* not indicated */
	public static final int	SND_PCM_DIG1_PRO_USERBITS_192 =	(8<<4);	/* 192-bit structure */
	public static final int	SND_PCM_DIG1_PRO_USERBITS_UDEF =	(12<<4);	/* user defined application */
	public static final int	SND_PCM_DIG1_CON_CATEGORY =	0x7f;
	public static final int	SND_PCM_DIG1_CON_GENERAL =	0x00;
	public static final int	SND_PCM_DIG1_CON_EXPERIMENTAL =	0x40;
	public static final int	SND_PCM_DIG1_CON_SOLIDMEM_MASK =	0x0f;
	public static final int	SND_PCM_DIG1_CON_SOLIDMEM_ID =	0x08;
	public static final int	SND_PCM_DIG1_CON_BROADCAST1_MASK = 0x07;
	public static final int	SND_PCM_DIG1_CON_BROADCAST1_ID =	0x04;
	public static final int	SND_PCM_DIG1_CON_DIGDIGCONV_MASK = 0x07;
	public static final int	SND_PCM_DIG1_CON_DIGDIGCONV_ID =	0x02;
	public static final int	SND_PCM_DIG1_CON_ADC_COPYRIGHT_MASK = 0x1f;
	public static final int	SND_PCM_DIG1_CON_ADC_COPYRIGHT_ID = 0x06;
	public static final int	SND_PCM_DIG1_CON_ADC_MASK =	0x1f;
	public static final int	SND_PCM_DIG1_CON_ADC_ID	 =	0x16;
	public static final int	SND_PCM_DIG1_CON_BROADCAST2_MASK = 0x0f;
	public static final int	SND_PCM_DIG1_CON_BROADCAST2_ID =	0x0e;
	public static final int	SND_PCM_DIG1_CON_LASEROPT_MASK =	0x07;
	public static final int	SND_PCM_DIG1_CON_LASEROPT_ID =	0x01;
	public static final int	SND_PCM_DIG1_CON_MUSICAL_MASK =	0x07;
	public static final int	SND_PCM_DIG1_CON_MUSICAL_ID =	0x05;
	public static final int	SND_PCM_DIG1_CON_MAGNETIC_MASK =	0x07;
	public static final int	SND_PCM_DIG1_CON_MAGNETIC_ID =	0x03;
	public static final int	SND_PCM_DIG1_CON_IEC908_CD =	(SND_PCM_DIG1_CON_LASEROPT_ID|0x00);
	public static final int	SND_PCM_DIG1_CON_NON_IEC908_CD =	(SND_PCM_DIG1_CON_LASEROPT_ID|0x08);
	public static final int	SND_PCM_DIG1_CON_PCM_CODER =	(SND_PCM_DIG1_CON_DIGDIGCONV_ID|0x00);
	public static final int	SND_PCM_DIG1_CON_SAMPLER =	(SND_PCM_DIG1_CON_DIGDIGCONV_ID|0x20);
	public static final int	SND_PCM_DIG1_CON_MIXER =		(SND_PCM_DIG1_CON_DIGDIGCONV_ID|0x10);
	public static final int	SND_PCM_DIG1_CON_RATE_CONVERTER =	(SND_PCM_DIG1_CON_DIGDIGCONV_ID|0x18);
	public static final int	SND_PCM_DIG1_CON_SYNTHESIZER =	(SND_PCM_DIG1_CON_MUSICAL_ID|0x00);
	public static final int	SND_PCM_DIG1_CON_MICROPHONE =	(SND_PCM_DIG1_CON_MUSICAL_ID|0x08);
	public static final int	SND_PCM_DIG1_CON_DAT =		(SND_PCM_DIG1_CON_MAGNETIC_ID|0x00);
	public static final int	SND_PCM_DIG1_CON_VCR =		(SND_PCM_DIG1_CON_MAGNETIC_ID|0x08);
	public static final int	SND_PCM_DIG1_CON_ORIGINAL =	(1<<7);	/* this bits depends on the category code */
	public static final int	SND_PCM_DIG2_PRO_SBITS =		(7<<0);	/* mask - sample bits */
	public static final int	SND_PCM_DIG2_PRO_SBITS_20 =	(2<<0);	/* 20-bit - coordination */
	public static final int	SND_PCM_DIG2_PRO_SBITS_24 =	(4<<0);	/* 24-bit - main audio */
	public static final int	SND_PCM_DIG2_PRO_SBITS_UDEF =	(6<<0);	/* user defined application */
	public static final int	SND_PCM_DIG2_PRO_WORDLEN =	(7<<3);	/* mask - source word length */
	public static final int	SND_PCM_DIG2_PRO_WORDLEN_NOTID =	(0<<3);	/* not indicated */
	public static final int	SND_PCM_DIG2_PRO_WORDLEN_22_18 =	(2<<3);	/* 22-bit or 18-bit */
	public static final int	SND_PCM_DIG2_PRO_WORDLEN_23_19 =	(4<<3);	/* 23-bit or 19-bit */
	public static final int	SND_PCM_DIG2_PRO_WORDLEN_24_20 =	(5<<3);	/* 24-bit or 20-bit */
	public static final int	SND_PCM_DIG2_PRO_WORDLEN_20_16 =	(6<<3);	/* 20-bit or 16-bit */
	public static final int	SND_PCM_DIG2_CON_SOURCE =		(15<<0);	/* mask - source number */
	public static final int	SND_PCM_DIG2_CON_SOURCE_UNSPEC =	(0<<0);	/* unspecified */
	public static final int	SND_PCM_DIG2_CON_CHANNEL =	(15<<4);	/* mask - channel number */
	public static final int	SND_PCM_DIG2_CON_CHANNEL_UNSPEC =	(0<<4);	/* unspecified */
	public static final int	SND_PCM_DIG3_CON_FS =		(15<<0);	/* mask - sample frequency */
	public static final int	SND_PCM_DIG3_CON_FS_44100 =	(0<<0);	/* 44.1kHz */
	public static final int	SND_PCM_DIG3_CON_FS_48000 =	(2<<0);	/* 48kHz */
	public static final int	SND_PCM_DIG3_CON_FS_32000 =	(3<<0);	/* 32kHz */
	public static final int	SND_PCM_DIG3_CON_CLOCK =		(3<<4);	/* mask - clock accuracy */
	public static final int	SND_PCM_DIG3_CON_CLOCK_1000PPM =	(0<<4);	/* 1000 ppm */
	public static final int	SND_PCM_DIG3_CON_CLOCK_50PPM =	(1<<4);	/* 50 ppm */
	public static final int	SND_PCM_DIG3_CON_CLOCK_VARIABLE =	(2<<4);	/* variable pitch */



	static
	{
		System.loadLibrary("tritonusalsa");
	}



	/**
	 *	Holds the pointer to snd_pcm_t for the native code.
	 *	This must be long to be 64bit-clean.
	 */
	private long	m_lNativeHandle;



	public AlsaPcm(int nCard,
		       int nDevice,
		       int nDirection)
		throws	Exception
	{
		if (open(nCard,
			 nDevice,
			 nDirection) < 0)
		{
			throw new Exception();
		}
	}



	public AlsaPcm(int nCard,
		       int nDevice,
		       int nSubevice,
		       int nDirection)
		throws	Exception
	{
		if (open(nCard,
			 nDevice,
			 nSubevice,
			 nDirection) < 0)
		{
			throw new Exception();
		}
	}



	/**
	 *	Calls snd_pcm_open().
	 */
	private native int open(
		int nCard,
		int nDevice,
		int nDirection);



	/**
	 *	Calls snd_pcm_open_subdevice().
	 */
	private native int open(
		int nCard,
		int nDevice,
		int nSubdevice,
		int nDirection);



	/**
	 *	Calls snd_pcm_close().
	 */
	public native int close();


	/* does not seem useful:
	   file_descriptor
	 */

	public native int setNonblockMode(int nNonblock);


	/* TODO:
	   pcm_info
	   pcm_channel_info
	 */

	/**
	 *	anValues[0]	type
	 *	anValues[1]	flags
	 *	anValues[2]	playback subdevices
	 *	anValues[3]	capture subdevices
	 *
	 *	astrValues[0]	id
	 *	astrValues[1]	name
	 */
	public native int getInfo(int[] anValues,
				  String[] astrValues);

	/**
	 *	anValues[00]	subdevice
	 *	anValues[01]	channel
	 *	anValues[02]	mode
	 *	anValues[03]	flags
	 *	anValues[04]	formats
	 *	anValues[05]	rates
	 *	anValues[06]	min rate
	 *	anValues[07]	max rate
	 *	anValues[08]	min voices
	 *	anValues[09]	max voices
	 *	anValues[10]	buffer_size
	 *	anValues[11]	min fragment size
	 *	anValues[12]	max fragment size
	 *	anValues[13]	fragment align
	 *	anValues[14]	fifo size
	 *	anValues[15]	transfer block size
	 *	anValues[16]	mmap size
	 *	anValues[17]	mixer device
	 *
	 *	astrValues[0]	subdevice name
	 *
	 *	missing: sync info, digital info, mixer_eid
	 */
	public native int getChannelInfo(int[] anValues,
					 String[] astrValues);



	/**
	 *	Calls snd_pcm_channel_params().
	 */
	public native int setChannelParams(
		int nChannel,
		int nMode,
		// format
		boolean bInterleave,
		int nFormat,
		int nRate,
		int nVoices,
		int nSpecial,
		/* TODO: digital */
		int nStartMode,
		int nStopMode,
		boolean bTime,
		boolean bUstTime,
		int nQueueOrFragSize,
		int nMin,
		int nMax);



	/**
	 *	anValues[00]	channel
	 *	anValues[01]	mode
	 *
	 *	anValues[02]	format
	 *	anValues[03]	rate
	 *	anValues[04]	voices
	 *	anValues[05]	special
	 *
	 *	missing: digital
	 *
	 *	anValues[06]	queue size (only valid if stream mode)
	 *	anValues[07]	frag size (only valid if block mode)
	 *	anValues[08]	frags (only valid if block mode)
	 *	anValues[09]	min frags (only valid if block mode)
	 *	anValues[10]	max frags (only valid if block mode)
	 *	anValues[11]	most significant bits per sample
	 *
	 *	abValues[0]	interleave
	 */
	public native int getChannelSetup(int[] anValues,
					  boolean[] abValues);

	/**
	 *	anValues[0]	mode
	 *	anValues[1]	status
	 *
	 *	anValues[2]	byte count since start
	 *	anValues[3]	current fragment
	 *	anValues[4]	bytes in queue/buffer
	 *	anValues[5]	free bytes in queue
	 *	anValues[6]	underrun count (playback)
	 *	anValues[7]	overrun count (capture)
	 *	anValues[8]	ADC overrange count (capture)
	 *
	 *	alValues[0]	start time
	 *	alValues[1]	ust start time
	 */
	public native int getChannelStatus(int nChannel,
					   int[] anValues,
					   long[] alValues);

	public native int prepareChannel(int nChannel);
	public native int preparePlayback();
	public native int prepareCapture();

	public native int goChannel(int nChannel);
	public native int goPlayback();
	public native int goCapture();

	public native int goSync(/* TODO: */);

	public native int drainPlayback();
	public native int flushChannel(int nChannel);
	public native int flushPlayback();
	public native int flushCapture();
	public native int setPlaybackPause(boolean bEnable);
	public native int getTransferSize(int nChannel);
	public native int write(byte[] abData, int nOffset, int nLength);
	public native int read(byte[] abData, int nOffset, int nLength);

	/* not implemented:
	   writev
	   readv
	   mmap
	   munmap
	   *_format_*
	 */
}



/*** AlsaPcm.java ***/
