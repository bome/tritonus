/*
 *	MidiMessageTest.java
 */

import	javax.sound.midi.MidiMessage;
import	javax.sound.midi.ShortMessage;
import	javax.sound.midi.MetaMessage;
import	javax.sound.midi.SysexMessage;


public class MidiMessageTest
{
	public static void main(String[] args)
	{
		byte[]		data = new byte[1];
		MidiMessage	m;
		byte[]		t1;
		byte[]		t2;
		int		l1;

		data[0] = 5;
		m = new TestMessage(data);
		t1 = m.getMessage();
		l1 = m.getLength();
		if (t1.length == data.length)
		{
			out("MidiMessage.getMessage() returns length of the array passed to MidiMessage.<init>(byte[])");
		}
		else
		{
			out("MidiMessage.getMessage() returns length different from the array passed to MidiMessage.<init>(byte[])");
		}
		if (l1 == t1.length)
		{
			out("MidiMessage.getLength() returns length of the array returned by MidiMessage.getMessage()");
		}
		else
		{
			out("MidiMessage.getLength() returns length different from the array returned by MidiMessage.getMessage()");
		}
		if (l1 == data.length)
		{
			out("MidiMessage.getLength() returns length of the array passed to MidiMessage.<init>(byte[])");
		}
		else
		{
			out("MidiMessage.getLength() returns length different from the array passed to MidiMessage.<init>(byte[])");
		}
		if (data[0] != t1[0])
		{
			out("MidiMessage.getMessage() returns wrong data; cannot test copying");
		}
		data[0] = 77;
		t2 = m.getMessage();
		if (data[0] == t2[0])
		{
			out("MidiMessage.<init>(byte[]) does not copy");
		}
		else if (t2[0] != 5)
		{
			out("MidiMessage.<init>(byte[]) or MidiMessage.getMessage() do something obscure");
		}
		else
		{
			out("MidiMessage.<init>(byte[]) does copy");
		}


		data[0] = 5;
		m = new TestMessage(data);
		t1 = m.getMessage();
		t1[0] = 88;
		t2 = m.getMessage();
		if (t1 == t2)
		{
			out("MidiMessage.getMessage() returns the same reference on subsequent invocations (indicates not copying)");
		}
		else
		{
			out("MidiMessage.getMessage() returns the different reference on subsequent invocations (indicates copying)");
		}
		if (t1[0] == t2[0])
		{
			out("MidiMessage.getMessage() does not copy");
		}
		else if (t2[0] != 5)
		{
			out("MidiMessage.getMessage() or MidiMessage.getMessage() do something obscure");
		}
		else
		{
			out("MidiMessage.getMessage() does copy");
		}
		out("----------------------------------------");
		ShortMessage	sm = new ShortMessage();
		t1 = sm.getMessage();
		l1 = sm.getLength();
		out("ShortMessage() data: " + t1);
		for (int i = 0; i < t1.length; i++)
		{
			out("" + t1[i]);
		}
		out("ShortMessage().getLength(): " + l1);
		out("ShortMessage().getStatus(): " + sm.getStatus());
		out("ShortMessage().getData1(): " + sm.getData1());
		out("ShortMessage().getData2(): " + sm.getData2());
	}



	public static class TestMessage
		extends MidiMessage
	{
		/*
		  This constructor passes null to the superclass constructor.
		  This can be used to test the behaviour if the message
		  content is not set correctely.
		*/
		public TestMessage()
		{
			super(null);
		}



		/*
		  This constructor passes the passed byte array reference
		  straight ahead to the superclass constructor. This can be
		  used to test if the MidiMessage constructor copies the
		  passed array.
		*/
		public TestMessage(byte[] abData)
		{
			super(abData);
		}



		/*
		  not implemented for now.
		*/
		public Object clone()
		{
			return null;
		}
	}



	/*
	  only for lazy people.
	*/
	private static void out(String strMessage)
	{
		System.out.println(strMessage);
	}
}



/*** MidiMessageTest.java ***/
