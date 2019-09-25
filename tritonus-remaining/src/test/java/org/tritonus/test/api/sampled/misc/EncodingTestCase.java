/*
 *	EncodingTestCase.java
 */

/*
 *  Copyright (c) 2003 by Matthias Pfisterer
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

package org.tritonus.test.api.sampled.misc;

import javax.sound.sampled.AudioFormat.Encoding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



public class EncodingTestCase
{
    @Test
	public void testEquals()
	{
		assertTrue(! Encoding.ALAW.equals(null), "equals(null)");
		assertTrue(Encoding.ALAW.equals(Encoding.ALAW),
				   "equals() for same Encoding");
		assertTrue(! Encoding.ALAW.equals(Encoding.ULAW),
				   "equals() for different Encodings");
		String strEncodingName = "my fancy encoding";
		String strOtherEncodingName = "my other fancy encoding";
		Encoding encoding1 = new Encoding(strEncodingName);
		Encoding encoding2 = new Encoding(strEncodingName);
		Encoding encoding3 = new Encoding(strOtherEncodingName);
		assertTrue(encoding1.equals(encoding2),
				   "equals() for equal custom encodings");
		assertTrue(encoding2.equals(encoding1),
				   "equals() for equal custom encodings");
		assertTrue(! encoding1.equals(encoding3),
				   "equals() for different custom encodings");
		assertTrue(! encoding3.equals(encoding1),
				   "equals() for different custom encodings");
	}


    @Test
	public void testHashCode()
	{
		assertEquals(Encoding.ALAW.hashCode(), Encoding.ALAW.hashCode(), "hashCode() for multiple invocations");
		String strEncodingName1 = "my fancy encoding";
		String strEncodingName2 = "my fancy encoding";
		Encoding encoding1 = new Encoding(strEncodingName1);
		Encoding encoding2 = new Encoding(strEncodingName2);
		assertEquals(encoding1.hashCode(), encoding2.hashCode(), "hashCode() for same custom Encoding");
	}


    @Test
	public void testToString()
	{
		String strEncodingName = "my fancy encoding";
		Encoding encoding = new Encoding(strEncodingName);
		assertEquals(strEncodingName, encoding.toString(), "toString()");
	}


    @Test
	public void testStaticInstances()
	{
		assertEquals("PCM_SIGNED",
					 Encoding.PCM_SIGNED.toString(), "PCM_SIGNED.toString()");
		assertEquals("PCM_UNSIGNED",
					 Encoding.PCM_UNSIGNED.toString(), "PCM_UNSIGNED.toString()");
		assertEquals("ALAW",
					 Encoding.ALAW.toString(), "ALAW.toString()");
		assertEquals("ULAW",
					 Encoding.ULAW.toString(), "ULAW.toString()");
	}
}



/*** EncodingTestCase.java ***/
