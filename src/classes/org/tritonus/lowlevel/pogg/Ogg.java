/*
 *	Ogg.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2000 - 2001 by Matthias Pfisterer
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

package org.tritonus.lowlevel.pogg;

import org.tritonus.share.TDebug;


/**	libogg loading.
 */
public class Ogg
{
	private static boolean	sm_bIsLibraryAvailable = false;



	static
	{
		Ogg.loadNativeLibrary();
	}



	public static void loadNativeLibrary()
	{
		if (TDebug.TraceOggNative) { TDebug.out("Ogg.loadNativeLibrary(): begin"); }

		if (! isLibraryAvailable())
		{
			loadNativeLibraryImpl();
		}
		if (TDebug.TraceOggNative) { TDebug.out("Ogg.loadNativeLibrary(): end"); }
	}



	/** Load the native library for ogg vorbis.

	This method actually does the loading of the library.  Unlike
	{@link loadNativeLibrary() loadNativeLibrary()}, it does not
	check if the library is already loaded.

	 */
	private static void loadNativeLibraryImpl()
	{
		if (TDebug.TraceOggNative) { TDebug.out("Ogg.loadNativeLibraryImpl(): loading native library tritonuspvorbis"); }
		try
		{
			System.loadLibrary("tritonuspvorbis");
			// only reached if no exception occures
			sm_bIsLibraryAvailable = true;
		}
		catch (Error e)
		{
			if (TDebug.TraceOggNative ||
			    TDebug.TraceAllExceptions)
			{
				TDebug.out(e);
			}
			// throw e;
		}
		if (TDebug.TraceOggNative) { TDebug.out("Ogg.loadNativeLibraryImpl(): loaded"); }
	}



	/**	Returns whether the libraries are installed correctly.
	 */
	public static boolean isLibraryAvailable()
	{
		return sm_bIsLibraryAvailable;
	}
}



/*** Ogg.java ***/
