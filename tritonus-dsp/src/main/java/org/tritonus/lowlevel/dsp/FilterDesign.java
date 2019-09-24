/*
 *	FilterDesign.java
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

import org.tritonus.share.TDebug;

/**
 * Several methods to design digital filters. This is a design method for FIR
 * filters.
 */
public class FilterDesign {

	/**
	 * Shared instance of {@link RectangularWindow}.
	 */
	public static final FIRWindow RECTANGULAR_WINDOW = new RectangularWindow();
	/**
	 * Shared instance of {@link HammingWindow}.
	 */
	public static final FIRWindow HAMMING_WINDOW = new HammingWindow();

	private static final boolean DEBUG = false;

	public static FIRDirectFormFilterDescription getFirDirectFormFilterDescription(
			double[] adCoefficients) {
		float[] afCoefficients = new float[adCoefficients.length];
		for (int i = 0; i < adCoefficients.length; i++) {
			afCoefficients[i] = (float) adCoefficients[i];
		}
		return new FIRDirectFormFilterDescription(afCoefficients);
	}

	/**
	 * Filter design by frequency sampling. This is a design method for FIR
	 * filters. It allows to design filters with arbitrary frequency response.
	 */
	public static double[] designFrequencySampling(double[] adFrequencyResponse) {
		int nHalfLength = adFrequencyResponse.length;
		int nFullLength = nHalfLength * 2;
		Complex[] aFrequencyResponse = new Complex[nFullLength];
		// double dScaleFactor = (double) (nFullLength - 1) / (double)
		// nFullLength;
		for (int k = 0; k < nHalfLength; k++) {
			// double dPhase = -Math.PI * k * dScaleFactor;
		}
		// TODO: middle point has to be 0
		// TODO: check loop bounds
		for (int k = nHalfLength; k < nFullLength; k++) {
			// double dPhase = Math.PI - Math.PI * k * dScaleFactor;
		}
		Complex[] aComplexCoefficients = Util.IDFT(aFrequencyResponse);
		double[] aRealCoefficients = new double[nFullLength];
		for (int i = 0; i < nFullLength; i++) {
			aRealCoefficients[i] = aComplexCoefficients[i].real();
			if (DEBUG) {
				TDebug
						.out("FilterDesign.designFrequencySampling(): coefficient, imaginary part: "
								+ aComplexCoefficients[i].imag());
			}
		}
		return aRealCoefficients;
	}

	// /////////////////////////////////////////////////
	//
	// Rectangular Window methods
	//
	// /////////////////////////////////////////////////

	/**
	 * nOrder should be odd.
	 */
	public static double[] designRectangularLowPass(int nOrder,
			double dCornerOmega) {
		double[] adH = new double[nOrder];
		int nMiddle = nOrder / 2;
		for (int n = 0; n < nOrder; n++) {
			int k = (n - nMiddle);
			if (k == 0) {
				adH[n] = dCornerOmega / Math.PI;
			} else {
				double a = dCornerOmega * k;
				double sin = Math.sin(a);
				adH[n] = sin / (Math.PI * k);
			}
		}
		return adH;
	}

	/**
	 * nOrder should be odd.
	 */
	public static double[] designRectangularHighPass(int nOrder,
			double dCornerOmega) {
		double[] adH = new double[nOrder];
		int nMiddle = nOrder / 2;
		for (int n = 0; n < nOrder; n++) {
			adH[n] = 1.0 - Math.sin(dCornerOmega * (n - nMiddle))
					/ (Math.PI * (n - nMiddle));
		}
		return adH;
	}

	/**
	 * nOrder should be odd. o1 < o2 required
	 */
	public static double[] designRectangularBandPass(int nOrder,
			double dCornerOmega1, double dCornerOmega2) {
		double[] adH = new double[nOrder];
		int nMiddle = nOrder / 2;
		for (int n = 0; n < nOrder; n++) {
			adH[n] = (Math.sin(dCornerOmega2 * (n - nMiddle)) - Math
					.sin(dCornerOmega1 * (n - nMiddle)))
					/ (Math.PI * (n - nMiddle));
		}
		return adH;
	}

	/**
	 * nOrder should be odd.
	 */
	public static double[] designRectangularBandStop(int nOrder,
			double dCornerOmega1, double dCornerOmega2) {
		double[] adH = new double[nOrder];
		int nMiddle = nOrder / 2;
		for (int n = 0; n < nOrder; n++) {
			adH[n] = 1.0
					- (Math.sin(dCornerOmega2 * (n - nMiddle)) - Math
							.sin(dCornerOmega1 * (n - nMiddle)))
					/ (Math.PI * (n - nMiddle));
		}
		return adH;
	}

	// /////////////////////////////////////////////////
	//
	// Window methods
	//
	// /////////////////////////////////////////////////

	public static double[] designWindowLowPass(int nOrder, double dCornerOmega,
			FIRWindow window) {
		double[] adRectangular = designRectangularLowPass(nOrder, dCornerOmega);
		return applyWindow(adRectangular, window);
	}

	public static double[] designWindowHighPass(int nOrder,
			double dCornerOmega, FIRWindow window) {
		double[] adRectangular = designRectangularHighPass(nOrder, dCornerOmega);
		return applyWindow(adRectangular, window);
	}

	public static double[] designWindowBandPass(int nOrder,
			double dCornerOmega1, double dCornerOmega2, FIRWindow window) {
		double[] adRectangular = designRectangularBandPass(nOrder,
				dCornerOmega1, dCornerOmega2);
		return applyWindow(adRectangular, window);
	}

	public static double[] designWindowBandStop(int nOrder,
			double dCornerOmega1, double dCornerOmega2, FIRWindow window) {
		double[] adRectangular = designRectangularBandStop(nOrder,
				dCornerOmega1, dCornerOmega2);
		return applyWindow(adRectangular, window);
	}

	private static double[] applyWindow(double[] adRectangular, FIRWindow window) {
		double[] adWindow = window.getWindow(adRectangular.length);
		double[] adH = Util.multiply(adRectangular, adWindow);
		return adH;
	}
}

/*** FilterDesign.java ***/
