/*
 *	AlsaQueueHolder.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2001 by Matthias Pfisterer
 *
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
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.midi.device.alsa;

import org.tritonus.lowlevel.alsa.AlsaSeq;



/**	A representation of a physical MIDI port based on the ALSA sequencer.
 */
public class AlsaQueueHolder
{
	/**	The object interfacing to the ALSA sequencer.
	 */
	private AlsaSeq	m_aSequencer;

	/**	ALSA queue number.
	 */
	private int		m_nQueue;




	/**
	 */
	public AlsaQueueHolder(AlsaSeq aSequencer)
	{
		m_aSequencer = aSequencer;
		m_nQueue = m_aSequencer.allocQueue();
		if (m_nQueue < 0)
		{
			throw new RuntimeException("can't get ALSA sequencer queue");
		}
	}


	/**	Returns the allocated queue
		@return the queue number.
	 */
	public int getQueue()
	{
		return m_nQueue;
	}



	/**	Frees the queue.
	 */
	public void close()
	{
		m_aSequencer.freeQueue(getQueue());
	}



	public void finalize()
	{
		close();
	}
}



/*** AlsaQueueHolder.java ***/

