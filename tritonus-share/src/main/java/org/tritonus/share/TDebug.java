/*
 *	TDebug.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 - 2002 by Matthias Pfisterer
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.share;

import java.io.PrintStream;
import  java.util.StringTokenizer;
import  java.security.AccessControlException;



public class TDebug
{
	public static boolean		SHOW_ACCESS_CONTROL_EXCEPTIONS = false;
	private static final String	PROPERTY_PREFIX = "tritonus.";
	// The stream we output to
	public static PrintStream	m_printStream = System.out;

	private static String indent="";

	// meta-general
	public static boolean	TraceAllExceptions = getBooleanProperty("TraceAllExceptions");
	public static boolean	TraceAllWarnings = getBooleanProperty("TraceAllWarnings");

	// general
	public static boolean	TraceInit = getBooleanProperty("TraceInit");
	public static boolean	TraceCircularBuffer = getBooleanProperty("TraceCircularBuffer");
	public static boolean	TraceService = getBooleanProperty("TraceService");

	// sampled common implementation
	public static boolean	TraceAudioSystem = getBooleanProperty("TraceAudioSystem");
	public static boolean	TraceAudioConfig = getBooleanProperty("TraceAudioConfig");
	public static boolean	TraceAudioInputStream = getBooleanProperty("TraceAudioInputStream");
	public static boolean	TraceMixerProvider = getBooleanProperty("TraceMixerProvider");
	public static boolean	TraceControl = getBooleanProperty("TraceControl");
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

	// sampled specific implementation
	public static boolean	TraceEsdNative = getBooleanProperty("TraceEsdNative");
	public static boolean	TraceEsdStreamNative = getBooleanProperty("TraceEsdStreamNative");
	public static boolean	TraceEsdRecordingStreamNative = getBooleanProperty("TraceEsdRecordingStreamNative");
	public static boolean	TraceAlsaNative = getBooleanProperty("TraceAlsaNative");
	public static boolean	TraceAlsaMixerNative = getBooleanProperty("TraceAlsaMixerNative");
	public static boolean	TraceAlsaPcmNative = getBooleanProperty("TraceAlsaPcmNative");
	public static boolean	TraceMixingAudioInputStream = getBooleanProperty("TraceMixingAudioInputStream");
	public static boolean	TraceOggNative = getBooleanProperty("TraceOggNative");
	public static boolean	TraceVorbisNative = getBooleanProperty("TraceVorbisNative");

	// midi common implementation
	public static boolean	TraceMidiSystem = getBooleanProperty("TraceMidiSystem");
	public static boolean	TraceMidiConfig = getBooleanProperty("TraceMidiConfig");
	public static boolean	TraceMidiDeviceProvider = getBooleanProperty("TraceMidiDeviceProvider");
	public static boolean	TraceSequencer = getBooleanProperty("TraceSequencer");
	public static boolean	TraceSynthesizer = getBooleanProperty("TraceSynthesizer");
	public static boolean	TraceMidiDevice = getBooleanProperty("TraceMidiDevice");

	// midi specific implementation
	public static boolean	TraceAlsaSeq = getBooleanProperty("TraceAlsaSeq");
	public static boolean	TraceAlsaSeqDetails = getBooleanProperty("TraceAlsaSeqDetails");
	public static boolean	TraceAlsaSeqNative = getBooleanProperty("TraceAlsaSeqNative");
	public static boolean	TracePortScan = getBooleanProperty("TracePortScan");
	public static boolean	TraceAlsaMidiIn = getBooleanProperty("TraceAlsaMidiIn");
	public static boolean	TraceAlsaMidiOut = getBooleanProperty("TraceAlsaMidiOut");
	public static boolean	TraceAlsaMidiChannel = getBooleanProperty("TraceAlsaMidiChannel");

	public static boolean	TraceFluidNative = getBooleanProperty("TraceFluidNative");

	// misc
	public static boolean	TraceAlsaCtlNative = getBooleanProperty("TraceAlsaCtlNative");
	public static boolean	TraceCdda = getBooleanProperty("TraceCdda");
	public static boolean	TraceCddaNative = getBooleanProperty("TraceCddaNative");



	// make this method configurable to write to file, write to stderr,...
	public static void out(String strMessage)
	{
		if (strMessage.length()>0 && strMessage.charAt(0)=='<') {
			if (indent.length()>2) {	
				indent=indent.substring(2);
			} else {
				indent="";
			}
		}
		String newMsg=null;
		if (indent!="" && strMessage.indexOf("\n")>=0) {
			newMsg="";
			StringTokenizer tokenizer=new StringTokenizer(strMessage, "\n");
			while (tokenizer.hasMoreTokens()) {
				newMsg+=indent+tokenizer.nextToken()+"\n";
			}
		} else {
			newMsg=indent+strMessage;
		}
		m_printStream.println(newMsg);
		if (strMessage.length()>0 && strMessage.charAt(0)=='>') {
				indent+="  ";
		} 
	}



	public static void out(Throwable throwable)
	{
		throwable.printStackTrace(m_printStream);
	}



	public static void assertion(boolean bAssertion)
	{
		if (!bAssertion)
		{
			throw new AssertException();
		}
	}


	public static class AssertException
	extends RuntimeException
	{
		private static final long serialVersionUID = 1;

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
		catch (AccessControlException e)
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

