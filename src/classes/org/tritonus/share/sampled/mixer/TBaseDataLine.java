/*
 *	TBaseDataLine.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 - 2004 by Matthias Pfisterer
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

package org.tritonus.share.sampled.mixer;

import java.util.Collection;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;

import org.tritonus.share.TDebug;



/**	Base class for implementing SourceDataLine or TargetDataLine.
 */
public abstract class TBaseDataLine
extends	TDataLine
{
	public TBaseDataLine(TMixer mixer,
			     DataLine.Info info)
	{
		super(mixer,
		      info);
	}



	public TBaseDataLine(TMixer mixer,
						 DataLine.Info info,
						 Collection<Control> controls)
	{
		super(mixer,
		      info,
		      controls);
	}



	public void open(AudioFormat format, int nBufferSize)
		throws LineUnavailableException
	{
		if (TDebug.TraceDataLine) { TDebug.out("TBaseDataLine.open(AudioFormat, int): called with buffer size: " + nBufferSize); }
		setBufferSize(nBufferSize);
		open(format);
	}



	public void open(AudioFormat format)
		throws LineUnavailableException
	{
		if (TDebug.TraceDataLine) { TDebug.out("TBaseDataLine.open(AudioFormat): called"); }
		setFormat(format);
		open();
	}


	// IDEA: move to TDataLine or TLine?
	// necessary and wise at all?
	protected void finalize()
	{
		if (isOpen())
		{
			close();
		}
	}
}



/*** TBaseDataLine.java ***/

