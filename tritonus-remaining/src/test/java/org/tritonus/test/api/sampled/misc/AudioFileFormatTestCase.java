/*
 *	AudioFileFormatTestCase.java
 */

/*
 *  Copyright (c) 2003 - 2004 by Matthias Pfisterer
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

import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
/*
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
*/

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class AudioFileFormatTestCase
{
    @Test
	public void testNoMap()
	{
		AudioFileFormat fileFormat = new AudioFileFormat(null, null, 0);
		Map<String, Object> propReturn = fileFormat.properties();
		assertNotNull(propReturn);
		assertTrue(propReturn.isEmpty());
		Object result = propReturn.get("bitrate");
		assertNull(result);
	}


    @Test
	public void testNullMap()
	{
        assertThrows(NullPointerException.class, () -> {
            new AudioFileFormat(null, null, 0, null);
        });
	}


    @Test
	public void testEmptyMap()
	{
		Map<String, Object> prop = new HashMap<String, Object>();
		AudioFileFormat fileFormat = new AudioFileFormat(null, null, 0,
														 prop);
		Map<String, Object> propReturn = fileFormat.properties();
		assertTrue(propReturn.isEmpty());
		Object result = propReturn.get("bitrate");
		assertNull(result);
	}



    @Test
	public void testCopying()
	{
		Map<String, Object> prop = new HashMap<String, Object>();
		prop.put("bitrate", new Float(22.5F));
		AudioFileFormat fileFormat = new AudioFileFormat(null, null, 0,
														 prop);
		Map<String, Object> propReturn = fileFormat.properties();
		assertTrue(prop != propReturn);
		prop.put("bitrate", new Float(42.5F));
		Object result = propReturn.get("bitrate");
		assertEquals(new Float(22.5F), result);
	}


	public void testUnmodifiable()
	{
		Map<String, Object> prop = new HashMap<String, Object>();
		AudioFileFormat fileFormat = new AudioFileFormat(null, null, 0,
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


	public void testGet()
	{
		Map<String, Object> prop = new HashMap<String, Object>();
		prop.put("bitrate", new Float(22.5F));
		prop.put("author", "Matthias Pfisterer");
		AudioFileFormat fileFormat = new AudioFileFormat(null, null, 0,
														 prop);
		Map<String, Object> propReturn = fileFormat.properties();
		assertEquals(new Float(22.5F), propReturn.get("bitrate"));
		assertEquals("Matthias Pfisterer", propReturn.get("author"));
	}
}



/*** AudioFileFormatTestCase.java ***/
