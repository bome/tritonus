/*
 *	TDebug.java
 */

/*
 *  Copyright (c) 1999 - 2002 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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
 */

package	org.tritonus.mmapi;

import	java.io.PrintStream;
import  java.util.StringTokenizer;
import  java.security.AccessControlException;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.sound.sampled.SourceDataLine;

import javax.microedition.media.Manager;
import javax.microedition.media.protocol.DataSource;
import org.tritonus.mmapi.JavaSoundAudioPlayer;
import org.tritonus.mmapi.JavaSoundToneGenerator;
import org.tritonus.mmapi.TControllable;
import org.tritonus.mmapi.TDataSource;
import org.tritonus.mmapi.TPlayer;



public class TDebug
{
	/**	Prefix for property names.
		Properties used here have names like
		"tritonus.TraceXXX".
	*/
	private static final String	PROPERTY_PREFIX = "tritonus.";

	private static boolean		SHOW_ACCESS_CONTROL_EXCEPTIONS = false;

	// The stream we output to
	private static PrintStream	sm_printStream = System.out;

	private static String indent="";

	// general
	public static boolean	TraceAllExceptions = getBooleanProperty("TraceAllExceptions");
	public static boolean	TraceAllWarnings = getBooleanProperty("TraceAllWarnings");


	// mmapi
	public static boolean	TraceManager = getBooleanProperty("TraceManager");
	public static boolean	TraceToneGenerator = getBooleanProperty("TraceToneGenerator");
	public static boolean	TraceControllable = getBooleanProperty("TraceControllable");
	public static boolean	TraceDataSource = getBooleanProperty("TraceDataSource");

	// player
	public static boolean	TracePlayerCommon = getBooleanProperty("TracePlayerCommon");
	public static boolean	TracePlayerStates = getBooleanProperty("TracePlayerStates");
	public static boolean	TracePlayerStateTransitions = getBooleanProperty("TracePlayerStateTransitions");
	public static boolean	TraceSourceDataLine = getBooleanProperty("TraceSourceDataLine");





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
		sm_printStream.println(newMsg);
		if (strMessage.length()>0 && strMessage.charAt(0)=='>') {
				indent+="  ";
		} 
	}



	public static void out(Throwable throwable)
	{
		throwable.printStackTrace(sm_printStream);
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

/**
*/
privileged aspect AJDebug
{
	pointcut allExceptions(): handler(Throwable+);

	pointcut managerCalls(): execution(* Manager.*(..));

	pointcut toneGeneratorConstructors(): execution(JavaSoundToneGenerator.new(..));
	pointcut toneGeneratorCalls(): execution(* JavaSoundToneGenerator.*(..));
	pointcut toneGenerator(): toneGeneratorCalls() || toneGeneratorConstructors()
		|| execution(JavaSoundToneGenerator.ToneThread.new(..))
		|| execution(* JavaSoundToneGenerator.ToneThread.*(..));

	pointcut controllable():
		execution(TControllable.new(..)) ||
		execution(* TControllable.*(..));

	pointcut dataSource():
		execution(DataSource+.new(..)) ||
		execution(* DataSource+.*(..));

	pointcut playerCommon():
		execution(TPlayer+.new(..));

	pointcut playerStates():
		execution(private void TPlayer.setState(int));

	pointcut playerStateTransitions():
		execution(* TPlayer+.realize()) ||
		execution(* TPlayer+.doRealize()) ||
		execution(* TPlayer+.prefetch()) ||
		execution(* TPlayer+.doPrefetch()) ||
		execution(* TPlayer+.start()) ||
		execution(* TPlayer+.doStart()) ||
		execution(* TPlayer+.stop()) ||
		execution(* TPlayer+.doStop()) ||
		execution(* TPlayer+.deallocate()) ||
		execution(* TPlayer+.doDeallocate()) ||
		execution(* TPlayer+.close()) ||
		execution(* TPlayer+.doClose());

	pointcut sourceDataLine():
		call(* SourceDataLine+.*(..));


	// currently not used
	pointcut printVelocity(): execution(* JavaSoundToneGenerator.playTone(..)) && call(JavaSoundToneGenerator.ToneThread.new(..));

	// pointcut tracedCall(): execution(protected void JavaSoundAudioPlayer.doRealize() throws Exception);

	before(): managerCalls()
		{
			if (TDebug.TraceManager)
			{
				TDebug.out("Entering: " + thisJoinPoint);
			}
		}

	after(): managerCalls()
		{
			if (TDebug.TraceManager)
			{
				TDebug.out("Leaving: " + thisJoinPoint);
			}
		}

	before(): toneGenerator()
		{
			if (TDebug.TraceToneGenerator)
			{
				TDebug.out("Entering: " + thisJoinPoint);
			}
		}

	after(): toneGenerator()
		{
			if (TDebug.TraceToneGenerator)
			{
				TDebug.out("Leaving: " + thisJoinPoint);
			}
		}

	before(): controllable()
		{
			if (TDebug.TraceControllable)
			{
				TDebug.out("Entering: " + thisJoinPoint);
			}
		}

	after(): controllable()
		{
			if (TDebug.TraceControllable)
			{
				TDebug.out("Leaving: " + thisJoinPoint);
			}
		}

	before(): dataSource()
		{
			if (TDebug.TraceDataSource)
			{
				TDebug.out("Entering: " + thisJoinPoint);
			}
		}

	after(): dataSource()
		{
			if (TDebug.TraceDataSource)
			{
				TDebug.out("Leaving: " + thisJoinPoint);
			}
		}

	before(): playerCommon()
		{
			if (TDebug.TracePlayerCommon)
			{
				TDebug.out("Entering: " + thisJoinPoint);
			}
		}

	after(): playerCommon()
		{
			if (TDebug.TracePlayerCommon)
			{
				TDebug.out("Leaving: " + thisJoinPoint);
			}
		}

	before(int nState): playerStates() && args(nState)
		{
			if (TDebug.TracePlayerStates)
			{
				TDebug.out("TPlayer.setState(): " + nState);
			}
		}

	before(): playerStateTransitions()
		{
			if (TDebug.TracePlayerStateTransitions)
			{
				TDebug.out("Entering: " + thisJoinPoint);
			}
		}

	after(): playerStateTransitions()
		{
			if (TDebug.TracePlayerStateTransitions)
			{
				TDebug.out("Leaving: " + thisJoinPoint);
			}
		}

	before(): sourceDataLine()
		{
			if (TDebug.TraceSourceDataLine)
			{
				TDebug.out("before: " + thisJoinPoint);
			}
		}

	after(): sourceDataLine()
		{
			if (TDebug.TraceSourceDataLine)
			{
				TDebug.out("after: " + thisJoinPoint);
			}
		}

	Synthesizer around(): call(* MidiSystem.getSynthesizer())
		{
			Synthesizer	s = proceed();
			if (TDebug.TraceToneGenerator)
			{
				TDebug.out("MidiSystem.getSynthesizer() gives:  " + s);
			}
			return s;
		}

// TODO: v gives an error; find out what to do
// 	before(int v): printVelocity() && args(nVelocity)
// 		{
// 			if (TDebug.TraceToneGenerator)
// 			{
// 				TDebug.out("velocity: " + v);
// 			}
// 		}

	before(Throwable t): allExceptions() && args(t)
		{
			if (TDebug.TraceAllExceptions)
			{
				TDebug.out(t);
			}
		}
}


/*** TDebug.java ***/

