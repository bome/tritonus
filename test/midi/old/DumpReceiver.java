/*
 *	DumpReceiver.java
 */

import	java.io.PrintStream;

import javax.sound.midi.Receiver;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.SysexMessage;

/**
 *	This is an temporary dummy receiver.
 *	A perfect example for how to obtain MidiMessages.
 * @author    <A HREF="mailto:niels@bonneville.nl">Niels Gorisse</A>, 22 april 1999, Bonneville Music Software
 * @author    <A HREF="mailto:G.Gehnen@atrie.de">Gerrit Gehnen</A>, 11 june 1999
 * @version   0.92
 **/



// IDEA: "debug" mode, which additionaly outputs the hex digits
public class DumpReceiver
	implements	Receiver
{
	private static String[]		sm_asKeyName = {"C", "C#", "D", "D#", "E", "F",
							"F#", "G", "G#", "A", "A#", "B"};

	private PrintStream		m_printStream;
	private boolean			m_bDebug;



	public DumpReceiver(PrintStream printStream)
	{
		m_printStream = printStream;
		// TODO: should be false for production version
		m_bDebug = true;
	}


	public void send(MidiMessage message, long lTimeStamp)
	{
		String	s = null;
		Integer	i = new Integer(0);
		// System.out.println("DumpReceiver.send(): called");
		// m_printStream.println("Class of Message: "+message.getClass().getName());
		// TODO: rework
		if (isDebug())
		{
			// TODO: must be message-specific
			String	d = "";
		}
		String	h = ""; //message.getTick() + ": ";
/*
  m_printStream.println("(DummyReceiver says:) Received a MidiMessage: " + i.toHexString(message.getStatus()) +
  " Type: " + message.getType() +
  //+" "+message.getData1()+" "+message.getData2
  ", Length: " + message.getLength() +
  " at " + message.getTick());
*/
		if (message instanceof ShortMessage)
		{
			ShortMessage	shortMessage = (ShortMessage) message;
			String	sC = "[Channel " + shortMessage.getChannel() + "] ";
			switch (shortMessage.getCommand())
			{
			case 0x80:
				s = "Note Off  Key: " + sC + getKeyName(shortMessage.getData1()) +
					" Velocity: "+shortMessage.getData2();
				break;

			case 0x90:
				s = "Note On Key: " + sC + getKeyName(shortMessage.getData1()) +
					" Velocity: " + shortMessage.getData2();
				break;

			case 0xa0:
				// TODO:
				s = "Polyphonic Key Pressure: " + sC + getKeyName(shortMessage.getData1()) +
					" Pressure: " + shortMessage.getData2();
				break;

			case 0xb0:
				if (shortMessage.getData1() < 120)
				{
					s = "Controller No.: " + sC + shortMessage.getData1() +
						" Value: " + shortMessage.getData2();
				}
				else
				{
					s = "ChannelMode Message No.: " + sC + shortMessage.getData1() +
						" Value: " + shortMessage.getData2();
				}
				break;

			case 0xc0:
				s = "Program Change No: " + sC + shortMessage.getData1();
				break;

			case 0xd0:
				s = "Channel Aftertouch Pressure: " + sC + shortMessage.getData1();
				break;

			case 0xe0:
				s = "Pitch: " + sC + get14bitValue(shortMessage.getData1(), shortMessage.getData2());
				break;

			case 0xF0:
				switch (shortMessage.getChannel())
				{
				case 0x0:
					s = "System Exclusive (should not be in ShortMessage!)";
					break;

				case 0x1:
					s = "Undefined";
					break;

				case 0x2:
					s = "Song Position: " + get14bitValue(shortMessage.getData1(), shortMessage.getData2());
					break;

				case 0x3:
					s = "Song Select: " + shortMessage.getData1();
					break;

				case 0x4:
					s = "Undefined";
					break;

				case 0x5:
					s = "Undefined";
					break;

				case 0x6:
					s = "Tune Request";
					break;

				case 0x7:
					s = "End of SysEx (should not be in ShortMessage!)";
					break;

				case 0x8:
					s = "Timing clock";
					break;

				case 0x9:
					s = "Undefined";
					break;

				case 0xA:
					s = "Start";
					break;

				case 0xB:
					s = "Continue";
					break;

				case 0xC:
					s = "Stop";
					break;

				case 0xD:
					s = "Undefined";
					break;

				case 0xE:
					s = "Active Sensing";
					break;

				case 0xF:
					s = "System Reset";
					break;

				}
				break;
			}
		}
		else if (message instanceof SysexMessage)
		{
			// TODO:
			s = "SysexMessage!";
		}
		else if (message instanceof MetaMessage)
		{
			// TODO:
			s = "MetaMessage!";
		}
		m_printStream.println(h + s);
	}



	public void close()
	{
	}



	public boolean isDebug()
	{
		return m_bDebug;
	}



	public void setDebug(boolean bDebug)
	{
		m_bDebug = bDebug;
	}



	public static String getKeyName(int nKey)
	{
		if (nKey > 127)
		{
			return "illegal value";
		}
		else
		{
			int	nNote = nKey % 12;
			int	nOctave = nKey / 12;
			return sm_asKeyName[nNote] + " " + (nOctave - 1);
		}
	}



	public static int get14bitValue(int nLSB, int nMSB)
	{
		return (nLSB & 0x7F) | ((nMSB & 0x7F) << 7);
	}
}



/*** DumpReceiver.java ***/
