/*
 *	KaiserWindow.java
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
 * An implementation of the Kaiser window.
 */
public class KaiserWindow implements FIRWindow {
	/**
	 * The alpha parameter of the Kaiser window.
	 */
	private double m_dAlpha;

	/**
	 * Constructor taking alpha.
	 * 
	 * @param dAlpha
	 *            The alpha parameter of the Kaiser window.
	 */
	public KaiserWindow(double dAlpha) {
		m_dAlpha = dAlpha;
	}

	/**
	 * Returns alpha.
	 * 
	 * @return alpha.
	 */
	public double getAlpha() {
		return m_dAlpha;
	}

	/** {@inheritDoc} */
	@Override
	public double[] getWindow(int nOrder) {
		double[] adWindow = new double[nOrder];
		for (int n = 0; n < nOrder; n++) {
			adWindow[n] = Util.I0(getAlpha()
					* Math.sqrt(n * (2.0 * nOrder - n)) / nOrder)
					/ Util.I0(getAlpha());
		}
		return adWindow;
	}
}

/*** KaiserWindow.java ***/
