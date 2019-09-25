/*
 *	PageTestCase.java
 */

/*
 *  Copyright (c) 2005 by Matthias Pfisterer
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

package org.tritonus.test.tritonus.lowlevel.pogg;

import org.junit.jupiter.api.Test;
import org.tritonus.lowlevel.pogg.Page;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**	Tests for classes org.tritonus.lowlevel.pogg.Page.
 */
public class PageTestCase
{
	/* First and last, uncontinued, pos 0, serial 0x04030201,
	   page 0, 1 segment, 1 packet */
	private static final byte[] HEADER1 = new byte[]
	{
		0x4f, 0x67, 0x67, 0x53, 0, 0x06,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x01, 0x02, 0x03, 0x04, 0, 0, 0, 0,
		0x15, (byte) 0xed, (byte) 0xec, (byte) 0x91,
		1, 17
	};


	/* First , uncontinued, pos -1, serial 0x04030201,
	   page 8, 7 segments, 1 packet */
	private static final byte[] HEADER2 = new byte[]
	{
		0x4f, 0x67, 0x67, 0x53, 0, 0x02,
		(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
		(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 
		0x01, 0x02, 0x03, 0x04, 8, 0, 0, 0,
		0x15, (byte) 0xed, (byte) 0xec, (byte) 0x91,
		7, (byte) 255, (byte) 255, (byte) 255, (byte) 255,
		(byte) 255, (byte) 255, 12
	};


    @Test
	public void testSetData()
		throws Exception
	{
		Page p = new Page();
		byte[] abHeader = new byte[1024];
		for (int i = 0; i < abHeader.length; i++)
		{
			abHeader[i] = (byte) (i + 128);
		}
		byte[] abBody = new byte[1024];
		for (int i = 0; i < abBody.length; i++)
		{
			abBody[i] = (byte) i;
		}
		p.setData(abHeader, 0, abHeader.length,
				  abBody, 0, abBody.length);
		checkData(p, "set data test", abHeader, abBody);
	}



    @Test
	public void testSetDataOffset()
		throws Exception
	{
		Page p = new Page();
		byte[] abHeader = new byte[102];
		for (int i = 0; i < abHeader.length; i++)
		{
			abHeader[i] = (byte) (i + 128);
		}
		byte[] abBody = new byte[1024];
		for (int i = 0; i < abBody.length; i++)
		{
			abBody[i] = (byte) i;
		}
		p.setData(abHeader, 12, 88,
				  abBody, 511, 513);
		byte[] abHeaderCompare = new byte[88];
		System.arraycopy(abHeader, 12, abHeaderCompare, 0, 88);
		byte[] abBodyCompare = new byte[513];
		System.arraycopy(abBody, 511, abBodyCompare, 0, 513);
		checkData(p, "set data offset test", abHeaderCompare, abBodyCompare);
	}



    @Test
	public void testHeaderProperties()
		throws Exception
	{
		checkHeaderProperties("header properties test 1", HEADER1,
							  0, false, 1,
							  true, true, 0L,
							  0x04030201, 0);
		checkHeaderProperties("header properties test 2", HEADER2,
							  0, false, 1,
							  true, false, -1L,
							  0x04030201, 8);
	}



	private void checkHeaderProperties(
		String strMessage, byte[] abHeader,
		int nVersionExpected, boolean bContinuedExpected, int nPacketsExpected,
		boolean bBosExpected, boolean bEosExpected, long lGranulePosExpected,
		int nSerialNoExpected, int nPageNoExpected)
		throws Exception
	{
		Page p = new Page();
		byte[] abData = new byte[12];
		p.setData(abHeader, 0, abHeader.length,
				  abData, 0, abData.length);

		assertEquals(nVersionExpected,
					 p.getVersion(), constructErrorMessage(strMessage, "version"));
		assertEquals(bContinuedExpected,
					 p.isContinued(), constructErrorMessage(strMessage, "continued flag"));
		assertEquals(nPacketsExpected,
					 p.getPackets(), constructErrorMessage(strMessage, "packets"));
		assertEquals(bBosExpected,
					 p.isBos(), constructErrorMessage(strMessage, "bos flag"));
		assertEquals(bEosExpected,
					 p.isEos(), constructErrorMessage(strMessage, "eos flag"));
		assertEquals(lGranulePosExpected,
					 p.getGranulePos(), constructErrorMessage(strMessage, "granulepos"));
		assertEquals(nSerialNoExpected,
					 p.getSerialNo(), constructErrorMessage(strMessage, "serialno"));
		assertEquals(nPageNoExpected,
					 p.getPageNo(), constructErrorMessage(strMessage, "pageno"));
	}


	private void checkData(Page p, String strMessage,
						   byte[] abHeaderExpected, byte[] abBodyExpected)
		throws Exception
	{
		assertTrue(equals(abHeaderExpected, p.getHeader()),
				   constructErrorMessage(strMessage, "header content"));
		assertTrue(equals(abBodyExpected, p.getBody()),
				   constructErrorMessage(strMessage, "body content"));
	}



	private static boolean equals(byte[] b1, byte[] b2)
	{
		if (b1 == null && b2 == null)
			return true;
		if (b1 != null)
			return equals(b1, 0, b2, 0, b1.length);
		return false;
	}



	private static boolean equals(byte[] b1, int nOffset1,
						   byte[] b2, int nOffset2,
						   int nLength)
	{
		if (b1 == null && b2 == null)
			return true;
		if (nOffset1 + nLength > b1.length || nOffset2 + nLength > b2.length)
			return false;
		for (int i = 0; i < nLength; i++)
		{
			if (b1[nOffset1 + i] != b2[nOffset2 + i])
				return false;
		}
		return true;
	}



	private static String constructErrorMessage(String s1, String s2)
	{
		return s1 + ": " + s2;
	}
}



/*** PageTestCase.java ***/
