/*
 *	AlsaMixer.java
 */

/*
 *  Copyright (c) 1999 - 2001 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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


package	org.tritonus.sampled.mixer.alsa;


import	java.util.Arrays;
import	java.util.ArrayList;
import	java.util.Collection;
import	java.util.HashMap;
import	java.util.Map;
import	java.util.HashSet;
import	java.util.List;
import	java.util.Set;
import	java.util.Iterator;

import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.Clip;
import	javax.sound.sampled.DataLine;
import	javax.sound.sampled.SourceDataLine;
import	javax.sound.sampled.TargetDataLine;
import	javax.sound.sampled.LineUnavailableException;
import	javax.sound.sampled.Line;
import	javax.sound.sampled.Mixer;

import	org.tritonus.share.TDebug;
import	org.tritonus.share.TSettings;
import	org.tritonus.share.sampled.mixer.TMixer;
import	org.tritonus.share.sampled.mixer.TMixerInfo;
import	org.tritonus.share.sampled.mixer.TSoftClip;
import	org.tritonus.share.GlobalInfo;

import	org.tritonus.lowlevel.alsa.Alsa;
import	org.tritonus.lowlevel.alsa.AlsaPcm;
import	org.tritonus.lowlevel.alsa.AlsaPcm.HWParams;



public class AlsaMixer
	extends		TMixer
{
	private static final AudioFormat[]	EMPTY_AUDIOFORMAT_ARRAY = new AudioFormat[0];
	private static final int	CHANNELS_LIMIT = 32;

	// default buffer size in bytes.
	private static final int	DEFAULT_BUFFER_SIZE = 32768;

	private static AudioFormat[]	FORMATS =
	{
		// hack for testing.
		// new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, 44100/*AudioSystem.NOT_SPECIFIED*/, 8, 1, 1, 44100/*AudioSystem.NOT_SPECIFIED*/, true),
		new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, 11025/*AudioSystem.NOT_SPECIFIED*/, 16, 1, 2, 11025/*AudioSystem.NOT_SPECIFIED*/, false),

		// Formats supported directely by esd.
		new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, AudioSystem.NOT_SPECIFIED, 8, 1, 1, AudioSystem.NOT_SPECIFIED, true),
		new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, AudioSystem.NOT_SPECIFIED, 8, 1, 1, AudioSystem.NOT_SPECIFIED, false),
		new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, AudioSystem.NOT_SPECIFIED, 8, 2, 2, AudioSystem.NOT_SPECIFIED, true),
		new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, AudioSystem.NOT_SPECIFIED, 8, 2, 2, AudioSystem.NOT_SPECIFIED, false),


		new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, 16, 1, 2, AudioSystem.NOT_SPECIFIED, false),
		new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, 16, 2, 4, AudioSystem.NOT_SPECIFIED, false),

		/*
		 *	Format supported through "simple" conversions.
		 *	"Simple" conversions are changes in the byte order
		 *	and changing signed/unsigned for 8 bit.
		 */

		new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, 8, 1, 1, AudioSystem.NOT_SPECIFIED, true),
		new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, 8, 1, 1, AudioSystem.NOT_SPECIFIED, false),
		new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, 8, 2, 2, AudioSystem.NOT_SPECIFIED, true),
		new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, 8, 2, 2, AudioSystem.NOT_SPECIFIED, false),

		new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, 16, 1, 2, AudioSystem.NOT_SPECIFIED, true),
		new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, 16, 2, 4, AudioSystem.NOT_SPECIFIED, true),
	};

	private static Line.Info[]	SOURCE_LINE_INFOS =
	{
		new DataLine.Info(SourceDataLine.class, FORMATS, AudioSystem.NOT_SPECIFIED, AudioSystem.NOT_SPECIFIED),
	};

	private static Line.Info[]	TARGET_LINE_INFOS =
	{
		new DataLine.Info(TargetDataLine.class, FORMATS, AudioSystem.NOT_SPECIFIED, AudioSystem.NOT_SPECIFIED),
	};

	/*	The name of the sound card this mixer is representing.
	 */
	private String	m_strPcmName;



	public static String getDeviceNamePrefix()
	{
		if (TSettings.AlsaUsePlughw)
		{
			return "plughw";
		}
		else
		{
			return "hw";
		}
	}


	public static String getPcmName(int nCard)
	{
		String	strPcmName = getDeviceNamePrefix()
			+ ":" + nCard;
		if (TSettings.AlsaUsePlughw)
		{
			// strPcmName += ",0";
		}
		return strPcmName;
	}



	public AlsaMixer()
	{
		this(0);
	}



	public AlsaMixer(int nCard)
	{
		this(getPcmName(nCard));
	}



	public AlsaMixer(String strPcmName)
	{
		super(new TMixerInfo(
			"Alsa Mixer (" + strPcmName + ")",
			GlobalInfo.getVendor(),
			"Mixer for the Advanced Linux Sound Architecture (card " + strPcmName + ")",
			GlobalInfo.getVersion()),
		      new Line.Info(Mixer.class),
		      Arrays.asList(FORMATS),
		      Arrays.asList(FORMATS),
		      Arrays.asList(SOURCE_LINE_INFOS),
		      Arrays.asList(TARGET_LINE_INFOS));
		// TODO: use setSupportInformation() (after gathering the information from alsa-lib)
		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.<init>(String): begin."); }
		m_strPcmName = strPcmName;
		List	sourceFormats = getSupportedFormats(AlsaPcm.SND_PCM_STREAM_PLAYBACK);
		List	targetFormats = getSupportedFormats(AlsaPcm.SND_PCM_STREAM_CAPTURE);
		List	sourceLineInfos = new ArrayList();
		Line.Info	sourceLineInfo = new DataLine.Info(
			SourceDataLine.class,
			(AudioFormat[]) sourceFormats.toArray(EMPTY_AUDIOFORMAT_ARRAY),
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED);
		sourceLineInfos.add(sourceLineInfo);
		List	targetLineInfos = new ArrayList();
		Line.Info	targetLineInfo = new DataLine.Info(
			TargetDataLine.class,
			(AudioFormat[]) targetFormats.toArray(EMPTY_AUDIOFORMAT_ARRAY),
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED);
		targetLineInfos.add(targetLineInfo);
		setSupportInformation(sourceFormats, targetFormats, sourceLineInfos, targetLineInfos, new ArrayList());
		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.<init>(String): end."); }
	}



	public String getPcmName()
	{
		return m_strPcmName;
	}



	//////////////// Line //////////////////////////////////////


	// TODO: allow real close and reopen of mixer
	public void open()
	{
		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.open(): begin"); }

		// currently does nothing

		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.open(): end"); }
	}



	public void close()
	{
		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.close(): begin"); }

		// currently does nothing

		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.close(): end"); }
	}





	//////////////// Mixer //////////////////////////////////////


	public int getMaxLines(Line.Info info)
	{
		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.getMaxLines(): begin"); }
		// TODO:
		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.getMaxLines(): end"); }
		return 0;
	}




	//////////////// private //////////////////////////////////////


	// nBufferSize is in bytes!
	protected SourceDataLine getSourceDataLine(AudioFormat format, int nBufferSize)
		throws	LineUnavailableException
	{
		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.getSourceDataLine(): begin"); }
		if (TDebug.TraceMixer)
		{
			TDebug.out("AlsaMixer.getSourceDataLine(): format: " + format);
			TDebug.out("AlsaMixer.getSourceDataLine(): buffer size: " + nBufferSize);
		}
		if (nBufferSize < 1)
		{
			nBufferSize = DEFAULT_BUFFER_SIZE;
		}
		AlsaSourceDataLine	sourceDataLine = new AlsaSourceDataLine(this, format, nBufferSize);
		// sourceDataLine.start();
		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.getSourceDataLine(): returning: " + sourceDataLine); }
		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.getSourceDataLine(): end"); }
		return sourceDataLine;
	}


	// nBufferSize is in bytes!
	protected TargetDataLine getTargetDataLine(AudioFormat format, int nBufferSize)
		throws	LineUnavailableException
	{
		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.getTargetDataLine(): begin"); }
		int			nBufferSizeInBytes = nBufferSize * format.getFrameSize();
		AlsaTargetDataLine	targetDataLine = new AlsaTargetDataLine(this, format, nBufferSizeInBytes);
		// targetDataLine.start();
		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.getTargetDataLine(): returning: " + targetDataLine); }
		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.getTargetDataLine(): end"); }
		return targetDataLine;
	}



	protected Clip getClip(AudioFormat format)
		throws	LineUnavailableException
	{
		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.getClip(): begin"); }
		Clip	clip = new TSoftClip(this, format);
		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.getClip(): end"); }
		return clip;
	}



	/*
	  nDirection: should be AlsaPcm.SND_PCM_STREAM_PLAYBACK or
	  AlsaPcm.SND_PCM_STREAM_CAPTURE.
	 */
	private List getSupportedFormats(int nDirection)
	{
		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.getSupportedFormats(): begin"); }
		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.getSupportedFormats(): direction: " + nDirection); }
		List	supportedFormats = new ArrayList();
		AlsaPcm	alsaPcm = null;
		try
		{
			alsaPcm = new AlsaPcm(
				getPcmName(),
				nDirection,
				0);	// no special mode
		}
		catch (Exception e)
		{
			if (TDebug.TraceAllExceptions) { TDebug.out(e); }
			throw new RuntimeException("cannot open pcm");
		}
		int	nReturn;
		HWParams	hwParams = new HWParams();
		nReturn = alsaPcm.getAnyHWParams(hwParams);
		if (nReturn != 0)
		{
			TDebug.out("AlsaMixer.getSupportedFormats(): getAnyHWParams(): " + Alsa.getStringError(nReturn));
			throw new RuntimeException(Alsa.getStringError(nReturn));
		}
		HWParams.FormatMask	formatMask = new HWParams.FormatMask();
		int	nMinChannels = hwParams.getChannelsMin();
		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.getSupportedFormats(): min channels: " + nMinChannels); }
		int	nMaxChannels = hwParams.getChannelsMax();
		nMaxChannels = Math.min(nMaxChannels, CHANNELS_LIMIT);
		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.getSupportedFormats(): max channels: " + nMaxChannels); }
		hwParams.getFormatMask(formatMask);
		for (int i = 0; i < 32; i++)
		{
			if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.getSupportedFormats(): checking ALSA format index: " + i); }
			if (formatMask.test(i))
			{
				if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.getSupportedFormats(): ...supported"); }
				AudioFormat	audioFormat = AlsaUtils.getAlsaFormat(i);
				if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.getSupportedFormats(): adding AudioFormat: " + audioFormat); }
				addChanneledAudioFormats(supportedFormats, audioFormat, nMinChannels, nMaxChannels);
				// supportedFormats.add(audioFormat);
			}
			else
			{
			if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.getSupportedFormats(): ...not supported"); }
			}
		}
		// TODO: close/free mask & hwParams?
		alsaPcm.close();
		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.getSupportedFormats(): end"); }
		return supportedFormats;
	}



	private static void addChanneledAudioFormats(
		Collection collection,
		AudioFormat protoAudioFormat,
		int nMinChannels,
		int nMaxChannels)
	{
		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.addChanneledAudioFormats(): begin"); }
		for (int nChannels = nMinChannels; nChannels <= nMaxChannels; nChannels++)
		{
			AudioFormat	channeledAudioFormat = getChanneledAudioFormat(protoAudioFormat, nChannels);
			if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.addChanneledAudioFormats(): adding AudioFormat: " + channeledAudioFormat); }
			collection.add(channeledAudioFormat);
		}
		if (TDebug.TraceMixer) { TDebug.out("AlsaMixer.addChanneledAudioFormats(): end"); }
	}



	// TODO: better name
	// TODO: calculation of frame size is not perfect
	private static AudioFormat getChanneledAudioFormat(AudioFormat audioFormat, int nChannels)
	{
		AudioFormat	channeledAudioFormat = new AudioFormat(
			audioFormat.getEncoding(),
			audioFormat.getSampleRate(),
			audioFormat.getSampleSizeInBits(),
			nChannels,
			(audioFormat.getSampleSizeInBits() / 8) * nChannels,
			audioFormat.getFrameRate(),
			audioFormat.isBigEndian());
		return channeledAudioFormat;
	}
}



/*** AlsaMixer.java ***/
