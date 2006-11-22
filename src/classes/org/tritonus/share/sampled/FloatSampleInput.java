/*
 * FloatSampleInput.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2006 by Florian Bomers <http://www.bomers.de>
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 |<---            this code is formatted to fit into 80 columns             --->|
 */
package org.tritonus.share.sampled;

/**
 * Master interface for all classes providing audio data in FloatSampleBuffers.
 * 
 * @author florian
 */
public interface FloatSampleInput {

	/**
	 * Fill the entire buffer with audio data. If fewer samples are read, this
	 * method will use buffer.changeSampleCount() to adjust the size of the
	 * buffer. If no samples can be written to the buffer, the buffer's sample
	 * count will be set to 0.
	 * <p>
	 * The buffer's channel count and sample rate may not be changed by the
	 * implementation of this method.
	 * 
	 * @param buffer the buffer to be filled
	 */
	public void read(FloatSampleBuffer buffer);

	/**
	 * Fill the specified portion of the buffer with the next audio data to be
	 * read. If fewer samples are read, this method will use
	 * buffer.changeSampleCount() to adjust the size of the buffer. If no
	 * samples can be written to the buffer, the buffer's sample count will be
	 * set to <code>offset</code>.
	 * <p>
	 * The buffer's channel count and sample rate may not be changed by the
	 * implementation of this method.
	 * 
	 * @param buffer the buffer to be filled
	 * @param offset the start index, in samples, where to start filling the
	 *            buffer
	 * @param sampleCount the number fo samples to fill into the buffer
	 */
	public void read(FloatSampleBuffer buffer, int offset, int sampleCount);

	/**
	 * Determine if this stream has reached its end. If true, subsequent calls
	 * to read() will return 0-sized buffers.
	 * 
	 * @return true if this stream reached its end.
	 */
	public boolean isDone();

	/**
	 * @return the number of audio channels of the audio data that this stream
	 *         provides. If it can support a variable number of channels, this
	 *         method returns AudioSystem.NOT_SPECIFIED.
	 */
	public int getChannels();

	/**
	 * @return the sample rate of the audio data that this stream provides. If
	 *         it can support different sample rates, this method returns a
	 *         negative number, e.g. AudioSystem.NOT_SPECIFIED.
	 */
	public float getSampleRate();

}
