/*
 *	JavaSoundToneGenerator.java
 */

package	org.tritonus.mmapi;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.microedition.media.MediaException;

import org.tritonus.share.TDebug;

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
		if (TDebug.TraceToneGenerator) { TDebug.out("JavaSoundToneGenerator.<init>(): begin"); }
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
		if (TDebug.TraceToneGenerator) { TDebug.out("JavaSoundToneGenerator.<init>(): Synthesizer: " + synth); }
		m_midiChannel = synth.getChannels()[0];
		if (TDebug.TraceToneGenerator) { TDebug.out("JavaSoundToneGenerator.<init>(): MidiChannel: " + m_midiChannel); }
		if (TDebug.TraceToneGenerator) { TDebug.out("JavaSoundToneGenerator.<init>(): end"); }
	}


	/**	TODO:
	*/
	public void playTone(int nNote,
			     int nDuration,
			     int nVolume)
		throws MediaException
	{
		if (TDebug.TraceToneGenerator) { TDebug.out("JavaSoundToneGenerator.playTone(): begin"); }
		// scale from [0..100] to [0..127]
		int	nVelocity = (nVolume * 127) / 100;
		if (TDebug.TraceToneGenerator) { TDebug.out("JavaSoundToneGenerator.playTone(): velocity: " + nVelocity); }
		Thread	thread = new ToneThread(nNote, nDuration, nVelocity);
		thread.start();
		if (TDebug.TraceToneGenerator) { TDebug.out("JavaSoundToneGenerator.playTone(): end"); }
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
			if (TDebug.TraceToneGenerator) { TDebug.out("JavaSoundToneGenerator.ToneThread.run(): begin"); }
			JavaSoundToneGenerator.this.m_midiChannel.noteOn(m_nNote, m_nVelocity);
			try
			{
				Thread.sleep(m_nDuration);
			}
			catch (InterruptedException e)
			{
				if (TDebug.TraceAllExceptions) { TDebug.out(e); }
			}
			JavaSoundToneGenerator.this.m_midiChannel.noteOff(m_nNote);
			if (TDebug.TraceToneGenerator) { TDebug.out("JavaSoundToneGenerator.ToneThread.run(): end"); }
		}
	}
}



/*** JavaSoundToneGenerator.java ***/
