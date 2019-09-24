/*
 *	AudioOutputStream.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2000 by Matthias Pfisterer
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

package org.tritonus.share.sampled.file;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;



/**	Represents a one-time writing of audio data to a destination (file or output stream).
 *
 *	This interface is the lowest abstraction level of writing audio data.
 *	Implementations of this interface should, when write() is called, never
 *	do buffering and they should never do format conversions. However,
 *	this interface is intended to abstract the file format (how the
 *	headers and data chunks look like) and the way of writing to the
 *	destination object. (implementation note [non-normative]: the last
 *	should be done by using TDataOutputStream implementing classes).
 *
 *	One reasoning behind this interface was to allow direct, unbuffered
 *	writing	of recorded data.
 *	In JS API 0.90, there was no obvious way for this.
 *	Data have had to be recorded to a buffer, then written to a file
 *	from that buffer.
 *	This gave problems with long recordings, where the main
 *	memory of the machine is not big enough to hold all data. There are
 *	two ways so solve this:
 *
 *	a) Having a special AudioInputStream that fetches its data from a
 *	TargetDataLine. This way, the loop inside the AudioFileWriters reads
 *	directely from the recording line via the special AudioInputStream.
 *	This is the solution Sun adopted for JS 1.0.
 *
 *	b) The other way is to expose a direct interface to the writing of the
 *	audio file with no loop inside it. This is to enable the application
 *	programmer to write the main loop itself, possibly doing some
 *	additional processing inside it. This is the more flexible way.
 *	The drawback is that it requires a new architecture for writing files.
 *
 *	This interface is the central part of a proposal for the second
 *	solution.
 *	The idea is now to use the new structure inside the old one to gain
 *	experience with it before proposing to make it a public interface
 *	(public in the sense that it is part of the javax.sound.sampled
 *	package).
 *
 * @author Matthias Pfisterer
 */
public interface AudioOutputStream
{
	/**
	 * Retrieves the AufioFormat of this AudioOutputStream.
	 */
	public AudioFormat getFormat();


	/**	Gives length of the stream.
	 *	This value is in bytes. It may be AudioSystem.NOT_SPECIFIED
	 *	to express that the length is unknown.
	 */
	public long getLength();



	/**	
	 * Writes a chunk of audio data to the destination (file or output stream).
	 */
	// IDEA: use long?
	public int write(byte[] abData, int nOffset, int nLength)
		throws IOException;



	/**	Closes the stream.
	 *	This does write remaining buffered data to the destination,
	 *	backpatch the header, if necessary, and closes the destination.
	 */
	public void close()
		throws IOException;
}



/*** AudioOutputStream.java ***/
