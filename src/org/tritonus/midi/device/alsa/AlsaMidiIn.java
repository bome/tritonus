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


import	javax.sound.midi.InvalidMidiDataException;
import	javax.sound.midi.MetaMessage;
import	javax.sound.midi.MidiDevice;
import	javax.sound.midi.MidiEvent;
import	javax.sound.midi.MidiMessage;
import	javax.sound.midi.MidiUnavailableException;
import	javax.sound.midi.Receiver;
import	javax.sound.midi.ShortMessage;
import	javax.sound.midi.SysexMessage;
import	javax.sound.midi.Transmitter;

import	org.tritonus.share.TDebug;
import	org.tritonus.lowlevel.alsa.AlsaSeq;




public class AlsaMidiIn
	extends		Thread
{
	private AlsaSeq			m_alsaSeq;
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
		m_alsaSeq = aSequencer;
		m_nDestPort = nDestPort;
		if (nTimestampingQueue >= 0)
		{
			getAlsaSeq().subscribePort(
				nSourceClient, nSourcePort,
				getAlsaSeq().getClientId(), nDestPort,
				nTimestampingQueue,
				false,		// exclusive
				bRealtime,	// realtime
				true);		// convert time
		}
		else
		{
			getAlsaSeq().subscribePort(
				nSourceClient, nSourcePort,
				getAlsaSeq().getClientId(), nDestPort);
		}
		setDaemon(true);
	}



	private AlsaSeq getAlsaSeq()
	{
		return m_alsaSeq;
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
			MidiEvent	event = getEvent();
			if (TDebug.TraceAlsaMidiIn) { TDebug.out("AlsaMidiIn.run(): got event: " + event); }

			// TODO: event == null indicates a serious bug
			if (event != null)
			{
				MidiMessage	m = event.getMessage();
				if (m instanceof MetaMessage)
				{
					MetaMessage	me = (MetaMessage) m;
					if (TDebug.TraceAlsaMidiIn) { TDebug.out("AlsaMidiIn.run(): MetaMessage.getData().length: " + me.getData().length); }
				}
				m_listener.dequeueEvent(event);
			}
		}
	}



	private MidiEvent getEvent()
	{
		int[]	anValues = new int[13];
		long[]	alValues = new long[1];
		Object[]	aValues = new Object[1];
		if (TDebug.TraceAlsaSeq) { TDebug.out("AlsaSeq.getEvent(): before getEvent(int[], long[])"); }
		while (true)
		{
			boolean	bEventPresent = getAlsaSeq().getEvent(anValues, alValues, aValues);
			if (bEventPresent)
			{
				break;
			}
			/*
			 *	Sleep for 1 ms to enable scheduling.
			 */
			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException e)
			{
				if (TDebug.TraceAllExceptions) { TDebug.out(e); }
			}
		}
		// TDebug.out("after getEvent()");
		MidiMessage	message = null;
		int	nType = anValues[0];
		switch (nType)
		{
		case AlsaSeq.SND_SEQ_EVENT_NOTEON:
		case AlsaSeq.SND_SEQ_EVENT_NOTEOFF:
		{
			if (TDebug.TraceAlsaSeq)
			{
				TDebug.out("AlsaSeq.getEvent(): note event");
			}
			ShortMessage	shortMessage = new ShortMessage();
			int	nCommand = (nType == AlsaSeq.SND_SEQ_EVENT_NOTEON) ? ShortMessage.NOTE_ON : ShortMessage.NOTE_OFF;
			int	nChannel = anValues[8] & 0xF;
			int	nKey = anValues[9] & 0x7F;
			int	nVelocity = anValues[10] & 0x7F;
			try
			{
				shortMessage.setMessage(nCommand, nChannel, nKey, nVelocity);
			}
			catch (InvalidMidiDataException e)
			{
				if (TDebug.TraceAlsaSeq || TDebug.TraceAllExceptions)
				{
					TDebug.out(e);
				}
			}
			message = shortMessage;
			break;
		}

		case AlsaSeq.SND_SEQ_EVENT_KEYPRESS:
		{
			if (TDebug.TraceAlsaSeq)
			{
				TDebug.out("AlsaSeq.getEvent(): key pressure event");
			}
			ShortMessage	shortMessage = new ShortMessage();
			int	nCommand = 0xA0;
			int	nChannel = anValues[8] & 0xF;
			int	nKey = anValues[9] & 0x7F;
			int	nValue = anValues[10] & 0x7F;
			try
			{
				shortMessage.setMessage(nCommand, nChannel, nKey, nValue);
			}
			catch (InvalidMidiDataException e)
			{
				if (TDebug.TraceAlsaSeq || TDebug.TraceAllExceptions)
				{
					TDebug.out(e);
				}
			}
			message = shortMessage;
			break;
		}

		case AlsaSeq.SND_SEQ_EVENT_CONTROLLER:
		{
			if (TDebug.TraceAlsaSeq)
			{
				TDebug.out("AlsaSeq.getEvent(): controller event");
			}
			ShortMessage	shortMessage = new ShortMessage();
			int	nCommand = 0xB0;
			int	nChannel = anValues[8] & 0xF;
			int	nControl = anValues[9] & 0x7F;
			int	nValue = anValues[10] & 0x7F;
			try
			{

				shortMessage.setMessage(nCommand, nChannel, nControl, nValue);
			}
			catch (InvalidMidiDataException e)
			{
				if (TDebug.TraceAlsaSeq || TDebug.TraceAllExceptions)
				{
					TDebug.out(e);
				}
			}
			message = shortMessage;
			break;
		}

		case AlsaSeq.SND_SEQ_EVENT_PGMCHANGE:
		{
			if (TDebug.TraceAlsaSeq)
			{
				TDebug.out("AlsaSeq.getEvent(): program change event");
			}
			ShortMessage	shortMessage = new ShortMessage();
			int	nCommand = 0xC0;
			int	nChannel = anValues[8] & 0xF;
			int	nProgram = anValues[10] & 0x7F;
			try
			{
				shortMessage.setMessage(nCommand, nChannel, nProgram, 0);
			}
			catch (InvalidMidiDataException e)
			{
				if (TDebug.TraceAllExceptions)
				{
					TDebug.out(e);
				}
			}
			message = shortMessage;
			break;
		}

		case AlsaSeq.SND_SEQ_EVENT_CHANPRESS:
		{
			if (TDebug.TraceAlsaSeq)
			{
				TDebug.out("AlsaSeq.getEvent(): channel pressure event");
			}
			ShortMessage	shortMessage = new ShortMessage();
			int	nCommand = 0xD0;
			int	nChannel = anValues[8] & 0xF;
			int	nPressure = anValues[10] & 0x7F;
			try
			{
				// TODO: dirty: assuming time in ticks
				shortMessage.setMessage(nCommand, nChannel, nPressure, 0);
			}
			catch (InvalidMidiDataException e)
			{
				if (TDebug.TraceAlsaSeq || TDebug.TraceAllExceptions) { TDebug.out(e); }
			}
			message = shortMessage;
			break;
		}

		case AlsaSeq.SND_SEQ_EVENT_PITCHBEND:
		{
			if (TDebug.TraceAlsaSeq)
			{
				TDebug.out("AlsaSeq.getEvent(): pitchbend event");
			}
			ShortMessage	shortMessage = new ShortMessage();
			int	nCommand = 0xE0;
			int	nChannel = anValues[8] & 0xF;
			int	nValueLow = anValues[10] & 0x7F;
			int	nValueHigh = (anValues[10] >> 7) & 0x7F;
			try
			{
				shortMessage.setMessage(nCommand, nChannel, nValueLow, nValueHigh);
			}
			catch (InvalidMidiDataException e)
			{
				if (TDebug.TraceAlsaSeq || TDebug.TraceAllExceptions)
				{
					TDebug.out(e);
				}
			}
			message = shortMessage;
			break;
		}

		case AlsaSeq.SND_SEQ_EVENT_USR_VAR4:
		{
			if (TDebug.TraceAlsaSeq) { TDebug.out("AlsaSeq.getEvent(): meta event"); }
			MetaMessage	metaMessage = new MetaMessage();
			byte[]	abTransferData = (byte[]) aValues[0];
			int	nMetaType = abTransferData[0];
			byte[]	abData = new byte[abTransferData.length - 1];
			System.arraycopy(abTransferData, 1, abData, 0, abTransferData.length - 1);
			try
			{
				metaMessage.setMessage(nMetaType, abData, abData.length);
			}
			catch (InvalidMidiDataException e)
			{
				if (TDebug.TraceAlsaSeq || TDebug.TraceAllExceptions) { TDebug.out(e); }
			}
			message = metaMessage;
			break;
		}

		case AlsaSeq.SND_SEQ_EVENT_SYSEX:
		{
			if (TDebug.TraceAlsaSeq)
			{
				TDebug.out("AlsaSeq.getEvent(): sysex event");
			}
			SysexMessage	sysexMessage = new SysexMessage();
			byte[]	abCompleteData = (byte[]) aValues[0];
			int	nStatus = 0;
			int	nDataStart = 0;
			if (abCompleteData[0] == 0xF0)
			{
				nStatus = 0xF0;
				nDataStart = 1;
			}
			else
			{
				nStatus = 0xF7;
			}
			byte[]	abData = new byte[abCompleteData.length - nDataStart];
			System.arraycopy(abCompleteData, nDataStart, abData, 0, abCompleteData.length - 1);
			try
			{
				sysexMessage.setMessage(nStatus, abData, abData.length);
			}
			catch (InvalidMidiDataException e)
			{
				if (TDebug.TraceAlsaSeq || TDebug.TraceAllExceptions) { TDebug.out(e); }
			}
			message = sysexMessage;
			break;
		}

		default:
			if (TDebug.TraceAlsaSeq) { TDebug.out("AlsaSeq.getEvent(): unknown event"); }

		}
		if (message != null)
		{
			/*
			  If the timestamp is in ticks, ticks in the MidiEvent
			  gets this value.
			  Otherwise, if the timestamp is in realtime (ns),
			  we put us in the tick value.
			*/
			long	lTick = 0L;
			if ((anValues[1] & AlsaSeq.SND_SEQ_TIME_STAMP_MASK) == AlsaSeq.SND_SEQ_TIME_STAMP_TICK)
			{
				lTick = alValues[0];
			}
			else
			{
				// ns -> µs
				lTick = alValues[0] / 1000;
			}
			MidiEvent	event = new MidiEvent(message, lTick);
			return event;
		}
		else
		{
			return null;
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
