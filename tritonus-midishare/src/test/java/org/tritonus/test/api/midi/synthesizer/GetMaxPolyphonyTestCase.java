/*
 *	GetMaxPolyphonyTestCase.java
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

import static org.junit.jupiter.api.Assertions.assertTrue;


/**	Tests for class javax.sound.midi.Synthesizer.
 */
public class GetMaxPolyphonyTestCase
extends BaseSynthesizerTestCase
{
	protected void checkSynthesizer(Synthesizer synth)
		throws Exception
	{
		int poly;
		synth.open();
		try
		{
			poly = synth.getMaxPolyphony();
			assertTrue(poly > 0,
					constructErrorMessage(synth, true));
		}
		finally
		{
			synth.close();
		}
	}


	private static String constructErrorMessage(Synthesizer synth,
												boolean bOpen)
	{
		return BaseSynthesizerTestCase.constructErrorMessage(synth,
				"getMaxPolyphony() result not positive", bOpen);
	}
}



/*** GetMaxPolyphonyTestCase.java ***/
