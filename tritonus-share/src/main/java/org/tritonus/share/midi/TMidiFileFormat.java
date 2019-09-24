/*
 *	TMidiFileFormat.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2000 by Matthias Pfisterer
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

package org.tritonus.share.midi;

import javax.sound.midi.MidiFileFormat;



/**	A MidiFileFormat that has information about the number of tracks.
 *	This class is used by org.tritonus.midi.file.StandardMidiFileReader.
 *	Its purpose is to carry the number of tracks from
 *	getMidiFileFormat() to getSequence().
 */
public class TMidiFileFormat
extends MidiFileFormat
{
	private int		m_nTrackCount;



	public TMidiFileFormat(int nType,
			      float fDivisionType,
			      int nResolution,
			      int nByteLength,
			      long lMicrosecondLength,
			       int nTrackCount)
	{
		super(nType,
		      fDivisionType,
		      nResolution,
		      nByteLength,
		      lMicrosecondLength);
		m_nTrackCount = nTrackCount;
	}



	public int getTrackCount()
	{
		return m_nTrackCount;
	}
}



/*** TMidiFileFormat.java ***/
