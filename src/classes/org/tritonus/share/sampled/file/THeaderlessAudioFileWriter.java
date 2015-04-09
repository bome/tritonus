/*
 *	THeaderlessAudioFileWriter.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2000 by Florian Bomers <http://www.bomers.de>
 *  Copyright (c) 2000 - 2002 by Matthias Pfisterer
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

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.share.sampled.file;

import java.io.IOException;
import java.util.Collection;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;

import org.tritonus.share.TDebug;



/**	Base class for formats without extra header.
	This AudioFileWriter is typically used for compressed formats
	where the encoder puts a header into the encoded stream. In this
	case, the AudioFileWriter needs not to add a header. This is why
	THeaderlessAudioOutputStream is used here.

	@author Florian Bomers
	@author Matthias Pfisterer
*/
public class THeaderlessAudioFileWriter
extends TAudioFileWriter
{
	protected THeaderlessAudioFileWriter(Collection<AudioFileFormat.Type> fileTypes,
										 Collection<AudioFormat> audioFormats)
	{
		super(fileTypes, audioFormats);
		if (TDebug.TraceAudioFileWriter) { TDebug.out("THeaderlessAudioFileWriter.<init>(): begin"); }
		if (TDebug.TraceAudioFileWriter) { TDebug.out("THeaderlessAudioFileWriter.<init>(): end"); }
	}



	@Override
	protected AudioOutputStream getAudioOutputStream(
		AudioFormat audioFormat,
		long lLengthInBytes,
		AudioFileFormat.Type fileType,
		TDataOutputStream dataOutputStream)
		throws IOException
	{
		if (TDebug.TraceAudioFileWriter) { TDebug.out("THeaderlessAudioFileWriter.getAudioOutputStream(): begin"); }
		AudioOutputStream	aos = new HeaderlessAudioOutputStream(
			audioFormat,
			lLengthInBytes,
			dataOutputStream);
		if (TDebug.TraceAudioFileWriter) { TDebug.out("THeaderlessAudioFileWriter.getAudioOutputStream(): end"); }
		return aos;
	}

}



/*** THeaderlessAudioFileWriter.java ***/
