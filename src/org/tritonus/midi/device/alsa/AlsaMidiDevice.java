/*
 *	AlsaMidiDevice.java
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


import	java.util.ArrayList;
import	java.util.Iterator;
import	java.util.List;

import	javax.sound.midi.MidiDevice;
import	javax.sound.midi.MidiEvent;
import	javax.sound.midi.MidiMessage;
import	javax.sound.midi.MidiUnavailableException;
import	javax.sound.midi.Receiver;
import	javax.sound.midi.Transmitter;

import	org.tritonus.TDebug;
import	org.tritonus.lowlevel.alsa.ASequencer;
import	org.tritonus.midi.device.TMidiDevice;
import	org.tritonus.midi.device.TMidiDeviceInfo;
import	org.tritonus.util.GlobalInfo;



/**	A representation of a physical MIDI port based on the ALSA sequencer.
 */
public class AlsaMidiDevice
	extends		TMidiDevice
	implements	AlsaMidiIn.AlsaMidiInListener, AlsaSequencerClient
{
	/**	ALSA client id of the physical port.
	 */
	private int		m_nClient;

	/**	ALSA port id of the physical port.
	 */
	private int		m_nPort;

	/**	Whether to handle input from the physical port.
	 */
	private boolean		m_bUseIn;

	/**	Whether to handle output to the physical port.
	 */
	private boolean		m_bUseOut;

	/**	The object interfacing to the ALSA sequencer.
	 */
	private ASequencer	m_aSequencer;

	/**	The ALSA port id of the handler.
	 *	This is used by m_aSequencer.
	 */
	private int		m_nOwnPort;

	/**	Handler for input from the physical MIDI port.
	 */
	private AlsaMidiIn	m_alsaMidiIn;

	/**	Handler for output to the physical MIDI port.
	 */
	private AlsaMidiOut	m_alsaMidiOut;




	/**
	 */
	public AlsaMidiDevice(int nClient, int nPort)
	{
		this(nClient, nPort, true, true);
	}



	public AlsaMidiDevice(int nClient, int nPort, boolean bUseIn, boolean bUseOut)
	{
		this(new TMidiDeviceInfo("ALSA MIDI port (" + nClient + ":" + nPort + ")",
					 GlobalInfo.getVendor(),
					 "ALSA MIDI port (" + nClient + ":" + nPort + ")",
					 GlobalInfo.getVersion()),
		     nClient, nPort, bUseIn, bUseOut);
	}



	protected AlsaMidiDevice(MidiDevice.Info info, int nClient, int nPort, boolean bUseIn, boolean bUseOut)
	{
		super(info);
		m_nClient = nClient;
		m_nPort = nPort;
		m_bUseIn = bUseIn;
		m_bUseOut = bUseOut;
	}



	private boolean getUseIn()
	{
		return m_bUseIn;
	}



	private boolean getUseOut()
	{
		return m_bUseOut;
	}



	protected void openImpl()
	{
		if (TDebug.TraceAlsaMidiDevice)
		{
			TDebug.out("AlsaMidiDevice.openImpl(): called");
		}
		// create an ALSA client...
		m_aSequencer = new ASequencer("Tritonus Midi port handler");
		// ...and an ALSA port
		m_nOwnPort = m_aSequencer.createPort("handler port", ASequencer.SND_SEQ_PORT_CAP_WRITE | ASequencer.SND_SEQ_PORT_CAP_SUBS_WRITE | ASequencer.SND_SEQ_PORT_CAP_READ | ASequencer.SND_SEQ_PORT_CAP_SUBS_READ, 0, ASequencer.SND_SEQ_PORT_TYPE_APPLICATION, 0, 0, 0);
		if (getUseIn())
		{
			/*
			 *	AlsaMidiIn listend to incoming event on the
			 *	MIDI port.
			 *	It calls this.dequeueEvent() if
			 *	it receives an event.
			 */
			m_alsaMidiIn = new AlsaMidiIn(m_aSequencer, m_nOwnPort, getClient(), getPort(), this);
			m_alsaMidiIn.start();
		}
		if (getUseOut())
		{
			// uses subscribers, immediately
			m_alsaMidiOut = new AlsaMidiOut(m_aSequencer, m_nOwnPort);
			m_alsaMidiOut.subscribe(getClient(), getPort());
		}
		if (TDebug.TraceAlsaMidiDevice)
		{
			TDebug.out("AlsaMidiDevice.openImpl(): completed");
		}
	}



	protected void closeImpl()
	{
		if (getUseIn())
		{
			m_alsaMidiIn.interrupt();
			m_alsaMidiIn = null;
		}
		// TODO:
		// m_aSequencer.destroyPort(m_nOwnPort);
		m_aSequencer.close();
		m_aSequencer = null;
	}



	public long getMicroSecondPosition()
	{
		return -1;
	}



	/**	Pass MidiMessage from Receivers to physical MIDI port.
	 */
	protected void receive(MidiMessage message, long lTimeStamp)
	{
		if (isOpen())
		{
			m_alsaMidiOut.enqueueMessage(message, lTimeStamp);
		}
	}



	// for AlsaMidiInListener
	// passes events read from the device to the Transmitters
	public void dequeueEvent(MidiEvent event)
	{
		MidiMessage	message = event.getMessage();
		if (TDebug.TraceAlsaMidiDevice)
		{
			TDebug.out("AlsaMidiDevice.dequeueEvent(): message: " + message);
		}
		// send via superclass method
		sendImpl(message, -1L);
	}



	// for AlsaSequencerClient
	public int getClient()
	{
		return m_nClient;
	}




	// for AlsaSequencerClient
	public int getPort()
	{
		return m_nPort;
	}



	public Receiver getReceiver()
		throws	MidiUnavailableException
	{
		// TODO: check number
		return new AlsaReceiver();
	}



	public Transmitter getTransmitter()
		throws	MidiUnavailableException
	{
		// TODO: check number
		return new AlsaTransmitter();
	}


/////////////////// INNER CLASSES //////////////////////////////////////

	private class AlsaReceiver
		extends		TReceiver
		implements	AlsaSequencerReceiver
	{



		public AlsaReceiver()
		{
			super();
		}



		/**	Subscribe to the passed port.
		 *	This establishes a subscription in the ALSA sequencer
		 *	so that the device this Receiver belongs to receives
		 *	event from the client:port passed as parameters.
		 *
		 *	@return true if subscription was established,
		 *		false otherwise
		 */
		public boolean subscribeTo(int nClient, int nPort)
		{
			try
			{
				AlsaMidiDevice.this.m_aSequencer.subscribePort(
					nClient, nPort,
					AlsaMidiDevice.this.getClient(), AlsaMidiDevice.this.getPort());
				return true;
			}
			catch (RuntimeException e)
			{
				return false;
			}
		}


	}




	private class AlsaTransmitter
		extends		TTransmitter
	{
		private boolean		m_bReceiverSubscribed;



		public AlsaTransmitter()
		{
			super();
			m_bReceiverSubscribed = false;
		}



		public void setReceiver(Receiver receiver)
		{
			super.setReceiver(receiver);
			/*
			 *	Try to establish a subscription of the Receiver
			 *	to the ALSA seqencer client of the device this
			 *	Transmitter belongs to.
			 */
			if (receiver instanceof AlsaSequencerReceiver)
			{
				m_bReceiverSubscribed = ((AlsaSequencerReceiver) receiver).subscribeTo(getClient(), getPort());
			}
		}



		public void send(MidiMessage message, long lTimeStamp)
		{
			/*
			 *	Send message via Java methods only if not
			 *	subscription was established. If there is a
			 *	subscription, the message is routed inside of
			 *	the ALSA sequencer.
			 */
			if (! m_bReceiverSubscribed)
			{
				super.send(message, lTimeStamp);
			}
		}



		public void close()
		{
			super.close();
			// TODO: remove subscription
		}
	}

}



/*** AlsaMidiDevice.java ***/

