/*
 *	TSequencer.java
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

import	java.io.InputStream;
import	java.io.IOException;

import	java.util.Set;
import	java.util.Iterator;

import	javax.sound.midi.MidiSystem;
import	javax.sound.midi.Sequencer;
import	javax.sound.midi.Sequence;
import	javax.sound.midi.InvalidMidiDataException;
import	javax.sound.midi.MetaMessage;
import	javax.sound.midi.MetaEventListener;
import	javax.sound.midi.MidiMessage;
import	javax.sound.midi.ShortMessage;
import	javax.sound.midi.ControllerEventListener;
import	javax.sound.midi.MidiDevice;

import	org.tritonus.util.ArraySet;



public abstract class TSequencer
	extends		TMidiDevice
	implements	Sequencer
{
	private Sequence	m_sequence;
	private Set		m_metaListeners;
	private Set		m_controllerListeners;



	protected TSequencer(MidiDevice.Info info)
	{
		super(info);
		m_sequence = null;
		m_metaListeners = new ArraySet();
		m_controllerListeners = new ArraySet();	
	}



	public void setSequence(Sequence sequence)
		throws	InvalidMidiDataException
	{
		// TODO: what if playing is in progress?
		m_sequence = sequence;
		// yes, this is required by the specification
		setTempoFactor(1.0F);
	}



	public void setSequence(InputStream inputStream)
		throws	InvalidMidiDataException, IOException
	{
		Sequence	sequence = MidiSystem.getSequence(inputStream);
		setSequence(sequence);
	}



	public Sequence getSequence()
	{
		return m_sequence;
	}



	public boolean addMetaEventListener(MetaEventListener listener)
	{
		synchronized (m_metaListeners)
		{
			return m_metaListeners.add(listener);
		}
	}



	public void removeMetaEventListener(MetaEventListener listener)
	{
		synchronized (m_metaListeners)
		{
			m_metaListeners.remove(listener);
		}
	}


	protected Iterator getMetaEventListeners()
	{
		synchronized (m_metaListeners)
		{
			return m_metaListeners.iterator();
		}
	}



	protected void sendMetaMessage(MetaMessage message)
	{
		Iterator	iterator = getMetaEventListeners();
		while (iterator.hasNext())
		{
			MetaEventListener	metaEventListener = (MetaEventListener) iterator.next();
			MetaMessage	copiedMessage = (MetaMessage) message.clone();
			metaEventListener.meta(copiedMessage);
		}
	}



	public int[] addControllerEventListener(ControllerEventListener listener, int[] anControllers)
	{
		synchronized (m_controllerListeners)
		{
			m_controllerListeners.add(listener);
			// TODO:
			return null;
		}
	}



	public int[] removeControllerEventListener(ControllerEventListener listener, int[] anControllers)
	{
		synchronized (m_controllerListeners)
		{
			m_controllerListeners.remove(listener);
			// TODO:
			return null;
		}
	}


	protected Iterator getControllerEventListeners()
	{
		synchronized (m_controllerListeners)
		{
			return m_controllerListeners.iterator();
		}
	}



	protected void sendControllerEvent(ShortMessage message)
	{
		Iterator	iterator = getControllerEventListeners();
		while (iterator.hasNext())
		{
			ControllerEventListener	controllerEventListener = (ControllerEventListener) iterator.next();
			ShortMessage	copiedMessage = (ShortMessage) message.clone();
			controllerEventListener.controlChange(copiedMessage);
		}
	}
}



/*** TSequencer.java ***/
