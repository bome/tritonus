/*
 *	MidiEvent.java
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

package javax.sound.midi;



// NOTE: sun implementation is not serializable
public class MidiEvent
implements java.io.Serializable
{
	private MidiMessage	m_message;
	private long		m_lTick;



	public MidiEvent(MidiMessage message, long lTick)
	{
		m_message = message;
		m_lTick = lTick;
	}



	public MidiMessage getMessage()
	{
		return m_message;
	}



	public long getTick()
	{
		return m_lTick;
	}



	public void setTick(long lTick)
	{
		m_lTick = lTick;
	}
}



/*** MidiEvent.java ***/
