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

import	org.tritonus.share.TDebug;

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
		try
		{
			device.open();
		}
		catch (MidiUnavailableException e)
		{
			e.printStackTrace();
		}
		Receiver	midir = null;
		try
		{
			midir = device.getReceiver();

		}
		catch (MidiUnavailableException e)
		{
			e.printStackTrace();
		}
		Sequencer	seq = null;
		try
		{
			seq = MidiSystem.getSequencer();
			TDebug.out("Sequencer: " + seq);
			seq.open();
		}
		catch (MidiUnavailableException e)
		{
			TDebug.out("no Sequencer");
			e.printStackTrace();
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
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
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
			e.printStackTrace();
		}

		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
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
				e.printStackTrace();
			}
			// System.out.println("in endless loop");
		}
/*
  seq.stop();
  seq.close();
*/
	}
}
