/*
 *	Track.java
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


package	javax.sound.midi;


import	java.util.ArrayList;
import	java.util.List;




public class Track
{
	private List		m_events;
	private long		m_lTicks;




	public Track()
	{
		m_lTicks = 0;
		m_events = new ArrayList();
	}



	public synchronized boolean add(MidiEvent event)
	{
		// if (!m_events.contains(event))
		// {
			m_events.add(event);
			return true;
/*
		}
		else
		{
			return false;
		}
*/
	}



	public synchronized boolean remove(MidiEvent event)
	{
		return m_events.remove(event);
	}



	public synchronized MidiEvent get(int nIndex)
		// TODO: throws ArrayIndexOutOfBoundsException ??
	{
		return (MidiEvent) m_events.get(nIndex);
	}



	public synchronized int size()
	{
		return m_events.size();
	}



	public long ticks()
	{
		return m_lTicks;
	}

}



/*** Track.java ***/
