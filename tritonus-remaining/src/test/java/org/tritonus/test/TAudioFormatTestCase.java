/*
 *	TAudioFormatTestCase.java
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

package org.tritonus.test;

import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;

import org.junit.jupiter.api.Test;
import org.tritonus.share.sampled.TAudioFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;



public class TAudioFormatTestCase
{
    @Test
	public void testEmptyMap()
	{
		Map<String, Object> prop = new HashMap<String, Object>();
		TAudioFormat format = new TAudioFormat(AudioFormat.Encoding.PCM_SIGNED,
											   44100.0F, 16, 2, 4, 44100.0F,
											   true, prop);
		Map<String, Object> propReturn = format.properties();
		assertTrue(propReturn.isEmpty());
		Object result = propReturn.get("bitrate");
		assertNull(result);
	}



    @Test
	public void testCopying()
	{
		Map<String, Object> prop = new HashMap<String, Object>();
		prop.put("bitrate", new Float(22.5F));
		TAudioFormat format = new TAudioFormat(AudioFormat.Encoding.PCM_SIGNED,
											   44100.0F, 16, 2, 4, 44100.0F,
											   true, prop);
		Map<String, Object> propReturn = format.properties();
		assertTrue(prop != propReturn);
		prop.put("bitrate", new Float(42.5F));
		Object result = propReturn.get("bitrate");
		assertEquals(new Float(22.5F), result);
	}


    @Test
	public void testUnmodifiable()
	{
		Map<String, Object> prop = new HashMap<String, Object>();
		TAudioFormat format = new TAudioFormat(AudioFormat.Encoding.PCM_SIGNED,
											   44100.0F, 16, 2, 4, 44100.0F,
											   true, prop);
		Map<String, Object> propReturn = format.properties();
		try
		{
			propReturn.put("author", "Matthias Pfisterer");
			fail("returned Map allows modifications");
		}
		catch (UnsupportedOperationException e)
		{
		}
	}


    @Test
	public void testGet()
	{
		Map<String, Object> prop = new HashMap<String, Object>();
		prop.put("bitrate", new Float(22.5F));
		prop.put("author", "Matthias Pfisterer");
		TAudioFormat format = new TAudioFormat(AudioFormat.Encoding.PCM_SIGNED,
											   44100.0F, 16, 2, 4, 44100.0F,
											   true, prop);
		Map<String, Object> propReturn = format.properties();
		assertEquals(new Float(22.5F), propReturn.get("bitrate"));
		assertEquals("Matthias Pfisterer", propReturn.get("author"));
	}
}



/*** TAudioFormatTestCase.java ***/
