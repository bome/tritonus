/*
 *	JavaSoundToneGenerator.java
 */

/*
 *  Copyright (c) 2002 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
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
 */

package	org.tritonus.mmapi;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.microedition.media.MediaException;



/**	TODO:
*/
public class JavaSoundToneGenerator
implements ToneGenerator
{
	/**	TODO:
	*/
	private MidiChannel	m_midiChannel;


	/**	TODO:
	*/
	public JavaSoundToneGenerator()
		throws MediaException
	{
		Synthesizer	synth = null;
		try
		{
			synth = MidiSystem.getSynthesizer();
			synth.open();
		}
		catch (MidiUnavailableException e)
		{
			throw new MediaException("Java Sound Synthesizer not available");
		}
		m_midiChannel = synth.getChannels()[0];
	}



	/**	TODO:
	*/
	public void playTone(int nNote,
			     int nDuration,
			     int nVolume)
		throws MediaException
	{
		// scale from [0..100] to [0..127]
		int	nVelocity = (nVolume * 127) / 100;
		// if (TDebug.TraceToneGenerator) { TDebug.out("JavaSoundToneGenerator.playTone(): velocity: " + nVelocity); }
		Thread	thread = new ToneThread(nNote, nDuration, nVelocity);
		thread.start();
	}


	private class ToneThread
	extends Thread
	{
		private int	m_nNote;
		private int	m_nDuration;
		private int	m_nVelocity;


		public ToneThread(int nNote, int nDuration, int nVelocity)
		{
			m_nNote = nNote;
			m_nDuration = nDuration;
			m_nVelocity = nVelocity;
		}


		public void run()
		{
			JavaSoundToneGenerator.this.m_midiChannel.noteOn(m_nNote, m_nVelocity);
			try
			{
				Thread.sleep(m_nDuration);
			}
			catch (InterruptedException e)
			{
				// IGNORED
			}
			JavaSoundToneGenerator.this.m_midiChannel.noteOff(m_nNote);
		}
	}
}



/*** JavaSoundToneGenerator.java ***/
