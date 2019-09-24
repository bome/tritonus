/*
 *	TSeekableDataOutputStreamTestCase.java
 */

/*
 *  Copyright (c) 2004 by Matthias Pfisterer
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

package org.tritonus.test.tritonus.share.sampled.file;

import java.io.File;

import org.tritonus.share.sampled.file.TSeekableDataOutputStream;
import org.tritonus.share.sampled.file.TDataOutputStream;

import org.tritonus.test.Util;



public class TSeekableDataOutputStreamTestCase
extends BaseDataOutputStreamTestCase
{
	private File	m_file;


	public TSeekableDataOutputStreamTestCase()
	{
		super(true); // seekable
	}



	protected TDataOutputStream createDataOutputStream()
		throws Exception
	{
		m_file = new File("/tmp/dataoutputstream.tmp");
		return new TSeekableDataOutputStream(m_file);
	}


	protected byte[] getWrittenData()
		throws Exception
	{
		return Util.getByteArrayFromFile(m_file);
	}
}



/*** TSeekableDataOutputStreamTestCase.java ***/
