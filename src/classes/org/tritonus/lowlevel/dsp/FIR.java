/*
 *	FIR.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2002 by Matthias Pfisterer
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

package org.tritonus.lowlevel.dsp;

/**
 * A Finite Impulse Response (FIR) filter.
 */
public class FIR implements Filter {
	/**
	 * The length of the filter (number of coefficients).
	 */
	private int m_nLength;

	/**
	 * The filter coefficients.
	 */
	private float[] m_afCoefficients;

	/**
	 * The buffer for past input values. This stores the input values needed for
	 * convolution. The buffer is used as a circular buffer.
	 */
	private float[] m_afBuffer;

	/**
	 * The index into m_afBuffer. Since m_afBuffer is used as a circular buffer,
	 * a buffer pointer is needed.
	 */
	private int m_nBufferIndex;

	/**
	 * Constructor with filter coefficients.
	 * 
	 * @param afCoefficients
	 *            The array of filter coefficients
	 */
	public FIR(FIRDirectFormFilterDescription filterDescription) {
		float[] afCoefficients = filterDescription.getCoefficients();
		m_nLength = afCoefficients.length;
		m_afCoefficients = new float[m_nLength];
		System.arraycopy(afCoefficients, 0, m_afCoefficients, 0, m_nLength);
		m_afBuffer = new float[m_nLength];
		m_nBufferIndex = 0;
	}

	/**
	 * Change filter coefficients on the fly.
	 * 
	 * <p>
	 * Note that this method does not allow to change the order of the filter
	 * (by passing a different number of filter coefficients) from the value set
	 * in the constructor.
	 * </p>
	 * 
	 * @param filterDescription
	 *            filter description containing the new coefficients
	 * @throws IllegalArgumentException
	 *             if the number of coefficients is different from the current
	 *             number of coefficients
	 */
	public void setFilterDescription(
			FIRDirectFormFilterDescription filterDescription) {
		float[] afCoefficients = filterDescription.getCoefficients();
		if (afCoefficients.length != m_nLength) {
			throw new IllegalArgumentException("cannot change length of filter");
		}
		System.arraycopy(afCoefficients, 0, m_afCoefficients, 0, m_nLength);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float process(float fInput) {
		m_afBuffer[m_nBufferIndex] = fInput;
		int nBufferIndex = m_nBufferIndex;
		float fOutput = 0.0F;
		for (int i = 0; i < m_nLength; i++) {
			fOutput += m_afCoefficients[i] * m_afBuffer[nBufferIndex];
			nBufferIndex--;
			if (nBufferIndex < 0) {
				nBufferIndex += m_nLength;
			}
		}
		m_nBufferIndex = (m_nBufferIndex + 1) % m_nLength;
		return fOutput;
	}

	/**
	 * Returns the length of the filter. This returns the length of the filter
	 * (the number of coefficients). Note that this is not the same as the order
	 * of the filter. Commonly, the 'order' of a FIR filter is said to be the
	 * number of coefficients minus 1: Since a single coefficient is only an
	 * amplifier/attenuator, this is considered order zero.
	 * 
	 * @return The length of the filter (the number of coefficients).
	 */
	public int getLength() {
		return m_nLength;
	}
}

/*** FIR.java ***/
