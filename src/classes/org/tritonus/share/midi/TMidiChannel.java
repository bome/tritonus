/*
 *	TMidiChannel.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2006 by Matthias Pfisterer
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

package org.tritonus.share.midi;

import javax.sound.midi.MidiChannel;


/**
 * Base class for MidiChannel implementations.
 * 
 * <p>This base class serves two purposes:</p>
 * 
 * <ol>
 * <li>It contains a channel number property so that the MidiChannel
 * object knows its own MIDI channel number.</li>
 *
 * <li>It maps some of the methods to others.</li>
 * </ol>
 *
 * @author Matthias Pfisterer
 */
public abstract class TMidiChannel
implements MidiChannel
{
	private int		m_nChannel;

	protected TMidiChannel(int nChannel)
	{
		m_nChannel = nChannel;
	}


	protected int getChannel()
	{
		return m_nChannel;
	}


	public void noteOff(int nNoteNumber)
	{
		noteOff(nNoteNumber, 0);
	}


	public void programChange(int nBank, int nProgram)
	{
		int nBankMSB = nBank >> 7;
		int nBankLSB = nBank & 0x7F;
		controlChange(0, nBankMSB);
		controlChange(32, nBankLSB);
		programChange(nProgram);
	}


	public void resetAllControllers()
	{
		controlChange(121, 0);
	}


	public void allNotesOff()
	{
		controlChange(123, 0);
	}


	public void allSoundOff()
	{
		controlChange(120, 0);
	}


	public boolean localControl(boolean bOn)
	{
		controlChange(122, bOn ? 127 : 0);
		return getController(122) >= 64;
	}



	public void setMono(boolean bMono)
	{
		// TODO: check this
		controlChange(bMono ? 126 : 127, 0);
	}


	public boolean getMono()
	{
		// TODO: check this
		return getController(126) == 0;
	}


	public void setOmni(boolean bOmni)
	{
		controlChange(bOmni ? 125 : 124, 0);
	}


	public boolean getOmni()
	{
		// TODO: check this
		return getController(125) == 0;
	}
}



/*** TMidiChannel.java ***/
