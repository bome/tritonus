/*
 *	AudioSystemShadow.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999, 2000 by Matthias Pfisterer
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

package org.tritonus.share.sampled;

import java.io.File;
import java.io.OutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;

import org.tritonus.share.sampled.file.AudioOutputStream;
import org.tritonus.share.sampled.file.TDataOutputStream;
import org.tritonus.share.sampled.file.TSeekableDataOutputStream;
import org.tritonus.share.sampled.file.TNonSeekableDataOutputStream;
import org.tritonus.sampled.file.AiffAudioOutputStream;
import org.tritonus.sampled.file.AuAudioOutputStream;
import org.tritonus.sampled.file.WaveAudioOutputStream;



/**	Experminatal area for AudioSystem.
 *	This class is used to host features that may become part of the
 *	Java Sound API (In which case they will be moved to AudioSystem).
 */
public class AudioSystemShadow
{
	public static TDataOutputStream getDataOutputStream(File file)
		throws IOException
	{
			return new TSeekableDataOutputStream(file);
	}



	public static TDataOutputStream getDataOutputStream(OutputStream stream)
		throws IOException
	{
			return new TNonSeekableDataOutputStream(stream);
	}



	// TODO: lLengthInBytes actually should be lLengthInFrames (design problem of A.O.S.)
	public static AudioOutputStream getAudioOutputStream(AudioFileFormat.Type type, AudioFormat audioFormat, long lLengthInBytes, TDataOutputStream dataOutputStream)
	{
		AudioOutputStream	audioOutputStream = null;

		if (type.equals(AudioFileFormat.Type.AIFF) ||
			type.equals(AudioFileFormat.Type.AIFF))
		{
			audioOutputStream = new AiffAudioOutputStream(audioFormat, type, lLengthInBytes, dataOutputStream);
		}
		else if (type.equals(AudioFileFormat.Type.AU))
		{
			audioOutputStream = new AuAudioOutputStream(audioFormat, lLengthInBytes, dataOutputStream);
		}
		else if (type.equals(AudioFileFormat.Type.WAVE))
		{
			audioOutputStream = new WaveAudioOutputStream(audioFormat, lLengthInBytes, dataOutputStream);
		}
		return audioOutputStream;
	}



	public static AudioOutputStream getAudioOutputStream(AudioFileFormat.Type type, AudioFormat audioFormat, long lLengthInBytes, File file)
		throws IOException
	{
		TDataOutputStream	dataOutputStream = getDataOutputStream(file);
		AudioOutputStream	audioOutputStream = getAudioOutputStream(type, audioFormat, lLengthInBytes, dataOutputStream);
		return audioOutputStream;
	}



	public static AudioOutputStream getAudioOutputStream(AudioFileFormat.Type type, AudioFormat audioFormat, long lLengthInBytes, OutputStream outputStream)
		throws IOException
	{
		TDataOutputStream	dataOutputStream = getDataOutputStream(outputStream);
		AudioOutputStream	audioOutputStream = getAudioOutputStream(type, audioFormat, lLengthInBytes, dataOutputStream);
		return audioOutputStream;
	}
}


/*** AudioSystemShadow.java ***/
