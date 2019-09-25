/*
 *	TCircularBufferTestCase.java
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

package org.tritonus.test;

import org.junit.jupiter.api.Test;
import org.tritonus.share.TCircularBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



public class TCircularBufferTestCase
{
    @Test
	public void testBufferSize()
	{
		int	nSize = 45678;
		TCircularBuffer	buffer = new TCircularBuffer(
			nSize, false, false, null);
		assertEquals(nSize, buffer.availableWrite(), "buffer size");
		nSize = 0;
		buffer = new TCircularBuffer(
			nSize, false, false, null);
		assertEquals(nSize, buffer.availableWrite(), "buffer size");
	}



    @Test
	public void testAvailable()
	{
		int	nBufferSize = 45678;
		int	nWriteSize1 = nBufferSize / 2;
		int	nWriteSize2 = nBufferSize / 5;
		int	nReadSize1 = nBufferSize / 10;
		int	nReadSize2 = nBufferSize / 3;
		TCircularBuffer	buffer = new TCircularBuffer(
			nBufferSize, true, true, null);
		assertEquals(nBufferSize, buffer.availableWrite(), "availableWrite()");
		assertEquals(0, buffer.availableRead(), "availableRead()");
		buffer.write(new byte[nBufferSize]);
		assertEquals(0, buffer.availableWrite(), "availableWrite()");
		assertEquals(nBufferSize, buffer.availableRead(), "availableRead()");
		buffer.read(new byte[nBufferSize]);
		assertEquals(nBufferSize, buffer.availableWrite(), "availableWrite()");
		assertEquals(0, buffer.availableRead(), "availableRead()");


		buffer.write(new byte[nWriteSize1]);
		assertEquals(nBufferSize - nWriteSize1, buffer.availableWrite(), "availableWrite()");
		assertEquals(nWriteSize1, buffer.availableRead(), "availableRead()");
		buffer.write(new byte[nWriteSize2]);
		assertEquals(nBufferSize - nWriteSize1 - nWriteSize2, buffer.availableWrite(), "availableWrite()");
		assertEquals(nWriteSize1 + nWriteSize2, buffer.availableRead(), "availableRead()");
		buffer.read(new byte[nReadSize1]);
		assertEquals(nBufferSize - nWriteSize1 - nWriteSize2 + nReadSize1, buffer.availableWrite(), "availableWrite()");
		assertEquals(nWriteSize1 + nWriteSize2 - nReadSize1, buffer.availableRead(), "availableRead()");
		buffer.read(new byte[nReadSize2]);
		assertEquals(nBufferSize - nWriteSize1 - nWriteSize2 + nReadSize1 + nReadSize2, buffer.availableWrite(), "availableWrite()");
		assertEquals(nWriteSize1 + nWriteSize2 - nReadSize1 - nReadSize2, buffer.availableRead(), "availableRead()");
	}


    @Test
	public void testReadWrite()
	{
		int	nBufferSize = 8901 * 4;
		int	nResult;
		byte[]	abWriteArray = new byte[nBufferSize];
		byte[]	abReadArray = new byte[nBufferSize];
		TCircularBuffer	buffer = new TCircularBuffer(
			nBufferSize, true, true, null);
		for (int i = 0 ; i < abWriteArray.length; i++)
		{
			abWriteArray[i] = (byte)(i % 256);
		}
		nResult = buffer.write(abWriteArray);
		assertEquals(abWriteArray.length, nResult, "written length");
		nResult = buffer.read(abReadArray);
		assertEquals(abReadArray.length, nResult, "read length");
		assertTrue(Util.compareByteArrays(abReadArray, 0, abWriteArray, 0, abReadArray.length), "data content");

		buffer.write(new byte[nBufferSize / 3]);
		nResult = buffer.write(abWriteArray, nBufferSize / 4, nBufferSize / 2);
		assertEquals(nBufferSize / 2, nResult, "written length");
		buffer.read(new byte[nBufferSize / 3]);
		nResult = buffer.read(abReadArray, 0, nBufferSize / 2);
		assertEquals(nBufferSize / 2, nResult, "read length");
		assertTrue(Util.compareByteArrays(abReadArray, 0, abWriteArray, nBufferSize / 4, nBufferSize / 2), "data content");
	}


    @Test
	public void testTrigger()
	{
		TestTrigger	trigger = new TestTrigger();
		
		int	nBufferSize = 45678;
		TCircularBuffer	buffer = new TCircularBuffer(
			nBufferSize, false, true, trigger);
		buffer.read(new byte[10]);
		assertTrue(trigger.isCalled(), "trigger called");

		trigger.reset();
		buffer.write(new byte[nBufferSize / 3]);
		buffer.read(new byte[nBufferSize / 2]);
		assertTrue(trigger.isCalled(), "trigger called");
	}


    @Test
	public void testClose()
	{
		int	nResult;
		int	nBufferSize = 45678;
		TestTrigger	trigger = new TestTrigger();
		TCircularBuffer	buffer = new TCircularBuffer(
			nBufferSize, true, true, trigger);
		buffer.write(new byte[nBufferSize / 2]);
		assertEquals(nBufferSize / 2, buffer.availableWrite(), "availableWrite()");
		assertEquals(nBufferSize / 2, buffer.availableRead(), "availableRead()");
		buffer.close();
		assertEquals(nBufferSize / 2, buffer.availableWrite(), "availableWrite()");
		assertEquals(nBufferSize / 2, buffer.availableRead(), "availableRead()");
		nResult = buffer.read(new byte[nBufferSize / 2]);
		assertEquals(nBufferSize / 2, nResult, "read length");
		assertEquals(nBufferSize, buffer.availableWrite(), "availableWrite()");
		assertEquals(0, buffer.availableRead(), "availableRead()");
		nResult = buffer.read(new byte[nBufferSize / 2]);
		assertEquals(-1, nResult, "read length");
		assertTrue(!trigger.isCalled(), "trigger invocation");
	}




	private static class TestTrigger
	implements TCircularBuffer.Trigger
	{
		private boolean	m_bCalled = false;


		public void execute()
		{
			m_bCalled = true;
		}


		public boolean isCalled()
		{
			return m_bCalled;
		}



		public void reset()
		{
			m_bCalled = false;
		}
	}
}



/*** TCircularBufferTestCase.java ***/
