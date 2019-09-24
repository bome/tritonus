/*
 *	CddaMidLevel.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2001 by Matthias Pfisterer
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

package org.tritonus.lowlevel.cdda;

import java.io.InputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.sound.sampled.AudioInputStream;



/**	Mid-level interface definition for reading CDs
 */
public interface CddaMidLevel
{
	/**	Size of a cdda frame in bytes.
	 */
	public static final int		FRAME_SIZE = 2352;


	// TODO: document!!
	/**	Gives the available CDROM devices.
		The returned iteration should contain a list of Strings.
		Each String represents an internal name of a CDROM drive.
		This String should be considered implementation-specific.
		It may contain no useful information (however, most
		time it does). Currently, it is required that the
		String starts with exactely one '/'.
		Should only those drives returned that have an audio CD in?
	 */
	public Iterator getDevices();


	/**	Gives the default drive.
		A String should be returned that represents the default drive.
		The String has to follow the conventions described in
		getDevices(). The String returned by this method should also
		appear as one of the elements in the iteration returned
		by getDevices().
	 */
	public String getDefaultDevice();


	public InputStream getTocAsXml(String strDevice)
		throws IOException;

	public AudioInputStream getTrack(String strDevice, int nTrack)
		throws IOException;
}



/*** CddaMidLevel.java ***/
