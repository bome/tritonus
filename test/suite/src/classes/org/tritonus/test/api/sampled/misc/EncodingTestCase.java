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

import junit.framework.TestCase;

import javax.sound.sampled.AudioFormat.Encoding;



public class EncodingTestCase
extends TestCase
{
	public EncodingTestCase(String strName)
	{
		super(strName);
	}



	public void testEquals()
	{
		assertTrue("equals(null)", ! Encoding.ALAW.equals(null));
		assertTrue("equals() for same Encoding",
				   Encoding.ALAW.equals(Encoding.ALAW));
		assertTrue("equals() for different Encodings",
				   ! Encoding.ALAW.equals(Encoding.ULAW));
		String strEncodingName = "my fancy encoding";
		String strOtherEncodingName = "my other fancy encoding";
		Encoding encoding1 = new Encoding(strEncodingName);
		Encoding encoding2 = new Encoding(strEncodingName);
		Encoding encoding3 = new Encoding(strOtherEncodingName);
		assertTrue("equals() for equal custom encodings",
				   encoding1.equals(encoding2));
		assertTrue("equals() for equal custom encodings",
				   encoding2.equals(encoding1));
		assertTrue("equals() for different custom encodings",
				   ! encoding1.equals(encoding3));
		assertTrue("equals() for different custom encodings",
				   ! encoding3.equals(encoding1));
	}


	public void testHashCode()
	{
		assertEquals("hashCode() for multiple invocations",
					 Encoding.ALAW.hashCode(), Encoding.ALAW.hashCode());
		String strEncodingName1 = "my fancy encoding";
		String strEncodingName2 = "my fancy encoding";
		Encoding encoding1 = new Encoding(strEncodingName1);
		Encoding encoding2 = new Encoding(strEncodingName2);
		assertEquals("hashCode() for same custom Encoding",
					 encoding1.hashCode(), encoding2.hashCode());
	}


	public void testToString()
	{
		String strEncodingName = "my fancy encoding";
		Encoding encoding = new Encoding(strEncodingName);
		assertEquals("toString()", strEncodingName, encoding.toString());
	}


	public void testStaticInstances()
	{
		assertEquals("PCM_SIGNED.toString()", "PCM_SIGNED",
					 Encoding.PCM_SIGNED.toString());
		assertEquals("PCM_UNSIGNED.toString()", "PCM_UNSIGNED",
					 Encoding.PCM_UNSIGNED.toString());
		assertEquals("ALAW.toString()", "ALAW",
					 Encoding.ALAW.toString());
		assertEquals("ULAW.toString()", "ULAW",
					 Encoding.ULAW.toString());
	}
}



/*** EncodingTestCase.java ***/
