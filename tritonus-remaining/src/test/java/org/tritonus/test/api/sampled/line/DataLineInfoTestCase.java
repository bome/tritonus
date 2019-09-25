/*
 *	DataLineInfoTestCase.java
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

package org.tritonus.test.api.sampled.line;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.SourceDataLine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



public class DataLineInfoTestCase
{
    @Test
	public void testConstructors()
	{
		DataLine.Info info;
		info = new DataLine.Info(String.class,
								 new AudioFormat[0],
								 123, 456);
		checkInfo(info, String.class, 0, 123, 456);
	}



	private void checkInfo(DataLine.Info info,
						   Class expectedLineClass,
						   int nExpectedFormatsArrayLength,
						   int nExpectedMinBufferSize,
						   int nExpectedMaxBufferSize)
	{
		assertEquals(expectedLineClass, info.getLineClass(), "lineClass");
		assertEquals(nExpectedFormatsArrayLength, info.getFormats().length,
					 "AudioFormat array length");
		assertEquals(nExpectedMinBufferSize, info.getMinBufferSize(),
					 "min buffer size");
		assertEquals(nExpectedMaxBufferSize, info.getMaxBufferSize(),
					 "max buffer size");
	}

    @Test
	public void testMatches()
	{
		DataLine.Info info1 = new DataLine.Info(SourceDataLine.class,
												null);
		Line.Info info2 = new Line.Info(SourceDataLine.class);
		assertTrue(! info1.matches(info2), "DataLine.Info against Line.Info");
		assertTrue(info2.matches(info1), "Line.Info against DataLine.Info");
	}


    @Test
	public void testToString()
	{
	}
}



/*** DataLineInfoTestCase.java ***/
