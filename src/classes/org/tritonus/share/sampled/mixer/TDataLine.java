/*
 *	TDataLine.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 - 2004 by Matthias Pfisterer
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

package org.tritonus.share.sampled.mixer;

import java.util.Collection;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;

import org.tritonus.share.TDebug;



/**	Base class for classes implementing DataLine.
 */
public abstract class TDataLine
extends	TLine
implements DataLine
{
	private static final int	DEFAULT_BUFFER_SIZE = 128000;

	private AudioFormat		m_format;
	private int			m_nBufferSize;
	private boolean			m_bRunning;
	// private boolean			m_bActive;




	public TDataLine(TMixer mixer,
			 DataLine.Info info)
	{
		super(mixer,
		      info);
		init(info);
	}



	public TDataLine(TMixer mixer,
					 DataLine.Info info,
					 Collection<Control> controls)
	{
		super(mixer,
		      info,
		      controls);
		init(info);
	}



	// IDEA: extract format and bufsize from info?
	private void init(DataLine.Info info)
	{
		m_format = null;
		m_nBufferSize = AudioSystem.NOT_SPECIFIED;
		setRunning(false);
		// setActive(false);
	}



	// not defined here:
	// public void drain()
	// public void flush()



	public void start()
	{
		if (TDebug.TraceSourceDataLine)
		{
			TDebug.out("TDataLine.start(): called");
		}
		setRunning(true);
	}



	public void stop()
	{
		if (TDebug.TraceSourceDataLine)
		{
			TDebug.out("TDataLine.stop(): called");
		}
		setRunning(false);
	}



	public boolean isRunning()
	{
		return m_bRunning;
	}



	// TODO: recheck
	protected void setRunning(boolean bRunning)
	{
		boolean	bOldValue = isRunning();
		m_bRunning = bRunning;
		if (bOldValue != isRunning())
		{
			if (isRunning())
			{
				startImpl();
				notifyLineEvent(LineEvent.Type.START);
			}
			else
			{
				stopImpl();
				notifyLineEvent(LineEvent.Type.STOP);
			}
		}
	}



	protected void startImpl()
	{
	}



	protected void stopImpl()
	{
	}



	/**
	 *	This implementation returns the status of isRunning().
	 *	Subclasses should overwrite this method if there is more
	 *	precise information about the status of the line available.
	 */
	public boolean isActive()
	{
		return isRunning();
	}


/*
	public boolean isStarted()
	{
		return m_bStarted;
	}
*/

	// TODO: should only ALLOW engaging in data I/O.
	// actual START event should only be sent when line really becomes active
/*
	protected void setStarted(boolean bStarted)
	{
		m_bStarted = bStarted;
		if (!isRunning())
		{
			setActive(false);
		}
	}
*/


	public AudioFormat getFormat()
	{
		return m_format;
	}



	protected void setFormat(AudioFormat format)
	{
		if (TDebug.TraceDataLine)
		{
			TDebug.out("TDataLine.setFormat(): setting: " + format);
		}
		m_format = format;
	}



	public int getBufferSize()
	{
		return m_nBufferSize;
	}



	protected void setBufferSize(int nBufferSize)
	{
		if (TDebug.TraceDataLine)
		{
			TDebug.out("TDataLine.setBufferSize(): setting: " + nBufferSize);
		}
		m_nBufferSize = nBufferSize;
	}



	// not defined here:
	// public int available()



	public int getFramePosition()
	{
		// TODO:
		return -1;
	}



	public long getLongFramePosition()
	{
		// TODO:
		return -1;
	}



	public long getMicrosecondPosition()
	{
		return (long) (getFramePosition() * getFormat().getFrameRate() * 1000000);
	}



	/*
	 *	Has to be overridden to be useful.
	 */
	public float getLevel()
	{
		return AudioSystem.NOT_SPECIFIED;
	}



	protected void checkOpen()
	{
		if (getFormat() == null)
		{
			throw new IllegalStateException("format must be specified");
		}
		if (getBufferSize() == AudioSystem.NOT_SPECIFIED)
		{
			setBufferSize(getDefaultBufferSize());
		}
	}



	protected int getDefaultBufferSize()
	{
		return DEFAULT_BUFFER_SIZE;
	}



	protected void notifyLineEvent(LineEvent.Type type)
	{
		notifyLineEvent(new LineEvent(this, type, getFramePosition()));
	}
}



/*** TDataLine.java ***/

