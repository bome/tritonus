/*
 *	TAudioInputStreamTestCase.java
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

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.tritonus.share.sampled.convert.TAudioInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;



public class TAudioInputStreamTestCase
{
    @Test
	public void testEmptyMap()
	{
        AudioFormat format = new AudioFormat(44100.0F, 16, 2, true, false);
		Map<String, Object> prop = new HashMap<>();
		TAudioInputStream fileFormat = new TAudioInputStream(
			null, format,
			AudioSystem.NOT_SPECIFIED,
			prop);
		Map<String, Object> propReturn = fileFormat.properties();
		assertTrue(propReturn.isEmpty());
		Object result = propReturn.get("bitrate");
		assertNull(result);
	}



    @Test
	public void testCopying()
	{
        AudioFormat format = new AudioFormat(22.5F, 16, 2, true, false);
		Map<String, Object> prop = new HashMap<>();
		prop.put("bitrate", new Float(22.5F));
		TAudioInputStream fileFormat = new TAudioInputStream(
			null, format,
			AudioSystem.NOT_SPECIFIED,
			prop);
		Map<String, Object> propReturn = fileFormat.properties();
		assertTrue(prop != propReturn);
		prop.put("bitrate", new Float(42.5F));
		Object result = propReturn.get("bitrate");
		// TAudioInputStream.properties() returns Collections.unmodifiableMap
		// which holds the original map object inside the class.
		// thus copying should not be happened.
		if (prop.hashCode() != propReturn.hashCode()) {
		    assertEquals(new Float(22.5F), result);
		} else {
            assertEquals(new Float(42.5F), result);
		}
	}


    @Test
	public void testUnmodifiable()
	{
        AudioFormat format = new AudioFormat(44100.0F, 16, 2, true, false);
		Map<String, Object> prop = new HashMap<>();
		TAudioInputStream fileFormat = new TAudioInputStream(
			null, format,
			AudioSystem.NOT_SPECIFIED,
			prop);
		Map<String, Object> propReturn = fileFormat.properties();
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
        AudioFormat format = new AudioFormat(44100.0F, 16, 2, true, false);
		Map<String, Object> prop = new HashMap<>();
		prop.put("bitrate", new Float(22.5F));
		prop.put("author", "Matthias Pfisterer");
		TAudioInputStream fileFormat = new TAudioInputStream(
			null, format,
			AudioSystem.NOT_SPECIFIED,
			prop);
		Map<String, Object> propReturn = fileFormat.properties();
		assertEquals(new Float(22.5F), propReturn.get("bitrate"));
		assertEquals("Matthias Pfisterer", propReturn.get("author"));
	}
}



/*** TAudioInputStreamTestCase.java ***/
