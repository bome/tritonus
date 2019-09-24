/*
 *	TPreloadingSequencer.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2003 - 2004 by Matthias Pfisterer
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.share.midi;

import java.util.Collection;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiDevice;

import org.tritonus.share.TDebug;



/** Base class for sequencers that work with an internal queue.
	To be more precise, this is the base class for sequencers that
	do not load the complete Sequence to internal data structures before start,
	but take single events from the Sequence and put them to the sequencing
	queue while running.
 */
public abstract class TPreloadingSequencer
extends TSequencer
{
	/** The default value for {@link m_nLatency}.
		This default value is set in the constructor.
	 */
	private static final int DEFAULT_LATENCY = 100;

	/**
	 */
	private int m_nLatency;

	@SuppressWarnings("unused")
	private Thread				m_loaderThread;

	/**
	   Sets the latency to the default value.
	 */
	protected TPreloadingSequencer(MidiDevice.Info info,
								   Collection<SyncMode> masterSyncModes,
								   Collection<SyncMode> slaveSyncModes)
	{
		super(info, masterSyncModes,
			  slaveSyncModes);
		if (TDebug.TraceSequencer) { TDebug.out("TPreloadingSequencer.<init>(): begin"); }
		m_nLatency = DEFAULT_LATENCY;
		if (TDebug.TraceSequencer) { TDebug.out("TPreloadingSequencer.<init>(): end"); }
	}






	/** Sets the preloading intervall.
		This is the time span between preloading events to an internal
		queue and playing them. This intervall should be kept constant
		by the implementation. However, this cannot be guaranteed.
	*/
	public void setLatency(int nLatency)
	{
		// TODO: preload if latency becomes shorter
		m_nLatency = nLatency;
	}



	/** Get the preloading intervall.

	@return the preloading intervall in milliseconds, or -1 if the sequencer
	doesn't repond to changes in the <code>Sequence</code> at all.
	*/
	public int getLatency()
	{
		return m_nLatency;
	}


	// currently not called by subclasses. order has to be assured (subclass first)
	protected void openImpl()
	{
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.openImpl(): begin"); }
		// m_loaderThread = new LoaderThread();
		// m_loaderThread.start();
	}



	/**	Put a message into the queue.
		This is Claus-Dieter's special method: it puts the message to
		the ALSA queue for delivery at the specified time.
		The time has to be given in ticks according to the resolution
		of the currently active Sequence. For this method to work,
		the Sequencer has to be started. The message is delivered
		the same way as messages from a Sequence, i.e. to all
		registered Transmitters. If the current queue position (as
		returned by getTickPosition()) is
		already behind the desired schedule time, the message is
		ignored.

		@param message the MidiMessage to put into the queue.

		@param lTick the desired schedule time in ticks.
	*/
	public abstract void sendMessageTick(MidiMessage message, long lTick);

}



/*** TPreloadingSequencer.java ***/
