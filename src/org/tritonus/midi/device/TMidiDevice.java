/*
 *	TMidiDevice.java
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


/**	Base class for MidiDevice implementations.
 *	The goal of this class is to supply the common functionality for
 *	classes that implement the interface MidiDevice.
 */
public abstract class TMidiDevice
	implements	MidiDevice
{
	/**	The Info object for a certain instance of MidiDevice.
	 */
	private MidiDevice.Info		m_info;

	/**	A flag to store whether the device is "open".
	 */
	private boolean			m_bOpen;

	/**	The number of Receivers that refer to this MidiDevice.
	 *	This is currently only maintained for information purposes.
	 *
	 *	@see #addReceiver
	 *	@see #removeReceiver
	 */
	private int			m_nNumReceivers;

	/**	The collection of Transmitter that refer to this MidiDevice.
	 */
	private List			m_transmitters;



	/**	Initialize this class.
	 *	This sets the info from the passed one, sets the open status
	 *	to false, the number of Receivers to zero and the collection
	 *	of Transmitters to be empty.
	 *
	 *	@param info	The info object that describes this instance.
	 */
	public TMidiDevice(MidiDevice.Info info)
	{
		m_info = info;
		m_bOpen = false;
		m_nNumReceivers = 0;
		m_transmitters = new ArrayList();
	}



	/**	Retrieves a description of this instance.
	 *	This returns the info object passed to the constructor.
	 *
	 *	@return the description
	 *
	 *	@see #TMidiDevice
	 */
	public MidiDevice.Info getDeviceInfo()
	{
		return m_info;
	}



	public void open()
		throws	MidiUnavailableException
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
		throws	MidiUnavailableException
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
		/*
		 *	The value -1 means unlimited.
		 */
		return -1;
	}



	public int getMaxTransmitters()
	{
		/*
		 *	The value -1 means unlimited.
		 */
		return -1;
	}



	/**	Creates a new Receiver object associated with this instance.
	 *	In this implementation, an unlimited number of Receivers
	 *	per MidiDevice can be created.
	 */
	public Receiver getReceiver()
		throws	MidiUnavailableException
	{
		// TODO: check number
		return new TReceiver();
	}



	/**	Creates a new Transmitter object associated with this instance.
	 *	In this implementation, an unlimited number of Transmitters
	 *	per MidiDevice can be created.
	 */
	public Transmitter getTransmitter()
		throws	MidiUnavailableException
	{
		// TODO: check number
		return new TTransmitter();
	}


	/*
	 *	Intended for overriding by subclasses to receive messages.
	 *	This method is called by TMidiDevice.Receiver object on
	 *	receipt of a MidiMessage.
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



	/**	Send a MidiMessage to all Transmitters.
	 *	This method should be called by subclasses when they get a
	 *	message from a physical MIDI port.
	 */
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


	/**	Receiver proxy class.
	 *	This class' objects are handed out on calls to
	 *	TMidiDevice.getReceiver(). 
	 */
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



		/**	Receive a MidiMessage.
		 *
		 */
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



		/**	Closes the receiver.
		 *	After a receiver has been closed, it does no longer
		 *	propagate MidiMessages to its associated MidiDevice.
		 */
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



		/**	Closes the transmitter.
		 *	After a transmitter has been closed, it no longer
		 *	passes MidiMessages to a Receiver previously set for
		 *	it.
		 */
		public void close()
		{
			TMidiDevice.this.removeTransmitter(this);
			synchronized (this)
			{
				m_receiver = null;
			}
		}
	}



	/*
	 *	This is needed only because MidiDevice.Info's constructor
	 *	is protected (in the Sun jdk1.3).
	 */
	public class Info
		extends MidiDevice.Info
	{
		public Info(String a, String b, String c, String d)
		{
			super(a, b, c, d);
		}
	}

}



/*** TMidiDevice.java ***/

