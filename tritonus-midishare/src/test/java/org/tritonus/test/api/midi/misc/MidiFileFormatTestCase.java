/*
 *	MidiFileFormatTestCase.java
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

package org.tritonus.test.api.midi.misc;

import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiFileFormat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;



public class MidiFileFormatTestCase
{
	private static final float DELTA = 1E-9F;


    @Test
	public void testGetValues()
	{
		checkGetValues(0, 0.0F, 0, 0, 0L, false);
		checkGetValues(0, 0.0F, 0, 0, 0L, true);
		checkGetValues(2, -1.0F, 25, 725, 600000L, false);
		checkGetValues(2, -1.0F, 25, 725, 600000L, true);
	}


	private void checkGetValues(int nType, float fDivisionType,
								int nResolution, int nByteLength,
								long lMicrosecondLength, boolean bWithMap)
	{
		MidiFileFormat fileFormat;
		if (bWithMap)
		{
			Map<String, Object> prop = new HashMap<>();
			fileFormat = new MidiFileFormat(nType, fDivisionType,
											nResolution, nByteLength,
											lMicrosecondLength, prop);
		}
		else
		{
			fileFormat = new MidiFileFormat(nType, fDivisionType,
											nResolution, nByteLength,
											lMicrosecondLength);
		}
		assertEquals(nType, fileFormat.getType(), "type");
		assertEquals(fDivisionType, fileFormat.getDivisionType(), DELTA, "division type");
		assertEquals(nResolution, fileFormat.getResolution(), "resolution");
		assertEquals(nByteLength, fileFormat.getByteLength(), "byte length");
		assertEquals(lMicrosecondLength, fileFormat.getMicrosecondLength(), "microsecond length");
	}


    @Test
	public void testNoMap()
	{
		MidiFileFormat fileFormat = new MidiFileFormat(0, 0.0F, 0, 0, 0L);
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
    		new MidiFileFormat(0, 0.0F, 0, 0, 0L, null);
        });
	}


    @Test
	public void testEmptyMap()
	{
		Map<String, Object> prop = new HashMap<>();
		MidiFileFormat fileFormat = new MidiFileFormat(0, 0.0F, 0, 0, 0L,
													   prop);
		Map<String, Object> propReturn = fileFormat.properties();
		assertTrue(propReturn.isEmpty());
		Object result = propReturn.get("bitrate");
		assertNull(result);
	}


    @Test
	public void testCopying()
	{
		Map<String, Object> prop = new HashMap<>();
		prop.put("bitrate", new Float(22.5F));
		MidiFileFormat fileFormat = new MidiFileFormat(0, 0, 0, 0, 0,
													   prop);
		Map<String, Object> propReturn = fileFormat.properties();
		assertTrue(prop != propReturn);
		prop.put("bitrate", new Float(42.5F));
		Object result = propReturn.get("bitrate");
		assertEquals(new Float(22.5F), result);
	}


    @Test
	public void testUnmodifiable()
	{
		Map<String, Object> prop = new HashMap<>();
		MidiFileFormat fileFormat = new MidiFileFormat(0, 0.0F, 0, 0, 0L,
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
		Map<String, Object> prop = new HashMap<>();
		prop.put("bitrate", new Float(22.5F));
		prop.put("author", "Matthias Pfisterer");
		MidiFileFormat fileFormat = new MidiFileFormat(0, 0.0F, 0, 0, 0L,
													   prop);
		Map<String, Object> propReturn = fileFormat.properties();
		assertEquals(new Float(22.5F), propReturn.get("bitrate"));
		assertEquals("Matthias Pfisterer", propReturn.get("author"));
	}
}



/*** MidiFileFormatTestCase.java ***/
