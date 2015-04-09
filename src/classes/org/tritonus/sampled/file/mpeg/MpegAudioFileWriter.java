/*
 *	MpegAudioFileWriter.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2000 by Florian Bomers <http://www.bomers.de>
 *
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
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.sampled.file.mpeg;

import java.util.Arrays;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;

import org.tritonus.share.TDebug;
import org.tritonus.share.sampled.file.THeaderlessAudioFileWriter;



/** Class for writing mpeg files
 *
 * @author Florian Bomers
 */
public class MpegAudioFileWriter extends THeaderlessAudioFileWriter {

	private static final AudioFileFormat.Type[]	FILE_TYPES = {
	    //new AudioFileFormat.Type("MPEG", "mpeg"),
	    // workaround for the fixed extension problem in AudioFileFormat.Type
	    // see org.tritonus.share.sampled.AudioFileTypes.java
	    new AudioFileFormat.Type("MP3", "mp3"),
	    new AudioFileFormat.Type("MP2", "mp2"),
	};

	public static final AudioFormat.Encoding MPEG1L3 = new AudioFormat.Encoding("MPEG1L3");
	public static final AudioFormat.Encoding MPEG2L3 = new AudioFormat.Encoding("MPEG2L3");
	public static final AudioFormat.Encoding MPEG2DOT5L3 = new AudioFormat.Encoding("MPEG2DOT5L3");

	private static final AudioFormat[]	AUDIO_FORMATS = {
	    new AudioFormat(MPEG1L3, ALL, ALL, 1, ALL, ALL, false),
	    new AudioFormat(MPEG1L3, ALL, ALL, 1, ALL, ALL, true),
	    new AudioFormat(MPEG1L3, ALL, ALL, 2, ALL, ALL, false),
	    new AudioFormat(MPEG1L3, ALL, ALL, 2, ALL, ALL, true),
	    new AudioFormat(MPEG2L3, ALL, ALL, 1, ALL, ALL, false),
	    new AudioFormat(MPEG2L3, ALL, ALL, 1, ALL, ALL, true),
	    new AudioFormat(MPEG2L3, ALL, ALL, 2, ALL, ALL, false),
	    new AudioFormat(MPEG2L3, ALL, ALL, 2, ALL, ALL, true),
	    new AudioFormat(MPEG2DOT5L3, ALL, ALL, 1, ALL, ALL, false),
	    new AudioFormat(MPEG2DOT5L3, ALL, ALL, 1, ALL, ALL, true),
	    new AudioFormat(MPEG2DOT5L3, ALL, ALL, 2, ALL, ALL, false),
	    new AudioFormat(MPEG2DOT5L3, ALL, ALL, 2, ALL, ALL, true),
	};

	public MpegAudioFileWriter()
	{
		super(Arrays.asList(FILE_TYPES),
		      Arrays.asList(AUDIO_FORMATS));
		if (TDebug.TraceAudioFileWriter) { TDebug.out("MpegAudioFileWriter.<init>(): begin"); }
		if (TDebug.TraceAudioFileWriter) { TDebug.out("MpegAudioFileWriter.<init>(): end"); }
	}
}


/*** MpegAudioFileWriter.java ***/
