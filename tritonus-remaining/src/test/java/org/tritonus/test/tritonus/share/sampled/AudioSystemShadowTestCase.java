/*
 *	AudioSystemShadowTestCase.java
 */

/*
 *  Copyright (c) 2001 - 2002 by Matthias Pfisterer
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

package org.tritonus.test.tritonus.share.sampled;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.junit.jupiter.api.Test;
import org.tritonus.share.sampled.AudioSystemShadow;
import org.tritonus.share.sampled.file.TDataOutputStream;
import org.tritonus.test.Util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;



public class AudioSystemShadowTestCase
{
    @Test
	public void testGetDataOutputStreamFile()
		throws Exception
	{
		File file = new File("/tmp/dataoutputstream.tmp");
		TDataOutputStream	dataOutputStream = AudioSystemShadow.getDataOutputStream(file);
		checkTDataOutputStream(dataOutputStream, true);
 		byte[]	abResultingData = Util.getByteArrayFromFile(file);
		// Util.dumpByteArray(abResultingData);
		checkTDataOutputStream2(abResultingData);
	}



    @Test
	public void testGetDataOutputStreamOutputStream()
		throws Exception
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		TDataOutputStream	dataOutputStream = AudioSystemShadow.getDataOutputStream(baos);
		checkTDataOutputStream(dataOutputStream, false);
 		byte[]	abResultingData = baos.toByteArray();
		// Util.dumpByteArray(abResultingData);
		checkTDataOutputStream2(abResultingData);
	}



    @Test
	private void checkTDataOutputStream(TDataOutputStream dataOutputStream,
										boolean bSeekable)
		throws Exception
	{
		assertNotNull(dataOutputStream);
		assertEquals(bSeekable, dataOutputStream.supportsSeek(), "seekable");
		dataOutputStream.writeLittleEndian32(0x12345678);
		dataOutputStream.writeLittleEndian16((short) 0x2345);
		dataOutputStream.close();
	}



    @Test
	private void checkTDataOutputStream2(byte[] abResultingData)
		throws Exception
	{
 		byte[]	abExpectedData = new byte[]{0x78, 0x56, 0x34, 0x12, 0x45, 0x23};
 		assertTrue(Util.compareByteArrays(abExpectedData, 0, abResultingData, 0, abExpectedData.length), "data ok");
	}
}



/*** AudioSystemShadowTestCase.java ***/
