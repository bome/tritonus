/*
 *	AlsaMidiIn.java
 */

/*
 *  Copyright (c) 1999 - 2001 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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

import	org.tritonus.share.TDebug;
import	org.tritonus.lowlevel.alsa.AlsaSeq;





public class AlsaMidiIn
	extends		Thread
{
	private AlsaSeq		m_aSequencer;
	private int			m_nSourceClient;
	private int			m_nSourcePort;
	private int			m_nDestPort;
	private AlsaMidiInListener	m_listener;


	/**
	   Does establish a subscription where events are routed directely
	   (not getting a timestamp).
	 */
	public AlsaMidiIn(AlsaSeq aSequencer,
			  int nDestPort,
			  int nSourceClient,
			  int nSourcePort,
			  AlsaMidiInListener listener)
	{
		this(aSequencer,
		     nDestPort,
		     nSourceClient,
		     nSourcePort,
		     -1, false,		// signals: do not do timestamping
		     listener);
	}



	/**
	   Does establish a subscription where events are routed through
	   a queue to get a timestamp.
	 */
	public AlsaMidiIn(AlsaSeq aSequencer,
			  int nDestPort,
			  int nSourceClient,
			  int nSourcePort,
			  int nTimestampingQueue,
			  boolean bRealtime,
			  AlsaMidiInListener listener)
	{
		m_nSourceClient = nSourceClient;
		m_nSourcePort = nSourcePort;
		m_listener = listener;
		m_aSequencer = aSequencer;
		m_nDestPort = nDestPort;
		if (nTimestampingQueue >= 0)
		{
			m_aSequencer.subscribePort(
				nSourceClient, nSourcePort,
				m_aSequencer.getClientId(), nDestPort,
				nTimestampingQueue,
				false,		// exclusive
				bRealtime,	// realtime
				true);		// convert time
		}
		else
		{
			m_aSequencer.subscribePort(
				nSourceClient, nSourcePort,
				m_aSequencer.getClientId(), nDestPort);
		}
		setDaemon(true);
	}


	/**	The working part of the class.
		Here, the thread repeats in blocking in a call to
		AlsaSeq.getEvent() and calling the listener's
		dequeueEvent() method.
	 */
	public void run()
	{
		// TODO: recheck interupt mechanism
		while (!interrupted())
		{
			// TODO: do we need a filter in AlsaSeq?
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



	/**
	 */
	public static interface AlsaMidiInListener
	{
		public void dequeueEvent(MidiEvent event);
	}
}



/*** AlsaMidiIn.java ***/
