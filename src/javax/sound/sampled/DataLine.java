/*
 *	DataLine.java
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


package	javax.sound.sampled;


import	java.util.List;
import	java.util.Arrays;
import	java.util.Iterator;

import	org.tritonus.TDebug;
import	org.tritonus.sampled.AudioFormats;



public interface DataLine
	extends	Line
{
	public void drain();


	public void flush();


	public void start();


	public void stop();


	public boolean isRunning();


	public boolean isActive();


	public AudioFormat getFormat();


	public int getBufferSize();


	public int available();


	public int getFramePosition();


	public long getMicrosecondPosition();


	public float getLevel();




	public static class Info
		extends		Line.Info
	{
		private AudioFormat[]	EMPTY_AUDIO_FORMAT_ARRAY = new AudioFormat[0];

		private List		m_audioFormats;
		private int		m_nMinBufferSize;
		private int		m_nMaxBufferSize;


		public Info(Class lineClass,
			    AudioFormat[] aAudioFormats,
			    int nMinBufferSize,
			    int nMaxBufferSize)
		{
			super(lineClass);
			m_audioFormats = Arrays.asList(aAudioFormats);
			m_nMinBufferSize = nMinBufferSize;
			m_nMaxBufferSize = nMaxBufferSize;
		}



		public Info(Class lineClass,
			    AudioFormat audioFormat,
			    int nBufferSize)
		{
			this(lineClass,
			     new AudioFormat[]{audioFormat},
			     nBufferSize,
			     nBufferSize);
		}



		public Info(Class lineClass,
			    AudioFormat audioFormat)
		{
			this(lineClass,
			     audioFormat,
			     AudioSystem.NOT_SPECIFIED);
		}



		public AudioFormat[] getFormats()
		{
			return (AudioFormat[]) m_audioFormats.toArray(EMPTY_AUDIO_FORMAT_ARRAY);
		}



		public boolean isFormatSupported(AudioFormat audioFormat)
		{
			Iterator	formats = m_audioFormats.iterator();
			while (formats.hasNext())
			{
				AudioFormat	format = (AudioFormat) formats.next();
				if (AudioFormats.matches(format, audioFormat))
				{
					return true;
				}
			}
			return false;
		}



		public int getMinBufferSize()
		{
			return m_nMinBufferSize;
		}



		public int getMaxBufferSize()
		{
			return m_nMaxBufferSize;
		}



		public boolean matches(Line.Info info)
		{
			if (TDebug.TraceDataLine)
			{
				TDebug.out("DataLine.Info.matches(): called");
				TDebug.out("DataLine.Info.matches(): own info: " + this.toString());
				TDebug.out("DataLine.Info.matches(): test info: " + info.toString());
			}
			if (!super.matches(info))
			{
				if (TDebug.TraceDataLine)
				{
					TDebug.out("DataLine.Info.matches(): super.matches does not match()");
				}
				return false;
			}
			DataLine.Info	dataLineInfo = (DataLine.Info) info;
			// TODO: check against documentation !!
			if ( getMinBufferSize() < dataLineInfo.getMinBufferSize() ||
			     getMaxBufferSize() > dataLineInfo.getMaxBufferSize() )
			{
				// TDebug.out("DataLine.Info.matches(): buffer sizes do not match");
				return false;
			}
			AudioFormat[]	infoFormats = dataLineInfo.getFormats();
			for (int i = 0; i < infoFormats.length; i++)
			{
				if (TDebug.TraceDataLine)
				{
					TDebug.out("DataLine.Info.matches(): checking if supported: " + infoFormats[i]);
				}
				if (! isFormatSupported(infoFormats[i]))
				{
					if (TDebug.TraceDataLine)
					{
						TDebug.out("DataLine.Info.matches(): formats do not match");
					}
					return false;
				}
			}
			return true;
		}



		public String toString()
		{
			AudioFormat[]	aFormats = getFormats();
			String	strFormats = "formats:\n";
			for (int i = 0; i < aFormats.length; i++)
			{
				strFormats += aFormats[i].toString() + "\n";
			}
			return super.toString() + strFormats + "minBufferSize=" + getMinBufferSize() + " maxBufferSize=" + getMaxBufferSize();
		}

	}
}



/*** DataLine.java ***/
