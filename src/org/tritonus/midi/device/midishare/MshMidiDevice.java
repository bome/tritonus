/*
 *	MshMidiDevice.java
 */

/*
 *   Copyright © Grame 2000 for the Tritonus project by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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
 *  Grame Research Laboratory, 9, rue du Garet 69001 Lyon - France
 *  grame@grame.fr
 *
 */


package	org.tritonus.midi.device.midishare;


import	com.sun.java.util.collections.ArrayList;
import	com.sun.java.util.collections.Iterator;
import	com.sun.java.util.collections.List;

/*
import	java.util.ArrayList;
import	java.util.List;
import	java.util.Iterator;
*/

import	javax.sound.midi.MidiDevice;
import	javax.sound.midi.MidiEvent;
import	javax.sound.midi.MidiMessage;
import	javax.sound.midi.MidiUnavailableException;
import	javax.sound.midi.Receiver;
import	javax.sound.midi.Transmitter;

import	org.tritonus.TDebug;
import	org.tritonus.lowlevel.alsa.ASequencer;
import	org.tritonus.midi.device.TMidiDevice;
import	org.tritonus.util.GlobalInfo;

import	grame.midishare.Midi;


public class MshMidiDevice
	extends		TMidiDevice
	implements	MshMidiIn.MshMidiInListener, MshClient
{
	private int			m_refNum;		 // the MidiShare application refnum
	public int 			m_filter = 0;    // the application filter

	private boolean		m_bUseIn;
	private boolean		m_bUseOut;
	
	private MshMidiIn	m_mshMidiIn;
	private MshMidiOut	m_mshMidiOut;
	
	
	public MshMidiDevice()
	{
		super(new MidiDevice.Info("MidiShare MIDI client",
					 GlobalInfo.getVendor(),
					 "this client uses MidiShare",
					 GlobalInfo.getVersion()));
		m_bUseIn = true;
		m_bUseOut = true;
	}
	

	/*
	public MshMidiDevice(int refnum)
	{
		this(refnum, true, true);
	}
	
	public MshMidiDevice(int refnum, boolean bUseIn, boolean bUseOut)
	{
		this(new MidiDevice.Info("MidiShare MIDI client (" + refnum + ")",
					 GlobalInfo.getVendor(),
					 "this cleint uses MidiShare",
					 GlobalInfo.getVersion()),
		     refnum, bUseIn, bUseOut);
	}

	protected MshMidiDevice(MidiDevice.Info info, int refnum, boolean bUseIn, boolean bUseOut)
	{
		super(info);
		m_refNum = refnum;
		m_bUseIn = bUseIn;
		m_bUseOut = bUseOut;
	}
	*/

	private boolean getUseIn()
	{
		return m_bUseIn;
	}


	private boolean getUseOut()
	{
		return m_bUseOut;
	}

	public int getRefnum() 
	{
		return m_refNum;
	}

	protected void openImpl()
	{
		int i;
		TDebug.out("MidiShareMidiDevice.openImpl(): called");
		m_refNum = Midi.Open("JavaSound In/Out");
		
		if (m_refNum < 0)  TDebug.out(" Midi.Open : error ");
		if ((m_filter = Midi.NewFilter()) == 0) TDebug.out(" Midi.NewFilter : error ");
		
		// Midi Filter configuration
		for (i = 0 ; i<256; i++) {
	          Midi.AcceptPort(m_filter, i, 1);
	          Midi.AcceptType(m_filter, i, 1);
	    }
	                    
	    for (i = 0 ; i<16; i++) { Midi.AcceptChan(m_filter, i, 1); }
	   	Midi.SetFilter(m_refNum, m_filter);
	   	
	   	// TODO : types to be rejected 
	   	Midi.AcceptType(m_filter, Midi.typeActiveSens,0);
			
		
		if (getUseIn())
		{
			m_mshMidiIn = new MshMidiIn(m_refNum, this);
			m_mshMidiIn.start();
		}
		
		if (getUseOut())
		{
			m_mshMidiOut = new MshMidiOut(m_refNum);
		}
		
		if (TDebug.TraceAlsaMidiDevice)
		{
			TDebug.out("MidiShareMidiDevice.openImpl(): completed");
		}
	}


	protected void closeImpl()
	{
		if (getUseIn())
		{
			m_mshMidiIn.interrupt();
			m_mshMidiIn = null;
		}
		
		if (m_filter != 0){
			Midi.SetFilter(m_refNum, 0);
			Midi.FreeFilter(m_filter);
			m_filter = 0;
		}
		
		if (m_refNum > 0) Midi.Close(m_refNum);
		m_refNum = -1;
	}


	public long getMicroSecondPosition()
	{
		return -1;
	}


	protected void receive(MidiMessage message, long lTimeStamp)
	{
		if (isOpen())
		{
			m_mshMidiOut.enqueueMessage(message, lTimeStamp);
		}
	}


	// for MshMidiInListener
	// passes events read from the device to the receivers
	public void dequeueEvent(MidiEvent event)
	{
		MidiMessage	message = event.getMessage();
		if (TDebug.TraceAlsaMidiDevice)
		{
			TDebug.out("MshMidiDevice.dequeueEvent(): message: " + message);
		}
		sendImpl(message, -1L);
	}


	public Receiver getReceiver()
		throws	MidiUnavailableException
	{
		// TODO: check number
		return new MshReceiver();
	}


	public Transmitter getTransmitter()
		throws	MidiUnavailableException
	{
		// TODO: check number
		return new MshTransmitter();
	}


/////////////////// INNER CLASSES //////////////////////////////////////

	private class MshReceiver
		extends		TReceiver
		//implements	MshNativeReceiver
	{

		public MshReceiver()
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
		 
		 /*
		public boolean subscribeTo(int nClient, int nPort)
		{
			try
			{
				MidiShareMidiDevice.this.m_aSequencer.subscribePort(
					nClient, nPort,
					AlsaMidiDevice.this.getClient(), AlsaMidiDevice.this.getPort());
				return true;
			}
			catch (RuntimeException e)
			{
				return false;
			}
		}

		*/
	}


	private class MshTransmitter
		extends		TTransmitter
	{
		private boolean		m_bReceiverSubscribed;

		public MshTransmitter()
		{
			super();
			m_bReceiverSubscribed = false;
		}

		public void setReceiver(Receiver receiver)
		{
			super.setReceiver(receiver);
		
			if (receiver instanceof MshClient)
			{
				Midi.Connect (m_refNum, ((MshClient)receiver).getRefnum(),1);
			}
		}

		public void send(MidiMessage message, long lTimeStamp)
		{
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



/*** MshMidiDevice.java ***/

