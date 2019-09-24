/*
 *	IllegalStateTestCase.java
 */

/*
 *  Copyright (c) 2006 by Matthias Pfisterer
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

package org.tritonus.test.api.midi.synthesizer;

import javax.sound.midi.Synthesizer;

import org.junit.jupiter.api.Assertions;


/**	Tests for class javax.sound.midi.Synthesizer.
 */
public class IllegalStateTestCase
extends BaseSynthesizerTestCase
{
	protected void checkSynthesizer(Synthesizer synth)
		throws Exception
	{
		// Synthesizer is closed
		checkMethods(synth, false);

		// Synthesizer open
		synth.open();

		checkMethods(synth, true);

		// clean up
		synth.close();
	}


	private void checkMethods(Synthesizer synth, boolean bOpen)
		throws Exception
	{
		boolean bExpectingException = false;
		checkMethod(synth, "getMaxPolyphony()", bExpectingException, bOpen);
		checkMethod(synth, "getLatency()", bExpectingException, bOpen);
		checkMethod(synth, "getChannels()", bExpectingException, bOpen);
		checkMethod(synth, "getVoiceStatus()", bExpectingException, bOpen);
		checkMethod(synth, "getDefaultSoundbank()", bExpectingException, bOpen);
		checkMethod(synth, "getAvailableInstruments()", bExpectingException, bOpen);
		checkMethod(synth, "getLoadedInstruments()", bExpectingException, bOpen);
	}


	private void checkMethod(Synthesizer synth, String strMethodName,
							 boolean bExceptionExpected, boolean bOpen)
		throws Exception
	{
		try
		{
			if ("getMaxPolyphony()".equals(strMethodName))
				synth.getMaxPolyphony();
			else if ("getLatency()".equals(strMethodName))
				synth.getLatency();
			else if ("getChannels()".equals(strMethodName))
				synth.getChannels();
			else if ("getVoiceStatus()".equals(strMethodName))
				synth.getVoiceStatus();
			else if ("getDefaultSoundbank()".equals(strMethodName))
				synth.getDefaultSoundbank();
			else if ("getAvailableInstruments()".equals(strMethodName))
				synth.getAvailableInstruments();
			else if ("getLoadedInstruments()".equals(strMethodName))
				synth.getLoadedInstruments();
			else
				throw new RuntimeException("unknown method name");
			if (bExceptionExpected)
			{
				Assertions.fail(constructErrorMessage(synth, strMethodName, bExceptionExpected, bOpen));
			}
		}
		catch (IllegalStateException e)
		{
			if (! bExceptionExpected)
			{
				Assertions.fail(constructErrorMessage(synth, strMethodName, bExceptionExpected, bOpen));
			}
		}
	}


	private static String constructErrorMessage(Synthesizer synth,
												String strMethodName,
												boolean bExceptionExpected,
												boolean bOpen)
	{
		String strMessage = ": IllegalStateException ";
		strMessage += (bExceptionExpected ? "not thrown" : "thrown");
		strMessage += " on " + strMethodName;
		return BaseSynthesizerTestCase.constructErrorMessage(synth,
				strMessage, bOpen);
	}
}



/*** IllegalStateTestCase.java ***/
