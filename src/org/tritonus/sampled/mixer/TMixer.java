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
import	java.util.Set;

import	javax.sound.sampled.Line;
import	javax.sound.sampled.Mixer;
import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.SourceDataLine;
import	javax.sound.sampled.TargetDataLine;

import	org.tritonus.TDebug;
import	org.tritonus.sampled.AudioFormats;
import	org.tritonus.util.ArraySet;


// TODO: global controls (that use the system mixer)
public abstract class TMixer
	extends		TLine
	implements	Mixer
{
	private static Line.Info[]	EMPTY_LINE_INFO_ARRAY = new Line.Info[0];
	private static Line[]		EMPTY_LINE_ARRAY = new Line[0];

	private Mixer.Info	m_mixerInfo;
	private Collection	m_supportedSourceFormats;
	private Collection	m_supportedTargetFormats;
	private Collection	m_supportedSourceLineInfos;
	private Collection	m_supportedTargetLineInfos;
	private Set		m_openSourceDataLines;
	private Set		m_openTargetDataLines;



	public TMixer(Mixer.Info mixerInfo,
		      Line.Info lineInfo,
		      Collection supportedSourceFormats,
		      Collection supportedTargetFormats,
		      Collection supportedSourceLineInfos,
		      Collection supportedTargetLineInfos)
	{
		super(null,	// TMixer
		      lineInfo);
		m_mixerInfo = mixerInfo;
		m_supportedSourceFormats = supportedSourceFormats;
		m_supportedTargetFormats = supportedTargetFormats;
		m_supportedSourceLineInfos = supportedSourceLineInfos;
		m_supportedTargetLineInfos = supportedTargetLineInfos;
		m_openSourceDataLines = new ArraySet();
		m_openTargetDataLines = new ArraySet();
	}



	public Mixer.Info getMixerInfo()
	{
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.isLineSupported(): called");
		}
		return m_mixerInfo;
	}



	public Line.Info[] getSourceLineInfo()
	{
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.isLineSupported(): called");
		}
		return (Line.Info[]) m_supportedSourceLineInfos.toArray(EMPTY_LINE_INFO_ARRAY);
	}



	public Line.Info[] getTargetLineInfo()
	{
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.isLineSupported(): called");
		}
		return (Line.Info[]) m_supportedTargetLineInfos.toArray(EMPTY_LINE_INFO_ARRAY);
	}



	public Line.Info[] getSourceLineInfo(Line.Info info)
	{
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.getSourceLineInfo(): info to test: " + info);
		}
		// TODO:
		return EMPTY_LINE_INFO_ARRAY;
	}



	public Line.Info[] getTargetLineInfo(Line.Info info)
	{
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.getTargetLineInfo(): info to test: " + info);
		}
		// TODO:
		return EMPTY_LINE_INFO_ARRAY;
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



/*
  not implemented here:
  getLine(Line.Info)
  getMaxLines(Line.Info)
 */



	public Line[] getSourceLines()
	{
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.getSourceLines(): called");
		}
		return (Line[]) m_openSourceDataLines.toArray(EMPTY_LINE_ARRAY);
	}



	public Line[] getTargetLines()
	{
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.getTargetLines(): called");
		}
		return (Line[]) m_openTargetDataLines.toArray(EMPTY_LINE_ARRAY);
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
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.isSourceFormatSupported(): format to test: " + format);
		}
		Iterator	iterator = m_supportedSourceFormats.iterator();
		while (iterator.hasNext())
		{
			AudioFormat	supportedFormat = (AudioFormat) iterator.next();
			if (AudioFormats.matches(supportedFormat, format))
			{
				return true;
			}
		}
		return false;
	}



	protected boolean isTargetFormatSupported(AudioFormat format)
	{
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.isTargetFormatSupported(): format to test: " + format);
		}
		Iterator	iterator = m_supportedTargetFormats.iterator();
		while (iterator.hasNext())
		{
			AudioFormat	supportedFormat = (AudioFormat) iterator.next();
			if (AudioFormats.matches(supportedFormat, format))
			{
				return true;
			}
		}
		return false;
	}



	/*package*/ void registerOpenLine(Line line)
	{
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.registerOpenLine(): line to register: " + line);
		}
		if (line instanceof SourceDataLine)
		{
			synchronized (m_openSourceDataLines)
			{
				m_openSourceDataLines.add(line);
			}
		}
		else if (line instanceof TargetDataLine)
		{
			synchronized (m_openSourceDataLines)
			{
				m_openTargetDataLines.add(line);
			}
		}
	}



	/*package*/ void unregisterOpenLine(Line line)
	{
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.unregisterOpenLine(): line to unregister: " + line);
		}
		if (line instanceof SourceDataLine)
		{
			synchronized (m_openSourceDataLines)
			{
				m_openSourceDataLines.remove(line);
			}
		}
		else if (line instanceof TargetDataLine)
		{
			synchronized (m_openTargetDataLines)
			{
				m_openTargetDataLines.remove(line);
			}
		}
	}
}



/*** TMixer.java ***/

