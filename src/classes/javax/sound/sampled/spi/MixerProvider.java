/*
 *	MixerProvider.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 by Matthias Pfisterer
 *
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
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package javax.sound.sampled.spi;

import javax.sound.sampled.Mixer;



public abstract class MixerProvider
{
	// TODO: check if jdk uses getMixerInfo() or getMixer() to
	// implement this functionality (and document this officially).
	// $$mp 2003/01/11: bug filed.
	public boolean isMixerSupported(Mixer.Info info)
	{
		return false;
	}


	public abstract Mixer.Info[] getMixerInfo();

	public abstract Mixer getMixer(Mixer.Info info);
}



/*** MixerProvider.java ***/
