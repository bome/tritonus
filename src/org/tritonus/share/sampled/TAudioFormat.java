/*
 *	TAudioFormat.java
 */

/*
 *  Copyright (c) 2003 by Matthias Pfisterer
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
 */

package org.tritonus.share.sampled;

import java.util.Collections;
import java.util.Map;

import javax.sound.sampled.AudioFormat;



public class TAudioFormat
extends AudioFormat
{
	private static final String[] EMPTY_STRING_ARRAY = new String[0];

	private Map	m_properties;


	public TAudioFormat(AudioFormat.Encoding encoding,
						float sampleRate,
						int sampleSizeInBits,
						int channels,
						int frameSize,
						float frameRate,
						boolean bigEndian,
						Map properties)
	{
		super(encoding,
			  sampleRate,
			  sampleSizeInBits,
			  channels,
			  frameSize,
			  frameRate,
			  bigEndian);
		m_properties = Collections.unmodifiableMap(properties);
	}


	public TAudioFormat(float sampleRate,
						int sampleSizeInBits,
						int channels,
						boolean signed,
						boolean bigEndian,
						Map properties)
	{
		super(sampleRate,
			  sampleSizeInBits,
			  channels,
			  signed,
			  bigEndian);
		m_properties = Collections.unmodifiableMap(properties);
	}



	public Map properties()
	{
		return m_properties;
	}



	protected Object setProperty(String key, Object value)
	{
		throw new UnsupportedOperationException();
	}
}



/*** TAudioFormat.java ***/
