/*
 *	EsdSample.java
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

package org.tritonus.lowlevel.esd;



public class EsdSample
extends	Esd
{
	/**	Holds socket fd to EsounD.
	 *	This variable is only used by native code.
	 *	This field is long because on 64 bit architectures, the native
	 *	size of ints may be 64 bit.
	 */
	@SuppressWarnings("unused")
	private long			m_lNativeFd;

	/**	Holds the sample id.
	 *	This variable is only used by native code.
	 *	This field is long because on 64 bit architectures, the native
	 *	size of ints may be 64 bit.
	 */
	@SuppressWarnings("unused")
	private long			m_lNativeId;





	static
	{
		Esd.loadNativeLibrary();
	}



	public EsdSample()
	{
	}


	/**	Opens the connection to esd and starts uploading the sample data.
	 *	After this call, you can upload the sample data with
	 *	calls to write() (multiple calls allowed). Then you can
	 *	play or loop the sample.
	 *	To free native resources, call close() if you're done with
	 *	the sample (not after you've uploaded all data).
	 */
	public native void open(int nFormat, int nSampleRate, int nLength);
//		throws IOException;



	/**	Uploads a piece of data for the sample.
	 *	You have to open a connection to esd with open() before
	 *	calling this method. Multiple calls to this (write())
	 *	method are allowed. The data uploaded in consequtive
	 *	calls are simply concatenated.
	 *
	 *	@return	the number of bytes written
	 */
	public native int write(byte[] abData, int nOffset, int nLength);
		//throws IOException;



	/**	Closes the connection to esd.
	 *	Use only if you are completely done with this sample.
	 */
	public native void close();



	/**	Play the sample once.
	 */
	public native void play();



	/**	Play the sample indefinitely.
	 */
	public native void loop();

	/**	Stop a looping sample at end.
	 */
	public native void stop();

	/**	Stop a playing sample immediately.
	 */
	public native void kill();

	/**	Uncache a sample from the server.
	 *	This call frees resources in the server associated with
	 *	the sample and invalidated the internal id.
	 *	Calls to play() and loop() are no longer allowed after
	 *	return from this method.
	 */
	public native void free();

	/**	Sets the volume for the sample.
	 *
	 */
	public native void setVolume(int nLeftVolume, int nRightVolume);
}



/*** EsdSample.java ***/
