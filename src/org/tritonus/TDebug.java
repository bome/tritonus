/*
 *	TDebug.java
 */

/*
 *  Copyright (c) 1999, 2000 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
 *
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
 *
 */


package	org.tritonus;

import	java.io.PrintStream;



public class TDebug
{
	public static boolean		SHOW_ACCESS_CONTROL_EXCEPTIONS = false;
	private static final String	PROPERTY_PREFIX = "tritonus.";
	// The stream we output to
	public static PrintStream	m_printStream = System.out;

	// general
	public static boolean	TraceInit = getBooleanProperty("TraceInit");
	public static boolean	TraceCircularBuffer = getBooleanProperty("TraceCircularBuffer");
	public static boolean	TraceService = getBooleanProperty("TraceService");

	// sampled common implementation
	public static boolean	TraceAudioSystem = getBooleanProperty("TraceAudioSystem");
	public static boolean	TraceAudioConfig = getBooleanProperty("TraceAudioConfig");
	public static boolean	TraceAudioInputStream = getBooleanProperty("TraceAudioInputStream");
	public static boolean	TraceMixerProvider = getBooleanProperty("TraceMixerProvider");

	// sampled specific implementation
	public static boolean	TraceAlsaNative = getBooleanProperty("TraceAlsaNative");
	public static boolean	TraceLine = getBooleanProperty("TraceLine");
	public static boolean	TraceDataLine = getBooleanProperty("TraceDataLine");
	public static boolean	TraceMixer = getBooleanProperty("TraceMixer");
	public static boolean	TraceSourceDataLine = getBooleanProperty("TraceSourceDataLine");
	public static boolean	TraceTargetDataLine = getBooleanProperty("TraceTargetDataLine");
	public static boolean	TraceClip = getBooleanProperty("TraceClip");
	public static boolean	TraceAudioFileReader = getBooleanProperty("TraceAudioFileReader");
	public static boolean	TraceAudioFileWriter = getBooleanProperty("TraceAudioFileWriter");
	public static boolean	TraceAudioConverter = getBooleanProperty("TraceAudioConverter");
	public static boolean	TraceAudioOutputStream = getBooleanProperty("TraceAudioOutputStream");

	// midi common implementation
	public static boolean	TraceMidiSystem = getBooleanProperty("TraceMidiSystem");
	public static boolean	TraceMidiConfig = getBooleanProperty("TraceMidiConfig");
	public static boolean	TraceMidiDeviceProvider = getBooleanProperty("TraceMidiDeviceProvider");

	// midi specific implementation
	public static boolean	TraceASequencer = getBooleanProperty("TraceASequencer");
	public static boolean	TraceASequencerDetails = getBooleanProperty("TraceASequencerDetails");
	public static boolean	TracePortScan = getBooleanProperty("TracePortScan");
	// ASequencer0 separate?
	public static boolean	TraceTMidiDevice = getBooleanProperty("TraceTMidiDevice");
	public static boolean	TraceTSequencer = getBooleanProperty("TraceTSequencer");
	public static boolean	TraceAlsaMidiDevice = getBooleanProperty("TraceAlsaMidiDevice");
	public static boolean	TraceAlsaMidiIn = getBooleanProperty("TraceAlsaMidiIn");
	public static boolean	TraceAlsaMidiOut = getBooleanProperty("TraceAlsaMidiOut");
	public static boolean	TraceAlsaSequencer = getBooleanProperty("TraceAlsaSequencer");
	public static boolean	TraceAlsaMidiChannel = getBooleanProperty("TraceAlsaMidiChannel");
	public static boolean	TraceMshMidiDevice = getBooleanProperty("TraceMshMidiDevice");




	// make this method configurable to write to file, write to stderr,...
	public static void out(String strMessage)
	{
		m_printStream.println(strMessage);
	}



	public static void out(Throwable throwable)
	{
		throwable.printStackTrace(m_printStream);
	}



	public static void assert(boolean bAssertion)
	{
		if (!bAssertion)
		{
			throw new AssertException();
		}
	}


	public static class AssertException
		extends		RuntimeException
	{
		public AssertException()
		{
		}



		public AssertException(String sMessage)
		{
			super(sMessage);
		}
	}



	private static boolean getBooleanProperty(String strName)
	{
		String	strPropertyName = PROPERTY_PREFIX + strName;
		String	strValue = "false";
		try
		{
			strValue = System.getProperty(strPropertyName, "false");
		}
		catch (Exception e)
		{
			if (SHOW_ACCESS_CONTROL_EXCEPTIONS)
			{
				out(e);
			}
		}
		// TDebug.out("property: " + strPropertyName + "=" + strValue);
		boolean	bValue = strValue.toLowerCase().equals("true");
		// TDebug.out("bValue: " + bValue);
		return bValue;
	}
}



/*** TDebug.java ***/

