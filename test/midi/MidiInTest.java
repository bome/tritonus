/*
 *	MidiInTest.java
 */

/*
import	javax.sound.midi.MidiEvent;
import	javax.sound.midi.ShortEvent;
*/
import	javax.sound.midi.Sequence;
import	javax.sound.midi.Track;
import	javax.sound.midi.Transmitter;
import	javax.sound.midi.Receiver;
import	javax.sound.midi.InvalidMidiDataException;
import	javax.sound.midi.MidiUnavailableException;
import	javax.sound.midi.MidiDevice;
import	javax.sound.midi.MidiSystem;

import	org.tritonus.midi.device.alsa.AlsaMidiDevice;

import	gnu.getopt.Getopt;



public class MidiInTest
{
	private static boolean	DEBUG = false;



	public static void main(String[] args)
	{
		/*
		 *	Parsing of command-line options takes place...
		 */
		String	strDeviceName = null;
		Getopt	g = new Getopt("MidiInDump", args, "hlsmd:cS:D");
		int	c;
		while ((c = g.getopt()) != -1)
		{
			switch (c)
			{
			case 'h':
				printUsageAndExit();

			case 'l':
				listDevicesAndExit();

			case 'd':
				strDeviceName = g.getOptarg();
				if (DEBUG)
				{
					System.out.println("MidiPlayer.main(): device name: " + strDeviceName);
				}
				break;

			case 'D':
				DEBUG = true;
				break;

			case '?':
				printUsageAndExit();

			default:
				System.out.println("getopt() returned " + c);
				break;
			}
		}
		Transmitter	transmitter = null;
		try
		{
			if (strDeviceName != null)
			{
				MidiDevice.Info	deviceInfo = getMidiDeviceInfo(strDeviceName);
				if (deviceInfo == null)
				{
					System.out.println("Cannot find device " + strDeviceName);
					System.exit(1);
				}
				MidiDevice	device = MidiSystem.getMidiDevice(deviceInfo);
				device.open();
				transmitter = device.getTransmitter();
			}
			else
			{
				transmitter = MidiSystem.getTransmitter();
			}
		}
		catch (MidiUnavailableException e)
		{
			e.printStackTrace();
			System.exit(1);
		}


		Receiver	receiver = new DumpReceiver(System.out);
		transmitter.setReceiver(receiver);
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
	}


	private static void printUsageAndExit()
	{
		System.out.println("MidiPlayer: usage:");
		System.out.println("\tjava MidiPlayer [-s] [-m] [-d] <midifile>");
		System.out.println("\t-s\tplay on the internal synthesizer");
		System.out.println("\t-m\tplay on the MIDI port");
		System.out.println("\t-d\tdump on the console");
		System.out.println("All options may be used together.");
		System.out.println("No option is equal to giving -s.");
		System.exit(1);
	}



	private static void listDevicesAndExit()
	{
		System.out.println("Available MIDI Devices:");
		MidiDevice.Info[]	aInfos = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < aInfos.length; i++)
		{
			System.out.println(aInfos[i].getName());
		}
		if (aInfos.length == 0)
		{
			System.out.println("[No devices available]");
		}
		System.exit(1);
	}



	/*
	 *	This method tries to return a MidiDevice.Info whose name
	 *	matches the passed name. If no matching MidiDevice.Info is
	 *	found, null is returned.
	 */
	private static MidiDevice.Info getMidiDeviceInfo(String strDeviceName)
	{
		MidiDevice.Info[]	aInfos = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < aInfos.length; i++)
		{
			if (aInfos[i].getName().equals(strDeviceName))
			{
				return aInfos[i];
			}
		}
		return null;
	}
}



/*** MidiInTest.java ***/
