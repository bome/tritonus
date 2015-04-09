/*
 *	AudioFileWriter.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 by Matthias Pfisterer
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

package	javax.sound.sampled.spi;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;



public abstract class AudioFileWriter
{
	public abstract AudioFileFormat.Type[] getAudioFileTypes();


	public boolean isFileTypeSupported(AudioFileFormat.Type fileType)
	{
		AudioFileFormat.Type[]	aFileTypes = getAudioFileTypes();
		return isFileTypeSupportedImpl(aFileTypes, fileType);
	}



	public abstract AudioFileFormat.Type[] getAudioFileTypes(AudioInputStream audioInputStream);



	public boolean isFileTypeSupported(
		AudioFileFormat.Type fileType,
		AudioInputStream audioInputStream)
	{
		AudioFileFormat.Type[]	aFileTypes = getAudioFileTypes(audioInputStream);
		return isFileTypeSupportedImpl(aFileTypes, fileType);
	}



	public abstract int write(AudioInputStream audioInputStream,
				  AudioFileFormat.Type fileType,
				  OutputStream outputStream)
		throws IOException;



	public abstract int write(AudioInputStream audioInputStream,
				  AudioFileFormat.Type fileType,
				  File file)
		throws IOException;



	private boolean isFileTypeSupportedImpl(AudioFileFormat.Type[] aFileTypes, AudioFileFormat.Type fileType)
	{
		for (int i = 0; i < aFileTypes.length; i++)
		{
			if (aFileTypes[i].equals(fileType))
			{
				return true;
			}
		}
		return false;
	}
}



/*** AudioFileWriter.java ***/
