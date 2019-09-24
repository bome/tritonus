/*
 *	WrongSoundbankTestCase.java
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

import javax.sound.midi.Instrument;
import javax.sound.midi.Patch;
import javax.sound.midi.Soundbank;
import javax.sound.midi.SoundbankResource;
import javax.sound.midi.Synthesizer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


/**	Test for javax.sound.midi.Synthesizer.getLatency().
 */
public class WrongSoundbankTestCase
extends BaseSynthesizerTestCase
{
	protected void checkSynthesizer(Synthesizer synth)
		throws Exception
	{
		WrongSoundbank sb = new WrongSoundbank();
		Instrument instr = sb.new WrongInstrument();
		Patch[] patchlist = new Patch[1];
		patchlist[0] = new Patch(0, 0);

		synth.open();
		boolean bOpen = true;
		try
		{
			assertTrue(! synth.isSoundbankSupported(sb),
					constructErrorMessage(synth, "isSoundbankSupported() result wrong", true));

			try
			{
				synth.loadInstrument(instr);
				fail(constructErrorMessage(synth, "loadInstrument()",
						bOpen));
			}
			catch (IllegalArgumentException e)
			{
				// We expect this exception.
			}

			try
			{
				synth.unloadInstrument(instr);
				fail(constructErrorMessage(synth, "unloadInstrument()",
						bOpen));
			}
			catch (IllegalArgumentException e)
			{
				// We expect this exception.
			}

			try
			{
				synth.remapInstrument(instr, instr);
				fail(constructErrorMessage(synth, "remapInstrument()",
						bOpen));
			}
			catch (IllegalArgumentException e)
			{
				// We expect this exception.
			}

			try
			{
				synth.loadAllInstruments(sb);
				fail(constructErrorMessage(synth, "loadAllInstruments()",
						bOpen));
			}
			catch (IllegalArgumentException e)
			{
				// We expect this exception.
			}

			try
			{
				synth.unloadAllInstruments(sb);
				fail(constructErrorMessage(synth, "unloadAllInstruments()",
						bOpen));
			}
			catch (IllegalArgumentException e)
			{
				// We expect this exception.
			}

			try
			{
				synth.loadInstruments(sb, patchlist);
				fail(constructErrorMessage(synth, "loadInstruments()",
						bOpen));
			}
			catch (IllegalArgumentException e)
			{
				// We expect this exception.
			}

			try
			{
				synth.unloadInstruments(sb, patchlist);
				fail(constructErrorMessage(synth, "unloadInstruments()",
						bOpen));
			}
			catch (IllegalArgumentException e)
			{
				// We expect this exception.
			}
		}
		finally
		{
			synth.close();
		}
	}


	protected static String constructErrorMessage(Synthesizer synth,
			String strMethodName,
			boolean bOpen)
	{
		String strMessage = ": " + "IllegalArgumentException not thrown";
		strMessage += " on " + strMethodName;
		return BaseSynthesizerTestCase.constructErrorMessage(synth,
				strMessage, bOpen);
	}

	
	private class WrongSoundbank implements Soundbank
	{
		public class WrongInstrument extends Instrument
		{
			public WrongInstrument()
			{
				super(WrongSoundbank.this, null, null, null);
			}

			public Object getData()
			{
				return null;
			}
		}

		public String getDescription()
		{
			return null;
		}

		public Instrument getInstrument(Patch patch)
		{
			return new WrongInstrument();
		}

		public Instrument[] getInstruments()
		{
			Instrument[] instruments = new Instrument[1];
			instruments[0] = new WrongInstrument();
			return instruments;
		}

		public String getName()
		{
			return null;
		}

		public SoundbankResource[] getResources()
		{
			return null;
		}

		public String getVendor()
		{
			return null;
		}

		public String getVersion()
		{
			return null;
		}
	}
}



/*** WrongSoundbankTestCase.java ***/
