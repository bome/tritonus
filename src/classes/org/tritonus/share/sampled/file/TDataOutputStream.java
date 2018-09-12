/*
 *	TDataOutputStream.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 by Florian Bomers
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

import java.io.DataOutput;
import java.io.IOException;


/**
 * Interface for the file writing classes.
 * <p>Like that it is possible to write to a file without knowing
 * the length before.
 *
 * @author Florian Bomers
 */
public interface TDataOutputStream
extends DataOutput
{
	public boolean supportsSeek();



	public void seek(long position)
		throws IOException;



	public long getFilePointer()
		throws IOException;



	public long length()
		throws IOException;


	public void writeLittleEndian32(int value)
		throws IOException;


	public void writeLittleEndian16(short value)
		throws IOException;

	public void close()
		throws IOException;
}



/*** TDataOutputStream.java ***/
