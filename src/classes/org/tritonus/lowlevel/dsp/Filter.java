/*
 *	Filter.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2002 by Matthias Pfisterer
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

package org.tritonus.lowlevel.dsp;

/**
 * Common interface for all types of filters with sample processing.
 * 
 * <p>
 * This is for filters that have a sample-by-sample processing approach (opposed
 * to a block processing approach). This is intended for filters that consume
 * samples the same rate they output them. Examples of such filters are common
 * FIR and IIR filters.
 * </p>
 * <p>
 * This interface uses float as data type for samples. The samples are normally
 * in the range [-1.0 .. +1.0].
 * </p>
 */
public interface Filter {
	/**
	 * Process one sample through the filter.
	 * 
	 * @param fSample
	 *            the input sample
	 * @return the output sample
	 */
	public float process(float fSample);
}

/*** Filter.java ***/
