/*
 *	TMixer.java
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


package	org.tritonus.sampled.mixer;


import	java.util.Collection;
import	java.util.Iterator;

import	javax.sound.sampled.Line;
import	javax.sound.sampled.Mixer;
import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.SourceDataLine;
import	javax.sound.sampled.TargetDataLine;

import	org.tritonus.TDebug;



public abstract class TMixer
	extends		TLine
	implements	Mixer
{
	private static Line.Info[]	EMPTY_LINE_INFO_ARRAY = new Line.Info[0];

	private Mixer.Info	m_mixerInfo;
	private Collection	m_supportedSourceFormats;
	private Collection	m_supportedTargetFormats;
	private Collection	m_supportedSourceLineInfos;
	private Collection	m_supportedTargetLineInfos;



	public TMixer(Mixer.Info mixerInfo,
		      Line.Info lineInfo,
		      Collection supportedSourceFormats,
		      Collection supportedTargetFormats,
		      Collection supportedSourceLineInfos,
		      Collection supportedTargetLineInfos)
	{
		super(lineInfo);
		m_mixerInfo = mixerInfo;
		m_supportedSourceFormats = supportedSourceFormats;
		m_supportedTargetFormats = supportedTargetFormats;
		m_supportedSourceLineInfos = supportedSourceLineInfos;
		m_supportedTargetLineInfos = supportedTargetLineInfos;
	}



	public Mixer.Info getMixerInfo()
	{
		return m_mixerInfo;
	}


	public Line.Info[] getSourceLineInfo()
	{
		return (Line.Info[]) m_supportedSourceLineInfos.toArray(EMPTY_LINE_INFO_ARRAY);
	}



	public Line.Info[] getTargetLineInfo()
	{
		return (Line.Info[]) m_supportedTargetLineInfos.toArray(EMPTY_LINE_INFO_ARRAY);
	}



	public Line.Info[] getSourceLineInfo(Line.Info info)
	{
		// TODO:
		return null;
	}



	public Line.Info[] getTargetLineInfo(Line.Info info)
	{
		// TODO:
		return null;
	}



	public boolean isLineSupported(Line.Info info)
	{
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.isLineSupported(): info to test: " + info);
		}
		Class	lineClass = info.getLineClass();
		if (lineClass.equals(SourceDataLine.class))
		{
			return isLineSupportedImpl(info, m_supportedSourceLineInfos);
		}
		else if (lineClass.equals(TargetDataLine.class))
		{
			return isLineSupportedImpl(info, m_supportedTargetLineInfos);
		}
		else
		{
			return false;
		}
	}



	private static boolean isLineSupportedImpl(Line.Info info, Collection supportedLineInfos)
	{
		Iterator	iterator = supportedLineInfos.iterator();
		while (iterator.hasNext())
		{
			Line.Info	info2 = (Line.Info) iterator.next();
			if (info2.matches(info))
			{
				return true;
			}
		}
		return false;
	}



	public void synchronize(Line[] aLines,
				boolean bMaintainSync)
	{
		throw new IllegalArgumentException("synchronization not supported");
	}


	public void unsynchronize(Line[] aLines)
	{
		throw new IllegalArgumentException("synchronization not supported");
	}


	public boolean isSynchronizationSupported(Line[] aLines,
				boolean bMaintainSync)
	{
		return false;
	}



	protected boolean isSourceFormatSupported(AudioFormat format)
	{
		Iterator	iterator = m_supportedSourceFormats.iterator();
		while (iterator.hasNext())
		{
			AudioFormat	supportedFormat = (AudioFormat) iterator.next();
			if (supportedFormat.matches(format))
			{
				return true;
			}
		}
		return false;
	}



	protected boolean isTargetFormatSupported(AudioFormat format)
	{
		Iterator	iterator = m_supportedTargetFormats.iterator();
		while (iterator.hasNext())
		{
			AudioFormat	supportedFormat = (AudioFormat) iterator.next();
			if (supportedFormat.matches(format))
			{
				return true;
			}
		}
		return false;
	}
}



/*** TMixer.java ***/

