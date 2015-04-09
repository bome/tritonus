/*
 *	EsdMixerProvider.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 - 2002 by Matthias Pfisterer
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

package org.tritonus.sampled.mixer.esd;

import org.tritonus.lowlevel.esd.Esd;
import org.tritonus.share.TDebug;
import org.tritonus.share.sampled.mixer.TMixerProvider;



public class EsdMixerProvider
extends	TMixerProvider
{
	private static boolean	sm_bInitialized = false;



	public EsdMixerProvider()
	{
		super();
		if (TDebug.TraceMixerProvider) { TDebug.out("EsdMixerProvider.<init>(): begin"); }
		if (! sm_bInitialized && ! isDisabled())
		{
			/// TODO: adapt!
			if (! Esd.isLibraryAvailable())
			{
				disable();
			}
			else
			{
				staticInit();
				sm_bInitialized = true;
			}
		}
		else
		{
			if (TDebug.TraceMixerProvider) { TDebug.out("EsdMixerProvider.<init>(): already initialized or disabled"); }
		}

		if (TDebug.TraceMixerProvider) { TDebug.out("EsdMixerProvider.<init>(): end"); }
	}



	protected void staticInit()
	{
		if (TDebug.TraceMixerProvider) { TDebug.out("EsdMixerProvider.staticInit(): begin"); }
		addMixer(new EsdMixer());
		if (TDebug.TraceMixerProvider) { TDebug.out("EsdMixerProvider.staticInit(): end"); }
	}
}



/*** EsdMixerProvider.java ***/
