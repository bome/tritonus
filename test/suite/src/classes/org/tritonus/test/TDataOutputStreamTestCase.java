/*
 *	TDataOutputStreamTestCase.java
 */

/*
 *  Copyright (c) 2001 - 2002 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package org.tritonus.test;

import	java.io.ByteArrayOutputStream;
import	java.io.File;

import	junit.framework.TestCase;

import	org.tritonus.share.sampled.file.TDataOutputStream;
import	org.tritonus.share.sampled.file.TSeekableDataOutputStream;
import	org.tritonus.share.sampled.file.TNonSeekableDataOutputStream;



public class TDataOutputStreamTestCase
extends TestCase
{
	public TDataOutputStreamTestCase(String strName)
	{
		super(strName);
	}



	public void testNonSeekableDataOutputStream()
		throws Exception
	{
		ByteArrayOutputStream	baos = new ByteArrayOutputStream();
		TDataOutputStream	dataOutputStream = new TNonSeekableDataOutputStream(baos);
		checkTDataOutputStream(dataOutputStream, false);
 		byte[]	abResultingData = baos.toByteArray();
		checkTDataOutputStream2(abResultingData);
	}



	public void testSeekableDataOutputStream()
		throws Exception
	{
		File	file = new File("/tmp/dataoutputstream.tmp");
		TDataOutputStream	dataOutputStream = new TSeekableDataOutputStream(file);
		checkTDataOutputStream(dataOutputStream, true);
 		byte[]	abResultingData = Util.getByteArrayFromFile(file);
		// Util.dumpByteArray(abResultingData);
		checkTDataOutputStream2(abResultingData);
	}



	private void checkTDataOutputStream(TDataOutputStream dataOutputStream,
					   boolean bSeekability)
		throws Exception
	{
		assertEquals("seekability", bSeekability, dataOutputStream.supportsSeek());
		dataOutputStream.writeLittleEndian32(0x12345678);
		dataOutputStream.writeLittleEndian16((short) 0x2345);
		dataOutputStream.close();
	}



	private void checkTDataOutputStream2(byte[] abResultingData)
		throws Exception
	{
 		byte[]	abExpectedData = new byte[]{0x78, 0x56, 0x34, 0x12, 0x45, 0x23};
 		assertTrue(Util.compareByteArrays(abExpectedData, 0, abResultingData, 0, abExpectedData.length));
	}
}



/*** TDataOutputStreamTestCase.java ***/
