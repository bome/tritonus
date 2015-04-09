/*
 *	GSMAudioFileWriter.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2000 by Florian Bomers <http://www.bomers.de>
 *  Copyright (c) 2000 by Matthias Pfisterer
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

package org.tritonus.sampled.file.gsm;

import java.util.Arrays;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;

import org.tritonus.share.TDebug;
import org.tritonus.share.sampled.file.THeaderlessAudioFileWriter;



/**	Class for writing GSM streams
 *
 * @author Florian Bomers
 * @author Matthias Pfisterer
 */
public class GSMAudioFileWriter
extends THeaderlessAudioFileWriter
{
	private static final AudioFileFormat.Type[]	FILE_TYPES =
	{
		new AudioFileFormat.Type("GSM", "gsm")
	};

	private static final AudioFormat[]	AUDIO_FORMATS =
	{
		new AudioFormat(new AudioFormat.Encoding("GSM0610"), 8000.0F, ALL, 1, 33, 50.0F, false),
		new AudioFormat(new AudioFormat.Encoding("GSM0610"), 8000.0F, ALL, 1, 33, 50.0F, true),
	};



	public GSMAudioFileWriter()
	{
		super(Arrays.asList(FILE_TYPES),
		      Arrays.asList(AUDIO_FORMATS));
		if (TDebug.TraceAudioFileWriter) { TDebug.out("GSMAudioFileWriter.<init>(): begin"); }
		if (TDebug.TraceAudioFileWriter) { TDebug.out("GSMAudioFileWriter.<init>(): end"); }
	}
}



/*** GSMAudioFileWriter.java ***/
