/*
 *	AlsaMidiIn.java
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


package	org.tritonus.midi.device.alsa;


import	javax.sound.midi.MidiDevice;
import	javax.sound.midi.MidiEvent;
import	javax.sound.midi.MidiUnavailableException;
import	javax.sound.midi.Receiver;
import	javax.sound.midi.Transmitter;

import	org.tritonus.TDebug;
import	org.tritonus.lowlevel.alsa.ASequencer;





public class AlsaMidiIn
	extends		Thread
{
	private ASequencer		m_aSequencer;
	private int			m_nSourceClient;
	private int			m_nSourcePort;
	private int			m_nDestPort;
	private AlsaMidiInListener	m_listener;



	public AlsaMidiIn(ASequencer aSequencer, int nDestPort, int nSourceClient, int nSourcePort, AlsaMidiInListener listener)
	{
		m_nSourceClient = nSourceClient;
		m_nSourcePort = nSourcePort;
		m_listener = listener;
		m_aSequencer = aSequencer;
		m_nDestPort = nDestPort;
		m_aSequencer.subscribePort(nSourceClient, nSourcePort, m_aSequencer.getClientId(), nDestPort);
		setDaemon(true);
	}



	public void run()
	{
		// TODO: recheck interupt mechanism
		while (!interrupted())
		{
			// TODO: do we need a filter in ASequencer?
			MidiEvent	event = m_aSequencer.getEvent();
			if (TDebug.TraceAlsaMidiIn)
			{
				TDebug.out("AlsaMidiIn.run(): got event: " + event);
			}
			if (event != null)
			{
				m_listener.dequeueEvent(event);
			}
		}
	}



	public static interface AlsaMidiInListener
	{
		public void dequeueEvent(MidiEvent event);
	}
}



/*** AlsaMidiIn.java ***/
