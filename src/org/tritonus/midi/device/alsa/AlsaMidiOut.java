/*
 *	AlsaMidiOut.java
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


package	org.tritonus.midi.device.alsa;

import	javax.sound.midi.Sequence;
import	javax.sound.midi.Sequencer;
import	javax.sound.midi.Track;
import	javax.sound.midi.MidiMessage;
import	javax.sound.midi.ShortMessage;
import	javax.sound.midi.SysexMessage;
import	javax.sound.midi.MetaMessage;

import	org.tritonus.TDebug;
import	org.tritonus.lowlevel.alsa.ASequencer;



public class AlsaMidiOut
{
	/**	The low-level object to interface to the ALSA sequencer.
	 */
	private ASequencer	m_aSequencer;

	/**	The source port to use for sending messages via the ALSA sequencer.
	 */
	private int		m_nSourcePort;

	/**	The sequencer queue to use inside the ALSA sequencer.
	 *	This value is only used (and valid) if m_bImmediately
	 *	false. Otherwise, events are sent directely to the destination
	 *	client, circumventing queues.
	 */
	private int		m_nQueue;

	private boolean		m_bImmediately;

	private boolean		m_bHandleMetaMessages;




	/*
	 *	Sends to all subscribers via queue.
	 */
	public AlsaMidiOut(ASequencer aSequencer, int nSourcePort,
			   int nQueue)
	{
		this(aSequencer, nSourcePort,
		     nQueue, false);
	}



	/*
	 *	Sends to all subscribers immediately.
	 */
	public AlsaMidiOut(ASequencer aSequencer, int nSourcePort)
	{
		this(aSequencer, nSourcePort,
		     -1, true);
	}



	private AlsaMidiOut(ASequencer aSequencer, int nSourcePort,
			    int nQueue, boolean bImmediately)
	{
		m_aSequencer = aSequencer;
		m_nSourcePort = nSourcePort;
		m_nQueue = nQueue;
		m_bImmediately = bImmediately;
		m_bHandleMetaMessages = false;
	}



	public void subscribe(int nDestClient, int nDestPort)
	{
		m_aSequencer.subscribePort(
			m_aSequencer.getClientId(), getSourcePort(),
			nDestClient, nDestPort);
	}



	public void unsubscribe(int nDestClient, int nDestPort)
	{
/*	TODO:
		m_aSequencer.subscribePort(
			m_aSequencer.getClientId(), getSourcePort(),
			nDestClient, nDestPort);
*/
	}



	private int getSourcePort()
	{
		return m_nSourcePort;
	}



	private int getQueue()
	{
		return m_nQueue;
	}



	private boolean getImmediately()
	{
		return m_bImmediately;
	}


	public boolean getHandleMetaMessages()
	{
		return m_bHandleMetaMessages;
	}



	public void setHandleMetaMessages(boolean bHandleMetaMessages)
	{
		m_bHandleMetaMessages = bHandleMetaMessages;
	}



	protected void enqueueMessage(MidiMessage event, long lTick)
	{
		if (event instanceof ShortMessage)
		{
			enqueueShortMessage((ShortMessage) event, lTick);
		}
		else if (event instanceof SysexMessage)
		{
			enqueueSysexMessage((SysexMessage) event, lTick);
		}
		else if (event instanceof MetaMessage && getHandleMetaMessages())
		{
			enqueueMetaMessage((MetaMessage) event, lTick);
		}
		else
		{
			// Ignore it.
		}
	}



	private void enqueueShortMessage(ShortMessage shortMessage, long lTime)
	{
		int nChannel = shortMessage.getChannel();
		switch (shortMessage.getCommand())
		{
		case 0x80:	// note off
			sendNoteOffEvent(lTime, nChannel, shortMessage.getData1(), shortMessage.getData2());
			break;

		case 0x90:	// note on
			sendNoteOnEvent(lTime, nChannel, shortMessage.getData1(), shortMessage.getData2());
			break;

		case 0xa0:	// polyphonic key pressure
			sendKeyPressureEvent(lTime, nChannel, shortMessage.getData1(), shortMessage.getData2());
			break;

		case 0xb0:	// controller/channel mode
			sendControlChangeEvent(lTime, nChannel, shortMessage.getData1(), shortMessage.getData2());
			break;

		case 0xc0:	// program change
			sendProgramChangeEvent(lTime, nChannel, shortMessage.getData1());
			break;

		case 0xd0:	// channel  aftertouch
			sendChannelPressureEvent(lTime, nChannel, shortMessage.getData1());
			break;

		case 0xe0:	// pitch change
			sendPitchBendEvent(lTime, nChannel, get14bitValue(shortMessage.getData1(), shortMessage.getData2()));
			break;

		case 0xF0:
			//system realtime/system exclusive
			// these should not occur in a ShortMessage
			// Hey, system realtime are short messages!
		default:
			TDebug.out("AlsaMidiOut.enqueueShortMessage(): UNKNOWN EVENT TYPE!");
		}
	}



	public static int get14bitValue(int nLSB, int nMSB)
	{
		return (nLSB & 0x7F) | ((nMSB & 0x7F) << 7);
	}



	private void sendNoteOffEvent(long lTime, int nChannel, int nNote, int nVelocity)
	{
		// TDebug.out("hallo1");
		if (getImmediately())
		{
			if (TDebug.TraceAlsaMidiOut)
			{
				TDebug.out("AlsaMidiOut.enqueueShortMessage(): sending noteoff message (subscribers, immediately)");
			}
			m_aSequencer.sendNoteOffEventSubscribersImmediately(
				getSourcePort(),
				nChannel, nNote, nVelocity);
		}
		else	// send via queue
		{
			if (TDebug.TraceAlsaMidiOut)
			{
				TDebug.out("AlsaMidiOut.enqueueShortMessage(): sending noteoff message (subscribers, timed)");
			}
			// TDebug.out("hallo2");
			// TDebug.out("ASequencer: " + m_aSequencer);
			m_aSequencer.sendNoteOffEventSubscribersTicked(
				getQueue(), lTime,
				getSourcePort(),
				nChannel, nNote, nVelocity);
			// TDebug.out("hallo3");
		}
	}



	private void sendNoteOnEvent(long lTime, int nChannel, int nNote, int nVelocity)
	{
		if (getImmediately())
		{
			if (TDebug.TraceAlsaMidiOut)
			{
				TDebug.out("AlsaMidiOut.enqueueShortMessage(): sending noteon message (subscribers, immediately)");
			}
			m_aSequencer.sendNoteOnEventSubscribersImmediately(
				getSourcePort(),
				nChannel, nNote, nVelocity);
		}
		else
		{
			if (TDebug.TraceAlsaMidiOut)
			{
				TDebug.out("AlsaMidiOut.enqueueShortMessage(): sending noteon message (subscribers, timed)");
			}
			m_aSequencer.sendNoteOnEventSubscribersTicked(
				getQueue(), lTime,
				getSourcePort(),
				nChannel, nNote, nVelocity);
		}
	}



	private void sendKeyPressureEvent(long lTime, int nChannel, int nNote, int nPressure)
	{
		if (getImmediately())
		{
			if (TDebug.TraceAlsaMidiOut)
			{
				TDebug.out("AlsaMidiOut.enqueueShortMessage(): sending keypressure message (subscribers, immediately)");
			}
			m_aSequencer.sendKeyPressureEventSubscribersImmediately(
				getSourcePort(),
				nChannel, nNote, nPressure);
		}
		else
		{
			if (TDebug.TraceAlsaMidiOut)
			{
				TDebug.out("AlsaMidiOut.enqueueShortMessage(): sending keypressure message (subscribers, timed)");
			}
			m_aSequencer.sendKeyPressureEventSubscribersTicked(
				getQueue(), lTime,
				getSourcePort(),
				nChannel, nNote, nPressure);
		}
	}



	private void sendControlChangeEvent(long lTime, int nChannel, int nControl, int nValue)
	{
		if (getImmediately())
		{
			if (TDebug.TraceAlsaMidiOut)
			{
				TDebug.out("AlsaMidiOut.enqueueShortMessage(): sending control message (subscribers, immediately)");
			}
			m_aSequencer.sendControlChangeEventSubscribersImmediately(
				getSourcePort(),
				nChannel, nControl, nValue);
		}
		else
		{
			if (TDebug.TraceAlsaMidiOut)
			{
				TDebug.out("AlsaMidiOut.enqueueShortMessage(): sending control message (subscribers, timed)");
			}
			m_aSequencer.sendControlChangeEventSubscribersTicked(
				getQueue(), lTime,
				getSourcePort(),
				nChannel, nControl, nValue);
		}
	}



	private void sendProgramChangeEvent(long lTime, int nChannel, int nProgram)
	{
		if (getImmediately())
		{
			if (TDebug.TraceAlsaMidiOut)
			{
				TDebug.out("AlsaMidiOut.enqueueShortMessage(): sending programchange message (subscribers, immediately)");
			}
			m_aSequencer.sendProgramChangeEventSubscribersImmediately(
				getSourcePort(),
				nChannel, nProgram);
		}
		else
		{
			if (TDebug.TraceAlsaMidiOut)
			{
				TDebug.out("AlsaMidiOut.enqueueShortMessage(): sending programchange message (subscribers, timed)");
			}
			m_aSequencer.sendProgramChangeEventSubscribersTicked(
				getQueue(), lTime,
				getSourcePort(),
				nChannel, nProgram);
		}
	}



	private void sendChannelPressureEvent(long lTime, int nChannel, int nPressure)
	{
		if (getImmediately())
		{
			if (TDebug.TraceAlsaMidiOut)
			{
				TDebug.out("AlsaMidiOut.enqueueShortMessage(): sending channelpressure message (subscribers, immediately)");
			}
			m_aSequencer.sendChannelPressureEventSubscribersImmediately(
				getSourcePort(),
				nChannel, nPressure);
		}
		else
		{
			if (TDebug.TraceAlsaMidiOut)
			{
				TDebug.out("AlsaMidiOut.enqueueShortMessage(): sending channelpressure message (subscribers, timed)");
			}
			m_aSequencer.sendChannelPressureEventSubscribersTicked(
				getQueue(), lTime,
				getSourcePort(),
				nChannel, nPressure);
		}
	}



	private void sendPitchBendEvent(long lTime, int nChannel, int nPitch)
	{
		if (getImmediately())
		{
			if (TDebug.TraceAlsaMidiOut)
			{
				TDebug.out("AlsaMidiOut.enqueueShortMessage(): sending pitchbend message (subscribers, immediately)");
			}
			m_aSequencer.sendPitchBendEventSubscribersImmediately(
				getSourcePort(),
				nChannel, nPitch);
		}
		else
		{
			if (TDebug.TraceAlsaMidiOut)
			{
				TDebug.out("AlsaMidiOut.enqueueShortMessage(): sending pitchbend message (subscribers, timed)");
			}
			m_aSequencer.sendPitchBendEventSubscribersTicked(
				getQueue(), lTime,
				getSourcePort(),
				nChannel, nPitch);
		}
	}



	private void enqueueSysexMessage(SysexMessage message, long lTick)
	{
		// TODO: recheck!!
		int	n = 0;
		byte[]	abMessageData = new byte[message.getLength()];
		if (message.getStatus() == 0xF0)
		{
			n = 1;
			abMessageData[0] = (byte) message.getStatus();
		}
		byte[]	abData = message.getData();
		System.arraycopy(abData, n, abMessageData, 1, abData.length);
		sendSysexEvent(lTick, abMessageData, abMessageData.length);
	}



	private void enqueueMetaMessage(MetaMessage message, long lTick)
	{
		/*
		 *	We pack the type byte in front of the data bytes.
		 */
		byte[]	abMessageData = new byte[message.getLength() + 1];
		abMessageData[0] = (byte) message.getType();
		System.arraycopy(message.getMessage(), 0, abMessageData, 1, message.getLength());
		// TDebug.out("message data length: " + abMessageData.length);
		// TDebug.out("message length: " + message.getLength());
		
		sendMetaEvent(lTick, abMessageData, abMessageData.length);
	}



	private void sendMetaEvent(long lTime, byte[] abData, int nLength)
	{
		if (getImmediately())
		{
			if (TDebug.TraceAlsaMidiOut)
			{
				TDebug.out("AlsaMidiOut.enqueueShortMessage(): sending meta message to subscribers");
			}
			m_aSequencer.sendVarEventSubscribersImmediately(
				ASequencer.SND_SEQ_EVENT_USR_VAR4,
				getSourcePort(),
				abData, 0, nLength);
		}
		else
		{
			if (TDebug.TraceAlsaMidiOut)
			{
				TDebug.out("AlsaMidiOut.enqueueShortMessage(): sending meta message to subscribers");
			}
			m_aSequencer.sendVarEventSubscribersTicked(
				ASequencer.SND_SEQ_EVENT_USR_VAR4, getQueue(), lTime,
				getSourcePort(),
				abData, 0, nLength);
		}
	}



	private void sendSysexEvent(long lTime, byte[] abData, int nLength)
	{
		if (getImmediately())
		{
			if (TDebug.TraceAlsaMidiOut)
			{
				TDebug.out("AlsaMidiOut.enqueueShortMessage(): sending meta message to subscribers");
			}
			m_aSequencer.sendVarEventSubscribersImmediately(
				ASequencer.SND_SEQ_EVENT_SYSEX,
				getSourcePort(),
				abData, 0, nLength);
		}
		else
		{
			if (TDebug.TraceAlsaMidiOut)
			{
				TDebug.out("AlsaMidiOut.enqueueShortMessage(): sending meta message to subscribers");
			}
			m_aSequencer.sendVarEventSubscribersTicked(
				ASequencer.SND_SEQ_EVENT_SYSEX, getQueue(), lTime,
				getSourcePort(),
				abData, 0, nLength);
		}
	}






}



/*** AlsaMidiOut.java ***/
