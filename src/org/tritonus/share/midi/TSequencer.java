/*
 *	TSequencer.java
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


package	org.tritonus.share.midi;

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

import	org.tritonus.share.TDebug;
import	org.tritonus.share.ArraySet;




public abstract class TSequencer
	extends		TMidiDevice
	implements	Sequencer
{
	private static final float	MPQ_BPM_FACTOR = 6.0E7F;


	private boolean		m_bRunning;


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

	private float		m_fNominalTempoInMPQ;
	private float		m_fTempoFactor;


	protected TSequencer(MidiDevice.Info info)
	{
		super(info);
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.<init>(): begin"); }
		m_bRunning = false;
		m_sequence = null;
		m_metaListeners = new ArraySet();
		m_aControllerListeners = new Set[128];
		setTempoFactor(1.0F);
		setTempoInMPQ(500000);
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.<init>(): end"); }
	}



	public void setSequence(Sequence sequence)
		throws	InvalidMidiDataException
	{
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.setSequence(Sequence): begin"); }
		// TODO: what if playing is in progress?
		m_sequence = sequence;
		// yes, this is required by the specification
		setTempoFactor(1.0F);
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.setSequence(Sequence): end"); }
	}



	public void setSequence(InputStream inputStream)
		throws	InvalidMidiDataException, IOException
	{
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.setSequence(InputStream): begin"); }
		Sequence	sequence = MidiSystem.getSequence(inputStream);
		setSequence(sequence);
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.setSequence(InputStream): end"); }
	}



	public Sequence getSequence()
	{
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.getSequence(): begin"); }
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.getSequence(): end"); }
		return m_sequence;
	}



	public void start()
	{
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.start(): begin"); }
		if (! isRunning())
		{
			m_bRunning = true;
			// TODO: check open status, perhaps sequence present
			startImpl();
		}
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.start(): end"); }
	}


	/**
	 *	Subclasses have to override this method to be notified of
	 *	starting.
	 */
	protected void startImpl()
	{
		if (TDebug.TraceMidiDevice) { TDebug.out("TSequencer.startImpl(): begin"); }
		if (TDebug.TraceMidiDevice) { TDebug.out("TSequencer.startImpl(): end"); }
	}



	public void stop()
	{
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.stop(): begin"); }
		if (isRunning())
		{
			stopImpl();
			m_bRunning = false;
		}
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.stop(): end"); }
	}



	/**
	 *	Subclasses have to override this method to be notified of
	 *	stopping.
	 */
	protected void stopImpl()
	{
		if (TDebug.TraceMidiDevice) { TDebug.out("TSequencer.stopImpl(): begin"); }
		if (TDebug.TraceMidiDevice) { TDebug.out("TSequencer.stopImpl(): end"); }
	}



	public boolean isRunning()
	{
		return m_bRunning;
	}



	/**	Returns the resolution (ticks per quarter) of the current sequence.
		If no sequence is set, a bogus default value != 0 is returned.
	*/
	protected int getResolution()
	{
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.getResolution(): begin"); }
		Sequence	sequence = getSequence();
		int		nResolution;
		if (sequence != null)
		{
			nResolution = sequence.getResolution();
		}
		else
		{
			nResolution = 1;
		}
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.getResolution(): end"); }
		return nResolution;
	}



	protected float getNominalTempoInMPQ()
	{
		return m_fNominalTempoInMPQ;
	}



	protected void setNominalTempoInMPQ(float fMPQ)
	{
		m_fNominalTempoInMPQ = fMPQ;
		setRealTempo();
	}



	protected void setRealTempo()
	{
		float	fRealTempo = getNominalTempoInMPQ() / getTempoFactor();
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.setRealTempo(): real tempo: " + fRealTempo); }
		setTempoImpl(fRealTempo);
	}



	public float getTempoInBPM()
	{
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.getTempoInBPM(): begin"); }
		float	fBPM = MPQ_BPM_FACTOR / getTempoInMPQ();
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.getTempoInBPM(): end"); }
		return fBPM;
	}



	public void setTempoInBPM(float fBPM)
	{
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.setTempoInBPM(): begin"); }
		float	fMPQ = MPQ_BPM_FACTOR / fBPM;
		setTempoInMPQ(fMPQ);
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.setTempoInBPM(): end"); }
	}



	public float getTempoInMPQ()
	{
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.getTempoInMPQ(): begin"); }
		// float fMPQ = getTempoNative() * getTempoFactor();
		float	fMPQ = getNominalTempoInMPQ();
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.getTempoInMPQ(): end"); }
		return fMPQ;
	}



	public void setTempoInMPQ(float fMPQ)
	{
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.setTempoInMPQ(): begin"); }
		setNominalTempoInMPQ(fMPQ);
//		float	fRealTempo = fMPQ / getTempoFactor();
//		setTempoNative(fRealTempo);
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.setTempoInMPQ(): end"); }
	}



	public void setTempoFactor(float fFactor)
	{
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.setTempoFactor(): begin"); }
		m_fTempoFactor = fFactor;
		setRealTempo();
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.setTempoFactor(): end"); }
	}



	public float getTempoFactor()
	{
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.getTempoFactor(): begin"); }
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.getTempoFactor(): end"); }
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
	// TODO: no longer needed
	protected abstract float getTempoImpl();



	/**	Change the tempo of the native sequencer part.
	 *	This method has to be defined by subclasses according
	 *	to the native facilities they use for sequenceing.
	 *	The implementation should not take into account the
	 *	tempo factor. This is handled elsewhere.
	 */
	protected abstract void setTempoImpl(float fMPQ);



	// TODO: what should be the behaviour if no Sequence is set?
	// NOTE: has to be redefined if recording is done natively
	public long getTickLength()
	{
		return getSequence().getTickLength();
	}



	// TODO: what should be the behaviour if no Sequence is set?
	// NOTE: has to be redefined if recording is done natively
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



	protected void notifyListeners(MidiMessage message)
	{
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.sendToListeners(): begin"); }
		if (message instanceof MetaMessage)
		{
			// IDEA: use extra thread for event delivery
			sendMetaMessage((MetaMessage) message);
		}
		else if (message instanceof ShortMessage && ((ShortMessage) message).getCommand() == ShortMessage.CONTROL_CHANGE)
		{
			sendControllerEvent((ShortMessage) message);
		}
		if (TDebug.TraceSequencer) { TDebug.out("TSequencer.sendToListeners(): end"); }
	}
}



/*** TSequencer.java ***/
