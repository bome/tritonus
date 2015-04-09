/*
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
 * Description of a direct form Finite Impulse Response (FIR) filter.
 */
public class FIRDirectFormFilterDescription implements FilterDescription {
	/**
	 * The filter coefficients.
	 */
	private float[] m_afCoefficients;

	/**
	 * Constructor with filter coefficients.
	 * 
	 * @param afCoefficients
	 *            The array of filter coefficients
	 */
	public FIRDirectFormFilterDescription(float[] afCoefficients) {
		m_afCoefficients = new float[afCoefficients.length];
		System.arraycopy(afCoefficients, 0, m_afCoefficients, 0,
				afCoefficients.length);
	}

	public float[] getCoefficients()
	{
		return m_afCoefficients;
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
	private int getLength() {
		return m_afCoefficients.length;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getFrequencyResponse(double dOmega) {
		double dReal = 0.0;
		double dImag = 0.0;
		for (int i = 0; i < getLength(); i++) {
			dReal += m_afCoefficients[i] * Math.cos(i * dOmega);
			dImag += m_afCoefficients[i] * Math.sin(i * dOmega);
		}
		double dResult = Math.sqrt(dReal * dReal + dImag * dImag);
		return dResult;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getPhaseResponse(double dOmega) {
		double dReal = 0.0;
		double dImag = 0.0;
		for (int i = 0; i < getLength(); i++) {
			dReal += m_afCoefficients[i] * Math.cos(i * dOmega);
			dImag += m_afCoefficients[i] * Math.sin(i * dOmega);
		}
		double dResult = Math.atan2(dImag, dReal);
		return dResult;
	}
}
