/*
 *	SequencerToMidi.java
 */


import	java.io.File;
import	java.io.IOException;

import	javax.sound.midi.MidiEvent;
import	javax.sound.midi.MidiMessage;
import	javax.sound.midi.ShortMessage;
import	javax.sound.midi.Sequence;
import	javax.sound.midi.Track;
import	javax.sound.midi.Transmitter;
import	javax.sound.midi.Receiver;
import	javax.sound.midi.Sequencer;
import	javax.sound.midi.InvalidMidiDataException;
import	javax.sound.midi.MidiUnavailableException;
import	javax.sound.midi.MidiSystem;
import	javax.sound.midi.Synthesizer;




public class SequencerToMidi
{
	public static void main(String[] args)
	{
/*
		org.gnu.tritonus.TDebug.TraceASequencer = true;
		org.gnu.tritonus.TDebug.TraceASequencerDetails = true;
		org.gnu.tritonus.TDebug.TraceAlsaMidiOut = true;
		org.gnu.tritonus.TDebug.TraceAlsaMidiIn = false;
		org.gnu.tritonus.TDebug.TraceTMidiDevice = false;
		org.gnu.tritonus.TDebug.TraceMidiSystem = false;
*/
		Receiver	midiReceiver = null;
		Sequencer	seq = null;
		Transmitter	seqTransmitter = null;
		try
		{
			midiReceiver = MidiSystem.getReceiver();
			seq = MidiSystem.getSequencer();
			seq.open();
			seqTransmitter = seq.getTransmitter();
		}
		catch (MidiUnavailableException e)
		{
		}
		seqTransmitter.setReceiver(midiReceiver);

		Sequence	sequence = null;
		String	strFilename = (args.length == 1) ? args[0] : args[2];
		File	midiFile = new File(strFilename);
		try
		{
			sequence = MidiSystem.getSequence(midiFile);
			seq.setSequence(sequence);
		}
		catch (InvalidMidiDataException e)
		{
		}
		catch (IOException e)
		{
		}
		seq.start();
		while (true)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
			}
			// System.out.println("in endless loop");
		}
/*
		seq.stop();
		seq.close();
*/
	}
}



/*** SequencerToMidi.java ***/
