/*
 *	Clip.java
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

package javax.sound.sampled;

import java.io.IOException;



public interface Clip
extends DataLine
{
	// TODO: check value against sun implementation
	public static final int	LOOP_CONTINUOUSLY = -1;


	public void open(AudioFormat audioFormat,
			 byte[] abData,
			 int nOffset,
			 int nBufferSize)
		throws LineUnavailableException;



	public void open(AudioInputStream audioInputStream)
		throws LineUnavailableException, IOException;


	public int getFrameLength();


	public long getMicrosecondLength();


	public void setFramePosition(int nFrames);


	public void setMicrosecondPosition(long lMicroseconds);



	public void setLoopPoints(int nStart, int nEnd);


	public void loop(int nCount);
}



/*** Clip.java ***/

