/*
 *	TMidiDevice.java
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


package	org.tritonus.midi.device;


import	java.util.ArrayList;
import	java.util.Iterator;
import	java.util.List;

import	javax.sound.midi.MidiDevice;
import	javax.sound.midi.MidiMessage;
import	javax.sound.midi.MidiUnavailableException;
import	javax.sound.midi.Receiver;
import	javax.sound.midi.Transmitter;

import	org.tritonus.TDebug;



public abstract class TMidiDevice
	implements	MidiDevice
{
	private MidiDevice.Info		m_info;
	private boolean			m_bOpen;
	private int			m_nNumReceivers;
	private List			m_transmitters;


	public TMidiDevice(MidiDevice.Info info)
	{
		m_info = info;
		m_bOpen = false;
		m_nNumReceivers = 0;
		m_transmitters = new ArrayList();
	}



	public MidiDevice.Info getDeviceInfo()
	{
		return m_info;
	}



	public void open()
	{
		if (! isOpen())
		{
			openImpl();
			m_bOpen = true;
		}
	}



	/**
	 *	Subclasses have to override this method to be notified of
	 *	opening.
	 */
	protected void openImpl()
	{
	}



	public void close()
	{
		if (isOpen())
		{
			closeImpl();
			// TODO: close all Receivers and Transmitters
			m_bOpen = false;
		}
	}



	/**
	 *	Subclasses have to override this method to be notified of
	 *	closeing.
	 */
	protected void closeImpl()
	{
	}



	public boolean isOpen()
	{
		return m_bOpen;
	}


	public long getMicrosecondPosition()
	{
		return -1;
	}



	public int getMaxReceivers()
	{
		return Integer.MAX_VALUE;
	}



	public int getMaxTransmitters()
	{
		return Integer.MAX_VALUE;
	}



	public Receiver getReceiver()
		throws	MidiUnavailableException
	{
		// TODO: check number
		return new TReceiver();
	}



	public Transmitter getTransmitter()
		throws	MidiUnavailableException
	{
		// TODO: check number
		return new TTransmitter();
	}


	/*
	 *	Intended for overriding by subclasses to receive messages.
	 */
	protected void receive(MidiMessage message, long lTimeStamp)
	{
		if (TDebug.TraceTMidiDevice)
		{
			TDebug.out("### [should be overridden] TMidiDevice.receive(): message " + message);
		}
	}



	private void addReceiver()
	{
		m_nNumReceivers++;
	}



	private void removeReceiver()
	{
		m_nNumReceivers--;
	}




	private void addTransmitter(Transmitter transmitter)
	{
		synchronized (m_transmitters)
		{
			m_transmitters.add(transmitter);
		}
	}


	private void removeTransmitter(Transmitter transmitter)
	{
		synchronized (m_transmitters)
		{
			m_transmitters.remove(transmitter);
		}
	}



	protected void sendImpl(MidiMessage message, long lTimeStamp)
	{
		Iterator	transmitters = m_transmitters.iterator();
		while (transmitters.hasNext())
		{
			TTransmitter	transmitter = (TTransmitter) transmitters.next();
			MidiMessage	copiedMessage = (MidiMessage) message.clone();
			transmitter.send(copiedMessage, lTimeStamp);
		}
	}




/////////////////// INNER CLASSES //////////////////////////////////////

	public class TReceiver
		implements	Receiver
	{
		private boolean		m_bOpen;



		public TReceiver()
		{
			TMidiDevice.this.addReceiver();
			m_bOpen = true;
		}



		protected boolean isOpen()
		{
			return m_bOpen;
		}



		public void send(MidiMessage message, long lTimeStamp)
		{
			if (TDebug.TraceTMidiDevice)
			{
				TDebug.out("TMidiDevice.TReceiver.send(): message " + message);
			}
			if (m_bOpen)
			{
				TMidiDevice.this.receive(message, lTimeStamp);
			}
			else
			{
				throw new IllegalStateException("receiver is not open");
			}
		}


		public void close()
		{
			TMidiDevice.this.removeReceiver();
			m_bOpen = false;
		}
	}




	public class TTransmitter
		implements	Transmitter
	{
		private Receiver	m_receiver;



		public TTransmitter()
		{
			TMidiDevice.this.addTransmitter(this);
		}


		public void setReceiver(Receiver receiver)
		{
			synchronized (this)
			{
				m_receiver = receiver;
			}
		}



		public Receiver getReceiver()
		{
			return m_receiver;
		}


		public void send(MidiMessage message, long lTimeStamp)
		{
			if (getReceiver() != null)
			{
				getReceiver().send(message, lTimeStamp);
			}
		}


		public void close()
		{
			TMidiDevice.this.removeTransmitter(this);
			synchronized (this)
			{
				m_receiver = null;
			}
		}
	}

}



/*** TMidiDevice.java ***/

