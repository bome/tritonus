/*
 *	TAudioFileFormat.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 by Matthias Pfisterer
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

package org.tritonus.share.sampled.file;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;



/**
 * This class is just to have a public constructor taking the
 * number of bytes of the whole file. The public constructor of
 * AudioFileFormat doesn't take this parameter, the one who takes
 * it is protected.
 *
 * @author Matthias Pfisterer
 */
public class TAudioFileFormat
extends	AudioFileFormat
{
	private Map<String, Object>	m_properties;
	private Map<String, Object>	m_unmodifiableProperties;


	/*
	 *	Note that the order of the arguments is different from
	 *	the one in AudioFileFormat.
	 */
	public TAudioFileFormat(Type type,
							AudioFormat audioFormat,
							int nLengthInFrames,
							int nLengthInBytes)
	{
		super(type,
			  nLengthInBytes,
			  audioFormat,
			  nLengthInFrames);
	}


	public TAudioFileFormat(Type type,
							AudioFormat audioFormat,
							int nLengthInFrames,
							int nLengthInBytes,
							Map<String, Object> properties)
	{
		super(type,
			  nLengthInBytes,
			  audioFormat,
			  nLengthInFrames);
		initMaps(properties);
	}


	private void initMaps(Map<String, Object> properties)
	{
		/* Here, we make a shallow copy of the map. It's unclear if this
		   is sufficient (of if a deep copy should be made).
		*/
		m_properties = new HashMap<String, Object>();
		m_properties.putAll(properties);
		m_unmodifiableProperties = Collections.unmodifiableMap(m_properties);
	}


	@Override
	public Map<String, Object> properties()
	{
		return m_unmodifiableProperties;
	}



	protected void setProperty(String key, Object value)
	{
		m_properties.put(key, value);
	}
}



/*** TAudioFileFormat.java ***/
