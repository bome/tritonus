/*
 *	Filter.java
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
 * Common interface for all types of filter descriptions.
 * 
 * <p>
 * This interface should be implemented by classes that contain filter
 * descriptions, i.e. containers for a set of filter coefficients.
 * </p>
 */
public interface FilterDescription {

	/**
	 * Get the frequency response of the filter at a specified frequency.
	 * 
	 * <p>
	 * This method calculates the frequency response of the filter for a
	 * specified frequency. Calling this method is allowed at any time, even
	 * while the filter is operating. It does not affect the operation of the
	 * filter.
	 * </p>
	 * 
	 * @param dOmega
	 *            The frequency for which the frequency response should be
	 *            calculated. Has to be given as omega values ([-PI .. +PI]).
	 * @return The calculated frequency response
	 */
	public double getFrequencyResponse(double dOmega);

	/**
	 * Get the phase response of the filter at a specified frequency.
	 * 
	 * <p>
	 * This method calculates the phase response of the filter for a specified
	 * frequency. Calling this method is allowed at any time, even while the
	 * filter is operating. It does not affect the operation of the filter.
	 * </p>
	 * 
	 * @param dOmega
	 *            The frequency for which the phase response should be
	 *            calculated. Has to be given as omega values ([-PI .. +PI]).
	 * @return The calculated phase response
	 */
	public double getPhaseResponse(double dOmega);
}

/*** Filter.java ***/
