/*
 *	SourceDataLine.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 by Matthias Pfisterer
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

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package javax.sound.sampled;




public interface SourceDataLine
extends DataLine
{
	public void open(AudioFormat audioFormat,
			 int nBufferSize)
		throws LineUnavailableException;


	public void open(AudioFormat audioFormat)
		throws LineUnavailableException;


	/** Write data to the line.

	@param abData The buffer to use.

	@param nOffset

	@param nLength The length of the data that should be written,
	in bytes. Can be less that the length of abData.

	@return The number of bytes written. May be less than nLength.

	*/
	public int write(byte[] abData,
			 int nOffset,
			 int nLength);
}



/*** SourceDataLine.java ***/

