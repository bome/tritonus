/*
 *	MidiPlayer2.java
 *
 *	Plays a single MIDI or RMF file.
 *	This file is part of the JavaSound Examples
 */

/*
 *  Copyright (c) 1999, 2000 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
 *
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */


import	java.io.BufferedInputStream;
import	java.io.File;
import	java.io.FileInputStream;
import	java.io.InputStream;
import	java.io.IOException;

import	javax.sound.midi.InvalidMidiDataException;
import	javax.sound.midi.MidiSystem;
import	javax.sound.midi.MidiUnavailableException;
import	javax.sound.midi.ShortMessage;
import	javax.sound.midi.MetaMessage;
import	javax.sound.midi.MetaEventListener;
import	javax.sound.midi.ControllerEventListener;
import	javax.sound.midi.Sequencer;
import	javax.sound.midi.Synthesizer;
import	javax.sound.midi.Receiver;
import	javax.sound.midi.Transmitter;


/**	Plays a single MIDI or RMF file.
	    <dl>
	      <dt><strong>Purpose</strong></dt>
	      <dd>Plays a single MIDI or RMF file, then exits.</dd>
	      <dt><strong>Type</strong></dt>
	      <dd>Command-line program</dd>
	      <dt><strong>Usage</strong></dt>
	      <dd><tt>java MidiPlayer2 [-s] [-m] [-d] &lt;midifile&gt;</tt></dd>
	      <dt><strong>Parameters</strong></dt>
	      <dd>
		<tt>-s</tt>: play on the internal
		synthesizer<br>
		<tt>-m</tt>: play on the MIDI port<br>
		<tt>-d</tt>: dump on the console<br>
		All options may be used together.
		No option is equal to giving <tt>-s</tt>.<br>
		<tt>&lt;midifile&gt;</tt>: the file
		name of the MIDI or RMF file that should be
		played</dd>
	      <dt><strong>Bugs, limitations</strong></dt>
	      <dd>For Sun's implementation of Java Sound, playing to
		the MIDI port and dumping to the console do not
		work. For Tritonus, playing RMF files does not
		work.</dd>
 */
public class MidiPlayer2
{
	/**	Flag for debugging messages.
	 *	If true, some messages are dumped to the console
	 *	during operation.	
	 */
	private static boolean		DEBUG = true;

	private static Sequencer	sm_sequencer = null;



	public static void main(String[] args)
	{
		/*
		 *	We check if there is no command-line argument at all
		 *	or the first one is '-h'.
		 *	If so, we display the usage message and
		 *	exit.
		 */
		if (args.length < 1 || args[0].equals("-h"))
		{
			printUsageAndExit();
		}

		boolean	bUseSynthesizer = false;
		boolean	bUseMidiPort = false;
		boolean bUseConsoleDump = false;

		int	nArgumentIndex;
		for (nArgumentIndex = 0; nArgumentIndex < args.length; nArgumentIndex++)
		{
			String	strArgument = args[nArgumentIndex];
			if (strArgument.equals("-s"))
			{
				bUseSynthesizer = true;
			}
			else if (strArgument.equals("-m"))
			{
				bUseMidiPort = true;
			}
			else if (strArgument.equals("-d"))
			{
				bUseConsoleDump = true;
			}
			else
			{
				break;
			}
		}

		/*
		 *	If no destination option is choosen at all,
		 *	we default to playing on the internal synthesizer.
		 */
		if (!(bUseSynthesizer | bUseMidiPort | bUseConsoleDump))
		{
			bUseSynthesizer = true;
		}

		/*
		 *	Now, that we're shure there is an argument, we take
		 *	it as the filename of the MIDI file we want to play.
		 */
		String	strFilename = args[nArgumentIndex];
		File	midiFile = new File(strFilename);
	
		/*
		 *	We create an (File)InputStream and decorate it with
		 *	a buffered stream. This is set later at the Sequencer
		 *	as the source of a sequence.
		 *
		 *	There is another programming technique: Creating a
		 *	Sequence object from the file and set this at the
		 *	Sequencer. While this technique seems more natural,
		 *	it in fact is less efficient on Sun's implementation
		 *	of the Java Sound API. Furthermore, it sucks for RMF
		 *	files. So for now, I consider the technique used
		 *	here as the "official" one.
		 *	But note that this depends
		 *	on facts that are implementation-dependant; it is
		 *	only true for the Sun implementation. In Tritonus,
		 *	efficiency is the other way round.
		 *	(And Tritonus has no RMF support because the
		 *	specs are proprietary.)
		 */
		InputStream	sequenceStream = null;
		try
		{
			sequenceStream = new FileInputStream(midiFile);
			sequenceStream = new BufferedInputStream(sequenceStream, 1024);
		}
		catch (IOException e)
		{
			/*
			 *	In case of an exception, we dump the exception
			 *	including the stack trace to the console
			 *	output. Then, we exit the program.
			 */
			e.printStackTrace();
			System.exit(1);
		}

		/*
		 *	Now, we need a Sequencer to play the Sequence.
		 *	Here, we simply request the default sequencer.
		 */
		try
		{
			sm_sequencer = MidiSystem.getSequencer();
		}
		catch (MidiUnavailableException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		if (sm_sequencer == null)
		{
			System.out.println("MidiPlayer2.main(): can't get a Sequencer");
			System.exit(1);
		}

		/*
		 *	There is a bug in JavaSound 0.90 (jdk1.3beta).
		 *	It prevents correct termination of the VM.
		 *	So we have to exit ourselves.
		 *	To accomplish this, we register a Listener to the Line.
		 *	It is called when there are "meta" events. Meta event
		 *	47 is end of track.
		 *
		 *	Thanks to Espen Riskedal for finding this trick.
		 */
		sm_sequencer.addMetaEventListener(new MetaEventListener()
					       {
						       public void meta(MetaMessage event)
							       {
								       if (event.getType() == 47)
								       {
									       System.out.println("before exit");
									       sm_sequencer.close();
									       System.exit(0);
								       }
							       }
					       });

		/*
		 *	The Sequencer is still a dead object.
		 *	We have to open() it to become live.
		 *	This is necessary to allocate some ressources in
		 *	the native part.
		 */
		try
		{
			sm_sequencer.open();
		}
		catch (MidiUnavailableException e)
		{
			e.printStackTrace();
			System.exit(1);
		}

		sm_sequencer.addMetaEventListener(
			new MetaEventListener()
			{
				public void meta(MetaMessage message)
					{
						System.out.println("%%% MetaMessage: " + message);
						System.out.println("%%% MetaMessage type: " + message.getType());
						System.out.println("%%% MetaMessage length: " + message.getLength());
					}
			});

		int[]	anControllers = sm_sequencer.addControllerEventListener(
			new ControllerEventListener()
			{
				public void controlChange(ShortMessage message)
					{
						System.out.println("%%% ShortMessage: " + message);
						System.out.println("%%% ShortMessage controller: " + message.getData1());
						System.out.println("%%% ShortMessage value: " + message.getData2());
					}
			},
				null);

		System.out.println("Listened controllers:");
		for (int i = 0; i < anControllers.length; i++)
		{
			System.out.print(anControllers[i] + " ");
		}
		System.out.println("");

		/*
		 *	Next step is to tell the Sequencer which
		 *	Sequence it has to play. In this case, we
		 *	set it as the InputStream created above.
		 */
		try
		{
			sm_sequencer.setSequence(sequenceStream);
		}
		catch (InvalidMidiDataException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}

		/*
		 *	Now, we set up the destinations the Sequence should be
		 *	played on.
		 */
		if (bUseSynthesizer)
		{
			/*
			 *	We try to get the default synthesizer, open()
			 *	it and chain it to the sequencer with a
			 *	Transmitter-Receiver pair.
			 */
			try
			{
				Synthesizer	synth = MidiSystem.getSynthesizer();
				synth.open();
				Receiver	synthReceiver = synth.getReceiver();
				Transmitter	seqTransmitter = sm_sequencer.getTransmitter();
				seqTransmitter.setReceiver(synthReceiver);
			}
			catch (MidiUnavailableException e)
			{
				e.printStackTrace();
			}
		}

		if (bUseMidiPort)
		{
			/*
			 *	We try to get a Receiver which is already
			 *	associated with the default MIDI port.
			 *	It is then linked to a sequencer's
			 *	Transmitter.
			 */
			try
			{
				Receiver	midiReceiver = MidiSystem.getReceiver();
				Transmitter	midiTransmitter = sm_sequencer.getTransmitter();
				midiTransmitter.setReceiver(midiReceiver);
			}
			catch (MidiUnavailableException e)
			{
				e.printStackTrace();
			}
		}

		if (bUseConsoleDump)
		{
			/*
			 *	We allocate a DumpReceiver object. Its job
			 *	is to print information on all received events
			 *	to the console.
			 *	It is then linked to a sequencer's
			 *	Transmitter.
			 */
			try
			{
				Receiver	dumpReceiver = new DumpReceiver(System.out);
				Transmitter	dumpTransmitter = sm_sequencer.getTransmitter();
				dumpTransmitter.setReceiver(dumpReceiver);
			}
			catch (MidiUnavailableException e)
			{
				e.printStackTrace();
			}
		}

		/*
		 *	Now, we can start over.
		 */
		sm_sequencer.start();
		try
		{
			Thread.sleep(10000000);
		}
		catch (InterruptedException e)
		{
		}
	}



	private static void printUsageAndExit()
	{
			System.out.println("MidiPlayer2: usage:");
			System.out.println("\tjava MidiPlayer2 [-s] [-m] [-d] <midifile>");
			System.out.println("\t-s\tplay on the internal synthesizer");
			System.out.println("\t-m\tplay on the MIDI port");
			System.out.println("\t-d\tdump on the console");
			System.out.println("All options may be used together.");
			System.out.println("No option is equal to giving -s.");
			System.exit(1);
	}
}



/*** MidiPlayer2.java ***/
