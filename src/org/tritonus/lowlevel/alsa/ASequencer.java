/*
 *	ASequencer.java
 */

/*
 *  Copyright (c) 1999, 2000 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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


package	org.tritonus.lowlevel.alsa;


import	java.lang.UnsupportedOperationException;

import	java.util.Iterator;

import	javax.sound.midi.MidiEvent;
import	javax.sound.midi.MidiMessage;
import	javax.sound.midi.ShortMessage;
import	javax.sound.midi.SysexMessage;
import	javax.sound.midi.MetaMessage;
import	javax.sound.midi.InvalidMidiDataException;

import	org.tritonus.TDebug;


public class ASequencer
	extends		ASequencer0
{
	private SystemInfo	m_systemInfo;


	public ASequencer()
	{
		super();
	}



	public ASequencer(String strName)
	{
		super();
		setClientName(strName);
	}



	public SystemInfo getSystemInfo()
	{
		if (m_systemInfo == null)
		{
			int[]	anValues = new int[4];
			getSystemInfo(anValues);
			m_systemInfo = new SystemInfo(anValues[0], anValues[1], anValues[2], anValues[3]);
		}
		return m_systemInfo;
	}



	public ClientInfo getClientInfo()
	{
		return getClientInfo(getClientId());
	}



	public ClientInfo getClientInfo(int nClient)
	{
		int[]		anValues = new int[4];
		String[]	astrValues = new String[2];
		int	nSuccess = getClientInfo(nClient, anValues, astrValues);
		if (nSuccess == 0)
		{
			return new ClientInfo(anValues[0], anValues[1], astrValues[0], anValues[2], astrValues[1], anValues[3]);
		}
		else
		{
			return null;
		}
	}



	public Iterator getClientInfos()
	{
		return new ClientInfoIterator();
	}



	public Iterator getPortInfos(int nClient)
	{
		return new PortInfoIterator(nClient);
	}



	public void subscribePort(
		int nSenderClient, int nSenderPort,
		int nDestClient, int nDestPort)
	{
		subscribePort(
			nSenderClient, nSenderPort,
			nDestClient, nDestPort,
			0, false, false, false);
	}



	public void subscribePort(
		int nSenderClient, int nSenderPort,
		int nDestClient, int nDestPort,
		int nQueue, boolean bExclusive, boolean bRealtime, boolean bConvertTime)
	{
		subscribePort(
			nSenderClient, nSenderPort,
			nDestClient, nDestPort,
			nQueue, bExclusive, bRealtime, bConvertTime,
			0, 0, 0);
	}



	/**	Get the playback position of a sequencer queue.
	 *
	 *	@return the current playback position in ticks
	 */
	public long getQueuePositionTick(int nQueue)
	{
		int[]	anValues = new int[3];
		long[]	alValues = new long[2];
		getQueueStatus(nQueue,
			       anValues,
			       alValues);
		return alValues[0];
	}



	/**	Get the playback position of a sequencer queue.
	 *
	 *	@return the current playback position in nanoseconds
	 */
	public long getQueuePositionTime(int nQueue)
	{
		int[]	anValues = new int[3];
		long[]	alValues = new long[2];
		getQueueStatus(nQueue,
			       anValues,
			       alValues);
		return alValues[1];
	}



	public void startQueue(int nSourcePort, int nQueue)
	{
		controlQueue(SND_SEQ_EVENT_START, nSourcePort, nQueue);
	}



	public void stopQueue(int nSourcePort, int nQueue)
	{
		controlQueue(SND_SEQ_EVENT_STOP, nSourcePort, nQueue);
	}



	/**	Set the playback position of a sequencer queue.
	 *
	 *	@param lTick the desired playback position in ticks
	 */
	public void setQueuePositionTick(int nSourcePort, int nQueue,
					 long lTick)
	{
		controlQueue(SND_SEQ_EVENT_SETPOS_TICK, nSourcePort, nQueue,
			     lTick);
	}



	/**	Set the playback position of a sequencer queue.
	 *
	 *	@param lTick the desired playback position in nanoseconds
	 */
	public void setQueuePositionTime(int nSourcePort, int nQueue,
					 long lTime)
	{
		controlQueue(SND_SEQ_EVENT_SETPOS_TIME, nSourcePort, nQueue,
			     lTime);
	}



	public void controlQueue(int nType, int nSourcePort, int nQueue)
	{
		sendQueueControlEvent(
			nType, SND_SEQ_TIME_STAMP_REAL | SND_SEQ_TIME_MODE_REL, 0, SND_SEQ_QUEUE_DIRECT, 0L,
			nSourcePort, SND_SEQ_CLIENT_SYSTEM, SND_SEQ_PORT_SYSTEM_TIMER,
			nQueue, 0, 0);
	}


	public void controlQueue(int nType, int nSourcePort, int nQueue,
				 long lTime)
	{
		sendQueueControlEvent(
			nType, SND_SEQ_TIME_STAMP_REAL | SND_SEQ_TIME_MODE_REL, 0, SND_SEQ_QUEUE_DIRECT, 0L,
			nSourcePort, SND_SEQ_CLIENT_SYSTEM, SND_SEQ_PORT_SYSTEM_TIMER,
			nQueue, 0, lTime);
	}



	/**	Returns tempo in MPQ.
	 */
	public int getQueueTempo(int nQueue)
	{
		int[]	anValues = new int[2];
		getQueueTempo(nQueue,
			      anValues);
		return anValues[0] * anValues[1];
	}



	/////////////////////////// sending MIDI messages


	public void sendNoteOffEventImmediately(
		int nSourcePort, int nDestClient, int nDestPort,
		int nChannel, int nNote, int nVelocity)
	{
		sendNoteEventImmediately(
			SND_SEQ_EVENT_NOTEOFF,
			nSourcePort,
			nDestClient, nDestPort, nChannel,
			nNote, nVelocity);
	}



	public void sendNoteOffEventTicked(
		int nQueue, long lTick,
		int nSourcePort, int nDestClient, int nDestPort,
		int nChannel, int nNote, int nVelocity)
	{
		sendNoteEventTicked(
			SND_SEQ_EVENT_NOTEOFF, nQueue, lTick,
			nSourcePort, nDestClient, nDestPort,
			nChannel, nNote, nVelocity);
	}



	public void sendNoteOffEventSubscribersTicked(
		int nQueue, long lTick,
		int nSourcePort,
		int nChannel, int nNote, int nVelocity)
	{
		sendNoteEventSubscribersTicked(
			SND_SEQ_EVENT_NOTEOFF, nQueue, lTick,
			nSourcePort,
			nChannel, nNote, nVelocity);
	}



	public void sendNoteOffEventSubscribersImmediately(
		int nSourcePort,
		int nChannel, int nNote, int nVelocity)
	{
		sendNoteEventSubscribersImmediately(
			SND_SEQ_EVENT_NOTEOFF,
			nSourcePort,
			nChannel, nNote, nVelocity);
	}



	public void sendNoteOnEventImmediately(
		int nSourcePort, int nDestClient, int nDestPort,
		int nChannel, int nNote, int nVelocity)
	{
		sendNoteEventImmediately(
			SND_SEQ_EVENT_NOTEON,
			nSourcePort,
			nDestClient, nDestPort, nChannel,
			nNote, nVelocity);
	}



	public void sendNoteOnEventTicked(
		int nQueue, long lTime,
		int nSourcePort, int nDestClient, int nDestPort,
		int nChannel, int nNote, int nVelocity)
	{
		sendNoteEventTicked(
			SND_SEQ_EVENT_NOTEON, nQueue, lTime,
			nSourcePort, nDestClient, nDestPort,
			nChannel, nNote, nVelocity);
	}



	public void sendNoteOnEventSubscribersTicked(
		int nQueue, long lTime,
		int nSourcePort,
		int nChannel, int nNote, int nVelocity)
	{
		sendNoteEventSubscribersTicked(
			SND_SEQ_EVENT_NOTEON, nQueue, lTime,
			nSourcePort,
			nChannel, nNote, nVelocity);
	}



	public void sendNoteOnEventSubscribersImmediately(
		int nSourcePort,
		int nChannel, int nNote, int nVelocity)
	{
		sendNoteEventSubscribersImmediately(
			SND_SEQ_EVENT_NOTEON,
			nSourcePort,
			nChannel, nNote, nVelocity);
	}



	// sendNoteEventTimed: real-time timestamp?


	public void sendNoteEventImmediately(
		int nType,
		int nSourcePort, int nDestClient, int nDestPort,
		int nChannel, int nNote, int nVelocity)
	{
		sendNoteEvent(
			nType, SND_SEQ_TIME_STAMP_REAL | SND_SEQ_TIME_MODE_REL, 0, SND_SEQ_QUEUE_DIRECT, 0L,
			nSourcePort, nDestClient, nDestPort,
			nChannel, nNote, nVelocity, 0, 0);
	}



	public void sendNoteEventTicked(
		int nType, int nQueue, long lTick,
		int nSourcePort, int nDestClient, int nDestPort,
		int nChannel, int nNote, int nVelocity)
	{
		sendNoteEvent(
			nType, SND_SEQ_TIME_STAMP_TICK | SND_SEQ_TIME_MODE_ABS, 0, nQueue, lTick,
			nSourcePort, nDestClient, nDestPort,
			nChannel, nNote, nVelocity, 0, 0);
	}



	public void sendNoteEventSubscribersTicked(
		int nType, int nQueue, long lTick,
		int nSourcePort,
		int nChannel, int nNote, int nVelocity)
	{
		sendNoteEvent(
			nType, SND_SEQ_TIME_STAMP_TICK | SND_SEQ_TIME_MODE_ABS, 0, nQueue, lTick,
			nSourcePort, SND_SEQ_ADDRESS_SUBSCRIBERS, SND_SEQ_ADDRESS_UNKNOWN,
			nChannel, nNote, nVelocity, 0, 0);
	}



	public void sendNoteEventSubscribersImmediately(
		int nType,
		int nSourcePort,
		int nChannel, int nNote, int nVelocity)
	{
		sendNoteEvent(
			nType, SND_SEQ_TIME_STAMP_REAL | SND_SEQ_TIME_MODE_REL, 0, SND_SEQ_QUEUE_DIRECT, 0L,
			nSourcePort, SND_SEQ_ADDRESS_SUBSCRIBERS, SND_SEQ_ADDRESS_UNKNOWN,
			nChannel, nNote, nVelocity, 0, 0);
	}



	public void sendKeyPressureEventImmediately(
		int nSourcePort, int nDestClient, int nDestPort,
		int nChannel, int nNote, int nPressure)
	{
		sendControlEventImmediately(
			SND_SEQ_EVENT_KEYPRESS,
			nSourcePort, nDestClient, nDestPort,
			nChannel, nNote, nPressure);
	}



	public void sendKeyPressureEventTicked(
		int nQueue, long lTime,
		int nSourcePort, int nDestClient, int nDestPort,
		int nChannel, int nNote, int nPressure)
	{
		sendControlEventTicked(
			SND_SEQ_EVENT_KEYPRESS, nQueue, lTime,
			nSourcePort, nDestClient, nDestPort,
			nChannel, nNote, nPressure);
	}



	public void sendKeyPressureEventSubscribersTicked(
		int nQueue, long lTime,
		int nSourcePort,
		int nChannel, int nNote, int nPressure)
	{
		sendControlEventSubscribersTicked(
			SND_SEQ_EVENT_KEYPRESS, nQueue, lTime,
			nSourcePort,
			nChannel, nNote, nPressure);
	}



	public void sendKeyPressureEventSubscribersImmediately(
		int nSourcePort,
		int nChannel, int nNote, int nPressure)
	{
		sendControlEventSubscribersImmediately(
			SND_SEQ_EVENT_KEYPRESS,
			nSourcePort,
			nChannel, nNote, nPressure);
	}



	public void sendControlChangeEventImmediately(
		int nSourcePort, int nDestClient, int nDestPort,
		int nChannel, int nControl, int nValue)
	{
		sendControlEventImmediately(
			SND_SEQ_EVENT_CONTROLLER,
			nSourcePort, nDestClient, nDestPort,
			nChannel, nControl, nValue);
	}



	public void sendControlChangeEventTicked(
		int nQueue, long lTime,
		int nSourcePort, int nDestClient, int nDestPort,
		int nChannel, int nControl, int nValue)
	{
		sendControlEventTicked(
			SND_SEQ_EVENT_CONTROLLER, nQueue, lTime,
			nSourcePort, nDestClient, nDestPort,
			nChannel, nControl, nValue);
	}



	public void sendControlChangeEventSubscribersTicked(
		int nQueue, long lTime,
		int nSourcePort,
		int nChannel, int nControl, int nValue)
	{
		sendControlEventSubscribersTicked(
			SND_SEQ_EVENT_CONTROLLER, nQueue, lTime,
			nSourcePort,
			nChannel, nControl, nValue);
	}



	public void sendControlChangeEventSubscribersImmediately(
		int nSourcePort,
		int nChannel, int nControl, int nValue)
	{
		sendControlEventSubscribersImmediately(
			SND_SEQ_EVENT_CONTROLLER,
			nSourcePort,
			nChannel, nControl, nValue);
	}



	public void sendProgramChangeEventImmediately(
		int nSourcePort, int nDestClient, int nDestPort,
		int nChannel, int nProgram)
	{
		sendControlEventImmediately(
			SND_SEQ_EVENT_PGMCHANGE,
			nSourcePort, nDestClient, nDestPort,
			nChannel, 0, nProgram);
	}



	public void sendProgramChangeEventTicked(
		int nQueue, long lTime,
		int nSourcePort, int nDestClient, int nDestPort,
		int nChannel, int nProgram)
	{
		sendControlEventTicked(
			SND_SEQ_EVENT_PGMCHANGE,nQueue, lTime,
			nSourcePort, nDestClient, nDestPort,
			nChannel, 0, nProgram);
	}



	public void sendProgramChangeEventSubscribersTicked(
		int nQueue, long lTime,
		int nSourcePort,
		int nChannel, int nProgram)
	{
		sendControlEventSubscribersTicked(
			SND_SEQ_EVENT_PGMCHANGE,nQueue, lTime,
			nSourcePort,
			nChannel, 0, nProgram);
	}



	public void sendProgramChangeEventSubscribersImmediately(
		int nSourcePort,
		int nChannel, int nProgram)
	{
		sendControlEventSubscribersImmediately(
			SND_SEQ_EVENT_PGMCHANGE,
			nSourcePort,
			nChannel, 0, nProgram);
	}



	/////////////////// channel pressure /////////////////



	public void sendChannelPressureEventImmediately(
		int nSourcePort, int nDestClient, int nDestPort,
		int nChannel, int nPressure)
	{
		sendControlEventImmediately(
			SND_SEQ_EVENT_CHANPRESS,
			nSourcePort, nDestClient, nDestPort,
			nChannel, 0, nPressure);
	}



	public void sendChannelPressureEventTicked(
		int nQueue, long lTime,
		int nSourcePort, int nDestClient, int nDestPort,
		int nChannel, int nPressure)
	{
		sendControlEventTicked(
			SND_SEQ_EVENT_CHANPRESS, nQueue, lTime,
			nSourcePort, nDestClient, nDestPort,
			nChannel, 0, nPressure);
	}



	public void sendChannelPressureEventSubscribersTicked(
		int nQueue, long lTime,
		int nSourcePort,
		int nChannel, int nPressure)
	{
		sendControlEventSubscribersTicked(
			SND_SEQ_EVENT_CHANPRESS, nQueue, lTime,
			nSourcePort,
			nChannel, 0, nPressure);
	}



	public void sendChannelPressureEventSubscribersImmediately(
		int nSourcePort,
		int nChannel, int nPressure)
	{
		sendControlEventSubscribersImmediately(
			SND_SEQ_EVENT_CHANPRESS,
			nSourcePort,
			nChannel, 0, nPressure);
	}



	////////////////// pitch bend /////////////////////



	public void sendPitchBendEventImmediately(
		int nSourcePort, int nDestClient, int nDestPort,
		int nChannel, int nPitch)
	{
		sendControlEventImmediately(
			SND_SEQ_EVENT_PITCHBEND,
			nSourcePort, nDestClient, nDestPort,
			nChannel, 0, nPitch);
	}



	public void sendPitchBendEventTicked(
		int nQueue, long lTime,
		int nSourcePort, int nDestClient, int nDestPort,
		int nChannel, int nPitch)
	{
		sendControlEventTicked(
			SND_SEQ_EVENT_PITCHBEND, nQueue, lTime,
			nSourcePort, nDestClient, nDestPort,
			nChannel, 0, nPitch);
	}



	public void sendPitchBendEventSubscribersTicked(
		int nQueue, long lTime,
		int nSourcePort,
		int nChannel, int nPitch)
	{
		sendControlEventSubscribersTicked(
			SND_SEQ_EVENT_PITCHBEND, nQueue, lTime,
			nSourcePort,
			nChannel, 0, nPitch);
	}



	public void sendPitchBendEventSubscribersImmediately(
		int nSourcePort,
		int nChannel, int nPitch)
	{
		sendControlEventSubscribersImmediately(
			SND_SEQ_EVENT_PITCHBEND,
			nSourcePort,
			nChannel, 0, nPitch);
	}



	/////////////////////// common procedures



	public void sendControlEventImmediately(
		int nType,
		int nSourcePort, int nDestClient, int nDestPort,
		int nChannel, int nParam, int nValue)
	{
		sendControlEvent(
			nType, SND_SEQ_TIME_STAMP_REAL | SND_SEQ_TIME_MODE_REL, 0, SND_SEQ_QUEUE_DIRECT, 0L,
			nSourcePort, nDestClient, nDestPort,
			nChannel, nParam, nValue);
	}



	public void sendControlEventTicked(
		int nType, int nQueue, long lTime,
		int nSourcePort, int nDestClient, int nDestPort,
		int nChannel, int nParam, int nValue)
	{
		sendControlEvent(
			nType, SND_SEQ_TIME_STAMP_TICK | SND_SEQ_TIME_MODE_ABS, 0, nQueue, lTime,
			nSourcePort, nDestClient, nDestPort,
			nChannel, nParam, nValue);
	}



	public void sendControlEventSubscribersTicked(
		int nType, int nQueue, long lTime,
		int nSourcePort,
		int nChannel, int nParam, int nValue)
	{
		sendControlEvent(
			nType, SND_SEQ_TIME_STAMP_TICK | SND_SEQ_TIME_MODE_ABS, 0, nQueue, lTime,
			nSourcePort, SND_SEQ_ADDRESS_SUBSCRIBERS, SND_SEQ_ADDRESS_UNKNOWN,
			nChannel, nParam, nValue);
	}



	public void sendControlEventSubscribersImmediately(
		int nType,
		int nSourcePort,
		int nChannel, int nParam, int nValue)
	{
		sendControlEvent(
			nType, SND_SEQ_TIME_STAMP_REAL | SND_SEQ_TIME_MODE_REL, 0, SND_SEQ_QUEUE_DIRECT, 0L,
			nSourcePort, SND_SEQ_ADDRESS_SUBSCRIBERS, SND_SEQ_ADDRESS_UNKNOWN,
			nChannel, nParam, nValue);
	}



	/*
	 *	nTempo is in us/beat
	 */
	// TODO: check if used
	public void sendTempoEventSubscribersTicked(
		int nQueue, long lTime,
		int nSourcePort,
		int nTempo)
	{
		sendQueueControlEvent(
			ASequencer.SND_SEQ_EVENT_TEMPO, SND_SEQ_TIME_STAMP_TICK | SND_SEQ_TIME_MODE_ABS, 0, nQueue, lTime,
			nSourcePort, SND_SEQ_ADDRESS_SUBSCRIBERS, SND_SEQ_ADDRESS_UNKNOWN,
			nQueue, nTempo, 0);
	}


	public void sendVarEventSubscribersTicked(
		int nType, int nQueue, long lTime,
		int nSourcePort,
		byte[] abData, int nOffset, int nLength)
	{
		sendVarEvent(
			nType, SND_SEQ_TIME_STAMP_TICK | SND_SEQ_TIME_MODE_ABS | SND_SEQ_EVENT_LENGTH_VARIABLE, 0, nQueue, lTime,
			nSourcePort, SND_SEQ_ADDRESS_SUBSCRIBERS, SND_SEQ_ADDRESS_UNKNOWN,
			abData, nOffset, nLength);
	}



	public void sendVarEventSubscribersImmediately(
		int nType,
		int nSourcePort,
		byte[] abData, int nOffset, int nLength)
	{
		sendVarEvent(
			nType, SND_SEQ_TIME_STAMP_REAL | SND_SEQ_TIME_MODE_REL, 0, SND_SEQ_QUEUE_DIRECT, 0L,
			nSourcePort, SND_SEQ_ADDRESS_SUBSCRIBERS, SND_SEQ_ADDRESS_UNKNOWN,
			abData, nOffset, nLength);
	}



	///////////////// receiving of events ///////////////////////



	public MidiEvent getEvent()
	{
		int[]	anValues = new int[13];
		long[]	alValues = new long[1];
		Object[]	aValues = new Object[1];
		if (TDebug.TraceASequencer)
		{
			TDebug.out("ASequencer.getEvent(): before getEvent(int[], long[])");
		}
		while (true)
		{
			boolean	bEventPresent = getEvent(anValues, alValues, aValues);
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
			}
		}
		// TDebug.out("after getEvent()");
		MidiMessage	message = null;
		int	nType = anValues[0];
		switch (nType)
		{
		case SND_SEQ_EVENT_NOTEON:
		case SND_SEQ_EVENT_NOTEOFF:
		{
			if (TDebug.TraceASequencer)
			{
				TDebug.out("ASequencer.getEvent(): note event");
			}
			ShortMessage	shortMessage = new ShortMessage();
			int	nCommand = (nType == SND_SEQ_EVENT_NOTEON) ? 0x90 : 0x80;
			int	nChannel = anValues[8] & 0xF;
			int	nKey = anValues[9] & 0x7F;
			int	nVelocity = anValues[10] & 0x7F;
			try
			{
				shortMessage.setMessage(nCommand, nChannel, nKey, nVelocity);
			}
			catch (InvalidMidiDataException e)
			{
				if (TDebug.TraceASequencer)
				{
					TDebug.out(e);
				}
			}
			message = shortMessage;
			break;
		}

		case SND_SEQ_EVENT_KEYPRESS:
		{
			if (TDebug.TraceASequencer)
			{
				TDebug.out("ASequencer.getEvent(): key pressure event");
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
				if (TDebug.TraceASequencer)
				{
					TDebug.out(e);
				}
			}
			message = shortMessage;
			break;
		}

		case SND_SEQ_EVENT_CONTROLLER:
		{
			if (TDebug.TraceASequencer)
			{
				TDebug.out("ASequencer.getEvent(): controller event");
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
				if (TDebug.TraceASequencer)
				{
					TDebug.out(e);
				}
			}
			message = shortMessage;
			break;
		}

		case SND_SEQ_EVENT_PGMCHANGE:
		{
			if (TDebug.TraceASequencer)
			{
				TDebug.out("ASequencer.getEvent(): program change event");
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
			}
			message = shortMessage;
			break;
		}

		case SND_SEQ_EVENT_CHANPRESS:
		{
			if (TDebug.TraceASequencer)
			{
				TDebug.out("ASequencer.getEvent(): channel pressure event");
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
				if (TDebug.TraceASequencer)
				{
					TDebug.out(e);
				}
			}
			message = shortMessage;
			break;
		}

		case SND_SEQ_EVENT_PITCHBEND:
		{
			if (TDebug.TraceASequencer)
			{
				TDebug.out("ASequencer.getEvent(): pitchbend event");
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
				if (TDebug.TraceASequencer)
				{
					TDebug.out(e);
				}
			}
			message = shortMessage;
			break;
		}

		case SND_SEQ_EVENT_USR_VAR4:
		{
			if (TDebug.TraceASequencer)
			{
				TDebug.out("ASequencer.getEvent(): meta event");
			}
			MetaMessage	metaMessage = new MetaMessage();
			byte[]	abCompleteData = (byte[]) aValues[0];
			int	nMetaType = abCompleteData[0];
			byte[]	abData = new byte[abCompleteData.length - 1];
			System.arraycopy(abCompleteData, 1, abData, 0, abCompleteData.length - 1);
			try
			{
				metaMessage.setMessage(nMetaType, abData, abData.length);
			}
			catch (InvalidMidiDataException e)
			{
				if (TDebug.TraceASequencer)
				{
					TDebug.out(e);
				}
			}
			message = metaMessage;
			break;
		}

		case SND_SEQ_EVENT_SYSEX:
		{
			if (TDebug.TraceASequencer)
			{
				TDebug.out("ASequencer.getEvent(): sysex event");
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
				if (TDebug.TraceASequencer)
				{
					TDebug.out(e);
				}
			}
			message = sysexMessage;
			break;
		}

		default:
			if (TDebug.TraceASequencer)
			{
				TDebug.out("ASequencer.getEvent(): unknown event");
			}

		}
		if (message != null)
		{
			// TODO: dirty: assuming time in ticks
			long	lTick = alValues[0];
			MidiEvent	event = new MidiEvent(message, lTick);
			return event;
		}
		else
		{
			return null;
		}
	}

	///////////////////////////////////////////////////////////



	public void sendNoteEvent(
		int nType, int nFlags, int nTag, int nQueue, long lTime,
		int nSourcePort, int nDestClient, int nDestPort,
		int nChannel, int nNote, int nVelocity, int nOffVelocity, int nDuration)
	{
		if (TDebug.TraceASequencerDetails)
		{
			outputCommonFields(
				nType, nFlags, nTag, nQueue, lTime,
				nSourcePort, nDestClient, nDestPort);
			outputNoteFields(
				nChannel, nNote, nVelocity, nOffVelocity, nDuration);
		}
		super.sendNoteEvent(
			nType, nFlags, nTag, nQueue, lTime,
			nSourcePort, nDestClient, nDestPort,
			nChannel, nNote, nVelocity, nOffVelocity, nDuration);
	}



	private void outputCommonFields(
		int nType, int nFlags, int nTag, int nQueue, long lTime,
		int nSourcePort, int nDestClient, int nDestPort)
	{
		TDebug.out("ASequencer.sendNoteEvent():");
		TDebug.out("\t nType:" + nType);
		TDebug.out("\t nFlags:" + nFlags);
		TDebug.out("\t nTag:" + nTag);
		TDebug.out("\t nQueue:" + nQueue);
		TDebug.out("\t lTime:" + lTime);
		TDebug.out("\t nSourcePort:" + nSourcePort);
		TDebug.out("\t nDestClient:" + nDestClient);
		TDebug.out("\t nDestPort:" + nDestPort);
	}


	private void outputNoteFields(
		int nChannel, int nNote, int nVelocity, int nOffVelocity, int nDuration)
	{
		TDebug.out("\t nChannel:" + nChannel);
		TDebug.out("\t nNote:" + nNote);
		TDebug.out("\t nVelocity:" + nVelocity);
		TDebug.out("\t nOffVelocity:" + nOffVelocity);
		TDebug.out("\t nDuration:" + nDuration);
	}



	///////////////////////////////////////////////////////////


	/**	General information about the sequencer.
	 *	This class encapsulates the information of
	 *	snd_seq_system_info_t.
	 */
	public static class SystemInfo
	{
		private int	m_nMaxQueues;
		private int	m_nMaxClients;
		private int	m_nMaxPortsPerClient;
		private int	m_nMaxChannelsPerPort;


		public SystemInfo(int nMaxQueues, int nMaxClients, int nMaxPortsPerClient, int nMaxChannelsPerPort)
		{
			m_nMaxQueues = nMaxQueues;
			m_nMaxClients = nMaxClients;
			m_nMaxPortsPerClient = nMaxPortsPerClient;
			m_nMaxChannelsPerPort = nMaxChannelsPerPort;
		}



		public int getMaxQueues()
		{
			return m_nMaxQueues;
		}



		public int getMaxClients()
		{
			return m_nMaxClients;
		}



		public int getMaxPortsPerClient()
		{
			return m_nMaxPortsPerClient;
		}



		public int getMaxChannelsPerPort()
		{
			return m_nMaxChannelsPerPort;
		}
	}



	public static class ClientInfo
	{
		private int	m_nClientId;
		private int	m_nClientType;
		private String	m_strName;
		private int	m_nFilter;
		// TODO: multicast filter
		// TODO: event filter
		private String	m_strGroupName;
		private int	m_nNumPorts;



		public ClientInfo(int nClientId, int nClientType, String strName, int nFilter, String strGroupName, int nNumPorts)
		{
			m_nClientId = nClientId;
			m_nClientType = nClientType;
			m_strName = strName;
			m_nFilter = nFilter;
			// TODO: multicast filter
			// TODO: event filter
			m_strGroupName = strGroupName;
			m_nNumPorts = nNumPorts;
		}


		public int getClientId()
		{
			return m_nClientId;
		}



		public int getClientType()
		{
			return m_nClientType;
		}


		public String getName()
		{
			return m_strName;
		}
	}



	public static class PortInfo
	{
		private int	m_nClient;
		private int	m_nPort;
		private String	m_strName;
		private String	m_strGroupName;
		private int	m_nCapability;
		private int	m_nGroupCapability;
		private int	m_nType;
		private int	m_nMidiChannels;
		private int	m_nMidiVoices;
		private int	m_nSynthVoices;
		private int	m_nNumReadSubscribers;
		private int	m_nNumWriteSubscribers;




		public PortInfo(int nClient,
				int nPort,
				String strName,
				String strGroupName,
				int nCapability,
				int nGroupCapability,
				int nType,
				int nMidiChannels,
				int nMidiVoices,
				int nSynthVoices,
				int nNumReadSubscribers,
				int nNumWriteSubscribers)
		{
			m_nClient = nClient;
			m_nPort = nPort;
			m_strName = strName;
			m_strGroupName = strGroupName;
			m_nCapability = nCapability;
			m_nGroupCapability = nGroupCapability;
			m_nType = nType;
			m_nMidiChannels = nMidiChannels;
			m_nMidiVoices = nMidiVoices;
			m_nSynthVoices = nSynthVoices;
			m_nNumReadSubscribers = nNumReadSubscribers;
			m_nNumWriteSubscribers = nNumWriteSubscribers;
		}


		public int getClient()
		{
			return m_nClient;
		}



		public int getPort()
		{
			return m_nPort;
		}



		public String getName()
		{
			return m_strName;
		}



		public String getGroupName()
		{
			return m_strGroupName;
		}



		public int getCapability()
		{
			return m_nCapability;
		}



		public int getGroupCapability()
		{
			return m_nGroupCapability;
		}



		public int getType()
		{
			return m_nType;
		}



		public int getMidiChannels()
		{
			return m_nMidiChannels;
		}



		public int getMidiVoices()
		{
			return m_nMidiVoices;
		}



		public int getSynthVoices()
		{
			return m_nSynthVoices;
		}



		public int getNumReadSubscribers()
		{
			return m_nNumReadSubscribers;
		}



		public int getNumWriteSubscribers()
		{
			return m_nNumWriteSubscribers;
		}



	}


	private class ClientInfoIterator
		implements	Iterator
	{
		private int		m_nClient;
		private ClientInfo	m_clientInfo;



		public ClientInfoIterator()
		{
			m_nClient = -1;
			m_clientInfo = createNextClientInfo();
		}



		public boolean hasNext()
		{
			// TDebug.out("hasNext(): clientInfo: " + m_clientInfo);
			return m_clientInfo != null;
		}



		public Object next()
		{
			Object	next = m_clientInfo;
			m_clientInfo = createNextClientInfo();
			return next;
		}



		public void remove()
		{
			throw new UnsupportedOperationException();
		}


		private ClientInfo createNextClientInfo()
		{
			int[]		anValues = new int[4];
			String[]	astrValues = new String[2];
			int	nSuccess = getNextClientInfo(m_nClient, anValues, astrValues);
			// TDebug.out("succ: " + nSuccess);
			if (nSuccess == 0)
			{
				// TDebug.out("getNextClientInfo successful");
				m_nClient = anValues[0];
				return new ClientInfo(anValues[0], anValues[1], astrValues[0], anValues[2], astrValues[1], anValues[3]);
			}
			else
			{
				// TDebug.out("getNextClientInfo failed");
				return null;
			}
		}
	}



	private class PortInfoIterator
		implements	Iterator
	{
		private int		m_nClient;
		private int		m_nPort;
		private PortInfo	m_portInfo;



		public PortInfoIterator(int nClient)
		{
			m_nClient = nClient;
			m_nPort = -1;
			m_portInfo = createNextPortInfo();
		}



		public boolean hasNext()
		{
			return m_portInfo != null;
		}



		public Object next()
		{
			Object	next = m_portInfo;
			m_portInfo = createNextPortInfo();
			return next;
		}



		public void remove()
		{
			throw new UnsupportedOperationException();
		}


		private PortInfo createNextPortInfo()
		{
			int[]		anValues = new int[10];
			String[]	astrValues = new String[2];
			int	nSuccess = getNextPortInfo(m_nClient, m_nPort, anValues, astrValues);
			if (nSuccess == 0)
			{
				m_nPort = anValues[1];
				return new PortInfo(
					anValues[0], anValues[1],
					astrValues[0], astrValues[1],
					anValues[2], anValues[3], anValues[4],
					anValues[5], anValues[6], anValues[7],
					anValues[8], anValues[9]);
			}
			else
			{
				return null;
			}
		}
	}

}



/*** ASequencer.java ***/
