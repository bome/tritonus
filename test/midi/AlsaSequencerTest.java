/*
 *	AlsaSequencerTest.java
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

import	org.tritonus.midi.device.alsa.AlsaSequencer;
import	org.tritonus.midi.device.alsa.AlsaMidiDevice;



public class AlsaSequencerTest
{
	private static boolean		DEBUG = true;

	public static void main(String[] args)
	{
		int	nChannel = 64;
		int	nPort = 0;
		if (args.length == 3)
		{
			nChannel = Integer.parseInt(args[0]);
			nPort = Integer.parseInt(args[1]);
		}
		AlsaMidiDevice	device = new AlsaMidiDevice(nChannel, nPort, false, true);
		device.open();
		Receiver	midir = null;
		try
		{
			midir = device.getReceiver();

		}
		catch (MidiUnavailableException e)
		{
		}
		Sequencer	seq = null;
		try
		{
			seq = MidiSystem.getSequencer();
			org.tritonus.TDebug.out("Sequencer: " + seq);
			seq.open();
		}
		catch (MidiUnavailableException e)
		{
			org.tritonus.TDebug.out("no Sequencer");
		}
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
		Receiver	r = new DumpReceiver(System.out);
		try
		{
			Transmitter	t = seq.getTransmitter();
			// t.setReceiver(r);
			Transmitter	t2 = seq.getTransmitter();
			t2.setReceiver(midir);
		}
		catch (MidiUnavailableException e)
		{
		}

		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
		}

		System.out.println("starting...");
		seq.start();
/*
*/
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
