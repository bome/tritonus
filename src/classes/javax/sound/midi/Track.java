/*
 *	Track.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999, 2000 by Matthias Pfisterer
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


import java.util.ArrayList;
import java.util.List;
import java.util.Vector;



public class Track
{
	// not used; only to confirm with official API
	protected Vector	events;

	// this is the actual store
	private List<MidiEvent>		m_events;



	public Track()
	{
		m_events = new ArrayList<MidiEvent>();
	}



	public synchronized boolean add(MidiEvent event)
	{
		if (!m_events.contains(event))
		{
			int	nIndex = size() - 1;
			for (nIndex = size() - 1;
			     nIndex >= 0 && get(nIndex).getTick() > event.getTick();
			     nIndex--)
			{
			}
			m_events.add(nIndex + 1, event);
			return true;
		}
		else
		{
			return false;
		}
	}



	public synchronized boolean remove(MidiEvent event)
	{
		return m_events.remove(event);
	}



	public synchronized MidiEvent get(int nIndex)
		throws ArrayIndexOutOfBoundsException
	{
		return m_events.get(nIndex);
	}



	public synchronized int size()
	{
		return m_events.size();
	}




	public long ticks()
	{
		/*
		 *	Since ordering by tick value is guaranteed, we can
		 *	simply pick the last event and return its tick value.
		 */
		return get(size() - 1).getTick();
	}

}



/*** Track.java ***/
