/*
 *	AlsaMixer.java
 */

/*
 *  Copyright (c) 1999, 2000 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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
import	java.util.HashMap;
import	java.util.Map;
import	java.util.HashSet;
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

import	org.tritonus.TDebug;
import	org.tritonus.sampled.mixer.TMixer;
import	org.tritonus.sampled.mixer.TMixerInfo;
import	org.tritonus.sampled.mixer.TSoftClip;
import	org.tritonus.util.GlobalInfo;




public class AlsaMixer
	extends		TMixer
{
	// default buffer size in bytes.
	private static final int	DEFAULT_BUFFER_SIZE = 32768;

	private static AudioFormat[]	FORMATS =
	{
		// hack for testing.
		// new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, 44100/*AudioSystem.NOT_SPECIFIED*/, 8, 1, 1, 44100/*AudioSystem.NOT_SPECIFIED*/, true),
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

	/*	The number of the sound card this mixer is representing.
	 */
	private int	m_nCard;


	public AlsaMixer(int nCard)
	{
		super(new TMixerInfo(
			"Alsa Mixer (" + nCard + ")",
			GlobalInfo.getVendor(),
			"Mixer for the Advanced Linux Sound Architecture (card " + nCard + ")",
			GlobalInfo.getVersion()),
		      new Line.Info(Mixer.class),
		      Arrays.asList(FORMATS),
		      Arrays.asList(FORMATS),
		      Arrays.asList(SOURCE_LINE_INFOS),
		      Arrays.asList(TARGET_LINE_INFOS));
		if (TDebug.TraceMixer)
		{
			TDebug.out("AlsaMixer.<init>: beginning.");
		}
		m_nCard = nCard;
		if (TDebug.TraceMixer)
		{
			TDebug.out("AlsaMixer.<init>: end.");
		}
	}



	public int getCard()
	{
		return m_nCard;
	}



	//////////////// Line //////////////////////////////////////


	// TODO: allow real close and reopen of mixer
	public void open()
	{
	}



	public void close()
	{
	}





	//////////////// Mixer //////////////////////////////////////


	public Line getLine(Line.Info info)
		throws	LineUnavailableException
	{
		DataLine.Info	dataLineInfo = (DataLine.Info) info;
		Class		lineClass = info.getLineClass();
		AudioFormat[]	aFormats = dataLineInfo.getFormats();
		AudioFormat	format = null;
		if (lineClass == SourceDataLine.class)
		{
			format = getSupportedSourceFormat(aFormats);
			return getSourceDataLine(format, dataLineInfo.getMaxBufferSize());
		}
		else if (lineClass == Clip.class)
		{
			format = getSupportedSourceFormat(aFormats);
			return getClip(format);
		}
		else if (lineClass == TargetDataLine.class)
		{
			format = getSupportedTargetFormat(aFormats);
			return getTargetDataLine(format, dataLineInfo.getMaxBufferSize());
		}
		else
		{
			throw new LineUnavailableException("unknown line class: " + lineClass);
		}
	}


	private AudioFormat getSupportedSourceFormat(AudioFormat[] aFormats)
	{
		for (int i = 0; i < aFormats.length; i++)
		{
			if (isSourceFormatSupported(aFormats[i]))
			{
				return aFormats[i];
			}
		}
		throw new IllegalArgumentException("no line matchine one of the passed formats");
	}



	private AudioFormat getSupportedTargetFormat(AudioFormat[] aFormats)
	{
		for (int i = 0; i < aFormats.length; i++)
		{
			if (isTargetFormatSupported(aFormats[i]))
			{
				return aFormats[i];
			}
		}
		throw new IllegalArgumentException("no line matchine one of the passed formats");
	}


/*
	public Line.Info getLineInfo(Line.Info info)
	{
		// TODO:
		return null;
	}
*/


	public int getMaxLines(Line.Info info)
	{
		// TODO:
		return 0;
	}




	//////////////// private //////////////////////////////////////


	// nBufferSize is in bytes!
	private SourceDataLine getSourceDataLine(AudioFormat format, int nBufferSize)
		throws	LineUnavailableException
	{
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
		if (TDebug.TraceMixer)
		{
			TDebug.out("AlsaMixer.getSourceDataLine(): returning: " + sourceDataLine);
		}
		return sourceDataLine;
	}


	// nBufferSize is in bytes!
	private TargetDataLine getTargetDataLine(AudioFormat format, int nBufferSize)
		throws	LineUnavailableException
	{
		int			nBufferSizeInBytes = nBufferSize * format.getFrameSize();
		AlsaTargetDataLine	targetDataLine = new AlsaTargetDataLine(this, format, nBufferSizeInBytes);
		// targetDataLine.start();
		if (TDebug.TraceMixer)
		{
			TDebug.out("AlsaMixer.getTargetDataLine(): returning: " + targetDataLine);
		}
		return targetDataLine;
	}



	private Clip getClip(AudioFormat format)
		throws	LineUnavailableException
	{
		// return new AlsaClip(this);
		return new TSoftClip(this, format);
	}




	// -------------------------------------------------------------



}



/*** AlsaMixer.java ***/
