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


package	org.tritonus.share.sampled.mixer;


import	java.util.ArrayList;
import	java.util.Collection;
import	java.util.Iterator;
import	java.util.Set;

import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.Clip;
import	javax.sound.sampled.DataLine;
import	javax.sound.sampled.Line;
import	javax.sound.sampled.LineUnavailableException;
import	javax.sound.sampled.Mixer;
import	javax.sound.sampled.Port;
import	javax.sound.sampled.SourceDataLine;
import	javax.sound.sampled.TargetDataLine;

import	org.tritonus.share.TDebug;
import	org.tritonus.share.sampled.AudioFormats;
import	org.tritonus.share.ArraySet;



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
	private Collection	m_supportedPortLineInfos;
	private Set		m_openSourceDataLines;
	private Set		m_openTargetDataLines;


	/**	constructor for mixers without ports.
	 */
	protected TMixer(Mixer.Info mixerInfo,
			 Line.Info lineInfo,
			 Collection supportedSourceFormats,
			 Collection supportedTargetFormats,
			 Collection supportedSourceLineInfos,
			 Collection supportedTargetLineInfos)
	{
		this(mixerInfo,
		     lineInfo,
		     supportedSourceFormats,
		     supportedTargetFormats,
		     supportedSourceLineInfos,
		     supportedTargetLineInfos,
		     new ArrayList());
	}



	/**	Constructor for mixers with only ports.
	 */
	protected TMixer(Mixer.Info mixerInfo,
			 Line.Info lineInfo,
			 Collection supportedPortLineInfos)
	{
		this(mixerInfo,
		     lineInfo,
		     new ArrayList(),
		     new ArrayList(),
		     new ArrayList(),
		     new ArrayList(),
		     supportedPortLineInfos);
	}



	/**	Constructor for mixers with both normal lines and ports.
	 */
	protected TMixer(Mixer.Info mixerInfo,
			 Line.Info lineInfo,
			 Collection supportedSourceFormats,
			 Collection supportedTargetFormats,
			 Collection supportedSourceLineInfos,
			 Collection supportedTargetLineInfos,
			 Collection supportedPortLineInfos)
	{
		super(null,	// TMixer
		      lineInfo);
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.<init>(): begin");
		}
		m_mixerInfo = mixerInfo;
		setSupportInformation(
			supportedSourceFormats,
			supportedTargetFormats,
			supportedSourceLineInfos,
			supportedTargetLineInfos,
			supportedPortLineInfos);
		m_openSourceDataLines = new ArraySet();
		m_openTargetDataLines = new ArraySet();
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.<init>(): end");
		}
	}



	protected void setSupportInformation(
			 Collection supportedSourceFormats,
			 Collection supportedTargetFormats,
			 Collection supportedSourceLineInfos,
			 Collection supportedTargetLineInfos,
			 Collection supportedPortLineInfos)
	{
		if (TDebug.TraceMixer) { TDebug.out("TMixer.setSupportInformation(): begin"); }
		m_supportedSourceFormats = supportedSourceFormats;
		m_supportedTargetFormats = supportedTargetFormats;
		m_supportedSourceLineInfos = supportedSourceLineInfos;
		m_supportedTargetLineInfos = supportedTargetLineInfos;
		m_supportedPortLineInfos = supportedPortLineInfos;
		if (TDebug.TraceMixer) { TDebug.out("TMixer.setSupportInformation(): end"); }
	}



	public Mixer.Info getMixerInfo()
	{
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.getMixerInfo(): called");
		}
		return m_mixerInfo;
	}



	public Line.Info[] getSourceLineInfo()
	{
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.getSourceLineInfo(): called");
		}
		return (Line.Info[]) m_supportedSourceLineInfos.toArray(EMPTY_LINE_INFO_ARRAY);
	}



	public Line.Info[] getTargetLineInfo()
	{
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.getTargetLineInfo(): called");
		}
		return (Line.Info[]) m_supportedTargetLineInfos.toArray(EMPTY_LINE_INFO_ARRAY);
	}



	public Line.Info[] getSourceLineInfo(Line.Info info)
	{
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.getSourceLineInfo(Line.Info): info to test: " + info);
		}
		// TODO:
		return EMPTY_LINE_INFO_ARRAY;
	}



	public Line.Info[] getTargetLineInfo(Line.Info info)
	{
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.getTargetLineInfo(Line.Info): info to test: " + info);
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
		else if (lineClass.equals(Port.class))
		{
			return isLineSupportedImpl(info, m_supportedPortLineInfos);
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
			// Object	obj =  iterator.next();
			// System.out.println("line info:" + obj);
			// Line.Info	info2 = (Line.Info) obj;
			Line.Info	info2 = (Line.Info) iterator.next();
			if (info2.matches(info))
			{
				return true;
			}
		}
		return false;
	}



	public Line getLine(Line.Info info)
		throws	LineUnavailableException
	{
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.getLine(): begin");
		}
		DataLine.Info	dataLineInfo = (DataLine.Info) info;
		Class		lineClass = info.getLineClass();
		AudioFormat[]	aFormats = dataLineInfo.getFormats();
		AudioFormat	format = null;
		Line		line = null;
		if (lineClass == SourceDataLine.class)
		{
			if (TDebug.TraceMixer)
			{
				TDebug.out("TMixer.getLine(): type: SourceDataLine");
			}
			format = getSupportedSourceFormat(aFormats);
			line = getSourceDataLine(format, dataLineInfo.getMaxBufferSize());
		}
		else if (lineClass == Clip.class)
		{
			if (TDebug.TraceMixer)
			{
				TDebug.out("TMixer.getLine(): type: Clip");
			}
			format = getSupportedSourceFormat(aFormats);
			line = getClip(format);
		}
		else if (lineClass == TargetDataLine.class)
		{
			if (TDebug.TraceMixer)
			{
				TDebug.out("TMixer.getLine(): type: TargetDataLine");
			}
			format = getSupportedTargetFormat(aFormats);
			line = getTargetDataLine(format, dataLineInfo.getMaxBufferSize());
		}
		else
		{
			if (TDebug.TraceMixer)
			{
				TDebug.out("TMixer.getLine(): unknown line type, will throw exception");
			}
			throw new LineUnavailableException("unknown line class: " + lineClass);
		}
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.getLine(): end");
		}
		return line;
	}



	protected SourceDataLine getSourceDataLine(AudioFormat format, int nBufferSize)
		throws LineUnavailableException
	{
		if (TDebug.TraceMixer) { TDebug.out("TMixer.getSourceDataLine(): begin"); }
		throw new LineUnavailableException("not implemented");
	}



	protected Clip getClip(AudioFormat format)
		throws LineUnavailableException
	{
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.getClip(): begin");
		}
		throw new LineUnavailableException("not implemented");
	}



	protected TargetDataLine getTargetDataLine(AudioFormat format, int nBufferSize)
		throws LineUnavailableException
	{
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.getTargetDataLine(): begin");
		}
		throw new LineUnavailableException("not implemented");
	}



	private AudioFormat getSupportedSourceFormat(AudioFormat[] aFormats)
	{
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.getSupportedSourceFormat(): begin");
		}
		AudioFormat	format = null;
		for (int i = 0; i < aFormats.length; i++)
		{
			if (TDebug.TraceMixer)
			{
				TDebug.out("TMixer.getSupportedSourceFormat(): checking " + aFormats[i] + "...");
			}
			if (isSourceFormatSupported(aFormats[i]))
			{
				if (TDebug.TraceMixer)
				{
					TDebug.out("TMixer.getSupportedSourceFormat(): ...supported");
				}
				format = aFormats[i];
				break;
			}
			else
			{
				if (TDebug.TraceMixer)
				{
					TDebug.out("TMixer.getSupportedSourceFormat(): ...no luck");
				}
			}
		}
		if (format == null)
		{
			throw new IllegalArgumentException("no line matchine one of the passed formats");
		}
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.getSupportedSourceFormat(): end");
		}
		return format;
	}



	private AudioFormat getSupportedTargetFormat(AudioFormat[] aFormats)
	{
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.getSupportedTargetFormat(): begin");
		}
		AudioFormat	format = null;
		for (int i = 0; i < aFormats.length; i++)
		{
			if (TDebug.TraceMixer)
			{
				TDebug.out("TMixer.getSupportedTargetFormat(): checking " + aFormats[i] + " ...");
			}
			if (isTargetFormatSupported(aFormats[i]))
			{
				if (TDebug.TraceMixer)
				{
					TDebug.out("TMixer.getSupportedTargetFormat(): ...supported");
				}
				format = aFormats[i];
				break;
			}
			else
			{
				if (TDebug.TraceMixer)
				{
					TDebug.out("TMixer.getSupportedTargetFormat(): ...no luck");
				}
			}
		}
		if (format == null)
		{
			throw new IllegalArgumentException("no line matchine one of the passed formats");
		}
		if (TDebug.TraceMixer)
		{
			TDebug.out("TMixer.getSupportedTargetFormat(): end");
		}
		return format;
	}



/*
  not implemented here:
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

