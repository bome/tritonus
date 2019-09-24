/*
 *	Util.java
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


/**	Helper methods used for filter design.
 */
public class Util
{
	/**	Modified Bessel function of first kind and 0th order.
	 */
	public static double I0(double x)
	{
		double	eps = 10E-9;
		int	n = 1;
		double	S = 1.0;
		double	D = 1.0;
		while (D > eps * S)
		{
			double	T = x / (2.0 * n);
			n++;
			D *= (T * T);
			S += D;
		}
		return S;
	}


	/**	Compute an inverse discrete fourier transform (IDFT).
		This implementation works fully complex. It is not
		optimized for speed. I.e., it does not use a 'fast'
		algorithm.

		@param aFrequencyDomain The array containing the frequency
		domain factors.

		@return The reconstructed time domain values (returned as
		complex numbers for full generality).
	 */
	public static Complex[] IDFT(Complex[] aFrequencyDomain)
	{
		int		N = aFrequencyDomain.length;
		Complex[]	aTimeDomain = new Complex[N];
		double		dOneOverN = 1.0 / N;
		for (int n = 0; n < N; n++)
		{
			aTimeDomain[n] = new Complex(0.0, 0.0);
			for (int k = 0; k < N; k++)
			{
				Complex	exponent = new Complex(0.0, 2.0 * Math.PI * k * n * dOneOverN);
				Complex	term = Complex.times(aFrequencyDomain[k], Complex.exp(exponent));
				aTimeDomain[n] = Complex.plus(aTimeDomain[n], term);
			}
			aTimeDomain[n] = Complex.times(aTimeDomain[n], dOneOverN);
		}
		return aTimeDomain;
	}


	/**	Multiplication of two arrays.
	 */
	public static double[] multiply(double[] ad1, double[] ad2)
	{
		int		nLength = Math.min(ad1.length, ad2.length);
		double[]	adResult = new double[nLength];
		for (int i = 0; i < nLength; i++)
		{
			adResult[i] = ad1[i] * ad2[i];
		}
		return adResult;
	}


	/**	Converts frequency representation from omega to relative.
		This method converts a frequency represented in
		omega ([-PI .. +PI]) to relative (f/fs).

		@param dOmega The frequency represented in omega
		([-PI .. +PI]).

		@return The frequency represented relative to the sample rate
		(f/fs).
	 */
	public static double omega2relative(double dOmega)
	{
		double	dRelative = dOmega / (2.0 * Math.PI);
		return dRelative;
	}


	/**	Converts frequency representation from relative to omega.
		This method converts a frequency represented relative to
		the sample rate (f/fs) to omega ([-PI .. +PI]).

		@param dRelative The frequency represented relative to the
		sample rate (f/fs).

		@return The frequency represented in omega
		([-PI .. +PI]).
	 */
	public static double relative2omega(double dRelative)
	{
		double	dOmega = dRelative * 2.0 * Math.PI;
		return dOmega;
	}


	/**	Converts frequency representation from omega to absolute.
		This method converts a frequency represented in
		omega ([-PI .. +PI]) to absolute frequency (f).

		@param dOmega The frequency represented in omega
		([-PI .. +PI]).

		@param dSampleRate The sample rate (fs).

		@return The absolute frequency represented in Hz (f).
	 */
	public static double omega2absolute(double dOmega, double dSampleRate)
	{
		double	dAbsolute = omega2relative(dOmega) * dSampleRate;
		return dAbsolute;
	}


	/**	Converts frequency representation from absolute to omega.
		This method converts a frequency represented relative to
		the sample rate (f/fs) to omega ([-PI .. +PI]).

		@param dAbsolute The absolute frequency expressed in Hz (f).

		@param dSampleRate The sample rate (fs).

		@return The frequency represented in omega
		([-PI .. +PI]).
	 */
	public static double absolute2omega(double dAbsolute, double dSampleRate)
	{
		double	dOmega = relative2omega(dAbsolute / dSampleRate);
		return dOmega;
	}


	/**	Quantize constants from double to float.
	 */
	public static float[] quantizeToFloat(double[] adConstants)
	{
		float[]	afConstants = new float[adConstants.length];
		for (int i = 0; i < adConstants.length; i++)
		{
			afConstants[i] = (float) adConstants[i];
		}
		return afConstants;
	}
} 



/*** Util.java ***/
