/*
 *	TestSystemMixer.java
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
import	java.util.ArrayList;
import	java.util.HashMap;
import	java.util.Map;
import	java.util.HashSet;
import	java.util.Set;
import	java.util.Iterator;
import	java.util.List;

import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.BooleanControl;
import	javax.sound.sampled.Clip;
import	javax.sound.sampled.DataLine;
import	javax.sound.sampled.FloatControl;
import	javax.sound.sampled.SourceDataLine;
import	javax.sound.sampled.TargetDataLine;
import	javax.sound.sampled.LineUnavailableException;
import	javax.sound.sampled.Line;
import	javax.sound.sampled.Mixer;
import	javax.sound.sampled.Port;

import	org.tritonus.share.GlobalInfo;
import	org.tritonus.share.TDebug;
import	org.tritonus.share.sampled.mixer.TBooleanControl;
import	org.tritonus.share.sampled.mixer.TFloatControl;
import	org.tritonus.share.sampled.mixer.TMixer;
import	org.tritonus.share.sampled.mixer.TMixerInfo;
import	org.tritonus.share.sampled.mixer.TPort;
import	org.tritonus.share.sampled.mixer.TSoftClip;




public class TestSystemMixer
	extends		TMixer
{
	// default buffer size in bytes.
	private static final int	DEFAULT_BUFFER_SIZE = 32768;


	private static Line.Info[]	PORT_LINE_INFOS =
	{
		Port.Info.LINE_IN,
		Port.Info.LINE_OUT,
	};


	/**	The number of the sound card this mixer is representing.
	 */
	private int	m_nCard;

	private Port	m_inPort;
	private Port	m_outPort;


	public TestSystemMixer()
	{
		super(new TMixerInfo(
			"Test System Mixer #0",
			GlobalInfo.getVendor(),
			"Test System Mixer #0",
			GlobalInfo.getVersion()),
		      new Line.Info(Mixer.class)/*,
						  Arrays.asList(PORT_LINE_INFOS)*/);
		if (TDebug.TraceMixer)
		{
			TDebug.out("TestSystemMixer.<init>: beginning.");
		}

		List	inControls = new ArrayList();
		inControls.add(new TBooleanControl(BooleanControl.Type.MUTE, false, "muted", "unmuted"));
		inControls.add(new TBooleanControl(BooleanControl.Type.MUTE, false));
		inControls.add(new TFloatControl(FloatControl.Type.VOLUME,
						 -48.0F,
						 +12.0F,
						 0.01F,
						 1,
						 0.0F,
						 "dB"));
		m_inPort = new TPort(this, Port.Info.LINE_IN, inControls);

/*
		List	outControls = new ArrayList();
		outControls.add(new TBooleanControl(BooleanControl.Type.MUTE, false, "muted", "unmuted"));
		m_outPort = new TPort(this, Port.Info.LINE_OUT, outControls);
*/
		if (TDebug.TraceMixer)
		{
			TDebug.out("TestSystemMixer.<init>: end.");
		}
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
		Class		lineClass = info.getLineClass();
		if (lineClass == Port.class)
		{
			if (info.matches(Port.Info.LINE_IN))
			{
				return m_inPort;
			}
			else if (info.matches(Port.Info.LINE_OUT))
			{
				return m_outPort;
			}
			else
			{
				return null;
			}
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
	protected SourceDataLine getSourceDataLine(AudioFormat format, int nBufferSize)
		throws	LineUnavailableException
	{
		if (TDebug.TraceMixer)
		{
			TDebug.out("TestSystemMixer.getSourceDataLine(): format: " + format);
			TDebug.out("TestSystemMixer.getSourceDataLine(): buffer size: " + nBufferSize);
		}
// 		if (nBufferSize < 1)
// 		{
// 			nBufferSize = DEFAULT_BUFFER_SIZE;
// 		}
// 		AlsaSourceDataLine	sourceDataLine = new AlsaSourceDataLine(this, format, nBufferSize);
// 		// sourceDataLine.start();
// 		if (TDebug.TraceMixer)
// 		{
// 			TDebug.out("TestSystemMixer.getSourceDataLine(): returning: " + sourceDataLine);
// 		}
// 		return sourceDataLine;
		return null;
	}


	// nBufferSize is in bytes!
	protected TargetDataLine getTargetDataLine(AudioFormat format, int nBufferSize)
		throws	LineUnavailableException
	{
/*
		int			nBufferSizeInBytes = nBufferSize * format.getFrameSize();
		AlsaTargetDataLine	targetDataLine = new AlsaTargetDataLine(this, format, nBufferSizeInBytes);
		// targetDataLine.start();
		if (TDebug.TraceMixer)
		{
			TDebug.out("TestSystemMixer.getTargetDataLine(): returning: " + targetDataLine);
		}
		return targetDataLine;
*/
		return null;
	}



	protected Clip getClip(AudioFormat format)
		throws	LineUnavailableException
	{
		// return new AlsaClip(this);
		return new TSoftClip(this, format);
	}




	// -------------------------------------------------------------



}



/*** TestSystemMixer.java ***/
