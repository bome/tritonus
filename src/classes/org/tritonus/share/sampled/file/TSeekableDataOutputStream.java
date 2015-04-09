/*
 *	TSeekableDataOutputStream.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 by Florian Bomers <http://www.bomers.de>
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

import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;



/**
 * A TDataOutputStream that allows seeking.
 *
 * @author Florian Bomers
 * @author Matthias Pfisterer
 */
public class TSeekableDataOutputStream
extends RandomAccessFile
implements TDataOutputStream
{
	public TSeekableDataOutputStream(File file)
		throws IOException
	{
		super(file, "rw");
	}



	public boolean supportsSeek()
	{
		return true;
	}



	public void writeLittleEndian32(int value)
		throws IOException
	{
		writeByte(value & 0xFF);
    		writeByte((value >> 8) & 0xFF);
    		writeByte((value >> 16) & 0xFF);
	    	writeByte((value >> 24) & 0xFF);
	}



	public void writeLittleEndian16(short value)
		throws IOException
	{
		writeByte(value & 0xFF);
		writeByte((value >> 8) & 0xFF);
	}
}



/*** TSeekableDataOutputStream.java ***/
