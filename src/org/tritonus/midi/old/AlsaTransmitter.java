/*
 *	AlsaTransmitter.java
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


package	org.gnu.tritonus.midi;

import	java.util.Arrays;
import	java.util.Iterator;
import	java.util.HashSet;
import	java.util.List;
import	java.util.Set;

import	javax.media.sound.midi.MidiDevice;
import	javax.media.sound.midi.MidiEvent;
import	javax.media.sound.midi.MidiUnavailableException;
import	javax.media.sound.midi.Receiver;
import	javax.media.sound.midi.Transmitter;

import	org.gnu.tritonus.util.ArraySet;

import	org.gnu.tritonus.lowlevel.alsa.Seq;
// import	org.gnu.tritonus.lowlevel.alsa.ASequencer;


public abstract class AlsaTransmitter
	extends		TTransmitter
	implements	AlsaSequencerClient
{
	private Seq	m_seq;
	// private ASequencer	m_aSequencer;
	private Set		m_alsaReceivers;



	public AlsaTransmitter(MidiDevice.Info deviceInfo,
			       int[] anAllowedModes, Seq seq)
	{
		super(deviceInfo, anAllowedModes);
		// m_aSequencer = ASequencerClientFactory.getASequencer();
		m_alsaReceivers = new ArraySet();
	}


/*
  // sun: returns -1
  public int getMaxOutReceivers()
  {
  return Integer.MAX_VALUE;
  }
*/



	public void addOutReceiver(Receiver receiver)
		throws	MidiUnavailableException
	{
		if (receiver instanceof AlsaSequencerClient)
		{
			synchronized (m_alsaReceivers)
			{
				m_alsaReceivers.add(receiver);
			}
		}
		else
		{
			super.addOutReceiver(receiver);
		}
	}


	public void removeOutReceiver(Receiver receiver)
	{
		if (receiver instanceof AlsaSequencerClient)
		{
			synchronized (m_alsaReceivers)
			{
				m_alsaReceivers.remove(receiver);
			}
		}
		else
		{
			super.removeOutReceiver(receiver);
		}
	}



	public Receiver[] getOutReceivers()
	{
		Receiver[]	aReceivers = super.getOutReceivers();
		List	receiverList = Arrays.asList(aReceivers);
		Set	allReceivers = new ArraySet(receiverList);
		allReceivers.addAll(m_alsaReceivers);
		// TODO: unite the two sets, then return array
		return (Receiver[]) allReceivers.toArray(new Receiver[0]);
	}



	protected void sendImpl(MidiEvent event)
	{
		Iterator	receivers = m_alsaReceivers.iterator();
		while (receivers.hasNext())
		{
			AlsaSequencerClient	receiver = (AlsaSequencerClient) receivers.next();
			//MidiEvent	copiedEvent = (MidiEvent) event.clone();
			// TODO: native sending
			//receiver.send(copiedEvent);
		}
		super.sendImpl(event);
	}


/*
  // Let the base class do the job.
  public int getMaxThruReceivers()
  {
  return getMaxOutReceivers();
  }



  public void addThruReceiver(Receiver receiver)
  throws	MidiUnavailableException
  {
  addOutReceiver(receiver);
  }


  public void removeThruReceiver(Receiver receiver)
  {
  removeOutReceiver(receiver);
  }



  public Receiver[] getThruReceivers()
  {
  return getOutReceivers();
  }
*/

	private void enqueueEvent(MidiEvent event)
	{
		long	lTick = event.getTick();
		if (event instanceof ShortEvent)
		{
			ShortEvent	shortEvent = (ShortEvent) event;
			int nChannel = shortEvent.getChannel();
			switch (shortEvent.getCommand())
			{
			case 0x80:	// note off
				m_seq.sendNoteOffEvent(lTick, nChannel, shortEvent.getData1(), shortEvent.getData2());
				break;

			case 0x90:	// note on
				m_seq.sendNoteOnEvent(lTick, nChannel, shortEvent.getData1(), shortEvent.getData2());
				break;

			case 0xa0:	// polyphonic key pressure
				// TODO:
/*
				s = "Polyphonic Key Pressure: " + sC + getKeyName(shortEvent.getData1()) +
					" Pressure: " + shortEvent.getData2();
*/
				break;

			case 0xb0:	// controller/channel mode
				if (shortEvent.getData1() < 120)
				{
/*
					s = "Controller No.: " + sC + shortEvent.getData1() +
						" Value: " + shortEvent.getData2();
*/
				}
				else
				{
/*
					s = "ChannelMode Message No.: " + sC + shortEvent.getData1() +
						" Value: " + shortEvent.getData2();
*/
				}
				break;

			case 0xc0:	// program change
/*
				s = "Program Change No: " + sC + shortEvent.getData1();
				if (shortEvent.getData2() != 0)
				{
					s = s + " WARNING: second byte is not zero";
				}
*/
				break;

			case 0xd0:	// channel  aftertouch
/*
				s = "Channel Aftertouch Pressure: " + sC + shortEvent.getData1();
				if (shortEvent.getData2() != 0)
				{
					s = s + " WARNING: second byte is not zero";
				}
*/
				break;

			case 0xe0:	// pitch change
/*
				s = "Pitch: " + sC + get14bitValue(shortEvent.getData1(), shortEvent.getData2());
*/
				break;

			case 0xF0:	//system realtime/system exclusive
				// these should not occur in a ShortEvent
			default:
				// TODO: some sort of error
			}
		}
		else if (event instanceof SysexEvent)
		{
			SysexEvent	sysexEvent = (SysexEvent) event;
			// TODO:
		}
		else if (event instanceof MetaEvent)
		{
			MetaEvent	metaEvent = (MetaEvent) event;
			// TODO:
		}
		else
		{
			// Ignore it.
		}
	}



}



/*** AlsaTransmitter.java ***/
