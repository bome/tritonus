/*
 *	AudioInputStreamTestCase.java
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

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AudioInputStreamTestCase
{
    @Test
	public void testConstructorNullPointers()
	{
		@SuppressWarnings("unused") AudioInputStream ais = null;
//		InputStream is = new ByteArrayInputStream(new byte[0]);
		AudioFormat format = new AudioFormat(44100.0F, 16, 2, true, false);
		try
		{
			ais = new AudioInputStream(null, format, AudioSystem.NOT_SPECIFIED);
		}
		catch (NullPointerException e)
		{
            Assertions.fail("no NullpointerException thrown for null InputStream");
		}

		// jdk source throws npe when format is null.
//		try
//		{
//			ais = new AudioInputStream(is, null, AudioSystem.NOT_SPECIFIED);
//			Assertions.fail("no NullpointerException thrown for null AudioFormat");
//		}
//		catch (NullPointerException e)
//		{
//		}
	}



// 	public void testLength()
// 	{
// 		Map prop = new HashMap();
// 		prop.put("bitrate", new Float(22.5F));
// 		AudioInputStream format = new AudioInputStream(AudioFormat.Encoding.PCM_SIGNED,
// 											   44100.0F, 16, 2, 4, 44100.0F,
// 											   true, prop);
// 		Map propReturn = format.properties();
// 		assertTrue(prop != propReturn);
// 		prop.put("bitrate", new Float(42.5F));
// 		Object result = propReturn.get("bitrate");
// 		assertEquals(new Float(22.5F), result);
// 	}


// 	public void testUnmodifiable()
// 	{
// 		Map prop = new HashMap();
// 		AudioInputStream format = new AudioInputStream(AudioFormat.Encoding.PCM_SIGNED,
// 											   44100.0F, 16, 2, 4, 44100.0F,
// 											   true, prop);
// 		Map propReturn = format.properties();
// 		try
// 		{
// 			propReturn.put("author", "Matthias Pfisterer");
// 			fail("returned Map allows modifications");
// 		}
// 		catch (UnsupportedOperationException e)
// 		{
// 		}
// 	}


// 	public void testGet()
// 	{
// 		Map prop = new HashMap();
// 		prop.put("bitrate", new Float(22.5F));
// 		prop.put("author", "Matthias Pfisterer");
// 		AudioInputStream format = new AudioInputStream(AudioFormat.Encoding.PCM_SIGNED,
// 											   44100.0F, 16, 2, 4, 44100.0F,
// 											   true, prop);
// 		Map propReturn = format.properties();
// 		assertEquals(new Float(22.5F), propReturn.get("bitrate"));
// 		assertEquals("Matthias Pfisterer", propReturn.get("author"));
// 	}
}



/*** AudioInputStreamTestCase.java ***/
