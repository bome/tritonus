/*
 *	BaseMixerTestCase.java
 */

/*
 *  Copyright (c) 2003 by Matthias Pfisterer
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

package org.tritonus.test.api.sampled.mixer;

import javax.sound.sampled.Mixer;
import javax.sound.sampled.AudioSystem;

/**	Base class for tests of javax.sound.sampled.Mixer.
 */
public abstract class BaseMixerTestCase
{
	/**	Iterate over all available Mixers.
	*/
	protected void checkMixer(Check check)
		throws Exception
	{
		Mixer.Info[] infos = AudioSystem.getMixerInfo();
		for (int i = 0; i < infos.length; i++)
		{
			Mixer mixer = AudioSystem.getMixer(infos[i]);
			System.out.println("testing mixer: " + mixer);
			check.check(mixer);
		}
	}



	/** Get the prefix for error messages (containing the sequencer's name).
	 */
	protected static String getMessagePrefix(Mixer mixer)
	{
		return mixer.getMixerInfo().getName();
	}


	protected interface Check
	{
		public void check(Mixer mixer)
			throws Exception;
	}
}



/*** BaseMixerTestCase.java ***/
