/*
 *	MpegEncoding.java
 */

/*
 *  Copyright (c) 1999, 2000 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
 *
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
 *
 */


package	org.tritonus.sampled.file;


import	javax.sound.sampled.AudioFormat;

import	org.tritonus.TDebug;
import	org.tritonus.sampled.Encodings;


/**	Encodings used by the MPEG audio decoder.
 *
 *	@author Matthias Pfisterer
 */
public class MpegEncoding
// extends		AudioFormat.Encoding
{
	public static final AudioFormat.Encoding	MPEG1L1 = Encodings.getEncoding("MPEG1L1");
	public static final AudioFormat.Encoding	MPEG1L2 = Encodings.getEncoding("MPEG1L2");
	public static final AudioFormat.Encoding	MPEG1L3 = Encodings.getEncoding("MPEG1L3");
	public static final AudioFormat.Encoding	MPEG2L1 = Encodings.getEncoding("MPEG2L1");
	public static final AudioFormat.Encoding	MPEG2L2 = Encodings.getEncoding("MPEG2L2");
	public static final AudioFormat.Encoding	MPEG2L3 = Encodings.getEncoding("MPEG2L3");
	public static final AudioFormat.Encoding	MPEG2DOT5L1 = Encodings.getEncoding("MPEG2DOT5L1");
	public static final AudioFormat.Encoding	MPEG2DOT5L2 = Encodings.getEncoding("MPEG2DOT5L2");
	public static final AudioFormat.Encoding	MPEG2DOT5L3 = Encodings.getEncoding("MPEG2DOT5L3");


/*
	public MpegEncoding(String strName)
	{
		super(strName);
	}
*/
}



/*** MpegEncoding.java ***/

