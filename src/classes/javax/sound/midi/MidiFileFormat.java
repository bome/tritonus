/*
 *	MidiFileFormat.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 - 2004 by Matthias Pfisterer
 *
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package javax.sound.midi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;



public class MidiFileFormat
{
	public static final int		UNKNOWN_LENGTH = -1;

	protected int		type;
	protected float		divisionType;
	protected int		resolution;
	protected int		byteLength;
	protected long		microsecondLength;

	private Map<String, Object>	m_properties;
	private Map<String, Object>	m_unmodifiableProperties;



	public MidiFileFormat(int nType,
						  float fDivisionType,
						  int nResolution,
						  int nByteLength,
						  long lMicrosecondLength)
	{
		this(nType,
			 fDivisionType,
			 nResolution,
			 nByteLength,
			 lMicrosecondLength,
			 null);
	}


	public MidiFileFormat(int nType,
						  float fDivisionType,
						  int nResolution,
						  int nByteLength,
						  long lMicrosecondLength,
						  Map<String, Object> properties)
	{
		type = nType;
		divisionType = fDivisionType;
		resolution = nResolution;
		byteLength = nByteLength;
		microsecondLength = lMicrosecondLength;
		/* Here, we make a shallow copy of the map. It's unclear if this
		   is sufficient (or if a deep copy should be made).
		*/
		m_properties = new HashMap<String, Object>();
		if (properties != null)
		{
			m_properties.putAll(properties);
		}
		m_unmodifiableProperties = Collections.unmodifiableMap(m_properties);
	}


	public int getType()
	{
		return type;
	}



	public float getDivisionType()
	{
		return divisionType;
	}



	public int getResolution()
	{
		return resolution;
	}



	public int getByteLength()
	{
		return byteLength;
	}



	public long getMicrosecondLength()
	{
		return microsecondLength;
	}


	public Map<String, Object> properties()
	{
		return m_unmodifiableProperties;
	}



	public Object getProperty(String key)
	{
		return m_properties.get(key);
	}



	protected void setProperty(String key, Object value)
	{
		m_properties.put(key, value);
	}
}



/*** MidiFileFormat.java ***/
