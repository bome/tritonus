/*
 *	TSequencer.java
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

import	org.tritonus.TDebug;
import	org.tritonus.util.ArraySet;




public abstract class TSequencer
	extends		TMidiDevice
	implements	Sequencer
{
	/**	The Sequence to play or to record to.
	 */
	private Sequence	m_sequence;

	/**	The listeners that want to be notified of MetaMessages.
	 */
	private Set		m_metaListeners;

	/**	The listeners that want to be notified of control change events.
	 *	They are organized as follows: this array is indexed with
	 *	the number of the controller change events listeners are
	 *	interested in. If there is any interest, the array element
	 *	contains a reference to a Set containing the listeners.
	 *	These sets are allocated on demand.
	 */
	private Set[]		m_aControllerListeners;

	// private float		m_fTempoInMPQ;
	private float		m_fTempoFactor;


	protected TSequencer(MidiDevice.Info info)
	{
		super(info);
		m_sequence = null;
		m_metaListeners = new ArraySet();
		m_aControllerListeners = new Set[128];
		m_fTempoFactor = 1.0F;
/*
  TODO: find a way of safe calling
		setTempoFactor(1.0F);
		setTempoInMPQ(500000);
*/
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



	public float getTempoInBPM()
	{
		return 6.0E7F / getTempoInMPQ();
	}



	public void setTempoInBPM(float fBPM)
	{
		setTempoInMPQ(6.0E7F / fBPM);
	}



	public float getTempoInMPQ()
	{
		return getTempoNative() * getTempoFactor();
	}



	public void setTempoInMPQ(float fMPQ)
	{
		float	fRealTempo = fMPQ / getTempoFactor();
		if (TDebug.TraceTSequencer)
		{
			TDebug.out("TSequencer.setTempoInMPQ(): real tempo: " + fRealTempo);
		}
		setTempoNative(fRealTempo);
	}



	public void setTempoFactor(float fFactor)
	{
		/*
		 *	Get nominal tempo, using the old tempo factor.
		 */
		float	fNominalTempo = getTempoInMPQ();
		m_fTempoFactor = fFactor;
		/*
		 *	Calculate the new real tempo, using the new tempo
		 *	factor.
		 */
		float	fRealTempo = fNominalTempo / fFactor;
		if (TDebug.TraceTSequencer)
		{
			TDebug.out("TSequencer.setTempoFactor(): real tempo: " + fRealTempo);
		}
		setTempoNative(fRealTempo);
	}



	public float getTempoFactor()
	{
		return m_fTempoFactor;
	}



	/**	Get the tempo of the native sequencer part.
	 *	This method has to be defined by subclasses according
	 *	to the native facilities they use for sequenceing.
	 *	The implementation should not take into account the
	 *	tempo factor. This is handled elsewhere.
	 *
	 *	@return the real tempo of the native sequencer in MPQ
	 */
	protected abstract float getTempoNative();



	/**	Change the tempo of the native sequencer part.
	 *	This method has to be defined by subclasses according
	 *	to the native facilities they use for sequenceing.
	 *	The implementation should not take into account the
	 *	tempo factor. This is handled elsewhere.
	 */
	protected abstract void setTempoNative(float fMPQ);



	// TODO: what should be the behaviour if no Sequence is set?
	// TODO: has to be redefined if recording is done natively
	public long getTickLength()
	{
		return getSequence().getTickLength();
	}



	// TODO: what should be the behaviour if no Sequence is set?
	// TODO: has to be redefined if recording is done natively
	public long getMicrosecondLength()
	{
		return getSequence().getMicrosecondLength();
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
		synchronized (m_aControllerListeners)
		{
			if (anControllers == null)
			{
				/*
				 *	Add to all controllers. NOTE: this
				 *	is an implementation-specific
				 *	semantic!
				 */
				for (int i = 0; i < 128; i++)
				{
					addControllerListener(i, listener);
				}
			}
			else
			{
				for (int i = 0; i < anControllers.length; i++)
				{
					addControllerListener(anControllers[i], listener);
				}
			}
		}
		return getListenedControllers(listener);
	}



	private void addControllerListener(int i,
					   ControllerEventListener listener)
	{
		if (m_aControllerListeners[i] == null)
		{
			m_aControllerListeners[i] = new ArraySet();
		}
		m_aControllerListeners[i].add(listener);
	}



	public int[] removeControllerEventListener(ControllerEventListener listener, int[] anControllers)
	{
		synchronized (m_aControllerListeners)
		{
			if (anControllers == null)
			{
				/*
				 *	Remove from all controllers. Unlike
				 *	above, this is specified semantics.
				 */
				for (int i = 0; i < 128; i++)
				{
					removeControllerListener(i, listener);
				}
			}
			else
			{
				for (int i = 0; i < anControllers.length; i++)
				{
					removeControllerListener(anControllers[i], listener);
				}
			}
		}
		return getListenedControllers(listener);
	}



	private void removeControllerListener(int i,
					      ControllerEventListener listener)
	{
		if (m_aControllerListeners[i] != null)
		{
			m_aControllerListeners[i].add(listener);
		}
	}



	private int[] getListenedControllers(ControllerEventListener listener)
	{
		int[]	anControllers = new int[128];
		int	nIndex = 0;	// points to the next position to use.
		for (int nController = 0; nController < 128; nController++)
		{
			if (m_aControllerListeners[nController] != null &&
			    m_aControllerListeners[nController].contains(listener))
			{
				anControllers[nIndex] = nController;
				nIndex++;
			}
		}
		int[]	anResultControllers = new int[nIndex];
		System.arraycopy(anControllers, 0, anResultControllers, 0, nIndex);
		return anResultControllers;
	}



	protected void sendControllerEvent(ShortMessage message)
	{
		// TDebug.out("TSequencer.sendControllerEvent(): called");
		int	nController = message.getData1();
		if (m_aControllerListeners[nController] != null)
		{
			Iterator	iterator = m_aControllerListeners[nController].iterator();
			while (iterator.hasNext())
			{
				ControllerEventListener	controllerEventListener = (ControllerEventListener) iterator.next();
				ShortMessage	copiedMessage = (ShortMessage) message.clone();
				controllerEventListener.controlChange(copiedMessage);
			}
		}
	}
}



/*** TSequencer.java ***/
